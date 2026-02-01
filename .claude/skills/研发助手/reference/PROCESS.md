# 执行流程规范

本文档规范创建模块、目录和文件时的执行步骤和验证要求。

## 1. 规范优先级

当创建模块、目录或文件时，必须遵循以下优先级：

1. **本规范文档**（最高优先级，必须严格遵守）
2. 用户具体需求
3. 现有项目的代码风格（仅限参考注解使用、Lombok 用法等代码风格，**绝不包括目录组织方式**）

**禁止事项：**
- 禁止以现有项目的目录结构作为参考模板
- 禁止在没有阅读规范文档的情况下开始创建
- 禁止用"我看现有代码就是这样写的"作为理由偏离规范
- 禁止凭"经验"或"习惯"替代规范要求

## 2. 执行步骤

### 第1步：阅读规范文档（必须执行）

根据任务类型阅读相应的规范文档：

| 任务类型 | 需要阅读的文档 |
|---------|--------------|
| 创建目录结构 | `STRUCTURE.md` |
| 编写代码文件 | `CODING.md` |
| 完整的模块创建 | `STRUCTURE.md` + `CODING.md` + `PROCESS.md` |

### 第2步：规划目录结构（必须执行）

在创建任何文件前，先根据 `STRUCTURE.md` 规划完整的目录树：

- **不要**参考现有项目的目录结构（即使现有项目结构可能与规范不符）
- 每一个目录的创建都必须有规范依据
- 明确文件放置位置（adapter、application、domain 各层职责）

示例规划：
```
thesistopics/
├── adapter/controller/ThesisTopicController.java
├── application/
│   ├── command/dto/CreateThesisTopicCommand.java
│   ├── command/CreateThesisTopicCommandHandler.java
│   └── query/GetThesisTopicQueryService.java
└── domain/
    ├── model/ThesisTopic.java
    ├── repository/ThesisTopicRepository.java
    └── value/TopicId.java
```

### 第3步：执行创建

按照以下顺序执行：

1. **创建目录** - 使用 `mkdir -p` 命令创建所有需要的目录
2. **创建领域层文件** - 先创建值对象、领域模型、仓储接口
3. **创建应用层文件** - 再创建命令/查询 DTO 和处理器
4. **创建适配器层文件** - 最后创建控制器

### 第4步：验证检查（必须执行）

创建完成后，逐条对照规范验证：

#### 目录结构验证
- [ ] 目录层级是否正确（adapter/application/domain 三层）
- [ ] 文件是否放置在正确的子目录下
- [ ] 包名是否与目录结构一致
- [ ] 是否按业务聚合组织目录

#### 文件命名验证
- [ ] Command DTO 是否以 `Command` 结尾
- [ ] Command Handler 是否以 `CommandHandler` 结尾
- [ ] Query Service 是否以 `QueryService` 结尾
- [ ] Result DTO 是否以 `Result` 结尾
- [ ] Controller 是否以 `Controller` 结尾
- [ ] Repository 是否以 `Repository` 结尾

#### 代码风格验证
- [ ] Lombok 注解使用是否正确
- [ ] Spring 注解使用是否正确
- [ ] Validation 注解使用是否正确
- [ ] 异常处理是否符合规范
- [ ] 时间处理是否使用 java.time

#### 业务逻辑验证
- [ ] 领域实体是否包含静态工厂方法
- [ ] 业务方法是否封装在领域实体中
- [ ] Command/Query 职责是否分离
- [ ] 是否使用了值对象表示 ID

如发现不符合规范的，立即修正。

## 3. 特殊情况处理

### （1）规范冲突处理
- 如果发现现有项目结构与规范不一致，**以规范为准**
- 如果发现规范存在歧义或冲突，先询问用户，不要自行决定

### （2）用户需求冲突
如果用户需求与规范冲突，优先级如下：
1. 用户明确要求的功能需求
2. 规范的目录结构和命名要求
3. 规范的代码风格要求

**示例：** 用户要求某个字段命名不符合规范，应该询问用户确认是否坚持使用该命名。

### （3）缺失规范
如果遇到规范中没有覆盖的情况：
1. 先询问用户是否有具体要求
2. 如无，按照 DDD 最佳实践和现有项目的代码风格（仅代码风格）来决定
3. 将该情况记录下来，建议补充到规范中

## 4. 任务跟踪建议

对于复杂的模块创建任务，建议使用 Task 工具跟踪进度：

```java
TaskCreate([
    "subject": "创建目录结构",
    "description": "...",
    "activeForm": "创建目录结构"
])
```

每个主要步骤创建一个任务，完成后更新状态为 `completed`。