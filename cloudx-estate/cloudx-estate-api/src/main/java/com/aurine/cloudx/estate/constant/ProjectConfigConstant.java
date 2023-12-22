package com.aurine.cloudx.estate.constant;

/**
 * Title: ProjectConfigConstant
 * Description: 项目设置相关常量
 *
 * @author guhl
 * @version 1.0.0
 * @date 2020/7/10 16:33
 */
public interface ProjectConfigConstant {
    /**
     * 增值服务默认全开
     */
    String SERVICE_INITAL_STATUS_OPEN = "1";
    /**
     * 增值服务默认全关
     */
    String SERVICE_INITAL_STATUS_CLOSE = "0";
    /**
     * 系统审核
     */
    String SYSTEM_IDENTITY = "1";
    /**
     * 物业审核
     */
    String PEOPLE_IDENTITY = "2";
    /**
     * 自动延期开启
     **/
    String AUTO_DELAY_ON = "1";
    /**
     * 自动延期关闭
     */
    String AUTO_DELAY_OFF = "0";
}
