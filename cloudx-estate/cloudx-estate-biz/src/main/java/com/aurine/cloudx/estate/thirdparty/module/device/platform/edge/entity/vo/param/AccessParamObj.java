package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 门禁参数对象 json名：accessParam
 * </p>
 *
 * @ClassName: AccessParamObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:03:06
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("accessParam")
public class AccessParamObj {

    /**
     * 是否启用人脸开门功能		 0禁用（默认）、1启用
     */
    Integer faceEnable;
    /**
     * 	是否启用二维码开门功能		 0禁用（默认）、1启用
     */
    Integer qrcodeEnable;
    /**
     * 是否启用密码开门功能			 0禁用（默认）、1启用
     */
    Integer pwdEnable;

    /**
     * 密码模式
     * 0：简易密码(默认)
     * 1：高级密码
     */
    Integer pwdMode;
    /**
     * 是否启用临时密码开门				 0禁用（默认）、1启用
     */
    Integer pwdTempEnable;
    /**
     * 是否启用刷卡开门功能				 0禁用（默认）、1启用
     */
    Integer cardEnable;


}
