# 代码编写流程

## 步骤

1. **需求分析**：阅读[代码结构](Codeing_Standards/01_code_structure.md)，分析需求涉及哪些类；

2. **制定计划**：列出需编写的类清单，常见组合如下：

   | 场景 | 需编写的类 |
   |:---|:---|
   | 写操作（增删改） | Controller + Command + Domain |
   | 读操作（查询） | Controller + Query |
   | 复杂业务逻辑 | + Domain Service |
   | 数据变更通知 | + Domain Event |
   | 调用外部模块 | + Gateway（防腐层） |
   | 新增/修改表结构 | 先 Migration |

3. **按清单编写**：每类代码必须参考对应规范文档：
   - Controller → [04_controller.md](Codeing_Standards/04_controller.md)
   - Command → [05_handler.md](Codeing_Standards/05_command_handler.md) + [06_dto.md](Codeing_Standards/06_command_dto.md)
   - Query → [07_service.md](Codeing_Standards/07_query_handler.md) + [08_dto.md](Codeing_Standards/08_query_dto.md)
   - Domain → [02_model.md](Codeing_Standards/02_domain_model.md) + [09_service.md](Codeing_Standards/09_domain_service.md)
   - Gateway → [12_gateway.md](Codeing_Standards/12_gateway.md)
   - Migration → [DataBase_migration.md](Migration/DataBase_migration.md)

## 原则

- 先分析再编码，禁止边写边想；
- 优先 Command/Query，避免过度设计；
- 控制器只转发，无业务逻辑；
- 按角色分包：`controller.teacher`、`controller.student`。