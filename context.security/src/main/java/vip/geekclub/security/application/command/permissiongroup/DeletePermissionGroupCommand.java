package vip.geekclub.security.application.command.permissiongroup;

import vip.geekclub.framework.command.Command;

public record DeletePermissionGroupCommand(Long id) implements Command {
}