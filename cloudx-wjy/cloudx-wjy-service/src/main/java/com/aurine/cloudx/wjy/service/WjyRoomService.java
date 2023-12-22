package com.aurine.cloudx.wjy.service;

import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.vo.RoomStandardVo;
import com.aurine.cloudx.wjy.vo.RoomVo;
import com.aurine.cloudx.wjy.pojo.RDataListPager;
import com.aurine.cloudx.wjy.vo.WjyRoom;
import com.aurine.cloudx.wjy.pojo.R;

import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/4/20
 * @description： 我家云房间管理接口
 */
public interface WjyRoomService {
    R<RDataListPager<List<WjyRoom>>> roomGetByPage(int rowCount, int currentPage, Project project, String queryName);
    boolean roomSave(Project project, List<RoomVo> rooms);
    boolean roomSaveStandard(Project project, List<RoomStandardVo> rooms);

}
