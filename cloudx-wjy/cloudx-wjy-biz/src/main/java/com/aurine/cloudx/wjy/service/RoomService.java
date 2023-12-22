package com.aurine.cloudx.wjy.service;

import com.aurine.cloudx.wjy.entity.Room;
import com.aurine.cloudx.wjy.vo.RoomStandardVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * @author ： huangjj
 * @date ： 2021/4/25
 * @description： 房间接口
 */
public interface RoomService extends IService<Room> {
    Room getByProjectIdAndRoomName(Integer projectId, String name);
    Room getByRoomId(String roomId);
    R addRoom(RoomStandardVo roomStandardVo);

}
