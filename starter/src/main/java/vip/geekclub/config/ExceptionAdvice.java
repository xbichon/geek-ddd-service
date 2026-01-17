package vip.geekclub.config;

import jakarta.persistence.RollbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vip.geekclub.common.controller.ApiResponse;
import vip.geekclub.common.exception.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vip.geekclub.common.exception.JwtParseException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理控制器层抛出的各种异常，返回标准化的错误响应
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    private static final String PARAMETER_ERROR_PREFIX = "参数传递错误：";
    private static final String VALIDATION_ERROR_PREFIX = "参数校验失败：";
    private static final String RESOURCE_NOT_FOUND_PREFIX = "接口不存在：";
    private static final String METHOD_NOT_SUPPORTED_PREFIX = "不支持";
    private static final String METHOD_NOT_SUPPORTED_SUFFIX = "方法，请使用";
    private static final String MEDIA_TYPE_PREFIX = "请使用Content-Type: ";
    private static final String SYSTEM_ERROR_PREFIX = "系统异常：";

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException businessException) {
        log.warn("业务异常: code={}, message={}", businessException.getCode(), businessException.getMessage());
        return ResponseEntity.ok(ApiResponse.fail(businessException.getCode(), businessException.getMessage()));
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(ValidationException validationException) {
        String message;
        if (validationException instanceof ConstraintViolationException constraintViolationException) {
            message = constraintViolationException.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
        } else {
            message = validationException.getMessage();
        }

        log.warn("参数校验异常: {}", message);
        return ResponseEntity.ok(ApiResponse.fail(400, message));
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingParameter(MissingServletRequestParameterException e) {
        String message = PARAMETER_ERROR_PREFIX + e.getMessage();
        log.warn("缺少请求参数: {}", message);
        return ResponseEntity.ok(ApiResponse.fail(400, message));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCertificateException(BadCredentialsException e) {
        return ResponseEntity.ok(ApiResponse.fail(400, e.getMessage()));
    }

    /**
     * 处理方法参数校验失败异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        String message = VALIDATION_ERROR_PREFIX + errorMsg;
        log.warn("方法参数校验失败: {}", message);
        return ResponseEntity.ok(ApiResponse.fail(400, message));
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(NoResourceFoundException e) {
        String message = RESOURCE_NOT_FOUND_PREFIX + e.getResourcePath();
        log.warn("资源未找到: {}", message);
        return ResponseEntity.ok(ApiResponse.fail(404, message));
    }

    /**
     * 处理HTTP方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        String message = METHOD_NOT_SUPPORTED_PREFIX + e.getMethod() + METHOD_NOT_SUPPORTED_SUFFIX + e.getSupportedHttpMethods();
        log.warn("HTTP方法不支持: {}", message);
        return ResponseEntity.ok(ApiResponse.fail(405, message));
    }

    /**
     * 处理媒体类型不支持异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        String message = MEDIA_TYPE_PREFIX + e.getContentType();
        log.warn("媒体类型不支持: {}", message);
        return ResponseEntity.ok(ApiResponse.fail(415, message));
    }

    @ExceptionHandler({JwtParseException.class})
    public ResponseEntity<ApiResponse<?>> handleJwtException(Exception e) {
        return ResponseEntity.ok(ApiResponse.fail(401, e.getMessage()));
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ApiResponse<?>> handleTransactionException(TransactionException e) {
        log.error("事务异常", e);
        if (e.getCause() == null || !(e.getCause() instanceof RollbackException)) {
            return handleGlobalException(e);
        }

        return switch (e.getCause().getCause()) {
            case BusinessException businessException -> handleBusinessException(businessException);
            case ValidationException validationException -> handleValidationException(validationException);
            case null, default -> handleGlobalException(e);
        };
    }

    /**
     * 全局异常兜底处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception e) {
        log.error("系统异常", e);
        return ResponseEntity.ok(ApiResponse.fail(500, SYSTEM_ERROR_PREFIX + e.getMessage()));
    }
}