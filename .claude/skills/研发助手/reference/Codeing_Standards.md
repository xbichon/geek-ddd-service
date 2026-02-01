# 编码规范
## 一、文件分类
架构采用领域驱动、CQRS的架构模式，系统分为三层，分别是适配器层(adapter)，应用层(application)和领域层(domain)。
 ```
 ├── vip/geekclub/{业务领域}/
 │   ├── adapter/              # 适配器层（防腐层）
 │   │   ├── controller/       # 控制器（处理HTTP请求）
 │   ├── application/          # 应用层
 │   │   ├── command/          # 命令处理器（写操作）
 │   │   │   ├── dto/          # 命令数据传输对象
 │   │   └── query/            # 查询处理器（读操作）
 │   │   │   └── dto/          # 查询数据传输对象
 │   │   ├── initialize/       # 初始化相关
 │   ├── domain/               # 领域层
 │   │   ├── model/            # 领域模型
 │   │   ├── repository/       # 领域仓储
 │   │   ├── service/          # 领域服务
 │   │   ├── event/            # 领域事件
 │   │   ├── value/            # 值对象
 │   │   └── exception/        # 领域异常
```
## 二、适配器层

## 三、应用层

## 四、领域层
领域层包括模型、仓储、服务、事件、值对象、异常，其编写规范如下:
### 1. 模型
#### (1) 规范
- 文件放置在`vip.geekclub.{业务领域}.domain.model`包下；
- 采用JPA的规范定义，
  * 必须包含ID主键以及对应的注解`@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`；
  * 类必须用`@Entity`注解标记，用`@Table`注解指定表名；
- 必须继承`vip.geekclub.framework.domain.model.AggregateRoot`抽象类；
- 采用lombok 注解，要求尽量保证所有的属性、默认构造函数，在没有必要的情况下使用private限制:
  * 使用`@Getter @Setter(AccessLevel.PRIVATE)`注解定义属性的访问权限；
  * 使用`@NoArgsConstructor(access = AccessLevel.PROTECTED)`注解定义无参构造函数；

#### (2) 示例
```java
package vip.geekclub.security.domain.model;

@Entity
@Table(name = "security_principal")
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Principal implements AggregateRoot<Long> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String userType;

    public Principal(String userType) {
        AssertUtil.notNull(userType, () -> "应用类型不能为空");
        this.userType = userType;
    }
}
```
### (4) 数据库迁移模块
