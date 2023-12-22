package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 服务器参数对象 json名：serverParam
 * </p>
 *
 * @ClassName: ServerParamObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:03:06
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("serverParam")
public class ServerParamObj {

    /**
     * 管理员机IP	 Ip地址，默认0.0.0.0
     */
    String managerIp;

    /**
     * 中心服务器IP Ip地址，默认0.0.0.0
     */
    String centerIp;

    /**
     * 流媒体服务器IP	 默认0.0.0.0
     */
    String rtspServer;

    /**
     * 电梯控制器IP		 默认0.0.0.0
     */
    String elevator;
    /**
     * 图片服务器url		 默认0.0.0.0
     */
    String imageServer;
    /**
     * 备份服务器url			 默认0.0.0.0
     */
    String backupServer;
}
