package vip.geekclub.security.application.command.principal;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.security.domain.model.Principal;
import vip.geekclub.security.domain.repository.PrincipalRepository;

@AllArgsConstructor
@Service
public class DeletePrincipalCommandHandler implements VoidCommandHandler<DeletePrincipalCommand> {

    private final PrincipalRepository principalRepository;

    @Override
    @Transactional
    public void executeVoid(DeletePrincipalCommand command) {
        // 1. 获取用户
        Principal principal = principalRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("用户不存在"));

        // 2. 删除用户
        principalRepository.delete(principal);
    }
}