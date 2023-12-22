package com.aurine.cloudx.estate.vo;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.estate.entity.EdgeCloudRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "边缘网关入云申请")
public class EdgeCloudRequestVo extends EdgeCloudRequest {

    private String edgeDeviceId;

}
