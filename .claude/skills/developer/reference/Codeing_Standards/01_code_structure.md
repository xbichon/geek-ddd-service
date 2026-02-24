## 一、代码结构
架构采用领域驱动、CQRS的架构模式，系统分为三层，分别是适配器层(adapter)，应用层(application)和领域层(domain)。
 ```
 ├── vip/geekclub/{业务领域}/
 │   ├── adapter/              # 适配器层（技术实现）
 │   │   ├── controller/       # 控制器（处理HTTP请求）
 │   │   └── gateway/          # 防腐层实现（调用外部模块）
 │   ├── application/          # 应用层
 │   │   ├── command/          # 命令处理器（写操作）
 │   │   │   └── dto/          # 命令数据传输对象
 │   │   ├── query/            # 查询处理器（读操作）
 │   │   │   └── dto/          # 查询数据传输对象
 │   │   ├── gateway/          # 防腐层接口（定义外部依赖契约）
 │   │   │   └── dto/          # 防腐层数据传输对象
 │   │   └── initialize/       # 初始化相关
 │   ├── domain/               # 领域层
 │   │   ├── model/            # 领域模型
 │   │   ├── repository/       # 领域仓储
 │   │   ├── service/          # 领域服务
 │   │   ├── event/            # 领域事件
 │   │   ├── value/            # 值对象
 │   │   └── exception/        # 领域异常
```

## 二、代码种类

本项目采用 DDD + CQRS 架构，Java 代码分为以下 12 种类型：

| 序号 | 代码类型 | 英文名称 | 所属分层 | 路径 | 作用 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | 控制器 | Controller | 适配器层 | `adapter/controller` | 处理 HTTP 请求，作为外部系统入口，负责参数校验、调用应用层服务、返回响应 |
| 2 | 命令处理器 | Command Handler | 应用层 | `application/command` | 处理写操作（创建、更新、删除），协调领域对象完成业务逻辑，一个命令对应一个明确的业务意图 |
| 3 | 命令数据传输对象 | Command DTO | 应用层 | `application/command/dto` | 封装命令请求的数据，用于在控制器和命令处理器之间传递写操作所需的信息 |
| 4 | 查询处理器 | Query Handler | 应用层 | `application/query` | 处理读操作（查询），直接返回数据视图，可绕过领域层以提高查询性能 |
| 5 | 查询数据传输对象 | Query DTO | 应用层 | `application/query/dto` | 封装查询请求参数或查询响应数据，针对读操作进行优化设计 |
| 6 | 初始化器 | Initializer | 应用层 | `application/initialize` | 处理系统启动时的初始化逻辑，如数据预加载、默认数据创建、缓存预热等 |
| 7 | 防腐层接口 | Gateway | 应用层 | `application/gateway` | 定义调用外部模块的接口，保护本模块领域模型免受外部变化影响 |
| 8 | 防腐层实现 | Gateway Implementation | 适配器层 | `adapter/gateway` | 实现防腐层接口，实际调用外部模块服务，处理技术细节和数据转换 |
| 9 | 领域模型/实体 | Model/Entity | 领域层 | `domain/model` | 核心业务对象，具有唯一标识和生命周期，封装业务规则和行为 |
| 10 | 领域仓储 | Repository | 领域层 | `domain/repository` | 定义领域对象的持久化接口，屏蔽底层数据访问细节，只定义契约，具体实现在基础设施层 |
| 11 | 领域服务 | Domain Service | 领域层 | `domain/service` | 封装不适合放在单个实体中的业务逻辑，处理跨实体的复杂业务规则 |
| 12 | 领域事件 | Domain Event | 领域层 | `domain/event` | 记录领域内的重要业务事件，用于实现最终一致性、解耦子系统、发布订阅模式 |
| 13 | 值对象 | Value Object | 领域层 | `domain/value` | 描述特征但无身份标识的对象，通过属性值判断相等性，如 Money、Address 等 |
| 14 | 领域异常 | Domain Exception | 领域层 | `domain/exception` | 定义业务规则违反时的自定义异常，用于表达领域特定的错误情况 |

