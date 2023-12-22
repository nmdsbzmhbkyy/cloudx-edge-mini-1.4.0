package com.aurine.cloudx.edge.sync.biz.annotation;

import javax.validation.constraints.NotNull;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 *
 * @author huxiang
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Dislock {

    /**
     * 分布式锁的key，一般可以用用户id，用户token这种作为唯一的key，达到自己的锁只能被自己解的效果，如#userId
     */
    @NotNull
    String localKey();

    /**
     * 业务分类 默认为All不分类，建议分类
     *
     * @return
     */
    String biz() default "ALL";

    /**
     * 锁等待时间 默认20秒
     *
     * @return
     */
    long waitTime() default 20 * 1000;

    /**
     * 锁释放时间 默认10秒
     *
     * @return
     */
    long leaseTime() default 10 * 1000;

    /**
     * 时间格式 默认：毫秒
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}

