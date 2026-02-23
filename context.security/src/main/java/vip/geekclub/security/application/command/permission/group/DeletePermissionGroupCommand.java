package vip.geekclub.security.application.command.permission.group;

import vip.geekclub.framework.command.Command;

public record DeletePermissionGroupCommand(Long id) implements Command<Void> {
}