package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectLayerDeviceInfoVo extends ProjectDeviceInfo {

    /*
     * 楼栋名
     * */
    private String buildingName;

    /*
     * 区域名
     * */
    private String regionName;

    /*
     * 楼层设置
     * */
    private String floorSet;

    /*
     * 开门模式 0 单开门 1 双开门
     * */
    private String openDoorMode;

    /**
     * 选中状态 0 全部 1 未被选 2 已被选
     */
    private String selectStatus;

}
