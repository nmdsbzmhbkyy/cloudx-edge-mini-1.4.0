

package com.aurine.cloud.code.platform;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloud.code.entity.IdCard;
import com.aurine.cloud.code.entity.QrCard;
import com.aurine.cloud.code.entity.dto.ResultCode;


/**
 * 检验健康码
 *
 * @ClassName: PassWayHealthService
 * @author: yz
 */
public interface PassWayHealthService extends BaseService {

    /**
     * 通过二维码校验健康码结果
     *
     * @param qrCard 健康吗对象
     * @return
     */
    ResultCode CheckHealth(QrCard qrCard);


    /**
     * 通过身份证校验健康码结果
     *
     * @param idCard 身份证对象
     * @return
     */
    ResultCode CheckHealthByCode(IdCard idCard);

}
