package vip.geekclub.security.application.command.role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.security.domain.model.Role;
import vip.geekclub.security.domain.repository.RoleRepository;

/**
 * 初始化系统管理员角色命令处理器
 */
@Service
@AllArgsConstructor
public class InitializeSystemAdminRoleCommandHandler implements VoidCommandHandler<InitializeSystemAdminRoleCommand> {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void executeVoid(InitializeSystemAdminRoleCommand command) {
        // 检查该用户类型下是否已存在系统管理员角色
        if (roleRepository.existsByUserTypeAndSystemAdminTrue(command.userType())) {
            throw new BusinessException(409, "该用户类型的系统管理员角色已存在");
        }

        // 创建系统管理员角色
        Role systemAdminRole = Role.createSystemAdminRole(command.userType());
        roleRepository.save(systemAdminRole);
    }
}
