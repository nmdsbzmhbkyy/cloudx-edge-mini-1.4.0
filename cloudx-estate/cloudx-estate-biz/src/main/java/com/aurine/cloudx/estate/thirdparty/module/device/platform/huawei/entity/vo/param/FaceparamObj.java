package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 人脸参数对象 json名：faceParam
 * </p>
 *
 * @ClassName: FaceparamObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:14:34
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("faceParam")
public class FaceparamObj {

    /**
     * 人脸启用状态
     * 0：不启用 1：启用
     */
    @NotNull
    Integer faceEnable;

    /**
     * 安全级别
     * 0：高 1：正常 2：普通
     */
    @NotNull
    Integer securityLevel;

    /**
     * 活体检测
     * 0：不启用 1：启用
     */
    @NotNull
    Integer livenessEnable;

    /**
     * 人脸感应
     * 0：不启用 1：启用
     */
    Integer inductionEnable;
}
