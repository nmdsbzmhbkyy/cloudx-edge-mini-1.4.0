package com.aurine.cloudx.estate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>查询带组团或不带组团的楼栋ID</p>
 * @author : 王良俊
 * @date : 2021-09-02 14:54:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameBuildingInfo {

    /**
     * 楼栋名（开启组团则带有组团信息使用'-'连接）
     */
    private String buildingName;

    /**
     * 楼栋ID
     */
    private String buildingId;

}
