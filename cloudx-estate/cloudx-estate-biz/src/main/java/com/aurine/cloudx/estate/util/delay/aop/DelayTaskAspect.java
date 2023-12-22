package com.aurine.cloudx.estate.util.delay.aop;

import cn.hutool.core.util.ArrayUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.delay.TaskUtil;
import com.aurine.cloudx.estate.util.delay.entity.BaseTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>延时任务AOP切面类</p>
 *
 * @author : 王良俊
 * @date : 2021-08-28 11:08:11
 */
@Aspect
@Component
@Slf4j
public class DelayTaskAspect {

    @Resource
    TaskUtil taskUtil;

    static ObjectMapper objectMapper = ObjectMapperUtil.instance();

    /**
     * <p>任务执行前设置项目ID</p>
     */
    @Before("@annotation(com.aurine.cloudx.estate.util.delay.aop.annotations.AutoConfigProjectId)")
    public void initProjectEnvironment(JoinPoint joinPoint) {
        BaseTask task = getBaseTask(joinPoint.getArgs());
        if (task != null) {
            try {
                log.info("[延时任务] 开始设置项目ID：{}", task.getProjectId());
                ProjectContextHolder.setProjectId(task.getProjectId());
                TenantContextHolder.setTenantId(1);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("[延时任务] 自动配置项目ID失败：{}", joinPoint);
    }


    /**
     * <p>任务执行完成后删除任务</p>
     */
    @AfterReturning("@annotation(com.aurine.cloudx.estate.util.delay.aop.annotations.AutoRemoveDelayTask)")
    public void delayConsumerBefore(JoinPoint joinPoint) {
        BaseTask task = getBaseTask(joinPoint.getArgs());
        if (task != null) {
            log.info("[延时任务] 开始删除任务：{}", task.toString());
            taskUtil.removeDelayTask(task);
        } else {
            log.warn("[延时任务] 延时任务删除失败：{}", joinPoint);
        }
    }

    /**
     * <p>任务抛出异常恢复任务为可被同步/执行状态</p>
     */
    @AfterThrowing("execution(* com.aurine.cloudx.estate.util.delay.consumer..*.*(..))")
    public void throwingHandle(JoinPoint joinPoint) {
        BaseTask task = getBaseTask(joinPoint.getArgs());
        log.info("[延时任务] 任务执行失败-准备恢复任务状态：{}", task);
        if (task != null) {
            taskUtil.resetTaskStatus(task);
        }
    }

    private BaseTask getBaseTask(Object[] args) {
        if (ArrayUtil.isNotEmpty(args)) {
            for (Object arg : args) {
                if (arg instanceof ConsumerRecord) {
                    ConsumerRecord<?, ?> record = (ConsumerRecord<?, ?>) arg;
                    try {
                        return objectMapper.readValue(record.value().toString(), BaseTask.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
