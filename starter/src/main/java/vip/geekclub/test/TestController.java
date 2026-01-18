//package vip.geekclub.test;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import vip.geekclub.common.controller.ApiResponse;
//import vip.geekclub.security.auth.adapter.controller.dto.WechatLoginRequest;
//import vip.geekclub.security.auth.application.query.dto.UserResult;
//import vip.geekclub.security.auth.domain.AccountType;
//
///**
// * 测试控制器
// * 用于系统测试和调试
// */
//@RestController
//@RequestMapping("/test")
//public class TestController {
//
//    /**
//     * 测试接口
//     * 返回模拟用户数据用于测试
//     */
//    @GetMapping("/home")
//    public ApiResponse<UserResult> getTestUser() {
//        return ApiResponse.success(new UserResult(1L, AccountType.TEACHER, true));
//    }
//
//    @PostMapping("/home")
//    public ApiResponse<Void> postTestUser(WechatLoginRequest request){
//
//        System.out.println(request);
//        return ApiResponse.success();
//    }
//}
