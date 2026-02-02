# 一、控制器编写规范

## 1、规范

- 文件放置在 `vip.geekclub.{业务领域}.adapter.controller` 包下；
- 类必须使用 `@RestController` 注解标记；
- 使用 `@RequestMapping` 注解定义基础路径；
- 构造函数注入优先，避免使用 `@Autowired` 字段注入，可使用 Lombok 的 `@RequiredArgsConstructor` 注解进行依赖注入；
- 方法参数使用 DTO 对象接收，配合 `@Valid` 进行参数校验；
- 返回类型统一使用 `vip.geekclub.framework.controller.ApiResponse<T>` 包装；
- 控制器层只负责参数校验、调用应用层服务、返回响应，不处理业务逻辑；
- 路径设计遵循 RESTful 风格，使用资源名词而非动词。

## 2、示例

```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class PasswordAuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthSessionManager authSessionManager;

    /**
     * 用户名密码登录
     */
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody @Valid UserNameLoginRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            request.username(),
            request.password()
        );
        UserAuthenticationToken authToken = (UserAuthenticationToken) authenticationManager.authenticate(token);
        String jwtToken = authSessionManager.createSession(authToken);
        return ApiResponse.success(jwtToken);
    }
}
```

## 3、注意事项

- **单一职责**：每个控制器应专注于一个业务领域或资源；
- **禁止业务逻辑**：控制器不应包含业务逻辑，应委托给 Command Handler 或 Query Service；
- **统一响应**：所有接口返回必须使用 `ApiResponse` 包装，确保响应格式一致；
- **异常处理**：异常统一由全局异常处理器处理，不在控制器中捕获处理业务异常。
- **禁止添加不需要的方法**：根据实际的用户需求添加方法，禁止添加暂时用不到的方法。