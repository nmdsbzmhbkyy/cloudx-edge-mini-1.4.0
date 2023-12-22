

package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设备拓展属性Vo对象")
public class ProjectDeviceAttrVo {

    /**
     * 属性编码
     */
    @ApiModelProperty(value = "属性编码")
    private String attrCode;

    /**
     * 属性值
     */
    @ApiModelProperty(value = "属性值")
    private String attrValue;

    /**
     * 属性名
     */
    @ApiModelProperty(value = "属性名")
    private String attrName;
}
