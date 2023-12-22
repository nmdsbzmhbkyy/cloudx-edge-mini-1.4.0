package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 * 图片服务器参数对象
 * </p>
 *
 * @ClassName: ImageServerObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:28:08
 * @Copyright:
 */
@Data
@AllArgsConstructor
public class ImageServerObj {

    /**
     * 服务器url地址
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
