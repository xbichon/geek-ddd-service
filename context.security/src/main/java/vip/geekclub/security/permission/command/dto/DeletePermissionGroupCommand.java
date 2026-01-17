package vip.geekclub.security.permission.command.dto;

import vip.geekclub.common.command.Command;

public record DeletePermissionGroupCommand(Long id) implements Command {
}