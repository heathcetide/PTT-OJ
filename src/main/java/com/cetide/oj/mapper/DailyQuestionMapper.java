package com.cetide.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cetide.oj.model.entity.DailyQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author Lenovo
* @description 针对表【daily_question(每日题目记录)】的数据库操作Mapper
* @createDate 2024-10-12 17:26:18
* @Entity com.yupi.yuoj.model.entity.DailyQuestion
*/
public interface DailyQuestionMapper extends BaseMapper<DailyQuestion> {

    @Select("select * from daily_question where date = #{date}")
    DailyQuestion selectByDate(String date);
}
