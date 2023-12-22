package com.aurine.cloudx.wjy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 房间映射实体
 * @author ： huangjj
 * @date ： 2021/4/14
 * @description： 我家云房间映射
 */
@TableName("wjy_room")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "房间映射实体")
public class Room extends Model<Room> {
    /**
     * 自增序列
     */
    @TableId
    @ApiModelProperty(value = "自增序列")
    private Integer seq;
    /**
     * 4.0房间id
     */
    @ApiModelProperty(value = "4.0房间id")
    private String roomId;
    /**
     * 我家云房间id
     */
    @ApiModelProperty(value = "我家云房间id")
    private String wjyRoomId;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 单元id
     */
    @ApiModelProperty(value = "单元id")
    private String unitId;
    /**
     * 房间名称
     */
    @ApiModelProperty(value = "房间名称")
    private String roomName;
    /**
     * 生成时间
     */
    @ApiModelProperty(value = "生成时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}