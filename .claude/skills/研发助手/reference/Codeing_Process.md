# 代码编写操作文档

## 一、操作步骤

- 第1步：阅读[代码结构及分类](Codeing_Standards/01_code_structure.md)文档，并做用户需求的设计分析，判断需要编写哪些代码及代码的类型；
- 第2步：根据代码的类型，阅读对应的编写规范，并根据需求进行相应的编写，每个类型对应的规范文档如下：
    * 控制器：[04_controller.md](Codeing_Standards/04_controller.md)
    * 命令处理器：[05_command_handler.md](Codeing_Standards/05_command_handler.md)
    * 命令数据传输对象：[06_command_dto.md](Codeing_Standards/06_command_dto.md)
    * 查询处理器：[07_query_handler.md](Codeing_Standards/07_query_handler.md)
    * 查询数据传输对象：[08_query_dto.md](Codeing_Standards/08_query_dto.md)
    * 领域模型：[02_domain_model.md](Codeing_Standards/02_domain_model.md)
    * 领域仓储：[03_repository.md](Codeing_Standards/03_repository.md)
    * 领域服务：[09_domain_service.md](Codeing_Standards/09_domain_service.md)
    * 领域事件：[10_domain_event.md](Codeing_Standards/10_domain_event.md)
    * 值对象：[11_value_object.md](Codeing_Standards/11_value_object.md)
    * 数据库迁移脚本：
- 第3步：如新增/修改领域模型或值对象，需要阅读[12_database_migration.md](Codeing_Standards/12_database_migration.md)规范文档，并编写对应的数据库迁移脚本
