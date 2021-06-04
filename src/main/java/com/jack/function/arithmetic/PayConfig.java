package com.jack.function.arithmetic;

import java.util.Objects;

/**
 *
 * @author liangchen
 * @date 2021/6/4
 */
@FunctionalInterface
public interface PayConfig {

    void execute(SupportPayMethod supportPayMethod);


    default PayConfig andThen(PayConfig after) {
        Objects.requireNonNull(after);
        return t -> {
             execute(t);
             after.execute(t);
        };

    }
}
