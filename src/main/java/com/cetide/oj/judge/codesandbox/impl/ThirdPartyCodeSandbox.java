package com.cetide.oj.judge.codesandbox.impl;

import com.cetide.oj.judge.codesandbox.CodeSandbox;
import com.cetide.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.cetide.oj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用网上现成的代码沙箱）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
