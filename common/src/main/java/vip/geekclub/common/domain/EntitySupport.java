package vip.geekclub.common.domain;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 基础聚合根类，提供审计信息和版本号
 * 该继承的设计属于过度设计，是为了平衡样板代码、提高编程效率、降低集合中的非业务代码 做的妥协，
 * 为了保持DDD的纯洁，需极致减少逻辑的添加。
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EntitySupport {

    /**
     * 审计信息
     */
    @Embedded
    private AuditInfo auditInfo;

    /**
     * 版本号，用于乐观锁
     */
    @Version
    private Long version;
}
