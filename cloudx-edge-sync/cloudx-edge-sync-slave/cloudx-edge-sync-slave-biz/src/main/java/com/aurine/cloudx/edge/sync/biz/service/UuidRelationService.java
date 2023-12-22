/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.aurine.cloudx.edge.sync.biz.service;

import com.aurine.cloudx.edge.sync.common.entity.po.UuidRelation;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @author pigx code generator
 * @date 2022-01-05 16:09:50
 */
public interface UuidRelationService extends IService<UuidRelation> {

    /**
     * 根据第三方uuid获取uuidRelation对象
     * 参数
     * thirdUuid 第三方Uuid
     * ProjectUuid 自己的ProjectUuidUuid
     * serviceName 服务名称
     * tenantId 租户Id
     *
     * @param taskInfoDto
     * @return
     */
    UuidRelation getByThirdUuid(TaskInfoDto taskInfoDto);

    /**
     * 根据uuid获取uuidRelation对象
     * 参数
     * uuid Uuid
     * ProjectUuid 自己的ProjectUuidUuid
     * serviceName 服务名称
     * tenantId 租户Id
     *
     * @param taskInfoDto
     * @return
     */
    UuidRelation getByUuid(TaskInfoDto taskInfoDto);


    /**
     * 类型为OperateType时
     * 保存uuid关系表数据,
     * 只在业务操作的新增操作处理请求
     *
     * @param taskInfoDto
     */
    Boolean saveUuidRelation(TaskInfoDto taskInfoDto);

    /**
     * 根据uuid更新oldMd5值
     * @param taskInfoDto
     * @return
     */
    Boolean updateOldMd5ByUuid(TaskInfoDto taskInfoDto);


    /**
     * 根据thirdUuid更新oldMd5值
     * @param taskInfoDto
     * @return
     */
    Boolean updateOldMd5ByThirdUuid(TaskInfoDto taskInfoDto);

    /**
     * 根据Uuid删除数据
     * @param taskInfoDto
     * @return
     */
    Boolean deleteByUuid(TaskInfoDto taskInfoDto);

    /**
     * 根据Uuid删除数据
     * @param taskInfoDto
     * @return
     */
    Boolean deleteByThirdUuid(TaskInfoDto taskInfoDto);

    /**
     * 检验oldMd5值
     *
     * @param oldMd5 oldMd5值
     * @return true / false
     */
    Boolean checkOldMd5(String oldMd5);
}
