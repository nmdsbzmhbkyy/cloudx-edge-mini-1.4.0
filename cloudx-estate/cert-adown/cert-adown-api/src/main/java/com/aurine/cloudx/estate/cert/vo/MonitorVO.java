package com.aurine.cloudx.estate.cert.vo;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.cert.entity.BaseEntity;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;


/**
 * @description: 监控VO
 * @author: wangwei
 * @date: 2021/12/27 12:30
 **/
@Data
public class MonitorVO extends BaseEntity {

	/**
	 * 缓存大小
	 */
	private long cacheSize;

	/**
	 * 缓存内容
	 */
	private List<SysCertAdownRequest> cacheList;

	/**
	 * 重试列表
	 */
	private Map<String, Long> retryList;

	/**
	 * 重试列表长度
	 */
	private long retrySize;


	/**
	 * 等待下发列表
	 */
	private List<SysCertAdownRequest> queueList;
	/**
	 * 等待下发列表长度
	 */
	private Long queueSize;


	/**
	 * 令牌数量
	 */
	private Long tokenCount;


}
