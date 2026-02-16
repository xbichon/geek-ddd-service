# 一、控制器编写规范

## 1、规范

- 文件放置在 `vip.geekclub.{业务领域}.adapter.controller` 包下；
- 使用 `@RestController` + `@RequestMapping` + `@RequiredArgsConstructor`；
- 方法参数使用 DTO + `@Valid` 校验；
- 返回类型统一使用 `ApiResponse<T>` 包装；
- **URL 四级结构**：`/{userType}/{boundedContext}/{resource}/{action}`
  - `userType`：用户角色，如 `teacher`、`student`、`admin`
  - `boundedContext`：界限上下文，如 `internship`、`auth`
  - `resource`：资源/聚合根，如 `selection`、`application`
  - `action`：业务操作，如 `list`、`submit`、`update`

## 2、示例

```java
@RestController("Teacher_SelectionController")
@RequestMapping("/teacher/internship/selection")
@RequiredArgsConstructor
public class SelectionController {

    @GetMapping("/list")
    public ApiResponse<PageResult<Result>> list(Query query) {
        return ApiResponse.success(service.list(query));
    }

    @PostMapping("/submit")
    public ApiResponse<Long> submit(@RequestBody @Valid SubmitRequest request) {
        return ApiResponse.success(service.submit(request));
    }
}
```

## 3、注意事项

- 控制器只负责参数校验和调用应用层服务，**禁止业务逻辑**；
- 异常由全局异常处理器处理，**不在控制器中捕获**；
- **禁止添加不需要的方法**，根据实际需求添加；
- 默认使用 `@PostMapping`，查询可用 `@GetMapping`；
- **单一资源原则**：按资源拆分控制器，每个控制器只负责一个资源的操作（如 `SelectionController` 只处理选题相关，`ApplicationController` 只处理申请相关）。