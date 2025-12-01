package vip.geekclub.common.command;

public interface VoidCommandHandler<C extends Command> extends CommandHandler<C, Void> {

    @Override
    default CommandResult<Void> execute(C command) {
        process(command);
        return CommandResult.ok();
    }

    /**
     * 处理命令
     *
     * @param command 要处理的命令对象
     */
    void process(C command);
}
