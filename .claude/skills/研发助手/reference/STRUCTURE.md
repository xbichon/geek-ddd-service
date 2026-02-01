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
│    │   │   │   │   │   ├── dto/          # 命令数据传输对象
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

### （1）业务领域模块 (context.*)
- 命名格式：`context.{业务领域}`
- 示例：`context.education`, `context.manager`, `context.security`
- 每个业务领域模块代表一个限界上下文

### （2）分层设计目录
1. **adapter** - 适配器层，对外部系统的适配
2. **application** - 应用层，协调领域对象完成业务逻辑
3. **domain** - 领域层，核心业务逻辑

### （3）应用层子目录
1. **command** - 命令处理（写操作）
2. **query** - 查询处理（读操作）
3. **initialize** - 初始化相关
4. **dto** - 数据传输对象（在 command 或 query 子目录下）

### （4）领域层子目录
1. **model** - 领域模型
2. **repository** - 领域仓储接口
3. **service** - 领域服务
4. **event** - 领域事件
5. **value** - 值对象
6. **exception** - 领域异常

### （5）数据库迁移
- 数据库迁移脚本放在 `infrastructure/src/main/resources/db/migration` 目录下
