package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/05/23 17:19
 * @Package: com.aurine.openv2.service
 * @Version: 1.0
 * @Remarks:
 **/
public interface OpenApiProjectPersonInfoService extends IService<ProjectPersonInfo> {
	/**
	 * 内部调用接口
	 * 根据住户手机号获取住户信息
	 *
	 * @param telephone 手机号
	 * @return
	 */
	R<ProjectPersonInfo> getPersonInfoByPhone(String telephone);

	/**
	 * 内部调用接口
	 * 根据住户手机号获取住户信息
	 *
	 * @param telephone 手机号
	 * @return
	 */
	R<String> getPersonIdByPhone(String telephone);

	/**
	 * 内部调用接口
	 * 根据住户姓名获取住户信息
	 *
	 * @param name 姓名
	 * @return
	 */
	R<String> getPersonIdByName(String name);

	/**
	 * 内部调用接口
	 * 根据住户姓名(支持模糊查询)，获取住户信息列表
	 *
	 * @param name 姓名
	 * @return
	 */
	R<List<String>> getPersonIdByNameLike(String name);

	/**
	 * 校验是否存在此住户Id
	 * @param personId
	 * @return
	 */
	Boolean checkPersonIdExist(String personId);

	/**
	 * 更新手机号
	 * @param oldTelephone 修改前手机号
	 * @param newTelephone 修改后手机号
	 * @return 更新是否成功
	 */
	R<Boolean> updatePersonPhone(String oldTelephone,String newTelephone);
}
