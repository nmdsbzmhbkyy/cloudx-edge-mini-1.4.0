package com.aurine.cloudx.estate.thirdparty.module.parking.platform.reformer.callback.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.reformer.callback.ReformerRemoteParkingCallbackService;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-06-09 11:50
 */
@Service
public class ReformerRemoteParkingCallbackServiceImplV1 implements ReformerRemoteParkingCallbackService {
    @Override
    public void enterCar(JSONObject jsonObject) {

    }

    @Override
    public void outerCar(JSONObject jsonObject) {

    }

    @Override
    public void enterImg(JSONObject jsonObject) {

    }

    @Override
    public void outerImg(JSONObject jsonObject) {

    }

    @Override
    public String getVersion() {
        return null;
    }
}
