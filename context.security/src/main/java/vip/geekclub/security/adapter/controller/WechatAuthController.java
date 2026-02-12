package vip.geekclub.security.adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.security.SessionStore;
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

    private final WechatService wechatService;
    private final SessionStore authSessionManager;

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

//        // 查询 WECHAT 凭证（使用 unionId）
//        CredentialResult credential = authenticationQueryService
//                .getAuthenticationByIdentifier(unionId, CredentialType.WECHAT)
//                .orElseThrow(() -> new BadCredentialsException("用户未注册，请先绑定微信账号"));
////
////        // 构建并返回 UserSession（内含 JwtToken）
////        JwtPrincipal jwtPrincipal = new JwtPrincipal(credential.userId(), credential.code());
//        var  userSession =new JwtAuthentication(jwtPrincipal);
//
//        String jwtToken = authSessionManager.create(userSession);
//        return ApiResponse.success(jwtToken);
        return ApiResponse.success(null);
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

//        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.identify(), request.code());
//        UserAuthentication userAuthenticationToken = (UserAuthentication) authenticationManager.authenticate(authRequest);
//        String unionId = wechatService.getUnionId(request.code());

        // 派发绑定命令
//        commandBus.dispatch(new CreateCredentialCommand());
//        String jwtToken = authSessionManager.create(userAuthenticationToken);
//        return ApiResponse.success(jwtToken);
        return ApiResponse.success(null);
    }
}