//package vip.geekclub.test.utils;
//
//import vip.geekclub.config.security.JwtPrincipal;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import vip.geekclub.common.utils.JwtUtil;
//import vip.geekclub.security.auth.common.UserType;
//
//@SpringBootTest
//public class JwtAuthUserTokenUtilTest {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Test
//    public void generateTokenTest() {
//        JwtPrincipal jwtUser = new JwtPrincipal(1L, UserType.TEACHER);
//        String token = jwtUtil.generateToken(jwtUser.getMap());
//
//        JwtPrincipal parseResult = JwtPrincipal.buildByJwt(jwtUtil.parseToken(token));
//
//    }
//
//
//}
