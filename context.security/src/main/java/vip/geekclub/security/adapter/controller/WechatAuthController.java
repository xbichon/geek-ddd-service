package vip.geekclub.security.adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.security.AuthSessionManager;
import vip.geekclub.framework.security.AuthSessionManagerImpl;
import vip.geekclub.framework.security.UserAuthenticationToken;
import vip.geekclub.framework.security.WechatAuthenticationToken;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.security.adapter.controller.dto.WechatBindRequest;
import vip.geekclub.security.adapter.controller.dto.WechatLoginRequest;
import vip.geekclub.security.adapter.gateway.WechatService;

/**
 * 微信认证控制器
 * 处理微信小程序登录和绑定功能
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/security/wechat")
public class WechatAuthController {

    private final AuthenticationManager authenticationManager;
    private final WechatService wechatService;
    private final AuthSessionManager authSessionManager;

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
        UserAuthenticationToken userSession = (UserAuthenticationToken) authenticationManager.authenticate(authRequest);
        String jwtToken = authSessionManager.createSession(userSession);
        return ApiResponse.success(jwtToken);
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
        UserAuthenticationToken userAuthenticationToken = (UserAuthenticationToken) authenticationManager.authenticate(authRequest);
        String unionId = wechatService.getUnionId(request.code());

        // 派发绑定命令
//        commandBus.dispatch(new CreateCredentialCommand());
        String jwtToken = authSessionManager.createSession(userAuthenticationToken);
        return ApiResponse.success(jwtToken);
    }
}