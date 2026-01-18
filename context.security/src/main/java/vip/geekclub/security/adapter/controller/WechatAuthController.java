package vip.geekclub.security.adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.security.JwtAuthentication;
import vip.geekclub.framework.security.WechatAuthenticationToken;
import vip.geekclub.security.adapter.controller.dto.WechatBindRequest;
import vip.geekclub.security.adapter.controller.dto.WechatLoginRequest;
import vip.geekclub.security.adapter.gateway.WechatService;
import vip.geekclub.security.application.command.dto.CreateWechatCredentialCommand;

/**
 * 微信认证控制器
 * 处理微信小程序登录和绑定功能
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/wechat")
public class WechatAuthController {

    private final AuthenticationManager authenticationManager;
    private final CommandBus commandBus;
    private final WechatService wechatService;
    private final static  long DEFAULT_EXPIRATION_SECONDS =  60 * 60 * 24 * 30;

    /**
     * 微信小程序登录
     *
     * @param request 微信登录请求
     * @return JWT Token
     */
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid WechatLoginRequest request) {
        // 调用微信接口换取 unionId
        String unionId = wechatService.getUnionId(request.code());
        WechatAuthenticationToken authRequest = new WechatAuthenticationToken(unionId);
        JwtAuthentication userSession = (JwtAuthentication) authenticationManager.authenticate(authRequest);

        return ApiResponse.success(userSession.getToken(DEFAULT_EXPIRATION_SECONDS));
    }

    /**
     * 绑定微信小程序
     * 将当前登录用户与微信OpenID进行绑定
     *
     * @param request 微信绑定请求
     * @return 绑定结果
     */
    @PostMapping("/bind")
    public ApiResponse<?> bind(@RequestBody @Valid WechatBindRequest request) {

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.identify(), request.code());
        JwtAuthentication userSession = (JwtAuthentication) authenticationManager.authenticate(authRequest);
        String unionId = wechatService.getUnionId(request.code());

        // 派发绑定命令
        commandBus.dispatch(new CreateWechatCredentialCommand(userSession.getUserId(), unionId));
        return ApiResponse.success(userSession.getToken(DEFAULT_EXPIRATION_SECONDS));
    }
}