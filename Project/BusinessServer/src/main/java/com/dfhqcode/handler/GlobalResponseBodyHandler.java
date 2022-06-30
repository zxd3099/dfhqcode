package com.dfhqcode.handler;

import com.dfhqcode.model.dto.JsonResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author zxd3099
 * @create 2022-06-27-16:52
 */
@ControllerAdvice(basePackages = "com/dfhqcode/controller")
public class GlobalResponseBodyHandler implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 如果已经是 JsonResult 类型，则直接返回
        if (body instanceof JsonResult) {
            return body;
        }
        // 如果不是，则包装成 JsonResult 类型
        return JsonResult.success(body);
    }
}
