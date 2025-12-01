//package vip.geekclub.test.utils;
//
//import vip.geekclub.common.utils.JwtUtil;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Map;
//
//@SpringBootTest
//class JwtUtilTest {
//
//    @Autowired
//    public JwtUtil jwtUtil;
//
//    @Test
//    void generateToken() {
//
//        String token = jwtUtil.generateToken("1", Map.of("userType", "teacher","userId",2));
//        System.out.println(token);
//    }
//
//    @Test
//    void parseToken() {
//
//        Map<String, Object> parseResult = jwtUtil.parseToken("");
//        System.out.println(parseResult);
//    }
//}