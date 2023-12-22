package com.aurine.cloudx.estate.entity;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>项目入云配置（云端）</p>
 *
 * @author : 王良俊
 * @date : 2021-12-10 15:31:17
 */
@Data
public class SystemInfo {

    /**
     * 序列
     */
    private Integer seq;

    /**
     * 配置ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    @ApiModelProperty(value = "CPU序列号")
    private String cpuId;
    @ApiModelProperty(value = "硬盘序列号")

    private String diskId;
    @ApiModelProperty(value = "主板序列号")

    private String mainboard;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "网卡")

    private String mac;
    @ApiModelProperty(value = "边缘网关sn")

    private String sn;
    @ApiModelProperty(value = "是否同步菜单")

    private String isSyncMenu;
    @ApiModelProperty(value = "校验是否通过")

    private String isVerify;

    @ApiModelProperty(value = "版本名称")

    private String versionCode;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "授权开始时间")
    private LocalDateTime startDate;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "授权过期时间")
    private LocalDateTime endDate;
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
/*    public String getConnectCode() {
        if (StrUtil.isEmpty(connectCode) && projectId != null) {
            connectCode = Integer.toUnsignedString(projectId, 36);
        }
        return connectCode;
    }
    */
}