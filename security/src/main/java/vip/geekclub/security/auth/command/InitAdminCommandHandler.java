package vip.geekclub.security.auth.command;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.security.auth.command.dto.InitAdminCommand;
import vip.geekclub.security.auth.domain.*;

@Slf4j
@AllArgsConstructor
@Service
public class InitAdminCommandHandler implements CommandHandler<InitAdminCommand, Void> {

    private final UserPrincipalRepository userPrincipalRepository;
    private final CredentialRepository credentialRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(InitAdminCommand command) {
        // 1. 检查是否已存在用户
        if (userPrincipalRepository.existsBy()) {
            return CommandResult.ok("管理员已存在，无需初始化");
        }

        // 2. 创建超级管理员用户
        UserPrincipal userPrincipal = UserPrincipal.newTeacherAdmin();
        userPrincipalRepository.save(userPrincipal);

        // 3. 创建管理员默认认证信息
        Credential adminDefaultCredential = Credential.newUsernameAuth(
                userPrincipal.getId(),
                "admin",
                "123456"
        );
        credentialRepository.save(adminDefaultCredential);

        log.info("管理员不存在，初始化管理员成功");
        return CommandResult.ok("管理员初始化成功");
    }
}