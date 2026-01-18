package vip.geekclub.security.permission.application.command.dto;

import vip.geekclub.common.command.Command;

public record DeletePermissionGroupCommand(Long id) implements Command {
}