//package vip.geekclub.test.manager.command;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import vip.geekclub.common.command.CommandBus;
//import vip.geekclub.common.command.CommandResult;
//import vip.geekclub.common.exception.NotFoundException;
//import vip.geekclub.common.exception.ValidationException;
//import vip.geekclub.manager.department.command.dto.CreateDepartmentCommand;
//import vip.geekclub.manager.department.command.dto.DeleteDepartmentCommand;
//import vip.geekclub.manager.department.command.dto.UpdateDepartmentCommand;
//import vip.geekclub.manager.department.common.DepartmentStatus;
//import vip.geekclub.manager.department.domain.ContactInfo;
//import vip.geekclub.manager.department.common.SortOrder;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class DepartmentCommandTest {
//
//    @Autowired
//    private CommandBus commandBus;
//
//    @Test
//    void shouldCreateDepartmentSuccessfully() {
//        // Given
//        CreateDepartmentCommand command = new CreateDepartmentCommand(
//                "测试部门",
//                SortOrder.of(1),
//                1L, // 父部门ID
//                ContactInfo.of("张三", "13800138000"),
//                "测试部门描述"
//        );
//
//        // When
//        CommandResult<Long> result = commandBus.dispatch(command);
//
//        // Then
//        assertNotNull(result);
//        assertNotNull(result.primaryKey());
//        assertTrue(result.primaryKey() > 0);
//        assertEquals("部门创建成功", result.message());
//    }
//
//    @Test
//    void shouldFailWhenCreateDepartmentWithInvalidParent() {
//        // Given
//        CreateDepartmentCommand command = new CreateDepartmentCommand(
//                "测试部门",
//                SortOrder.of(1),
//                999L, // 不存在的父部门ID
//                ContactInfo.of("张三", "13800138000"),
//                "测试部门描述"
//        );
//
//        // When & Then
//        assertThrows(ValidationException.class, () -> {
//            commandBus.dispatch(command);
//        });
//    }
//
//    @Test
//    void shouldFailWhenCreateDepartmentWithInvalidPhone() {
//        // Given
//        CreateDepartmentCommand command = new CreateDepartmentCommand(
//                "测试部门3",
//                SortOrder.of(1),
//                1L,
//                ContactInfo.of("张三", "100"),
//                "测试部门描述"
//        );
//        commandBus.dispatch(command);
////        // When & Then
////        assertThrows(ValidationException.class, () -> {
////            commandBus.dispatch(command);
////        });
//    }
//
//    @Test
//    void shouldUpdateDepartmentSuccessfully() {
//        // Given
//        Long departmentId = 2L; // 使用已存在的部门ID
//        UpdateDepartmentCommand command = new UpdateDepartmentCommand(
//                departmentId,
//                "更新后的部门名称",
//                SortOrder.of(1),
//                ContactInfo.of("李四", "13900139000"),
//                DepartmentStatus.ENABLED,
//                "更新后的描述"
//        );
//
//        // When
//        CommandResult<Void> result = commandBus.dispatch(command);
//
//        // Then
//        assertNotNull(result);
//        assertEquals("部门更新成功", result.message());
//    }
//
//    @Test
//    void shouldFailWhenUpdateNonExistentDepartment() {
//        // Given
//        UpdateDepartmentCommand command = new UpdateDepartmentCommand(
//                999L, // 不存在的部门ID
//                "更新后的部门名称",
//                SortOrder.of(2),
//                ContactInfo.of("李四", "13900139000"),
//                DepartmentStatus.ENABLED,
//                "更新后的描述"
//        );
//
//        // When & Then
//        assertThrows(NotFoundException.class, () -> {
//            commandBus.dispatch(command);
//        });
//    }
//
//    @Test
//    void shouldDeleteDepartmentSuccessfully() {
//        // Given - 先创建一个部门用于删除
//        CreateDepartmentCommand createCommand = new CreateDepartmentCommand(
//                "待删除部门",
//                SortOrder.of(1),
//                1L,
//                ContactInfo.of("王五", "13700137000"),
//                "待删除的部门"
//        );
//        CommandResult<Long> createResult = commandBus.dispatch(createCommand);
//        Long departmentId = createResult.primaryKey();
//
//        DeleteDepartmentCommand deleteCommand = new DeleteDepartmentCommand(departmentId);
//
//        // When
//        CommandResult<Void> result = commandBus.dispatch(deleteCommand);
//
//        // Then
//        assertNotNull(result);
//        assertEquals("部门删除成功", result.message());
//    }
//
//    @Test
//    void shouldFailWhenDeleteNonExistentDepartment() {
//        // Given
//        DeleteDepartmentCommand command = new DeleteDepartmentCommand(999L);
//
//        // When & Then
//        assertThrows(NotFoundException.class, () -> {
//            commandBus.dispatch(command);
//        });
//    }
//
//    @Test
//    void shouldFailWhenCreateDepartmentWithDuplicateName() {
//        // Given - 使用已存在的部门名称
//        CreateDepartmentCommand command = new CreateDepartmentCommand(
//                "计算机系", // 已存在的部门名称
//                SortOrder.of(1),
//                1L,
//                ContactInfo.of("张三", "13800138000"),
//                "重复名称测试"
//        );
//
//        // When & Then
//        assertThrows(ValidationException.class, () -> {
//            commandBus.dispatch(command);
//        });
//    }
//}
