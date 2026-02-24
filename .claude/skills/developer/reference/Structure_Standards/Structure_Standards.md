# 目录结构规范

本文档规范 geek-ddd 项目的目录结构和设计规范。

## 1. 总体结构

```
├── service/                        # 服务模块
│    ├── context.{业务领域}/                 # 业务领域模块
│    │   ├── src/main/
│    │   │   ├── java/vip/geekclub/{业务领域}/
│    │   │   │   ├── adapter/              # 适配器层（防腐层）
│    │   │   │   │   ├── controller/       # 控制器（处理HTTP请求）
│    │   │   │   ├── application/          # 应用层
│    │   │   │   │   ├── command/          # 命令处理器（写操作）
│    │   │   │   │   ├── initialize/       # 初始化相关
│    │   │   │   │   └── query/            # 查询处理器（读操作）
│    │   │   │   │   │   └── dto/          # 查询数据传输对象
│    │   │   │   ├── domain/               # 领域层
│    │   │   │   │   ├── model/            # 领域模型
│    │   │   │   │   ├── repository/       # 领域仓储
│    │   │   │   │   ├── service/          # 领域服务
│    │   │   │   │   ├── event/            # 领域事件
│    │   │   │   │   ├── value/            # 值对象
│    │   │   │   │   └── exception/        # 领域异常
│    ├── infrastructure/                   # 基础设施模块
│    │   ├── src/main/java/vip/geekclub/framework/ # 基础设施代码
│    │   ├── src/main/java/vip/geekclub/database/  # 数据库相关代码
│    ├── starter/                          # 启动模块
│    │   ├── src/main/java/vip/geekclub/   # 启动代码
│    │   └── src/main/resources/           # 配置文件
```

## 2. 设计规范

### （1）模块间通信与依赖原则

#### 通用域与业务域的调用关系

- **单向依赖**：业务域可以依赖通用域，通用域不应依赖业务域
- **通用域不主动暴露**：通用域（如 `context.security`）不应主动暴露 HTTP 接口（Controller），而是由业务域（如 `context.manager`）通过 **Gateway（防腐层）** 调用通用域的应用服务
- **接口暴露原则**：
  - 通用域只对外提供 **应用层服务**（Command/Query）或 **领域服务**
  - 业务域决定何时、如何暴露接口给外部（通过自身的 Controller）

**示例：**
```
manager 模块（业务域）         security 模块（通用域）
    │                              │
    ├─ Controller（对外暴露）      │
    │   /manager/auth/login        │
    │       │                      │
    │       ▼                      │
    ├─ Gateway（防腐层）           │
    │   SecurityGateway            │
    │       │                      │
    └───────┼──────────────────────┤
            ▼                      │
        CommandBus                 │
            │                      │
            ▼                      │
        CreatePrincipalCommand ────┼──▶ 应用层（内部服务）
                                   │
```

**禁止的做法：**
- security 模块直接暴露 `/security/auth/login` 给前端
- manager 模块直接调用 security 的 Controller

**推荐的做法：**
- manager 模块暴露 `/manager/auth/login`，内部通过 `SecurityGateway` 调用 security 的 `CreatePrincipalCommand`
- security 模块只提供应用层服务，不感知外部调用方

### （2）业务领域模块 (context.*)
- 命名格式：`context.{业务领域}`
- 示例：`context.education`, `context.manager`, `context.security`
- 每个业务领域模块代表一个限界上下文

### （3）分层设计目录
1. **adapter** - 适配器层，对外部系统的适配
2. **application** - 应用层，协调领域对象完成业务逻辑
3. **domain** - 领域层，核心业务逻辑

### （4）应用层子目录
1. **command** - 命令处理（写操作）
2. **query** - 查询处理（读操作）
3. **gateway** - 防腐层接口（调用外部模块）
4. **initialize** - 初始化相关

### （5）领域层子目录
1. **model** - 领域模型
2. **repository** - 领域仓储接口
3. **service** - 领域服务
4. **event** - 领域事件
5. **value** - 值对象
6. **exception** - 领域异常

### （6）数据库迁移
- 数据库迁移脚本放在 `infrastructure/src/main/resources/db/migration` 目录下

## 3. 编码规范

### （1）Record 类型参数注释

对于 Java Record 类型的参数注释，使用单行注释 `//`，避免使用多行注释 `/* */`。

**示例：**
```java
public record InternPageQuery(
    // 班级名称（精确查询）
    String className,

    // 学生姓名（模糊查询）
    String studentName
) {}
```
