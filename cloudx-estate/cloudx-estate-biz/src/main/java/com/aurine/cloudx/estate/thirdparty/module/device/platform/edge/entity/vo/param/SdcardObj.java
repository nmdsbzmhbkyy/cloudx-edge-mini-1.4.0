package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 存储卡信息对象 json名：sdcardInfo
 * </p>
 *
 * @ClassName: SdcardObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:07:57
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("sdcardInfo")
public class SdcardObj {

    /**
     * 总容量
     * 单位：MB
     */
    @NotNull
    Integer totalCapacity;

    /**
     * 已用容量
     * 单位：MB
     */
    @NotNull
    Integer usedCapacity;

    /**
     * 剩余容量
     * 单位：MB
     */
    @NotNull
    Integer freeCapacity;

    /**
     * 媒体信息列表
     * 媒体公告名称列表,多个媒体之间采用#分割
     */
    String mediaList;
}
