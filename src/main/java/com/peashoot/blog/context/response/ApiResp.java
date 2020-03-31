package com.peashoot.blog.context.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
public class ApiResp<T> {
    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * 错误代码
     */
    private int code;
    /**
     * 错误说明
     */
    private String message;
    /**
     * 具体信息
     */
    private T data;
    /**
     * 异常
     */
    private String exception;

    public ApiResp() {
        this(-1, "Uninitialized");
    }

    public ApiResp(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功后修改状态码
     *
     * @return
     */
    public ApiResp<T> success() {
        this.code = 200;
        this.message = "success";
        return this;
    }

    /**
     * 装载异常返回对象
     *
     * @param ex 异常
     * @return 通用响应对象
     */
    public static ApiResp<Object> createErrorResp(Exception ex) {
        ApiResp<Object> retResp = new ApiResp<>();
        retResp.setMessage(ex.getMessage());
        retResp.setException(ex.toString());
        retResp.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return retResp;
    }

    /**
     * 创建参数错误返回对象
     *
     * @param errDetail 参数错误说明
     * @return 通用响应对象
     */
    public static ApiResp<String> createArgumentErrorResp(String errDetail) {
        ApiResp<String> retResp = new ApiResp<>();
        retResp.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        retResp.setMessage("Argument isn't acceptable.");
        retResp.setData(errDetail);
        return retResp;
    }
}
