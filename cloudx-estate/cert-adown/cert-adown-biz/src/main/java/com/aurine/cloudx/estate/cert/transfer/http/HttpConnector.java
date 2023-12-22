package com.aurine.cloudx.estate.cert.transfer.http;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.cert.dto.CertAdownRequestDTO;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import com.aurine.cloudx.estate.cert.exception.BusinessException;
import com.aurine.cloudx.estate.cert.exception.code.ErrorCode;
import com.aurine.cloudx.estate.cert.vo.CertAdownRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


/**
 * @description: http连接器
 * @author: wangwei
 * @date: 2021/12/16 17:11
 **/
@Component
@Slf4j
public class HttpConnector {

	@Resource
	private RestTemplate restTemplate;

	public CertAdownRequestDTO toDto(SysCertAdownRequest sysCertAdownRequest) {
		String body = sysCertAdownRequest.getBody();
		sysCertAdownRequest.setBody(null);

		CertAdownRequestDTO dto =  BeanUtil.copyProperties(sysCertAdownRequest, CertAdownRequestDTO.class);

		dto.setBody(JSONObject.parseObject(body));
		sysCertAdownRequest.setBody(body);

		return dto;
	}

	/**
	 * post方式推送请求
	 *
	 * @param sysCertAdownRequest
	 * @param url
	 * @return
	 */
	public JSONObject post(SysCertAdownRequest sysCertAdownRequest, String url) {
		try {
			return conn(url, JSONObject.parseObject(JSONObject.toJSONString(toDto(sysCertAdownRequest))), HttpMethod.POST);
		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
				log.error("{}", "授权无效");
				throw new BusinessException(ErrorCode.HTTP_NOT_AUTH, sysCertAdownRequest);
			} else if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
				log.error("{}", "接口地址错误：" + url);
				throw new BusinessException(ErrorCode.HTTP_URL_NOT_FOUND, sysCertAdownRequest);
			} else {
				log.error("异常代码：{} 发生异常：{}", e.getRawStatusCode(), e.getMessage());
				throw new BusinessException(ErrorCode.HTTP_ERROR, e);
			}
		} catch (ResourceAccessException rae) {
			log.error("连接超时:{}", rae.getMessage());
			throw new BusinessException(ErrorCode.HTTP_OUT_OF_TIME, sysCertAdownRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BusinessException(ErrorCode.HTTP_ERROR, ex);
		}
	}

	@Async
	public void asyncPost(SysCertAdownRequest request, String url) {
		post(request, url);
	}

	/**
	 * http连接
	 *
	 * @param url
	 * @param requestBody
	 * @param httpMethod
	 * @return
	 * @throws HttpClientErrorException
	 */
	private JSONObject conn(String url, JSONObject requestBody, HttpMethod httpMethod) throws HttpClientErrorException {

		log.info("发送数据 发送方式：{} , 地址： {} ， 数据内容：{} ", httpMethod.toString(), url, requestBody);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<JSONObject> result;
		result = restTemplate.exchange(url, httpMethod, requestEntity, JSONObject.class);

//		JSONObject json = JSONObject.parseObject(result.getBody());
		log.debug("获取到响应：{}", result.getBody());

		return result.getBody();
	}


}
