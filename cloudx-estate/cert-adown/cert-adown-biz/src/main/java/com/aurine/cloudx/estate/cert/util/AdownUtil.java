package com.aurine.cloudx.estate.cert.util;


import com.aurine.cloudx.estate.cert.constant.AdownConstant;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import lombok.extern.slf4j.Slf4j;


/**
 * @description: 下发缓存工具类
 * 用户缓存下发请求信息
 * @author: wangwei
 * @date: 2021/12/23 14:26
 **/
@Slf4j
public class AdownUtil {


	/**
	 * 清空缓存
	 *
	 * @param requestId
	 * @return
	 */
	public static void clearCache(String requestId) {
		AdownCacheUtil.remove(requestId);
		AdownRetryUtil.remove(requestId);
		AdownSequenceUtil.remove(requestId);
	}


}
