package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 版本信息对象
 * </p>
 *
 * @ClassName: VersionObj json名：version
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:21:05
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("version")
public class VersionObj {

    /**
     * 系统版本
     */
    @NotNull
    String systemVer;

    /**
     * 软件版本
     */
    @NotNull
    String softwareVer;

    /**
     * 硬件版本
     */
    @NotNull
    String hardwareVer;

    /**
     * 编译版本
     */
    String buildVer;

    /**
     * 人脸库版本
     */
    String faceLibVer;

}
