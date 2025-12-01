package vip.geekclub.common.utils;

import java.util.function.Supplier;

/**
 * @author xiongrui
 */
public class LazyUtils<T> {

    private T value;
    private final Supplier<T> supplier;

    public LazyUtils(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T getValue() {
        if (value == null) {
            value = this.supplier.get();
        }

        return value;
    }
}
