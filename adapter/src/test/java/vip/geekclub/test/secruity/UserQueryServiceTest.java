//package vip.geekclub.test.secruity;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import vip.geekclub.security.auth.query.dto.UserResult;
//import vip.geekclub.security.permission.query.PermissionQueryService;
//import vip.geekclub.security.auth.query.UserQueryService;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//@SpringBootTest
//public class UserQueryServiceTest {
//
//    @Autowired
//    private UserQueryService userQueryService;
//
//    @Autowired
//    private PermissionQueryService permissionQueryService;
//
//
//    @Test
//    void getUserByIdTest() {
//        Optional<UserResult> userById = userQueryService.getUserById(1L);
//        Assertions.assertFalse(userById.isEmpty(), "查询用户失败");
//    }
//
//    @Test
//    void getPermissionsByUserIdTest() {
//        Set<String> permissionsByUserId = permissionQueryService.getPermissionByUserId(1L);
//        Assertions.assertFalse(permissionsByUserId.isEmpty(), "查询权限失败");
//    }
//}
