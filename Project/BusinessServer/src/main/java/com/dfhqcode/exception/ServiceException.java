package com.dfhqcode.exception;

/**
 * @author zxd3099
 * @create 2022-06-27-16:41
 */
public final class ServiceException extends RuntimeException {
    /**
     * 错误码
     */
    private final Integer code;

    public ServiceException(ServiceExceptionEnum serviceExceptionEnum)
    {
        // 使用父类message字段
        super(serviceExceptionEnum.getMessage());
        // 设置错误码
        this.code = serviceExceptionEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
