# 数据库迁移脚本编写规范

## 1、规范

- 文件放置在 `infrastructure/src/main/resources/database/migrations/{业务领域}/` 目录下
- 文件名格式：`V{日期序号}__{描述}.xml`，如 `V20250207_01__add_advisor_table.xml`
- 每个变更集包含：
    * `id`：唯一标识，格式 `{日期}-{序号}`
    * `author`：变更作者
    * 表名前缀：使用 `{业务领域}_` 作为前缀
    * 必须包含标准字段：`id`, `create_time`, `update_time`, `version`

## 2、示例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
         https://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.9.xsd">

    <changeSet id="20250207001-1" author="developer">
        <createTable tableName="internship_advisor" remarks="指导教师表">
            <column name="id" type="BIGINT" autoIncrement="true" remarks="主键ID">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="name" type="VARCHAR(100)" remarks="姓名">
                <constraints nullable="false"/>
            </column>
            <column name="create_time" type="DATETIME" defaultValueComputed="CURRENT_TIMESTAMP" remarks="创建时间"/>
            <column name="update_time" type="DATETIME" defaultValueComputed="CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP" remarks="更新时间"/>
            <column name="version" type="BIGINT" defaultValue="0" remarks="版本号"/>
        </createTable>

        <createIndex tableName="internship_advisor" indexName="idx_advisor_id" unique="true">
            <column name="id"/>
        </createIndex>
    </changeSet>

</databaseChangeLog>
```