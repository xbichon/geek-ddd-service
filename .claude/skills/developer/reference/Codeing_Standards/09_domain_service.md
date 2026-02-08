# 一、领域服务编写规范

## 1、规范

- 文件放置在 `vip.geekclub.{业务领域}.domain.service` 包下；
- 类名使用动词或动词短语命名，如 `IdentifierValidate`、`OrderCalculator`；
- 使用 `@Service` 或 `@Component` 注解标记；
- 领域服务封装不适合放在单个实体中的业务逻辑；
- 处理跨实体的复杂业务规则；
- 不维护自身状态，是无状态的服务类。

## 2、示例

```java
@Service
public class IdentifierValidate {

    List<IdentifierType> identifierTypes = List.of(
            new IdentifierType("phone", "^1[3-9]\\d{9}$"),
            new IdentifierType("email", "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"),
            new IdentifierType("username", "^(?!\\d{11}$)(?![^@]*@.*$)[a-zA-Z0-9_]{4,16}$")
    );
    
    public void validate(IdentifierValue identifierValue) {
        Optional<IdentifierType> identifierType = identifierTypes.stream()
                .filter(type -> type.getType().equals(identifierValue.type()))
                .findFirst();

        if (identifierType.isPresent() && !identifierType.get().match(identifierValue.value())) {
            throw new BusinessException(400, "标识符格式错误");
        }
    }
    
    public void validate(List<IdentifierValue> identifierValues) {
        identifierValues.forEach(this::validate);
    }
}
```

## 3、设计原则

- **无状态性**：领域服务不包含状态，所有操作通过方法参数传入；
- **单一职责**：每个领域服务只处理一类相关业务逻辑；
- **可测试性**：领域服务应易于单元测试，不依赖外部基础设施；
- **与实体协作**：领域服务协调多个实体完成业务操作，但不应替代实体的职责；
- **命名清晰**：使用描述业务行为的动词命名，如 `Validate`、`Calculate`、`Transfer`。