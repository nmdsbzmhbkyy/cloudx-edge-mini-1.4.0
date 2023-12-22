package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.EdgeCascadeConf;
import com.aurine.cloudx.estate.vo.EdgeCascadeConfVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * <p>项目入云连接码服务</p>
 *
 * @author : 王良俊
 * @date : 2021-12-10 15:40:10
 */
public interface EdgeCascadeConfService extends IService<EdgeCascadeConf> {

    /**
     * <p>启用联网</p>
     *
     * @param projectId 项目ID
     * @return 是否启用成功
     * @author: 王良俊
     */
    R enableNetwork(Integer projectId);

    /**
     * <p>禁用联网</p>
     *
     * @param projectId 项目ID
     * @return 是否启用成功
     * @author: 王良俊
     */
    R disableNetwork(Integer projectId);

    /**
     * <p>启用级联</p>
     *
     * @param projectId 项目ID
     * @return 是否启用成功
     */
    R enableCascade(Integer projectId);

    /**
     * <p>禁用级联</p>
     *
     * @param projectId 项目ID
     * @return 是否启用成功
     */
    R disableCascade(Integer projectId);


    /**
     * <p>根据项目ID获取配置</p>
     *
     * @param projectId 项目ID
     * @return 级联入云配置
     */
    EdgeCascadeConfVo getConf(Integer projectId);

    /**
     * <p>更新级联配置信息</p>
     *
     * @param conf 级联配置信息 带配置ID
     * @return 是否更新成功
     */
    R updateCascadeConfInfo(EdgeCascadeConf conf);

    /**
     * <p>获取边缘网关级联码</p>
     *
     * @return 级联码
     */
    String getConnectCode();

    /**
     * <p>判断级联码是否正确</p>
     *
     * @param connectCode 要判断的级联吗
     * @return 级联码是否正确
     * @author: 王良俊
     */
    boolean checkConnectCode(String connectCode);

    /**
     * <p>初始化级联项目级联配置（只能用在级联项目中）</p>
     *
     * @param projectId   项目ID
     */
    void initCascadeConf(Integer projectId);

}