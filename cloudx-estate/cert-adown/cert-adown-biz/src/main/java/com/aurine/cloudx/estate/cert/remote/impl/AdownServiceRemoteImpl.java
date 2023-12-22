package com.aurine.cloudx.estate.cert.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.cert.entity.SysAppConfig;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import com.aurine.cloudx.estate.cert.exception.BusinessException;
import com.aurine.cloudx.estate.cert.exception.code.ErrorCode;
import com.aurine.cloudx.estate.cert.exception.handler.BusinessExceptionHandler;
import com.aurine.cloudx.estate.cert.remote.AdownServiceRemote;
import com.aurine.cloudx.estate.cert.service.SysAppConfigService;
import com.aurine.cloudx.estate.cert.transfer.http.HttpConnector;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @description: 下发服务远程数据传输接口
 * @author: wangwei
 * @date: 2021/12/17 11:04
 **/
@Service
@Slf4j
public class AdownServiceRemoteImpl implements AdownServiceRemote {

	@Resource
	private HttpConnector httpConnector;
	@Resource
	private SysAppConfigService sysAppConfigService;
	@Resource
	private BusinessExceptionHandler businessExceptionHandler;
	@Resource
	KafkaTemplate<String, String> kafkaTemplate;
	private static final String TOPIC_ADOWN_SEND = "ADOWN_SEND";

	/**
	 * 给远端系统发送 下载指令
	 *
	 * @param sysCertAdownRequest
	 */
	@Override
	public boolean adown(SysCertAdownRequest sysCertAdownRequest) {
		//发送请求
		try {
//			httpConnector.post(sysCertAdownRequest, getReqCallbackUrl(sysCertAdownRequest));
			kafkaTemplate.send(TOPIC_ADOWN_SEND, JSONObject.toJSONString(httpConnector.toDto(sysCertAdownRequest)));

			return true;
		} catch (BusinessException be) {
			businessExceptionHandler.handle(be, sysCertAdownRequest);
			return false;
		}
	}

	/**
	 * 给远端系统发送 凭证状态变化
	 *
	 * @param sysCertAdownRequest
	 * @return
	 */
	@Override
	public Boolean update(SysCertAdownRequest sysCertAdownRequest) {
		//发送请求
		try {
//			httpConnector.post(sysCertAdownRequest, getRespCallbackUrl(sysCertAdownRequest));
			return true;
		} catch (BusinessException be) {
			businessExceptionHandler.handle(be, sysCertAdownRequest);
			return false;
		}
	}


	/**
	 * 获取请求回调地址
	 *
	 * @param sysCertAdownRequest
	 * @return
	 */
	private String getReqCallbackUrl(SysCertAdownRequest sysCertAdownRequest) {
		return getUrl(sysCertAdownRequest, true);
	}

	/**
	 * 获取响应回调地址
	 *
	 * @param sysCertAdownRequest
	 * @return
	 */
	private String getRespCallbackUrl(SysCertAdownRequest sysCertAdownRequest) {
		return getUrl(sysCertAdownRequest, false);
	}


	private String getUrl(SysCertAdownRequest sysCertAdownRequest, boolean getReqUrl) {
		SysAppConfig appConfig = sysAppConfigService.getOne(new QueryWrapper<SysAppConfig>().lambda().eq(SysAppConfig::getAppId, sysCertAdownRequest.getAppId()));
		if (appConfig != null) {
			if (getReqUrl) {
				return appConfig.getReqCallBackUrl();
			} else {
				return appConfig.getRespCallBackUrl();
			}

		} else {
			log.error("未找到APP：{} 的配置信息,{}", sysCertAdownRequest.getAppId(), sysCertAdownRequest);
			throw new BusinessException(ErrorCode.CONFIG_NO_CONFIG);
		}


	}
}
