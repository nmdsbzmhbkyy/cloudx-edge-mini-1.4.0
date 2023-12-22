package com.aurine.cloudx.wjy.feign;

import com.aurine.cloudx.wjy.vo.RoomStandardVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 房间管理
 * @author ：huangjj
 * @date ：2021/4/15
 * @description：房间管理路由
 */
@FeignClient(contextId = "remoteRoomService2", value = "cloudx-wjy-biz")
public interface RemoteRoomService {
    /**
     * 功能描述: 添加房间信息
     *
     * @author huangjj
     * @date 2021/4/15
     * @param RoomStandardVo 房间数据对象
     * @return 返回添加结果
     */
    @PostMapping("/room/add")
    public R addRoom(@RequestBody RoomStandardVo RoomStandardVo);
}
