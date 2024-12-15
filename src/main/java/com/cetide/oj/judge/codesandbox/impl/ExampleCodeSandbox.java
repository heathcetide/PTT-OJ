package com.cetide.oj.judge.codesandbox.impl;

import com.cetide.oj.judge.codesandbox.CodeSandbox;
import com.cetide.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.cetide.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.cetide.oj.judge.codesandbox.model.JudgeInfo;
import com.cetide.oj.model.enums.JudgeInfoMessageEnum;
import com.cetide.oj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱（仅为了跑通业务流程）
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
