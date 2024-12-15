package com.cetide.oj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cetide.oj.common.BaseResponse;
import com.cetide.oj.model.entity.DailyQuestion;
import com.cetide.oj.model.vo.DailyQuestionVO;

/**
* @author Lenovo
* @description 针对表【daily_question(每日题目记录)】的数据库操作Service
* @createDate 2024-10-12 17:26:18
*/
public interface DailyQuestionService extends IService<DailyQuestion> {

    BaseResponse<DailyQuestionVO> getDailyQuestion();
}
