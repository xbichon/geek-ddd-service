package vip.geekclub.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.common.command.CommandBus;
import vip.geekclub.common.controller.ApiResponse;
import vip.geekclub.config.security.UserSession;
import vip.geekclub.config.security.AuthenticationService;
import vip.geekclub.controller.auth.dto.WechatBindRequest;
import vip.geekclub.controller.auth.dto.WechatLoginRequest;
import vip.geekclub.gateway.wechat.WechatService;
import vip.geekclub.security.auth.command.dto.CreateWechatCredentialCommand;

/**
 * 微信认证控制器
 * 处理微信小程序登录和绑定功能
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/wechat")
public class WechatAuthController {

    private final AuthenticationService authenticationService;
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
        UserSession userSession = authenticationService.authenticateWechatUnionId(unionId);
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
        UserSession userSession = authenticationService.authenticateUsernamePassword(request.identify(), request.code());
        String unionId = wechatService.getUnionId(request.code());

        // 派发绑定命令
        commandBus.dispatch(new CreateWechatCredentialCommand(userSession.getUserId(), unionId));
        return ApiResponse.success(userSession.getToken(DEFAULT_EXPIRATION_SECONDS));
    }
}