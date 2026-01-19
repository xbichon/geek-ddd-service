package vip.geekclub.integration.eventhandler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import vip.geekclub.manager.domain.event.UserCreatedEvent;

@Service
public class CreateTeacherEventHandler {

    @EventListener
    public void handle(UserCreatedEvent event) {
        System.out.println("========================");
        System.out.println("处理教师创建事件: " + event);
    }
}
