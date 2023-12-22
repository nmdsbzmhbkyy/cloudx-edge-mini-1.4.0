package com.aurine.cloudx.common.log.event;

import com.aurine.cloudx.estate.entity.SysOperationLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName:  SysLogEvent   
 * @author: 林港 <ling@aurine.cn>
 * @date:   2020年6月22日 下午4:05:38      
 * @Copyright:
 */
@Getter
@AllArgsConstructor
public class ProjSysLogEvent {
	private final SysOperationLog sysOperationLog;
}
