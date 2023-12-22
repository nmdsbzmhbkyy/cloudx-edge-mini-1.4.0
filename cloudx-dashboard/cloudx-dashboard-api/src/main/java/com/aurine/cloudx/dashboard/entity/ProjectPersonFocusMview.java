package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 关注人群
 *
 * @ClassName: ProjectPersonFocusMview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-29 14:37
 * @Copyright:
 */
@Data
@TableName("PROJECT_PERSON_FOCUS_MVIEW")
public class ProjectPersonFocusMview extends BaseDashboardEntity {


    /**
     * 空穴老人
     */
    @ApiModelProperty("空穴老人")
    @TableField("CNT_KCLR")
    private Long kclr;
    /**
     * 留守儿童
     */
    @ApiModelProperty("留守儿童")
    @TableField("CNT_LSET")
    private Long lset;
    /**
     * 伤残人士
     */
    @ApiModelProperty("伤残人士")
    @TableField("CNT_SCRY")
    private Long scry;

    /**
     * 烈士家属
     */
    @ApiModelProperty("烈士家属")
    @TableField("CNT_LSJS")
    private Long lsjs;


}
