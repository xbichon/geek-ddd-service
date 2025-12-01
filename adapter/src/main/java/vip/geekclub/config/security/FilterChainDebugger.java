//package vip.geekclub.config.security;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.web.FilterChainProxy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * 过滤器链调试工具
// * 仅在开发环境启用，用于查看当前配置的所有过滤器
// */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class FilterChainDebugger {
//
//    private final FilterChainProxy filterChainProxy;
//
//    @PostConstruct
//    public void init() {
//        List<SecurityFilterChain> filterChains = filterChainProxy.getFilterChains();
//        log.info("=== Security Filter Chain Configuration ===");
//
//        int chainIndex = 1;
//        for (SecurityFilterChain filterChain : filterChains) {
//            log.info("Filter Chain {}: {}", chainIndex++, filterChain.getClass().getName());
//
//            // 获取此链中的所有过滤器
//            filterChainProxy.getFilters("/").forEach(filter ->
//                log.info("  - {}", filter.getClass().getName())
//            );
//        }
//
//        log.info("=========================================");
//    }
//}