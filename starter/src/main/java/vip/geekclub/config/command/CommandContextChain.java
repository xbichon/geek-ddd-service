package vip.geekclub.config.command;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.security.UserAuthentication;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandContext;
import vip.geekclub.framework.command.CommandHandlerChain;
import vip.geekclub.framework.command.CommandResult;

/**
 * 用户上下文命令拦截器
 * 在命令执行前从 Spring Security Context 中提取用户信息到 UserContext
 */
@Component
public class CommandContextChain extends CommandHandlerChain {

    @Override
    protected <R> CommandResult<R> handle(Command command, CommandHandlerChain chain) {
        try {
            // 命令执行前：从 SecurityContext 提取用户信息到 UserContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && authentication instanceof UserAuthentication userAuthenticationToken) {
                CommandContext.setCurrentUser(userAuthenticationToken.getPrincipal());
            }
            // 继续执行链
            return chain.handle(command);
        } finally {
            // 命令执行后：清理 ThreadLocal，防止内存泄漏
            CommandContext.clear();
        }
    }
}