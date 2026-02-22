# 数据库迁移脚本编写规范

## 1、基础规范

### 1.1 文件组织

- 文件放置在 `infrastructure/src/main/resources/db/migration/{业务领域}/` 目录下
- 文件名格式：`V{日期序号}__{描述}.xml`，如 `V20250207_01__add_advisor_table.xml`

### 1.2 变更集规范

每个 `changeSet` 必须包含：
- `id`：唯一标识，格式 `{日期}-{序号}`，如 `20250207-1`
- `author`：变更作者
- 表名前缀：使用 `{业务领域}_` 作为前缀，如 `internship_advisor`
- 必须包含标准字段：`id`, `create_time`, `update_time`, `version`

### 1.3 建表示例

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
    </changeSet>

</databaseChangeLog>
```

## 2、索引规范

### 2.1 何时需要创建索引

参考[查询处理器规范](../Codeing_Standards/07_query_handler.md#22-索引设计要求)中的索引场景清单。常见场景：
- WHERE 等值查询、范围查询
- JOIN 关联条件
- ORDER BY 排序
- 多条件组合查询

### 2.2 命名规范

| 索引类型 | 命名格式 | 示例 |
|:---|:---|:---|
| 主键 | `pk_{表名}` | `pk_internship_advisor` |
| 唯一索引 | `uk_{表名}_{字段名}` | `uk_internship_intern_student_no` |
| 普通索引 | `idx_{表名}_{字段名}` | `idx_internship_selector_student_id` |
| 复合索引 | `idx_{表名}_{字段1}_{字段2}` | `idx_internship_intern_class_advisor` |

### 2.3 索引示例

```xml
<changeSet id="20250207001-2" author="developer">
    <!-- 单列索引 -->
    <createIndex tableName="internship_selector" indexName="idx_internship_selector_student_id">
        <column name="student_id"/>
    </createIndex>

    <!-- 复合索引 -->
    <createIndex tableName="internship_intern" indexName="idx_internship_intern_class_advisor">
        <column name="class_name"/>
        <column name="advisor_name"/>
    </createIndex>

    <!-- 唯一索引 -->
    <createIndex tableName="internship_intern" indexName="uk_internship_intern_student_no" unique="true">
        <column name="student_no"/>
    </createIndex>
</changeSet>
```