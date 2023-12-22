package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>边缘网关-系统参数对象</p>
 *
 * @author : 王良俊
 * @date : 2021-10-20 11:40:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdgeSysParamObj {

    /**
     * 人脸开门总开关
     * 0禁用、1启用（默认）
     */
    private Integer faceEnable;

    /**
     * 云对讲功能总开关
     * 0禁用、1启用（默认）
     */
    private Integer cloudTalkEnable;

    /**
     * 云社区Id
     * 用于设备端二维码识别
     */
    private String cloudAreaId;

    /**
     * ftp服务器ip地址
     */
    private String ftpIp;

    /**
     * ftp服务器端口
     */
    private Integer ftpPort;

    /**
     * ftp账号Id
     */
    private String ftpUser;

    /**
     * ftp账号密码
     */
    private String ftpPwd;

    /**
     * ftp黑名单图片目录
     */
    private String ftpPathBlacklist;

    /**
     * ftp陌生人图片目录
     */
    private String ftpPathStranger;

    /**
     * 电梯控制器项目密钥
     */
    private String liftKey;

    private Integer projectId;

}
