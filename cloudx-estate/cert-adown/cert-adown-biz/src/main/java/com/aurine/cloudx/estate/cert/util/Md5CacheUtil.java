package com.aurine.cloudx.estate.cert.util;


import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * @description: MD5缓存校验工具类
 * @author: wangwei
 * @date: 2021/12/27 16:50
 **/
@Slf4j
public class Md5CacheUtil {
	final private static String MD5_KEY = "ADOWN_MD5:";
//	final private static String MD5_KEY_LOCK = "ADOWN_MD5_LOCK";
	final private static Long TTL = 7200L;
	final private static Long MD5_KEY_LOCK_TTL = 5L;


	/**
	 * 解析校验，MD5是否已存在
	 *
	 * @param request
	 * @return true 为正常，false为异常，md5已存在
	 */
	public static boolean checkMd5(SysCertAdownRequest request) {

		JSONObject object = JSONObject.parseObject(request.getBody());

		String md5 = getMd5(object);
		if (StringUtils.isEmpty(md5)) {
			//MD5不存在时，解析失败，不控制MD5重复问题
			return true;
		}

		String key = getRedisKey(request.getDeviceId(), md5);

		//验证是否存在缓存
//		RedisUtil.syncLock(MD5_KEY_LOCK, MD5_KEY_LOCK_TTL);
		if (CertRedisUtil.hasKey(key)) {
//			RedisUtil.unLock(MD5_KEY_LOCK);
			return false;
		} else {
			CertRedisUtil.set(key, 0, TTL);
//			RedisUtil.unLock(MD5_KEY_LOCK);
			return true;
		}

	}


	/**
	 * 移除MD5
	 *
	 * @param requestList
	 * @return
	 */
	public static void removeMd5(List<SysCertAdownRequest> requestList) {
		if (CollUtil.isNotEmpty(requestList)) {
			for (SysCertAdownRequest request : requestList) {
				removeMd5(request);
			}
		}
	}


	/**
	 * 移除MD5
	 *
	 * @param request
	 * @return
	 */
	public static void removeMd5(SysCertAdownRequest request) {
		JSONObject object = JSONObject.parseObject(request.getBody());
		String md5 = getMd5(object);
		log.info("删除MD5:{}", md5);

		if (StringUtils.isEmpty(md5)) {
			//MD5不存在时，解析失败，不控制MD5重复问题
			return;
		}
		String key = getRedisKey(request.getDeviceId(), md5);

//		RedisUtil.syncLock(MD5_KEY_LOCK, MD5_KEY_LOCK_TTL);
		CertRedisUtil.del(key);
//		RedisUtil.unLock(MD5_KEY_LOCK);
	}


	/**
	 * 解析华为中台对象MD5
	 *
	 * @param json
	 * @return
	 */
	private static String getMd5(JSONObject json) {
		//重排序
		JSONObject body = JSONObject.parseObject(JSON.toJSONString(json), Feature.OrderedField);

		//需升级责任链、工厂、函数式等模式
		String md5 = getMd5Huawei(body);

		if (StringUtils.isEmpty(md5)) {
			md5 = getMd5AurineEdge(body);
		}

		return md5;
	}

	/**
	 * 解析华为中台对象MD5
	 *
	 * @param json
	 * @return
	 */
	private static String getMd5Huawei(JSONObject json) {

		JSONObject huaweiDeviceCertVo = json.getJSONObject("huaweiDeviceCertVo");

		//华为中台的数据，存在云端、边侧同时操作可能性，需删除同一凭证，内容不同的部分
		if (huaweiDeviceCertVo != null) {
			huaweiDeviceCertVo.remove("huaweiConfigDTO");
			huaweiDeviceCertVo.remove("deviceInfo");

			return md5(huaweiDeviceCertVo);
		}

		return null;
	}

	/**
	 * 解析边侧小中台对象MD5
	 *
	 * @param json
	 * @return
	 */
	private static String getMd5AurineEdge(JSONObject json) {

		JSONObject aurineEdgeDeviceCertVo = json.getJSONObject("aurineEdgeDeviceCertVo");

		//华为中台的数据，存在云端、边侧同时操作可能性，需删除同一凭证，内容不同的部分
		if (aurineEdgeDeviceCertVo != null) {
			aurineEdgeDeviceCertVo.remove("config");
			aurineEdgeDeviceCertVo.remove("deviceInfo");

			return md5(aurineEdgeDeviceCertVo);
		}

		return null;
	}


	private static String md5(String str) {
		return DigestUtils.md5DigestAsHex(str.getBytes());
	}

	private static String md5(JSONObject json) {
		return md5(json.toJSONString());
	}


	/**
	 * 生成Key
	 * ADOWN_MD5:设备ID:MD5
	 *
	 * @param deviceId
	 * @param md5
	 * @return
	 */
	private static String getRedisKey(String deviceId, String md5) {
		return MD5_KEY + deviceId + ":" + md5;
	}

}
