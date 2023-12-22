package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 *   备份服务器参数对象
 * </p>
 * @ClassName: BackupServerObj
 * @author: 王良俊 <>
 * @date:  2020年11月25日 下午04:29:36
 * @Copyright: 
*/
@Data
@AllArgsConstructor
public class BackupServerObj {

    /**
     *服务器url地址
     * */
    String url;

    /**
     * 账户名
     * */
    String account;

    /**
     * 账户密码
     * */
    String accountPwd;
}
