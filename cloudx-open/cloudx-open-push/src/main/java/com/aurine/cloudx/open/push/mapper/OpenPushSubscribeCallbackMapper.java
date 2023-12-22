package com.aurine.cloudx.open.push.mapper;

import com.aurine.cloudx.open.common.entity.entity.OpenPushSubscribeCallback;
import com.aurine.cloudx.open.common.entity.vo.OpenPushSubscribeCallbackVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 推送订阅管理
 * @author : Qiu
 * @date : 2021 12 09 11:43
 */

@Mapper
public interface OpenPushSubscribeCallbackMapper extends BaseMapper<OpenPushSubscribeCallback> {

    /**
     * 获取回调列表
     *
     * @param po
     * @return
     */
    List<OpenPushSubscribeCallbackVo> getList(@Param("query") OpenPushSubscribeCallback po);

    /**
     * 获取回调列表
     *
     * @param po
     * @return
     */
    Page<OpenPushSubscribeCallbackVo> page(Page page, @Param("query") OpenPushSubscribeCallback po);

    /**
     * 根据appId、callbackType、projectUUID删除
     *
     * @param appId
     * @param callbackType
     * @param projectUUID
     * @return
     */
    Boolean delete(@Param("appId") String appId, @Param("callbackType") String callbackType, @Param("projectUUID") String projectUUID);
}
