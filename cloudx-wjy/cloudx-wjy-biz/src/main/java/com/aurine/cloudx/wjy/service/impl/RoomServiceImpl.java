package com.aurine.cloudx.wjy.service.impl;

import com.aurine.cloudx.wjy.entity.Building;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.entity.Room;
import com.aurine.cloudx.wjy.entity.Unit;
import com.aurine.cloudx.wjy.mapper.RoomMapper;
import com.aurine.cloudx.wjy.pojo.RDataListPager;
import com.aurine.cloudx.wjy.service.*;
import com.aurine.cloudx.wjy.vo.RoomStandardVo;
import com.aurine.cloudx.wjy.vo.WjyRoom;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/4/25
 * @description： 房间接口实现类
 */
@Service
public class RoomServiceImpl  extends ServiceImpl<RoomMapper, Room> implements RoomService {
    @Resource
    RoomMapper roomMapper;
    @Resource
    ProjectService projectService;
    @Resource
    WjyRoomService wjyRoomService;
    @Resource
    BuildingService buildingService;
    @Resource
    UnitService unitService;

    @Override
    public Room getByProjectIdAndRoomName(Integer projectId, String name) {
        return roomMapper.selectOne(new QueryWrapper<Room>().lambda().eq(Room::getProjectId,projectId).eq(Room::getRoomName,name));
    }

    @Override
    public Room getByRoomId(String roomId) {
        return roomMapper.selectOne(new QueryWrapper<Room>().lambda().eq(Room::getRoomId,roomId));
    }
    @Override
    public R addRoom(RoomStandardVo roomStandardVo){
        Project project = projectService.getByProjectId(roomStandardVo.getProjectId());
        if(project == null){
            return R.failed("未找到项目信息");
        }
        Building building = buildingService.getByProjectIdAndName(roomStandardVo.getProjectId(),roomStandardVo.getBuildingName());
        if(building == null){
            return R.failed("无法读取楼栋信息信息");
        }
        List<RoomStandardVo> roomStandardVos = new ArrayList<>();
        roomStandardVos.add(roomStandardVo);
        Boolean isSave = wjyRoomService.roomSaveStandard(project,roomStandardVos);
        if (!isSave){
            return R.failed("添加房间失败");
        }
        com.aurine.cloudx.wjy.pojo.R<RDataListPager<List<WjyRoom>>> r = wjyRoomService.roomGetByPage(1,1,project,roomStandardVo.getRoomName());
        if(r != null && r.isSuccess()){
            try {
                WjyRoom wjyRoom = r.getData().getList().get(0);

                Unit unit = unitService.getByProjectIdAndUnitName(project.getProjectId(),roomStandardVo.getBuildUnitName());
                if(unit == null){
                    unit = new Unit();
                }
                unit.setProjectId(project.getProjectId());
                unit.setUnitName(roomStandardVo.getBuildUnitName());
                unit.setUnitId(roomStandardVo.getUnitId());
                unit.setWjyUnitId(wjyRoom.getBuildUnitID());
                unit.setBuildingId(building.getBuildingId());
                unitService.saveOrUpdate(unit);

                Room room = getByProjectIdAndRoomName(project.getProjectId(),roomStandardVo.getRoomName());
                if(room == null){
                    room = new Room();
                }
                room.setProjectId(project.getProjectId());
                room.setRoomId(roomStandardVo.getSourceID());
                room.setRoomName(roomStandardVo.getRoomName());
                room.setWjyRoomId(wjyRoom.getId());
                room.setUnitId(unit.getUnitId());
                saveOrUpdate(room);
                return R.ok();
            } catch (Exception e) {
                e.printStackTrace();
                return R.failed("无法读取房间信息");
            }
        }
        return R.failed();
    }
}