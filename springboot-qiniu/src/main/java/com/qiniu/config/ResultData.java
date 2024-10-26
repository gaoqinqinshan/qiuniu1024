package com.qiniu.config;

import lombok.Data;

/**
 * @author gao
 * @version 1.0
 * @description: 统一返回格式
 * @date 2024/10/26 22:03
 */
@Data
public class ResultData<T> {
    /**
     * 结果状态 ,具体状态码参见ResultData.java
     */
    private int status;
    private String message;
    private T data;
    private long timestamp;

    public ResultData() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <t> ResultData<t> success(T data) {
        ResultData<t> resultData = new ResultData<>();
        resultData.setStatus(ReturnCode.RC100.getCode());
        resultData.setMessage(ReturnCode.RC100.getMessage());
        resultData.setData(data);
        return resultData;
    }

    public static <t> ResultData<t> fail(int code, String message) {
        ResultData<t> resultData = new ResultData<>();
        resultData.setStatus(code);
        resultData.setMessage(message);
        return resultData;
    }

}