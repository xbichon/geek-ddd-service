# 模块初始化操作文档
## 一、文档描述
该文档描述了如何在创建完一个模块后，进行初始化的一些操作,模块指的是一个界限上下文，也是JAVA中的一个model，它的命名遵循context.{领域模块}。

## 二、场景步骤
初始化的工作分为四个步骤，这里将按步骤进行讲解： 
### 步骤1：初始化前工作
1. 确定模块的路径，例如：context.security;
2. 确定模块下已经包含对应的包，格式为：vip.geekclub.{模块名}，例如：vip.geekclub.security。
    - 如果没有包含，根据模块的命名，创建对应的包，模块名要用对应的英文。

### 步骤2：创建目录结构
1. 阅读规范文件[STRUCTURE.md](Structure_Standards.md)，在规范中找到模块的目录结构，并创建对应的目录结构。

### 步骤3：修改POM文件
1. 在当前模块的pom.xml文件，添加插件，内容如下：
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>properties-maven-plugin</artifactId>
        </plugin>
        <plugin>
            <groupId>org.jooq</groupId>
            <artifactId>jooq-codegen-maven</artifactId>
            <configuration>
                <generator>
                    <database>
                        <includes>security_.*</includes>
                    </database>
                </generator>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 步骤4：创建数据迁移文件
在 infrastructure/src/main/resources/database/migrations 目录下创建{模块名}文件夹，并创建一个init.xml文件，内容如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
         https://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.9.xsd">
</databaseChangeLog>
```