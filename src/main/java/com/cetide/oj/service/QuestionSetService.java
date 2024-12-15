package com.cetide.oj.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cetide.oj.model.dto.questionSet.QuestionSetQueryRequest;
import com.cetide.oj.model.entity.QuestionSet;
import com.cetide.oj.model.vo.QuestionSetVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lenovo
* @description 针对表【question_set(题目集)】的数据库操作Service
* @createDate 2024-10-12 23:14:52
*/
public interface QuestionSetService extends IService<QuestionSet> {

    void validQuestionSet(QuestionSet questionSet, boolean b);


    Page<QuestionSetVO> getQuestionVOPage(Page<QuestionSet> questionPage, HttpServletRequest request);

    Wrapper<QuestionSet> getQueryWrapper(QuestionSetQueryRequest questionSetQueryRequest);
}
