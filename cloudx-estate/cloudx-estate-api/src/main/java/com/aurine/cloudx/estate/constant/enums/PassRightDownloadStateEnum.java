package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>通行权限 下载任务 状态</p>
 *
 * @ClassName: PassRightDownloadStateEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/25 18:00
 * @Copyright:
 */
@AllArgsConstructor
public enum PassRightDownloadStateEnum {
    /**
     * 下载成功
     */
    SUCCESS("1"),
    /**
     * 全部下载失败
     */
    ALLFAIL("0"),
    /**
     * 下载中的数据超过12小时
     */
    OUTTIME("3"),
    /**
     * 部分下载失败，ERROR
     */
    PARTFAIL("4"),

    /**
     * 下载中（或部分已经下载成功）
     */
    DOWNLOADING("2");
    public String code;
}
