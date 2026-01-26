package vip.geekclub.security.application.command.permission;

import vip.geekclub.framework.command.Command;

public record DeletePermissionCommand(Long id) implements Command {
}