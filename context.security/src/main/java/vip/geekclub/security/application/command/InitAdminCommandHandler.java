package vip.geekclub.security.application.command;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.security.application.command.dto.InitAdminCommand;
import vip.geekclub.security.domain.model.Credential;
import vip.geekclub.security.domain.repository.CredentialRepository;
import vip.geekclub.security.domain.model.Principal;
import vip.geekclub.security.domain.repository.PrincipalRepository;
import vip.geekclub.security.domain.value.CredentialType;

@Slf4j
@AllArgsConstructor
@Service
public class InitAdminCommandHandler implements CommandHandler<InitAdminCommand, Void> {

    private final PrincipalRepository principalRepository;
    private final CredentialRepository credentialRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(InitAdminCommand command) {
        // 1. 检查是否已存在用户
        if (principalRepository.existsByUserType(command.userType())) {
            log.info("管理员已存在，无需初始化");
            return CommandResult.ok();
        }

        // 2. 创建超级管理员用户
        Principal principal = Principal.newAdmin(command.userType());
        principalRepository.save(principal);

        // 3. 创建管理员默认认证信息
        Credential adminDefaultCredential = new Credential(
                principal.getId(),
                CredentialType.USERNAME,
                command.username(),
                command.password()
        );
        credentialRepository.save(adminDefaultCredential);

        log.info("管理员不存在，初始化管理员成功");
        return CommandResult.ok();
    }
}