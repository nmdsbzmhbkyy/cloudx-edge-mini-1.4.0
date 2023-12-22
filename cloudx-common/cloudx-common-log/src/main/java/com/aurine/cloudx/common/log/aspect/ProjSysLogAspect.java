package com.aurine.cloudx.common.log.aspect;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.common.log.annotation.ProjSysLog;
import com.aurine.cloudx.common.log.event.ProjSysLogEvent;
import com.aurine.cloudx.common.log.util.SysLogUtils;
import com.aurine.cloudx.estate.entity.SysOperationLog;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 操作日志使用spring event异步入库
 *
 * @author L.cm
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class ProjSysLogAspect {
	private final ApplicationEventPublisher publisher;

	@SneakyThrows
	@Around("@annotation(projSysLog)")
	public Object around(ProceedingJoinPoint point, ProjSysLog projSysLog) {
		String strClassName = point.getTarget().getClass().getName();
		String strMethodName = point.getSignature().getName();
		log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

		SysOperationLog logVo = SysLogUtils.getSysLog();
		logVo.setTitle(projSysLog.value());
		// 发送异步日志事件
		Long startTime = System.currentTimeMillis();
		Object obj = point.proceed();
		Long endTime = System.currentTimeMillis();
		logVo.setTime((endTime - startTime) + "");
		
		if (SecurityUtils.getUser() != null) {
		    logVo.setUserid(SecurityUtils.getUser().getId());
		}
		
		if (ProjectContextHolder.getProjectId() != null) {
            logVo.setProjectId(ProjectContextHolder.getProjectId());
        }
		
		if (TenantContextHolder.getTenantId() != null) {
		    logVo.setTenantId(TenantContextHolder.getTenantId());
        }
		publisher.publishEvent(new ProjSysLogEvent (logVo));
		return obj;
	}

}
