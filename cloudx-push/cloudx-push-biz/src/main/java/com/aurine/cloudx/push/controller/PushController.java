
package com.aurine.cloudx.push.controller;

import com.aurine.cloudx.push.dto.AppDTO;
import com.aurine.cloudx.push.dto.EmailDTO;
import com.aurine.cloudx.push.dto.SmsDTO;
import com.aurine.cloudx.push.dto.WxDTO;
import com.aurine.cloudx.push.service.PushService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lengleng
 * @date 2018/12/16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/push")
@Api(value = "push", tags = "信息推送模块")
@Inner(false)
public class PushController {

	private final PushService pushService;

	/**
	 * 发送email
	 *
	 * @param emailDTO
	 * @return
	 */
	@SysLog("通过Email推送信息")
	@PostMapping("/email")
	public R email(@Valid @RequestBody EmailDTO emailDTO) {
		return R.ok(pushService.pushToEmail(emailDTO.getEmail(), emailDTO.getTitle(), emailDTO.getMessage(),
				emailDTO.getEmailType()));
	}

	/**
	 * 群发发送email
	 *
	 * @param emailDTO
	 * @return
	 */
	@SysLog("通过Email群推信息")
	@PostMapping("/emails")
	public R emails(@RequestBody EmailDTO emailDTO) {
		return R.ok(pushService.pushToEmails(emailDTO.getEmailList(), emailDTO.getTitle(), emailDTO.getMessage(),
				emailDTO.getEmailType()));
	}

	/**
	 * 发送短信
	 *
	 * @param smsDTO
	 * @return
	 */
	@SysLog("通过短信推送信息")
	@PostMapping("/sms")
	public R sms(@RequestBody SmsDTO smsDTO) {
		return R.ok(pushService.pushToSMS(smsDTO.getMobile(), smsDTO.getMessage()));
	}

	/**
	 * 群发短信
	 *
	 * @param smsDTO
	 * @return
	 */
	@SysLog("通过短信群推信息")
	@PostMapping("/smss")
	public R smss(@RequestBody SmsDTO smsDTO) {
		return R.ok(pushService.pushToSMSs(smsDTO.getMobileList(), smsDTO.getMessage()));
	}

	/**
	 * 推送APP信息
	 *
	 * @param appDTO
	 * @return
	 */
	@SysLog("通过短信推送信息")
	@PostMapping("/app")
	public R app(@RequestBody AppDTO appDTO) {
		return R.ok(pushService.pushToApp(appDTO.getClientId(), appDTO.getOsType(), appDTO.getMessage(),
				appDTO.getPushSystemType(), appDTO.getAppName(), appDTO.getParamMap()));
	}

	/**
	 * 群推APP信息
	 *
	 * @param appDTO
	 * @return
	 */
	@SysLog("通过短信群推信息")
	@PostMapping("/apps")
	public R apps(@RequestBody AppDTO appDTO) {
		return R.ok(pushService.pushToApps(appDTO.getClientIdList(), appDTO.getOsTypeList(), appDTO.getMessage(),
				appDTO.getPushSystemType(), appDTO.getAppName(), appDTO.getParamMap()));
	}

	/**
	 * 发送公众号消息
	 *
	 * @param wxDTO
	 * @return
	 */
	@SysLog("发送公众号消息")
	@PostMapping("/wx")
	public R publicAccount(@RequestBody WxDTO wxDTO) {
		return R.ok(pushService.pushToWx(wxDTO.getTemplateId(), wxDTO.getUnionId(), wxDTO.getData()));

	}
}
