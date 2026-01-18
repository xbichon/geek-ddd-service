package vip.geekclub.security.permission.application.command.dto;

import vip.geekclub.framework.command.Command;

public record DeletePermissionCommand(Long id) implements Command {
}