package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param.elevator.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>电梯设备参数</p>
 * <p>参数类型：base</p>
 * <p>参数子类型：device</p>
 *
 * @author 王良俊
 * @date "2022/2/28"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiftDevice {

    /*
     * 楼栋
     **/
    private int building;

    /*
     * 单元
     **/
    private int unit;

    /*
     * 梯号长度
     **/
    private int stairNoLen;

    /*
     * 房号长度
     **/
    private int roomNoLen;

    /*
     * 单元长度
     **/
    private int cellNoLen;

    /*
     * 启用单元 1:启用 0未启用
     **/
    private int useCellNo;

    /*
     * 设备编号
     **/
    private String deviceNo;

    /*
     * 电梯编号
     **/
    private int liftNo;

    /*
     * 是否主机 1:主机 0非主机
     **/
    private int master;

    /*
     * 加密秘钥
     **/
    private String secretKey;

}
