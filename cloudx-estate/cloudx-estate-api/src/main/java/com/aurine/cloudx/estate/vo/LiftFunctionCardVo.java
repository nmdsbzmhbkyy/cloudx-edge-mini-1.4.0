package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: hjj
 * @Date: 2022/4/1 09:26
 * @Description: 电梯业主卡数据VO
 */
@Data
@ApiModel(value = "电梯业主卡数据DTO")
public class LiftFunctionCardVo {
    //卡号
    @ApiModelProperty("卡号")
    private String cardNo;

    @ApiModelProperty("卡类型")
    private Integer cardType;

    @ApiModelProperty("电梯组")
    private List<ProjectDeviceLiftVo> liftList;

    @ApiModelProperty(value = "屏蔽房间列表")
    private List<ShieldRoomVo> shieldRoomList;

    @ApiModelProperty(value = "屏蔽卡号列表")
    private List<ShieldCardVo> shieldCardList;

    @ApiModelProperty(value = "手动召梯响应时间")
    private Integer manualCallTime;

    @ApiModelProperty(value = "自动召梯自动按键触发间隔时间")
    private Integer autoCallTime;

    @ApiModelProperty(value = "时间yyyy-MM-dd hh:mm:ss")
    private String dateTime;

    @ApiModelProperty(value = "起始记录编号")
    private Integer startNum;

    @ApiModelProperty(value = "读取记录数量")
    private Integer userDataNum;

    @ApiModelProperty(value = "访客响应时间")
    private Integer visitRespTime;

    @ApiModelProperty(value = "楼层列表")
    private String[] floors;

    @ApiModelProperty(value = "是否屏蔽")
    private Integer shield;
    //特点时段起始时间
    @ApiModelProperty("特点时段起始时间")
    private String startTime;
    //特点时段结束时间
    @ApiModelProperty("特点时段结束时间")
    private String endTime;

    @ApiModelProperty("时段开关")
    private Integer durationSwitch;

    @ApiModelProperty("电梯分层器编号,即梯控编号")
    private String liftNo;

    @ApiModelProperty("电梯运行时段")
    private List<LiftDurationVo> durationObj;

    @ApiModelProperty("设置模式，0=正常模式，1=出厂模式")
    private String netMode;

    @ApiModelProperty("楼栋号")
    private String buildingNo;

    @ApiModelProperty("单元号")
    private String unitNo;

    @ApiModelProperty("设备编号")
    private String deviceNo;

    @ApiModelProperty("梯控IP")
    private String ipAddr;

    @ApiModelProperty("子网掩码")
    private String netMask;

    @ApiModelProperty("默认网关")
    private String gateway;

    @ApiModelProperty("中心服务器IP")
    private String centerIP;

    @ApiModelProperty("DNS1")
    private String dns1;

    @ApiModelProperty("DNS2")
    private String dns2;

    @ApiModelProperty("卡密钥，系统默认参数，不可修改")
    private String cardEncryptionKey;

    @ApiModelProperty("卡扇区偏置，系统默认参数，不可修改")
    private String cardSectorOffset;

    @ApiModelProperty("分机列表，最多16个，前端只可配置7个")
    private List<String> exitIps;

    @ApiModelProperty("梯号长度")
    private String buildingNoLength;

    @ApiModelProperty("房号长度")
    private String roomNoLength;
}
