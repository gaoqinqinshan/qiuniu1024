package com.qiniu.utils;

import lombok.Data;

/**
 * @author gao
 * @version 1.0
 * @description: 自定义统一返回格式
 * @date 2024/10/26 22:11
 */
@Data
public class R<T> {

    private Integer code; //状态码

    private String msg; //提示信息

    private T data; //数据

    private long timestamp;//接口请求时间

    public R() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> R<T> success(T data) {
        R<T> r = new R<>();
        r.setCode(ReturnCode.RC200.getCode());
        r.setMsg(ReturnCode.RC200.getMsg());
        r.setData(data);
        return r;
    }

    public static <T> R<T> error(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(null);
        return r;
    }
}
