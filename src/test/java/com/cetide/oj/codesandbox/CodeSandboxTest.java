package com.cetide.oj.codesandbox;

import com.cetide.oj.judge.codesandbox.CodeSandbox;
import com.cetide.oj.judge.codesandbox.CodeSandboxFactory;
import com.cetide.oj.judge.codesandbox.CodeSandboxProxy;
import com.cetide.oj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.cetide.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.cetide.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.cetide.oj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SpringBootTest
class CodeSandboxTest {

    @Value("${codesandbox.type:example}")
    private String type;

    @Test
    void executeCode() {
        CodeSandbox codeSandbox = new RemoteCodeSandbox();
        String code = "int main() { }";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage(language);
        executeCodeRequest.setInputList(inputList);
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }

    @Test
    void executeCodeByValue() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        String code = "int main() { }";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage(language);
        executeCodeRequest.setInputList(inputList);
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }

    @Test
    void executeCodeByProxy() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String code = "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = Integer.parseInt(args[0]);\n" +
                "        int b = Integer.parseInt(args[1]);\n" +
                "        System.out.println(\"结果:\" + (a + b));\n" +
                "    }\n" +
                "}";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage(language);
        executeCodeRequest.setInputList(inputList);
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String type = scanner.next();
            CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
            String code = "int main() { }";
            String language = QuestionSubmitLanguageEnum.JAVA.getValue();
            List<String> inputList = Arrays.asList("1 2", "3 4");
            ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
            executeCodeRequest.setCode(code);
            executeCodeRequest.setLanguage(language);
            executeCodeRequest.setInputList(inputList);
            ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        }
    }
}