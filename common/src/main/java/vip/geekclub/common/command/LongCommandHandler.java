package vip.geekclub.common.command;

public interface LongCommandHandler<C extends Command> extends CommandHandler<C, Long> {
    @Override
    default CommandResult<Long> execute(C command) {
        Long id = process(command);
        return CommandResult.ok(id);
    }

    /**
     * 处理命令
     *
     * @param command 要处理的命令对象
     */
    Long process(C command);
}
