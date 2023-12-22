package com.aurine.cloudx.wjy.controller;

import com.aurine.cloudx.wjy.entity.Building;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.entity.Room;
import com.aurine.cloudx.wjy.entity.Unit;
import com.aurine.cloudx.wjy.service.BuildingService;
import com.aurine.cloudx.wjy.service.ProjectService;
import com.aurine.cloudx.wjy.service.RoomService;
import com.aurine.cloudx.wjy.service.UnitService;
import com.aurine.cloudx.wjy.service.impl.BuildingServiceImpl;
import com.aurine.cloudx.wjy.service.impl.ProjectServiceImpl;
import com.aurine.cloudx.wjy.service.impl.RoomServiceImpl;
import com.aurine.cloudx.wjy.service.impl.UnitServiceImpl;
import com.aurine.cloudx.wjy.pojo.RDataListPager;
import com.aurine.cloudx.wjy.service.WjyRoomService;
import com.aurine.cloudx.wjy.service.impl.*;
import com.aurine.cloudx.wjy.vo.RoomStandardVo;
import com.aurine.cloudx.wjy.vo.WjyRoom;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 房间管理
 * @author ：huangjj
 * @date ：2021/4/15
 * @description：房间管理
 */
@RestController
@RequestMapping("/room")
public class RoomController {
    @Resource
    RoomService roomService;
    /**
     * 功能描述: 添加房间信息
     *
     * @author huangjj
     * @date 2021/4/15
     * @param roomStandardVo 房间数据对象
     * @return 返回添加结果
    */
    @PostMapping("/add")
    @Inner(value = false)
    public R addRoom(@RequestBody RoomStandardVo roomStandardVo){
        return roomService.addRoom(roomStandardVo);
    }

}
