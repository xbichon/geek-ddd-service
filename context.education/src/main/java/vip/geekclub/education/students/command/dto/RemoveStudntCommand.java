package vip.geekclub.education.students.command.dto;

import vip.geekclub.framework.command.Command;

public record RemoveStudntCommand(Long id) implements Command {
}
