package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 远程服务器参数对象
 * </p>
 *
 * @ClassName: RemoteServerObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:25:29
 * @Copyright:
 */
@Data
@AllArgsConstructor
public class RemoteServerObj {

    /**
     * 服务器名称
     * “ImageServer”：图片服务器
     * “CBS”：数据备份服务器
     */
    @NotNull
    String serverName;

    /**
     * 服务器地址
     */
    String url;

    /**
     * 账户名
     */
    String account;

    /**
     * 账户密码
     */
    String accountPwd;

}
