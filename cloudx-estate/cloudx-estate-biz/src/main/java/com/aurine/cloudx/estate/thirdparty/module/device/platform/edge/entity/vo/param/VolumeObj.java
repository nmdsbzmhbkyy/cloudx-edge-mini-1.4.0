package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 音量参数对象
 * </p>
 *
 * @ClassName: VolumeObj json名：volumeParam
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:05:29
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("volumeParam")
public class VolumeObj {

    /**
     * 通话音量
     * 音量值
     * */
    @NotNull
    Integer talkVolume;

    /**
     *提示音
     * 0:不启用 1:启用
     * */
    @NotNull
    Integer tipTone;

    /**
     *按键音
     * 0:不启用 1:启用
     * */
    @NotNull
    Integer keyTone;

    /**
     *媒体静音
     * 0:不启用 1:启用
     * */
    Integer mediaMute;
}
