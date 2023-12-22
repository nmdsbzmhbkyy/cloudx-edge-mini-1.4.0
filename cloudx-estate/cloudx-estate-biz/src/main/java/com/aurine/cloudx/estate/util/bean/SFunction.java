package com.aurine.cloudx.estate.util.bean;

import java.io.Serializable;
import java.util.function.Function;

/**
 * <p>
 *  复制 {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction}
 * </p>
 *
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
