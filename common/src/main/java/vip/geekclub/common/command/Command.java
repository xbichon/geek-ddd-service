package vip.geekclub.common.command;

/**
 * 命令标记接口
 * <p>
 * 在DDD架构中，命令代表用户的操作意图，用于封装业务操作的输入参数。
 * 所有具体的命令类都应该实现此接口，以便被命令总线识别和处理。
 * <p>
 * 命令应该是不可变的值对象，包含执行特定业务操作所需的所有数据。
 * 
 * @author leo
 */
public interface Command {
}