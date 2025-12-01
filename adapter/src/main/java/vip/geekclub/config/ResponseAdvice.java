//package vip.geekclub.config.web;
//
//import vip.geekclub.common.controller.ApiResponse;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//@RestControllerAdvice
//public class ResponseAdvice implements ResponseBodyAdvice<Object> {
//    @Override
//    public boolean supports(MethodParameter returnType, Class converterType) {
//        // 排除特定注解或返回类型（如文件下载接口）
//        //return !returnType.hasMethodAnnotation(IgnoreWrap.class);
//        return true;
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object body, MethodParameter returnType,
//                                  MediaType mediaType, Class converterType,
//                                  ServerHttpRequest request, ServerHttpResponse response) {
//        // 若已经是Result类型或返回String（需特殊处理），直接返回
//        if (body instanceof String || body instanceof ApiResponse || body instanceof Exception) {
//            return body;
//        }
//        return ApiResponse.ok(body); // 自动包装为统一结构
//    }
//}