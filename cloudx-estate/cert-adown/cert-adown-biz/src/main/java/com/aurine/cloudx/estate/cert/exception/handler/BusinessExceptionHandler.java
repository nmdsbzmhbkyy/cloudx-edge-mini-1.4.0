package com.aurine.cloudx.estate.cert.exception.handler;

import com.aurine.cloudx.estate.cert.constant.ADownStateEnum;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import com.aurine.cloudx.estate.cert.exception.BusinessException;
import com.aurine.cloudx.estate.cert.service.SysCertAdownRequestService;
import com.aurine.cloudx.estate.cert.util.Md5CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 异常处理接口
 */
@Component
@Slf4j
public class BusinessExceptionHandler {
	@Resource
	private SysCertAdownRequestService sysCertAdownRequestService;

	/**
	 * 处理异常
	 *
	 * @param exception
	 */
	public void handle(BusinessException exception, SysCertAdownRequest sysCertAdownRequest) {
		String errorMsg = exception.getErrorCode().getDesc();
		//删除MD5缓存
		Md5CacheUtil.removeMd5(sysCertAdownRequest);

		switch (exception.getErrorCode().getHandleType()) {
			case TO_DB:

				if (!StringUtils.isEmpty(exception.getMessage())) {
					errorMsg += " " + exception.getMessage();
				}

				log.error("处理入库异常，{}", errorMsg);

				sysCertAdownRequest.setState(ADownStateEnum.ERROR.getCode());
				sysCertAdownRequest.setErrMsg(errorMsg);

				sysCertAdownRequestService.updateById(sysCertAdownRequest);
				break;
			default:
				log.error("处理其他异常，{}", exception.getErrorCode().getDesc());
				break;
		}

	}
}
