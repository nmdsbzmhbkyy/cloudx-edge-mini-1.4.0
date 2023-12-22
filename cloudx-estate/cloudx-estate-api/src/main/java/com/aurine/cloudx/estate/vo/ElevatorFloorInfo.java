package com.aurine.cloudx.estate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>电梯分层信息类</p>
 *
 * @author 王良俊
 * @date "2022/2/28"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElevatorFloorInfo {

    /*
     * 楼层设置
     **/
    private String floorSet;

    /*
     * 楼层列表
     **/
    private List<String> floorList;

}
