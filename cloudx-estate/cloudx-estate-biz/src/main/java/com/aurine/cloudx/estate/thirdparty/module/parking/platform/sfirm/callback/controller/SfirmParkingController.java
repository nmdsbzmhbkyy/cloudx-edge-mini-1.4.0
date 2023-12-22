package com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.callback.controller;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.CallBackURIConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.callback.factory.SfirmParkingCallbackFactory;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.config.SfirmConstant;
import com.aurine.cloudx.estate.thirdparty.module.parking.util.SfirmUtil;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>赛菲姆车场回调接收</p>
 *
 * @ClassName: ParkingController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-23 15:53
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping(CallBackURIConstant.PARKING_SFIRM)
@Slf4j
@Api(value = "SfirmParkingController", tags = "赛菲姆车场回调接口")
public class SfirmParkingController {

    /**
     * 入场
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/parkingIn")
    public JSONObject parkingIn(@RequestBody JSONObject json) {

        log.info("赛菲姆车场： 获取到入场记录 {}", json);
        //校验数据头
        String method = json.getString("method");     //获取方法名称
        String version = json.getString("version");   //获取版本号
        String appid = json.getString("appid");       //获取appid
        JSONObject dataObj = json.getJSONObject("data");  //获取数据对象

        VersionEnum versionEnum = VersionEnum.V1;


        SfirmParkingCallbackFactory.getInstance(versionEnum).enterCar(dataObj);
        return successReturn();
    }

    /**
     * 入场照片
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/parkingInImg")
    public JSONObject parkingInPhoto(@RequestBody JSONObject json) {
        log.info("赛菲姆车场： 获取到入场照片 {}", json);
        //校验数据头
        String method = json.getString("method");     //获取方法名称
        String version = json.getString("version");   //获取版本号
        String appid = json.getString("appid");       //获取appid
        JSONObject dataObj = json.getJSONObject("data");  //获取数据对象

        VersionEnum versionEnum = VersionEnum.V1;

        SfirmParkingCallbackFactory.getInstance(versionEnum).enterImg(dataObj);
        return successReturn();
    }

    /**
     * 出场
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/parkingOut")
    public JSONObject parkingOut(@RequestBody JSONObject json) {
        log.info("赛菲姆车场： 获取到出场记录 {}", json);
        //校验数据头
        String method = json.getString("method");     //获取方法名称
        String version = json.getString("version");   //获取版本号
        String appid = json.getString("appid");       //获取appid
        JSONObject dataObj = json.getJSONObject("data");  //获取数据对象

        VersionEnum versionEnum = VersionEnum.V1;

        SfirmParkingCallbackFactory.getInstance(versionEnum).outerCar(dataObj);
        return successReturn();
    }

    /**
     * 出场照片
     *
     * @param json
     * @return
     */
    @Inner(value = false)
    @PostMapping("/parkingOutImg")
    public JSONObject parkingOutPhoto(@RequestBody JSONObject json) {
        log.info("赛菲姆车场： 获取到出场照片 {}", json);
        //校验数据头
        String method = json.getString("method");     //获取方法名称
        String version = json.getString("version");   //获取版本号
        String appid = json.getString("appid");       //获取appid
        JSONObject dataObj = json.getJSONObject("data");  //获取数据对象

        VersionEnum versionEnum = VersionEnum.V1;

        SfirmParkingCallbackFactory.getInstance(versionEnum).outerImg(dataObj);
        return successReturn();
    }


    /**
     * 成功回调内容
     *
     * @return
     */
    private JSONObject successReturn() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", "1");
        resultJson.put("msg", "ok");
        resultJson.put("rand", SfirmUtil.random());
        resultJson.put("sign", SfirmUtil.signGenerator(resultJson, SfirmConstant.appSecrets));
        return resultJson;
    }


}
