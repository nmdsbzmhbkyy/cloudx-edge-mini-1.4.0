package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AurineEdgeDeviceInfoDTO {

    /**
     * 产品ID 产品的唯一标识
     */
    private String productId;

    /**
     * 设备ID 	设备的唯一标识，采用UUID
     */
    private String devId;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 产品型号ID
     */
    private String modelId;

    /**
     * 厂商名称
     */
    private String manufacturer;

    /**
     * 设备编号
     */
    private String deviceNo;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 设备序列号
     */
    private String devSN;

    /**
     * 设备资源类型
     * djInDoor	可视对讲-室内机
     * djOutDoor	可视对讲-门口机
     * djUnitDoor	可视对讲-单元门口机
     * djWallDoor	可视对讲-区口机
     * djManager	可视对讲-管理员机
     * djUnifyDoor	可视对讲-门禁一体机
     */
    private String devResType;

    /**
     * 设备Mac地址信息
     */
    private String mac;

    /**
     * 设备经纬度信息
     */
    private String gisInfo;

    /**
     * 设备状态信息
     * 0：未激活
     * 1：在线
     * 2: 离线
     */
    private Integer devStatus;

    /**
     * 设备的服务能力列表
     */
    private JSONArray capabilities;

    /**
     * 设备扩展参数信息	JSON 对象数据信息
     */
    private AurineEdgeDeviceInfoExtParamDTO extParam;

    /**
     * 设备激活时间
     */
    private LocalDateTime createDate;

    /**
     * 设备更新时间
     */
    private LocalDateTime updateDate;
    /**
     * 设备最后上线时间
     */
    private Long lastOnLineDate;

    /**
     * 是否支持富文本
     * 0：不支持，1：支持
     */
    private Integer supportRichTxt;


    /**
     * 设备Ip
     */
    private String deviceIp;

    /**
     * 设备端口
     */
    private Integer devicePort;

    /**
     * 硬件版本
     */
    private String hardwareVersion;

    /**
     * 软件版本
     */
    private String softwareVersion;


    /**
     * 是否自动注册	0：手动；1：自动
     */
    private String autoReg;

    /**
     * 在线状态
     */
    private String online;


    /**
     * 	错误代码 (1：一般性错误（如编号不符合规则等错误）；3： 不存在（旧设备编号不存在）；7：已存在（设备编号重复）；)
     */
    private Integer failCode;


    /**
     * 	错误描述
     */
    private String failDesc;


    /**
     *  报警主机id
     */
    private String paramDevId;

    /**
     *  通讯密码
     */
    private String passwd;

    /**
     *  门号
     */
    private String doorNo;

    /**
     *  网关
     */
    private String netGate;

    /**
     *  子网掩码
     */
    private String netMask;

    /*
     * 设备编码，可存储第三方编码
     */
    private String thirdpartyCode;


    /**
     * 将能力集jsonArray 转换为 平台用String
     *
     * @return
     */
    public String getCapabilitiesString() {

        String result = "";
        JSONArray capabilities = this.getCapabilities();

        if (CollUtil.isNotEmpty(capabilities)) {

            for (int i = 0; i < this.getCapabilities().size(); i++) {
                result += "#" + capabilities.getString(i);
            }

        }
        return result;

    }

}
