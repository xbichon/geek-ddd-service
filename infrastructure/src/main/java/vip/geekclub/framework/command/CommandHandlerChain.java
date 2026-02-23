package vip.geekclub.framework.command;

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
     * @param <R>     命令返回类型
     * @return 命令执行结果
     */
    public abstract <R> R handle(Command<R> command);

    /**
     * 执行下一个责任链节点
     *
     * @param command 要处理的命令对象
     * @param <R>     命令返回类型
     * @return 命令执行结果
     */
    protected <R> R next(Command<R> command) {
        if (chain != null) {
            return chain.handle(command);
        }
        throw new IllegalStateException("责任链未正确配置：没有最终的处理器节点");
    }
}
