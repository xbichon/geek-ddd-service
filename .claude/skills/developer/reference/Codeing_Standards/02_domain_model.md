# 一、领域模型编写规范
## 1、规范
- 文件放置在`vip.geekclub.{业务领域}.domain.model`包下；
- 采用JPA的规范定义，
    * 必须包含ID主键以及对应的注解`@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`；
    * 类必须用`@Entity`注解标记，用`@Table`注解指定表名；
- 必须继承`vip.geekclub.framework.domain.model.AggregateRoot`抽象类；
- 采用lombok 注解，要求尽量保证所有的属性、默认构造函数，在没有必要的情况下使用private限制:
    * 使用`@Getter @Setter(AccessLevel.PRIVATE)`注解定义属性的访问权限；
    * 使用`@NoArgsConstructor(access = AccessLevel.PROTECTED)`注解定义无参构造函数；
- 聚合根之间通过外键ID关联，不直接引用实体类；
- 领域层属于命令端，禁止依赖查询端代码；
- 用户未明确指定属性时，询问用户或仅保留ID字段，禁止自行添加属性；

## 2、示例

```java
package vip.geekclub.security.domain.authorization.model;

@Entity
@Table(name = "security_principal")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Principal implements AggregateRoot<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userType;

    public Principal(String userType) {
        AssertUtil.notNull(userType, () -> "应用类型不能为空");
        this.userType = userType;
    }
}
```