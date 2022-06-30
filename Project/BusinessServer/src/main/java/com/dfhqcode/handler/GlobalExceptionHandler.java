package com.dfhqcode.handler;

import com.dfhqcode.exception.ServiceException;
import com.dfhqcode.exception.ServiceExceptionEnum;
import com.dfhqcode.model.dto.JsonResult;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author zxd3099
 * @create 2022-06-27-16:45
 */
@ControllerAdvice(basePackages = "com/dfhqcode/controller")
public class GlobalExceptionHandler {
    /**
     * 处理 ServiceException 异常
     */
    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public JsonResult serviceExceptionHandler(HttpServletRequest req, ServiceException ex) {
        // 包装 JsonResult 结果
        return JsonResult.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理 MissingServletRequestParameterException 异常
     *
     * SpringMVC 参数不正确
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public JsonResult missingServletRequestParameterExceptionHandler(HttpServletRequest req, MissingServletRequestParameterException ex) {
        // 包装 JsonResult 结果
        return JsonResult.error(ServiceExceptionEnum.MISSING_REQUEST_PARAM_ERROR.getCode(),
                ServiceExceptionEnum.MISSING_REQUEST_PARAM_ERROR.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public JsonResult constraintViolationExceptionHandler(HttpServletRequest req, ConstraintViolationException ex) {
        // 拼接错误
        StringBuilder detailMessage = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
            // 使用 ; 分隔多个错误
            if (detailMessage.length() > 0) {
                detailMessage.append(";");
            }
            // 拼接内容到其中
            detailMessage.append(constraintViolation.getMessage());
        }
        // 包装 JsonResult 结果
        return JsonResult.error(ServiceExceptionEnum.INVALID_REQUEST_PARAM_ERROR.getCode(),
                ServiceExceptionEnum.INVALID_REQUEST_PARAM_ERROR.getMessage() + ":" + detailMessage.toString());
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public JsonResult bindExceptionHandler(HttpServletRequest req, BindException ex) {
        // 拼接错误
        StringBuilder detailMessage = new StringBuilder();
        for (ObjectError objectError : ex.getAllErrors()) {
            // 使用 ; 分隔多个错误
            if (detailMessage.length() > 0) {
                detailMessage.append(";");
            }
            // 拼接内容到其中
            detailMessage.append(objectError.getDefaultMessage());
        }
        // 包装 JsonResult 结果
        return JsonResult.error(ServiceExceptionEnum.INVALID_REQUEST_PARAM_ERROR.getCode(),
                ServiceExceptionEnum.INVALID_REQUEST_PARAM_ERROR.getMessage() + ":" + detailMessage.toString());
    }

    /**
     * 处理其它 Exception 异常
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public JsonResult exceptionHandler(HttpServletRequest req, Exception e) {
        // 返回 ERROR CommonResult
        return JsonResult.error(ServiceExceptionEnum.SYS_ERROR.getCode(),
                ServiceExceptionEnum.SYS_ERROR.getMessage());
    }
}
