package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>介质下载状态类型</p>
 *
 * @ClassName: BuildingUnitTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/9 8:52
 * @Copyright:
 */
@AllArgsConstructor
public enum PassRightCertDownloadStatusEnum {
    /**
     * 已停用
     */
    FREEZE("0"),
    /**
     * 下载成功
     */
    SUCCESS("1"),
    /**
     * 下载失败
     */
    FAIL("2"),
    /**
     * 下载中
     */
    DOWNLOADING("3"),
    /**
     * 空间不足
     */
    OUT_OF_MEMORY("4"),
    /**
     * 删除中
     */
    DELETING("5"),
    /**
     * 正在停用
     */
    FREEZING("6"),
    /**
     * 重试下载（不是介质下载状态，如果是这个则说明要对该介质进行重新下发操作）
     */
    RE_DOWNLOAD("99");

    public String code;
}
