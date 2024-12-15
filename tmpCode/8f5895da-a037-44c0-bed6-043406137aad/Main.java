import java.io.File;
import java.io.IOException;

public class JavaCodeSandboxTemplate {

    public ExecuteMessage compileFile(File userCodeFile) {
        // 检查文件是否存在并且是文件
        if (!userCodeFile.exists()) {
            throw new RuntimeException("文件不存在: " + userCodeFile.getAbsolutePath());
        }

        if (!userCodeFile.isFile()) {
            throw new RuntimeException("不是文件: " + userCodeFile.getAbsolutePath());
        }

        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");

            if (executeMessage.getExitValue() != 0) {
                // 获取编译错误信息
                String errorMessage = executeMessage.getErrorMessage();
                throw new RuntimeException("编译错误: " + errorMessage);
            }

            return executeMessage;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
