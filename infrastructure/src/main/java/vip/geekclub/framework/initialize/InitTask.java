package vip.geekclub.framework.initialize;

/**
 * 框架初始化接口
 * 用于在框架启动时执行初始化操作
 */
public interface InitTask {

    /**
     * 执行初始化操作
     */
    void initialize();
}