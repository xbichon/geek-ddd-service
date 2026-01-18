package vip.geekclub.security.application.command.dto;

import vip.geekclub.framework.command.Command;

public record DeletePermissionGroupCommand(Long id) implements Command {
}