# 一、值对象编写规范

## 1、规范

- 文件放置在 `vip.geekclub.{业务领域}.domain.value` 包下；
- 值对象分为两类：
  - **枚举类型**：使用 `enum` 定义固定取值范围的值对象
  - **包装类型**：使用 Java Record 或 final class 定义复杂值对象
- 值对象没有唯一标识，通过属性值判断相等性；
- 值对象是不可变的，创建后不能修改；
- 可以包含验证逻辑，确保值的有效性。

## 2、示例

### 枚举类型值对象

```java
package vip.geekclub.security.domain.value;

public enum UserType {
    /**
     * 学生
     */
    STUDENT,
}
```

### 包装类型值对象

```java
public record PermissionCode(String value) {

    private static final int MAX_LENGTH = 50;

    public PermissionCode {
        AssertUtil.notNull(value, () -> "权限编码不能为空");
        AssertUtil.isTrue(value.length() <= MAX_LENGTH,
            () -> "权限编码长度不能超过" + MAX_LENGTH);
    }

    public static PermissionCode of(String value) {
        return new PermissionCode(value);
    }
}
```

### 复杂值对象

```java
public record Money(
    BigDecimal amount,
    Currency currency
) {
    public Money {
        AssertUtil.notNull(amount, () -> "金额不能为空");
        AssertUtil.notNull(currency, () -> "货币类型不能为空");
        // 确保金额精度
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public Money add(Money other) {
        AssertUtil.isTrue(this.currency == other.currency,
            () -> "不同货币类型不能相加");
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        AssertUtil.isTrue(this.currency == other.currency,
            () -> "不同货币类型不能相减");
        return new Money(this.amount.subtract(other.amount), this.currency);
    }
}

enum Currency {
    CNY, USD, EUR
}
```

## 3、设计原则

- **不可变性**：值对象一旦创建不可修改，修改时返回新的实例；
- **值相等性**：通过属性值判断相等性，而非引用或 ID；
- **自验证**：在构造函数中验证值的合法性；
- **行为丰富**：值对象可以包含与值相关的行为方法，如 `add`、`subtract` 等；
- **细粒度**：值对象应尽可能细粒度，如 `Money`、`Address`、`Email` 等。