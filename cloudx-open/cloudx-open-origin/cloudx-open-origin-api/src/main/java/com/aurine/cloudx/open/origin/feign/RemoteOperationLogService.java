package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.entity.SysOperationLog;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @ClassName:  RemoteLogService   
 * @author: 林港 <ling@aurine.cn>
 * @date:   2020年6月22日 下午4:10:18      
 * @Copyright:
 */
@FeignClient(contextId = "openRemoteOperationLogService", value = "cloudx-estate-biz")
public interface RemoteOperationLogService {
	/**
	 * 保存日志
	 *
	 * @param sysLog 日志实体
	 * @param from   是否内部调用
	 * @return succes、false
	 */
	@PostMapping("/projLog/save")
	R<Boolean> saveLog(@RequestBody SysOperationLog sysLog, @RequestHeader(SecurityConstants.FROM) String from);
}
