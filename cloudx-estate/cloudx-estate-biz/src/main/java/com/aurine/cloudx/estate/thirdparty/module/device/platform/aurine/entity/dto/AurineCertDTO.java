package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto;

import lombok.Data;

/**
 * 通行凭证VO
 *
 * @ClassName: AurineCertVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-18 11:41
 * @Copyright:
 */
@Data
public class AurineCertDTO {

    /**
     * 凭证第三方id
     */
    private String keyid;

    /**
     * 凭证类型
     * type 为1时value为卡号 type
     * 为2时，value为密码 type
     * 为3时，value为照片URL地址（必需公网 可见）
     * 返
     */
    private String keytype;

    /**
     * 凭证值
     */
    private String keyvalue;

    private String roomcode;



}
