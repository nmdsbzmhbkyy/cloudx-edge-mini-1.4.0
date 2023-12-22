package com.aurine.cloudx.estate.cert.remote;


import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;

/**
 * @description: 下发服务远程数据传输接口
 * @author: wangwei
 * @date: 2021/12/17 11:04
 **/
public interface AdownServiceRemote {

	/**
	 * 给远端系统发送 下载指令
	 * @param sysCertAdownRequest
	 */
	boolean adown(SysCertAdownRequest sysCertAdownRequest);

	/**
	 * 给远端系统发送 凭证状态变化
	 * @param sysCertAdownRequest
	 * @return
	 */
	Boolean update(SysCertAdownRequest sysCertAdownRequest);
}
