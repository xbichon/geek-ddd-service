package vip.geekclub.security.domain.model;

import jakarta.persistence.*;
import lombok.*;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.domain.model.EntitySupport;

@MappedSuperclass
@Getter
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Credential extends EntitySupport implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的用户ID
     */
    private Long principalId;
}