//package vip.geekclub.test.student;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import vip.geekclub.common.command.CommandBus;
//import vip.geekclub.education.students.command.dto.CreateStudentCommand;
//import vip.geekclub.education.students.command.dto.RemoveStudntCommand;
//import vip.geekclub.education.students.common.Sex;
//
//@SpringBootTest
//public class StudentTest {
//
//    @Autowired
//    private CommandBus commandBus;
//
//    @Test
//    public void test() {
//
//        // 创建的命令
//        CreateStudentCommand command=new CreateStudentCommand("张三三三三", Sex.FEMALE);
//
//        // 将命令发布到总线
//        commandBus.dispatch(command);
//    }
//
//    @Test
//    public void test2() {
//
//        // 创建的命令
//        RemoveStudntCommand command=new RemoveStudntCommand(1L);
//
//        // 将命令发布到总线
//        commandBus.dispatch(command);
//    }
//}
