package vip.geekclub.common.command;

public abstract class CommandHandlerChain {

    private CommandHandlerChain chain = null;

    public void setNextChain(CommandHandlerChain nextChain) {
        if (chain != null) {
            throw new IllegalStateException("HandlerChain 已经设置过了");
        }
        this.chain = nextChain;
    }

    /**
     * 处理命令
     *
     * @param command 要处理的命令对象
     */
    public <R> CommandResult<R> handle(Command command) {
        return handle(command, this.chain);
    }

    /**
     * 处理命令
     *
     * @param command 要处理的命令对象
     */
    protected abstract <R> CommandResult<R> handle(Command command, CommandHandlerChain chain);
}
