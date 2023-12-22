package com.aurine.cloudx.common.log.event;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import com.aurine.cloudx.estate.entity.SysOperationLog;
import com.aurine.cloudx.estate.feign.RemoteOperationLogService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lengleng
 * 异步监听日志事件
 */
@Slf4j
@AllArgsConstructor
public class ProjSysLogListener {
	private final RemoteOperationLogService remoteLogService;

	@Async
	@Order
	@EventListener(ProjSysLogEvent.class)
	public void saveSysLog(ProjSysLogEvent event) {
	    SysOperationLog sysLog = event.getSysOperationLog();
		remoteLogService.saveLog(sysLog, SecurityConstants.FROM_IN);
	}
}
