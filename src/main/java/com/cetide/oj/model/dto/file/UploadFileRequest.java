package com.cetide.oj.model.dto.file;

import java.io.Serializable;

/**
 * 文件上传请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class UploadFileRequest implements Serializable {

    /**
     * 业务
     */
    private String biz;


    private static final long serialVersionUID = 1L;

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }
}