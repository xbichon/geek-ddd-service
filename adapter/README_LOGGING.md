# 日志配置说明

## 配置概述

本项目使用Spring Boot默认的Logback日志框架，配置了滚动日志功能，满足以下要求：
- 日志文件存放在运行目录下的`logs`文件夹中
- 支持按大小滚动（单文件最大10MB）
- 支持按天滚动
- 日志保留30天

## 日志文件说明

### 1. 主日志文件
- **文件路径**: `logs/arc.log`
- **滚动规则**: 
  - 按天滚动：`arc.yyyy-MM-dd.i.log`
  - 按大小滚动：单文件最大10MB
- **保留时间**: 30天

### 2. 错误日志文件
- **文件路径**: `logs/error.log`
- **内容**: 仅记录ERROR级别的日志
- **滚动规则**: 同主日志文件
- **保留时间**: 30天

### 3. SQL日志文件
- **文件路径**: `logs/sql.log`
- **内容**: 记录JOOQ和Hibernate的SQL语句
- **滚动规则**: 同主日志文件
- **保留时间**: 30天

## 环境配置

### 开发环境 (dev)
- 控制台输出：开启
- 文件输出：开启
- SQL日志级别：DEBUG
- Hibernate参数日志：TRACE

### 生产环境 (prod)
- 控制台输出：关闭
- 文件输出：开启
- SQL日志级别：INFO

### 默认环境
- 控制台输出：开启
- 文件输出：开启
- SQL日志级别：INFO

## 日志格式

```
时间戳 [线程名] 日志级别 类名 - 日志消息
示例: 2024-01-15 10:30:45.123 [main] INFO  com.example.Controller - 请求处理完成
```

## 使用示例

在Java代码中使用日志：

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YourClass {
    private static final Logger logger = LoggerFactory.getLogger(YourClass.class);
    
    public void someMethod() {
        logger.info("这是一条信息日志");
        logger.error("这是一条错误日志");
        logger.debug("这是一条调试日志");
    }
}
```

## Spring Boot 日志说明

Spring Boot 默认使用 Logback 作为日志实现，通过 `spring-boot-starter-web` 依赖自动引入，无需额外配置依赖。

### 日志配置优先级
1. `logback-spring.xml` (推荐)
2. `logback.xml`
3. `application.yml` 中的 `logging` 配置

## 配置修改

如需修改日志配置，请编辑 `src/main/resources/logback-spring.xml` 文件。

主要配置项：
- `LOG_HOME`: 日志文件存储目录
- `maxFileSize`: 单个日志文件最大大小
- `MaxHistory`: 日志文件保留天数
- 日志级别：TRACE, DEBUG, INFO, WARN, ERROR
