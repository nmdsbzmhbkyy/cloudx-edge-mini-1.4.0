package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 设备车牌下发情况对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/8 14:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingDeviceCertDlstatusCountVo {

    /*
     * 设备ID
     **/
    private String deviceId;

    /*
     * 整体成功数
     **/
    private Integer successNum;

    /*
     * 整体失败数
     **/
    private Integer failedNum;

    /*
     * 整体下载中数量
     **/
    private Integer downloadingNum;

    /*
     * 正常成功数
     **/
    private Integer normalSuccessNum;

    /*
     * 正常失败数
     **/
    private Integer normalFailedNum;

    /*
     * 禁行成功数
     **/
    private Integer noEntrySuccessNum;

    /*
     * 禁行失败数
     **/
    private Integer noEntryFailedNum;

    /*
     * 项目ID
     **/
    private Integer projectId;


}
