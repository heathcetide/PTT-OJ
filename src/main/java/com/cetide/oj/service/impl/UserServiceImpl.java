package com.cetide.oj.service.impl;

import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cetide.oj.common.BaseResponse;
import com.cetide.oj.common.ErrorCode;
import com.cetide.oj.common.ResultUtils;
import com.cetide.oj.constant.CommonConstant;
import com.cetide.oj.exception.BusinessException;
import com.cetide.oj.mapper.UserMapper;
import com.cetide.oj.model.dto.user.UserQueryRequest;
import com.cetide.oj.model.entity.User;
import com.cetide.oj.model.enums.UserRoleEnum;
import com.cetide.oj.model.vo.LoginUserVO;
import com.cetide.oj.model.vo.UserVO;
import com.cetide.oj.service.UserService;
import com.cetide.oj.utils.SqlUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.cetide.oj.constant.UserConstant.*;

/**
 * 用户服务实现
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "yupi";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setUserName(NEW_USER_ROLE);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public LoginUserVO userLoginByMpOpen(WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request) {
        String unionId = wxOAuth2UserInfo.getUnionId();
        String mpOpenId = wxOAuth2UserInfo.getOpenid();
        // 单机锁
        synchronized (unionId.intern()) {
            // 查询用户是否已存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("unionId", unionId);
            User user = this.getOne(queryWrapper);
            // 被封号，禁止登录
            if (user != null && UserRoleEnum.BAN.getValue().equals(user.getUserRole())) {
                throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "该用户已被封，禁止登录");
            }
            // 用户不存在则创建
            if (user == null) {
                user = new User();
                user.setUnionId(unionId);
                user.setMpOpenId(mpOpenId);
                user.setUserAvatar(wxOAuth2UserInfo.getHeadImgUrl());
                user.setUserName(wxOAuth2UserInfo.getNickname());
                boolean result = this.save(user);
                if (!result) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败");
                }
            }
            // 记录用户的登录态
            request.getSession().setAttribute(USER_LOGIN_STATE, user);
            return getLoginUserVO(user);
        }
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String unionId = userQueryRequest.getUnionId();
        String mpOpenId = userQueryRequest.getMpOpenId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
        queryWrapper.eq(StringUtils.isNotBlank(mpOpenId), "mpOpenId", mpOpenId);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public BaseResponse<Boolean> sign(HttpServletRequest request) {
        // 获取当前用户
        User loginUser = this.getLoginUser(request);

        // 获取当前日期并拼接 Key（按月存储）
        LocalDateTime now = LocalDateTime.now();
        String keySuffix = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        String key = USER_SIGN_KEY + loginUser.getId() + ":" + keySuffix;

        // 获取今天是本月的第几天
        int dayOfMonth = now.getDayOfMonth() - 1;

        // 设置当天的签到位为 1
        Boolean isSigned = redisTemplate.opsForValue().setBit(key, dayOfMonth, true);
        return ResultUtils.success(isSigned);
//        //获取当前用户
//        User loginUser = this.getLoginUser(request);
//
//        //获取日期
//        LocalDateTime now = LocalDateTime.now();
//
//        //拼接key
//        String keySuffix = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        String key = USER_SIGN_KEY + loginUser.getId() + keySuffix;
//
//        //获取今天是本月的第几天
//        int dayOfMonth = now.getDayOfMonth();
//        Boolean b = redisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
//        //写入redis， SETBIT KEY offset 1
//        return ResultUtils.success(b);
    }

    @Override
    public BaseResponse<Integer> getUserSignCount(HttpServletRequest request) {
        // 获取当前用户
        User loginUser = this.getLoginUser(request);

        // 获取当前月份的 Redis Key
        LocalDateTime now = LocalDateTime.now();
        String keySuffix = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        String key = USER_SIGN_KEY + loginUser.getId() + ":" + keySuffix;

        // 获取本月到今天位置的签到记录
        int dayOfMonth = now.getDayOfMonth();
        List<Long> longs = redisTemplate.opsForValue().bitField(
                key,
                BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0)
        );

        if (longs == null || longs.isEmpty() || longs.get(0) == null) {
            return ResultUtils.success(0);
        }

        // 计算签到天数
        Long num = longs.get(0);
        int count = Long.bitCount(num);  // 计算 num 中 1 的数量
        return ResultUtils.success(count);
    }

//    @Override
//    public BaseResponse<Integer> getUserSignCount(HttpServletRequest request) {
//        //获取当前用户
//        User loginUser = this.getLoginUser(request);
//
//        //获取日期
//        LocalDateTime now = LocalDateTime.now();
//
//        //拼接key
//        String keySuffix = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        String key = USER_SIGN_KEY + loginUser.getId() + keySuffix;
//
//        //获取今天是本月的第几天
//        int dayOfMonth = now.getDayOfMonth();
//
//        //获取本月到今天位置的签到记录,返回的是一个十进制的数字 BITFIELD sign:1:userId:yyyyMMdd GET u14 0
//        List<Long> longs = redisTemplate.opsForValue().bitField(
//                key,
//                BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0)
//        );
//        if (longs == null || longs.isEmpty()){
//            return ResultUtils.success(0);
//        }
//        Long num = longs.get(0);
//        if (num == null || num == 0){
//            return ResultUtils.success(0);
//        }
//        int count = 0;
//        //循环遍历
//        //如果是零，则说明未签到，结束
//        //让这个数字与1做与运算，得到的数字的最后一个bit位
//        //判断这个bit位是否为0
//        while ((num & 1) != 0) {
//
//            //如果不为0，则说明已签到，计数器+1
//            count++;
//
//            //把数字右移一位，继续判断下一个bit位
//            num = num >> 1;
//        }
//        return ResultUtils.success(count);
//    }

    @Override
    public BaseResponse<Integer> getUserContinuousSignCount(HttpServletRequest request) {
        // 获取当前用户
        User loginUser = this.getLoginUser(request);

        // 获取当前月份的 Redis Key
        LocalDateTime now = LocalDateTime.now();
        String keySuffix = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        String key = USER_SIGN_KEY + loginUser.getId() + ":" + keySuffix;

        // 获取本月到今天位置的签到记录
        int dayOfMonth = now.getDayOfMonth();
        List<Long> longs = redisTemplate.opsForValue().bitField(
                key,
                BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0)
        );

        if (longs == null || longs.isEmpty() || longs.get(0) == null) {
            return ResultUtils.success(0);
        }

        Long num = longs.get(0);
        int continuousCount = 0;

        // 计算连续签到天数（从最后一天开始倒数连续的 1 的数量）
        for (int i = 0; i < dayOfMonth; i++) {
            if ((num & 1) == 1) {
                continuousCount++;
                num = num >> 1;
            } else {
                break;
            }
        }
        return ResultUtils.success(continuousCount);
    }
}
