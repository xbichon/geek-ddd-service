//package vip.geekclub.test.secruity;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import vip.geekclub.common.command.CommandBus;
//import vip.geekclub.common.command.CommandResult;
//import vip.geekclub.security.auth.command.dto.CreateUserCommand;
//import vip.geekclub.security.auth.common.UserType;
//
//@SpringBootTest
//public class CreateUserCommandTest {
//
//    @Autowired
//    private CommandBus commandBus;
//
//    @Test
//    public void testCreateUser() {
//        CreateUserCommand command = new CreateUserCommand("leo2", UserType.TEACHER,"123456");
//        CommandResult<Long> result = commandBus.dispatch(command);
//        Long userId = result.primaryKey();
//        System.out.println("用户创建成功，ID: " + userId + ", 消息: " + result.message());
//    }
//}
