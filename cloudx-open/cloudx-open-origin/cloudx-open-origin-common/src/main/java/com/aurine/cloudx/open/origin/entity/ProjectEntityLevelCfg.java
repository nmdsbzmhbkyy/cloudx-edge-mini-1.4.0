

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>项目框架层级配置</p>
 * @ClassName: BuildingFrameCfg
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/7 11:14
 * @Copyright:
 */
@Data
@TableName("project_entity_level_cfg")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "配置项目区域层级")
public class ProjectEntityLevelCfg extends OpenBasePo<ProjectEntityLevelCfg> {
private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value="序列")
    private Integer seq;

    /**
     * 项目编码
     */
    @ApiModelProperty(value="项目编码")
    private Integer projectId;
    /**
     * 层级编号
     */
    @ApiModelProperty(value="层级编号")
    private Integer level;
    /**
     * 用于和第三方对接，编号位数
     */
    @ApiModelProperty(value="用于和第三方对接，编号位数")
    private Integer codeRule;
    /**
     * 层级描述
     */
    @ApiModelProperty(value="层级描述")
    private String levelDesc;
    /**
     * 是否启用
     */
    @ApiModelProperty(value="是否启用 0：启动，1关闭")
    private String isDisable;
    }
