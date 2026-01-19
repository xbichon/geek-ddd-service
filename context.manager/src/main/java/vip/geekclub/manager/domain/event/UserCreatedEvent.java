package vip.geekclub.manager.domain.event;

import vip.geekclub.framework.domain.DomainEvent;
import java.util.UUID;

public record UserCreatedEvent(Long id, String email, String phone, UUID externalUuid)
        implements DomainEvent {
}