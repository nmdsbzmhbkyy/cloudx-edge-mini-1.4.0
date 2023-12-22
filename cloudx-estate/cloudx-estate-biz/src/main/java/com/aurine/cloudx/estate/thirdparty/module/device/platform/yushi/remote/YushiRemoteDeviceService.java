

package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.remote;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.request.YushiConnectDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.respond.YushiResponse;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;

public interface YushiRemoteDeviceService extends BaseRemote {

    JSONObject systemDeviceInfo(YushiConnectDTO yushiConnectDTO) throws Exception;

    JSONObject eventSubscribe(YushiConnectDTO yushiConnectDTO) throws Exception;

    JSONObject eventSubscribeRefresh(YushiConnectDTO yushiConnectDTO, Integer subscribeId) throws Exception;

    JSONObject videoLiveStream(YushiConnectDTO yushiConnectDTO) throws Exception;

}
