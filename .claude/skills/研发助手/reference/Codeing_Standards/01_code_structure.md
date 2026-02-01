## 一、代码结构
架构采用领域驱动、CQRS的架构模式，系统分为三层，分别是适配器层(adapter)，应用层(application)和领域层(domain)。
 ```
 ├── vip/geekclub/{业务领域}/
 │   ├── adapter/              # 适配器层（防腐层）
 │   │   ├── controller/       # 控制器（处理HTTP请求）
 │   ├── application/          # 应用层
 │   │   ├── command/          # 命令处理器（写操作）
 │   │   │   ├── dto/          # 命令数据传输对象
 │   │   └── query/            # 查询处理器（读操作）
 │   │   │   └── dto/          # 查询数据传输对象
 │   │   ├── initialize/       # 初始化相关
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

### 1. Controller（控制器）
- **所属分层**：适配器层 (adapter)
- **作用**：处理 HTTP 请求，作为外部系统与应用程序的入口，负责参数校验、调用应用层服务、返回响应

### 2. Command Handler（命令处理器）
- **所属分层**：应用层 (application/command)
- **作用**：处理写操作（创建、更新、删除），协调领域对象完成业务逻辑，一个命令对应一个明确的业务意图

### 3. Command DTO（命令数据传输对象）
- **所属分层**：应用层 (application/command/dto)
- **作用**：封装命令请求的数据，用于在控制器和命令处理器之间传递写操作所需的信息

### 4. Query Handler（查询处理器）
- **所属分层**：应用层 (application/query)
- **作用**：处理读操作（查询），直接返回数据视图，可绕过领域层以提高查询性能

### 5. Query DTO（查询数据传输对象）
- **所属分层**：应用层 (application/query/dto)
- **作用**：封装查询请求参数或查询响应数据，针对读操作进行优化设计

### 6. Initializer（初始化器）
- **所属分层**：应用层 (application/initialize)
- **作用**：处理系统启动时的初始化逻辑，如数据预加载、默认数据创建、缓存预热等

### 7. Model/Entity（领域模型/实体）
- **所属分层**：领域层 (domain/model)
- **作用**：核心业务对象，具有唯一标识和生命周期，封装业务规则和行为

### 8. Repository（领域仓储）
- **所属分层**：领域层 (domain/repository)
- **作用**：定义领域对象的持久化接口，屏蔽底层数据访问细节，只定义契约，具体实现在基础设施层

### 9. Domain Service（领域服务）
- **所属分层**：领域层 (domain/service)
- **作用**：封装不适合放在单个实体中的业务逻辑，处理跨实体的复杂业务规则

### 10. Domain Event（领域事件）
- **所属分层**：领域层 (domain/event)
- **作用**：记录领域内的重要业务事件，用于实现最终一致性、解耦子系统、发布订阅模式

### 11. Value Object（值对象）
- **所属分层**：领域层 (domain/value)
- **作用**：描述特征但无身份标识的对象，通过属性值判断相等性，如 Money、Address 等

### 12. Domain Exception（领域异常）
- **所属分层**：领域层 (domain/exception)
- **作用**：定义业务规则违反时的自定义异常，用于表达领域特定的错误情况

