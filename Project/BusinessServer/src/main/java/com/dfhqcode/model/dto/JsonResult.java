package com.dfhqcode.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zxd3099
 * @create 2022-06-26-21:47
 */
@Data
@ApiModel("Json数据返回")
public class JsonResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 成功状态码
     */
    public static final int CODE_SUCCESS = 200;
    /**
     * 失败状态码
     */
    public static final int CODE_ERROR = 500;
    /**
     * 警告状态码
     */
    public static final int CODE_WARNING = 501;
    /**
     * 无权限状态码
     */
    public static final int CODE_NOT_JUR = 403;
    /**
     * 未登录状态码
     */
    public static final int CODE_NOT_LOGIN = 401;
    /**
     * 无效请求状态码
     */
    public static final int CODE_INVALID_REQUEST = 400;

    @ApiModelProperty("返回码")
    private int code;

    @ApiModelProperty("返回详细信息")
    private String msg;

    @ApiModelProperty("返回详细数据")
    private T data;

    public static <T> JsonResult<T> success() {
        return jsonResult(null, CODE_SUCCESS, "操作成功");
    }

    public static <T> JsonResult<T> success(T data) {
        return jsonResult(data, CODE_SUCCESS, "操作成功");
    }

    public static <T> JsonResult<T> success(String msg) {
        return jsonResult(null, CODE_SUCCESS, msg);
    }

    public static <T> JsonResult<T> success(T data, String msg) {
        return jsonResult(data, CODE_SUCCESS, msg);
    }

    public static <T> JsonResult<T> error() {
        return jsonResult(null, CODE_ERROR, "操作失败");
    }

    public static <T> JsonResult<T> error(String msg) {
        return jsonResult(null, CODE_ERROR, msg);
    }

    public static <T> JsonResult<T> error(T data) {
        return jsonResult(data, CODE_WARNING, "操作失败");
    }

    public static <T> JsonResult<T> error(Integer code, String msg) {
        return jsonResult(null, code, msg);
    }

    public static <T> JsonResult<T> warning() {
        return jsonResult(null, CODE_WARNING, "操作警告");
    }

    public static <T> JsonResult<T> warning(String msg) {
        return jsonResult(null, CODE_WARNING, msg);
    }

    public static <T> JsonResult<T> notLogin() {
        return jsonResult(null, CODE_NOT_LOGIN, "没有登录");
    }

    public static <T> JsonResult<T> notLogin(String msg) {
        return jsonResult(null, CODE_NOT_LOGIN, msg);
    }

    public static <T> JsonResult<T> notJur() {
        return jsonResult(null, CODE_NOT_JUR, "没有访问权限");
    }

    public static <T> JsonResult<T> notJur(String msg) {
        return jsonResult(null, CODE_NOT_JUR, msg);
    }

    public static <T> JsonResult<T> invalidReq() {
        return jsonResult(null, CODE_INVALID_REQUEST, "无效请求");
    }

    public static <T> JsonResult<T> invalidReq(String msg) {
        return jsonResult(null, CODE_INVALID_REQUEST, msg);
    }

    private static <T> JsonResult<T> jsonResult(T data, int code, String msg) {
        JsonResult<T> result = new JsonResult<>();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }
}
