package com.cetide.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cetide.oj.common.BaseResponse;
import com.cetide.oj.common.ResultUtils;
import com.cetide.oj.mapper.DailyQuestionMapper;
import com.cetide.oj.model.entity.DailyQuestion;
import com.cetide.oj.model.entity.Question;
import com.cetide.oj.model.vo.DailyQuestionVO;
import com.cetide.oj.service.DailyQuestionService;
import com.cetide.oj.service.QuestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* @author Lenovo
* @description 针对表【daily_question(每日题目记录)】的数据库操作Service实现
* @createDate 2024-10-12 17:26:18
*/
@Service
public class DailyQuestionServiceImpl extends ServiceImpl<DailyQuestionMapper, DailyQuestion>
implements DailyQuestionService {

    @Resource
    private DailyQuestionMapper dailyQuestionMapper;

    @Resource
    private QuestionService questionService;

    @Override
    public BaseResponse<DailyQuestionVO> getDailyQuestion() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(new Date());
        DailyQuestion dailyQuestion = dailyQuestionMapper.selectByDate(formattedDate);
        Question question = questionService.getById(dailyQuestion.getQuestion_id());
        DailyQuestionVO questionVO = new DailyQuestionVO();
        questionVO.setQuestion(question);
        questionVO.setQuestionImg(dailyQuestion.getQuestion_img());
        return ResultUtils.success(questionVO);
    }
}
