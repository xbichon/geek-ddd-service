package vip.geekclub.common.controller;

import vip.geekclub.common.exception.ValidationException;
import vip.geekclub.common.utils.JsonUtils;
import vip.geekclub.common.utils.AssertUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Json 参数解析器
 *
 * @author leo
 */
@Component
public class JsonParamResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(JsonParam.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, @NonNull NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        JsonNode jsonNode = (JsonNode) nativeWebRequest.getAttribute("JsonNode", RequestAttributes.SCOPE_REQUEST);
        if (jsonNode == null) {
            try {
                String request = getRequestContent(nativeWebRequest);
                jsonNode = JsonUtils.getObjectMapper().readTree(request);
            } catch (IOException exception) {
                throw new ValidationException("Json 格式不正确");
            }
            nativeWebRequest.setAttribute("JsonNode", jsonNode, RequestAttributes.SCOPE_REQUEST);
        }
        JsonNode paramNode = jsonNode.get(methodParameter.getParameterName());
        return convertToValue(paramNode, methodParameter);
    }

    /**
     * 获取请求体的内容
     */
    private static String getRequestContent(NativeWebRequest nativeWebRequest)  {
        HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if (httpServletRequest == null) {
            return "";
        }

        try (BufferedReader reader = httpServletRequest.getReader()) {
            return reader.lines().collect(java.util.stream.Collectors.joining()).trim();
        } catch (IOException e) {
            throw new ValidationException("无法读取请求体");
        }
    }

    /**
     * 根据JsonNode 获取基础类型
     */
    private static Object convertToValue(JsonNode paramNode, MethodParameter methodParameter) {

        if (paramNode == null) {
            AssertUtil.isFalse(methodParameter.getParameterType().isPrimitive(), () -> methodParameter.getParameterName() + " 不能为空;");
            return null;
        }

        try {
            if (methodParameter.getParameterType().isEnum()) {
                return JsonUtils.getObjectMapper().treeToValue(paramNode, methodParameter.getParameterType());
            }
            return JsonUtils.jsonToObject(paramNode, methodParameter.getParameterType());
        } catch (JsonProcessingException | IllegalArgumentException exception) {
            throw new ValidationException(methodParameter.getParameterName() + " 格式不正确;");
        }
    }
}