package com.aurine.cloudx.edge.sync.biz.aop;

import com.aurine.cloudx.edge.sync.biz.annotation.Dislock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分布式锁AOP切面类
 *
 * @author huxiang
 */
@Aspect
@Component
public class DislockAspect {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 通过环绕通知实现加锁解锁
     * 切入@Dislock注解的point
     * @param proceedingJoinPoint
     * @return
     *
     * @throws Throwable
     */
    @Around("@annotation(com.aurine.cloudx.edge.sync.biz.annotation.Dislock)")
    public Object arround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = null;
        RLock lock = null;
        boolean status = false;
        try {
            Dislock dislock = getDislockInfo(proceedingJoinPoint);
            //生成lockKey，保证锁定资源的唯一性，并且只能解锁自己加的锁
            String lockKey = LockKeyParser.parse(proceedingJoinPoint, dislock.localKey(), dislock.biz());
            lock = redissonClient.getLock(lockKey);
            if (lock != null) {
                //试图加锁
                status = lock.tryLock(dislock.waitTime(), dislock.leaseTime(), dislock.timeUnit());
                if (status) {
                    //如果加锁成功，执行切点业务
                    object = proceedingJoinPoint.proceed();
                }
            }
        } finally {
            //判断是否需要解锁
            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return object;
    }

    public Dislock getDislockInfo(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        return methodSignature.getMethod().getAnnotation(Dislock.class);
    }

}
