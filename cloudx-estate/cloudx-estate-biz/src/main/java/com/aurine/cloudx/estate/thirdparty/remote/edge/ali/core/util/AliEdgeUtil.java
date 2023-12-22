package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiCacheConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiMethodConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.core.entity.AliEdgeCacheConstant;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Base64;
import java.util.UUID;

/**
 * 阿里边缘网关连接工具类
 *
 * @ClassName: AliEdgeUtil
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 14:50
 * @Copyright:
 */
@Slf4j
public class AliEdgeUtil {


    /**
     * Authorization生成
     * 生成规则：
     * http请求头Authorization部分，统一使用Basic方式，存放的Basic认证内容包括所有接口的4个公共参数appKey、timestamp（当前unix时间戳）、verify（校验字符串）、accesstoken（访问accesstoken）。
     * 内容格式为：“appKey:timestamp:verify:accesstoken”，各个参数位置固定不变，参数间用冒号“:”隔开，某项参数不传时对应位置留空（如“appKey:timestamp:verify::type”）,verify生成规则为MD5（timestamp+“|”+appSecret)。如下：
     * <p>
     * Authorization: Basic base64(10001:1408430177:verify验证串:accesstoken)
     *
     * @param appkey    AppKey
     * @param appSecret 授权码
     * @return
     */
    @SneakyThrows
    public static String signGenerator(String appkey, String appSecret, String token) {
        //verify 生成规则为 MD5（timestamp+“|”+appSecret)
        String timestamp = String.valueOf(System.currentTimeMillis());
        String verify = timestamp + "|" + appSecret;
        verify = DigestUtils.md5DigestAsHex(verify.getBytes());

        String sign = appkey + ":" + timestamp + ":" + verify + ":" + token;
//        sign = "Basic " + DigestUtils.md5DigestAsHex(sign.getBytes());

        String asB64 = Base64.getEncoder().encodeToString(sign.getBytes("utf-8"));
        sign = "Basic " + asB64;

        return sign;
    }
}
