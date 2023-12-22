package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums;

import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>凭证下载状态 WR20 - Cloud4.0  映射枚举</p>
 *
 * @ClassName: WR20CredentialTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-23 16:29
 * @Copyright:
 */
@AllArgsConstructor
public enum WR20CertDownloadStateEnum {

    DELETE("0", "0", "已删除"),
    FAIL("-3", PassRightCertDownloadStatusEnum.FAIL.code, "失败"),
    FULL("-4", PassRightCertDownloadStatusEnum.OUT_OF_MEMORY.code, "卡已满"),
    SUCCESS("1", PassRightCertDownloadStatusEnum.SUCCESS.code, "已下载"),
    FREEZE("2", PassRightCertDownloadStatusEnum.FREEZE.code, "已停用");

    /**
     * wr20 编码
     */
    public String wr20Code;

    /**
     * 4.0 编码
     */
    public String cloudCode;
    /**
     * 描述
     */
    public String desc;


    /**
     * @param wr20Code
     * @return
     */
    public static WR20CertDownloadStateEnum getByWR20Code(String wr20Code) {
        if (StringUtils.isEmpty(wr20Code)) {
            return null;
        }
        for (WR20CertDownloadStateEnum value : WR20CertDownloadStateEnum.values()) {
            if (value.wr20Code.equals(wr20Code)) {
                return value;
            }
        }
        return WR20CertDownloadStateEnum.FAIL;
    }

    /**
     * @param cloudCode
     * @return
     */
    public static WR20CertDownloadStateEnum getByCloudCode(String cloudCode) {
        for (WR20CertDownloadStateEnum value : WR20CertDownloadStateEnum.values()) {
            if (value.cloudCode.equals(cloudCode)) {
                return value;
            }
        }
        return WR20CertDownloadStateEnum.FAIL;
    }

}
