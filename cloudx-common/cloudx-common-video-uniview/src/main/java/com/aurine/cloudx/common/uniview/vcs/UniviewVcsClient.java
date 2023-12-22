package com.aurine.cloudx.common.uniview.vcs;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.aurine.cloudx.common.uniview.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 宇视云设备客户端
 */
public class UniviewVcsClient implements VcsClient {
    private static UniviewVcsClient client;

    private String domain = "https://ezcloud.uniview.com/openapi/";
    private String getTokenUrl = domain + "user/app/token/get";
    private String getDeviceUrl = domain + "device/get";
    private String getDeviceListUrl = domain + "device/list";
    private String getDeviceChannelListUrl = domain + "device/channel/list";
    private String getVideoUrl = domain + "cdn/video/get";
    private String startVideoUrl = domain + "cdn/video/start";
    private String stopVideoUrl = domain + "cdn/video/stop";
    // 应用ID
    private Long appId;
    // 秘钥
    private String secretKey;

    private GetTokenResponse token = null;

    // 初始化
    private UniviewVcsClient(Long appId, String secretKey) {
        this.appId = appId;
        this.secretKey = secretKey;
    }

    /**
     * 获取宇视客户端实例
     * @param appId
     * @param secretKey
     * @return
     */
    public static UniviewVcsClient getInstance(Long appId, String secretKey) throws Exception {
        if (client == null) {
            client = new UniviewVcsClient(appId, secretKey);
            client.getToken();
        }

        return client;
    }

    /**
     * 登录
     * 详见：http://ezcloud.uniview.com/doc/openapi/index.html#api-App-getAppToken
     */
    private void getToken() throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appId", appId);
        paramMap.put("secretKey", secretKey);

        String result = HttpUtil.post(getTokenUrl, paramMap);

        JSONObject jsonObject = new JSONObject(result);

        GetTokenResponse response = jsonObject.toBean(GetTokenResponse.class);

        if (response.getCode() == VcsResponse.SUCCESS) {
            this.token = response;
        } else {
            throw new Exception("宇视云平台接入失败，用户名或者密码异常");
        }
    }

    /**
     * 获取设备信息
     * http://ezcloud.uniview.com/doc/openapi/index.html#api-Device-getDevice
     */
    private String getDevice(String deviceSerial, String token) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceSerial", deviceSerial);

        HttpResponse response = HttpRequest.post(getDeviceUrl).header("Authorization", token).body(jsonObject.toString()).execute();

        return response.body();
    }

    /**
     * 获取设备列表
     * http://ezcloud.uniview.com/doc/openapi/index.html#api-Device-listDevice
     */
    private String getDeviceList(Integer pageNo, Integer pageSize, String token) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNo", pageNo);
        jsonObject.put("pageSize", pageSize);

        HttpResponse response = HttpRequest.post(getDeviceListUrl).header("Authorization", token).body(jsonObject.toString()).execute();

        return response.body();
    }

    /**
     * 获取设备通道列表
     * http://ezcloud.uniview.com/doc/openapi/index.html#api-Device-listChannel
     */
    private String getDeviceChannelList(String deviceSerial, Integer pageNo, Integer pageSize, String token) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceSerial", deviceSerial);
        jsonObject.put("pageNo", pageNo);
        jsonObject.put("pageSize", pageSize);

        HttpResponse response = HttpRequest.post(getDeviceChannelListUrl).header("Authorization", token).body(jsonObject.toString()).execute();

        return response.body();
    }

    /**
     * 获取指定设备通道的直播/回放视频地址
     * 详见：http://ezcloud.uniview.com/doc/openapi/index.html#api-CDN-getCdnVideo
     */
    private GetVideoResponse getVideo(GetVideoRequest params) throws Exception {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("deviceSerial", params.getDeviceSerial());
        jsonObject.put("channelNo", params.getChannelNo());
        jsonObject.put("streamIndex", params.getStreamIndex());

        if ("record".equals(params.getStreamType())) {
            jsonObject.put("streamType", params.getStreamType());
            jsonObject.put("startTime", params.getStartTime());
            jsonObject.put("endTime", params.getEndTime());
            jsonObject.put("recordTypes", params.getRecordTypes());
        }

        HttpResponse response = HttpRequest.post(getVideoUrl).header("Authorization", token.getToken()).body(jsonObject.toString()).execute();

        if (response.isOk()) {
            JSONObject json = new JSONObject(response.body());
            return json.toBean(GetVideoResponse.class);
        } else {
            throw new Exception("接口执行异常");
        }
    }

    /**
     * 开启直播/回放
     * http://ezcloud.uniview.com/doc/openapi/index.html#api-CDN-startCdnVideo
     */
    private StartVideoResponse startVideo(StartVideoRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("videoUrl", request.getVideoUrl());

        HttpResponse response = HttpRequest.post(startVideoUrl).header("Authorization", token.getToken()).body(jsonObject.toString()).execute();

        if (response.isOk()) {
            JSONObject json = new JSONObject(response.body());
            return json.toBean(StartVideoResponse.class);
        } else {
            throw new Exception("接口执行异常");
        }
    }

    /**
     * 停止推流
     */
    private String stopVideo(String liveId, String token) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("liveId", liveId);

        HttpResponse response = HttpRequest.post(stopVideoUrl).header("Authorization", token).body(jsonObject.toString()).execute();

        return response.body();
    }

    @Override
    public <T extends VcsResponse> T getVcsResponse(VcsRequest<T> request) throws Exception {
        VcsResponse resp = null;
        // token是否过期
        if (this.token == null || this.token.isExpire()) {
            getToken();
        }

        if (request instanceof GetVideoRequest) {
            resp = getVideo((GetVideoRequest) request);
        } else if (request instanceof StartVideoRequest) {
            resp = startVideo((StartVideoRequest) request);
        }

        if (resp == null) {
            throw new Exception("接口执行异常，当前版本无法使用改接口");
        } else if (resp.getCode() == VcsResponse.SUCCESS) {
            return (T) resp;
        } else if (resp.getCode() == VcsResponse.ERROR_TOKEN) {
            this.token = null;
            return getVcsResponse(request);
        } else {
            throw new Exception("接口执行异常，" + resp.getMessage());
        }
    }

    /**
     * 获取播放地址并执行推送流接口
     * @return
     */
    public String getLiveUrl(GetLiveUrlVo getLiveUrlVo) throws Exception {
        // 获取直播流
        GetVideoRequest getVideoRequest = new GetVideoRequest();

        getVideoRequest.setDeviceSerial(getLiveUrlVo.getDeviceSerial());
        getVideoRequest.setChannelNo(getLiveUrlVo.getChannelNo());
        getVideoRequest.setStreamIndex(getLiveUrlVo.getStreamIndex());

        GetVideoResponse getVideoResponse = getVcsResponse(getVideoRequest);
        String liveUrl = getVideoResponse.getLiveUrl(getLiveUrlVo.getType());

        // 启动直播流
        StartVideoRequest startVideoRequest = new StartVideoRequest();
        startVideoRequest.setVideoUrl(liveUrl);

        getVcsResponse(startVideoRequest);

        return liveUrl;
    }

    /**
     * 获取播放地址并执行推送流接口
     * @return
     */
    public String getRecordUrl(GetRecordUrlVo getRecordUrlVo) throws Exception {
        // 获取直播流
        GetVideoRequest getVideoRequest = new GetVideoRequest();

        getVideoRequest.setDeviceSerial(getRecordUrlVo.getDeviceSerial());
        getVideoRequest.setChannelNo(getRecordUrlVo.getChannelNo());
        getVideoRequest.setStreamIndex(getRecordUrlVo.getStreamIndex());
        getVideoRequest.setStreamType("record");
        getVideoRequest.setStartTime(getRecordUrlVo.getStartTime());
        getVideoRequest.setEndTime(getRecordUrlVo.getEndTime());
        getVideoRequest.setRecordTypes(getRecordUrlVo.getRecordTypes());

        GetVideoResponse getVideoResponse = getVcsResponse(getVideoRequest);
        String liveUrl = getVideoResponse.getLiveUrl(getRecordUrlVo.getType());

        // 启动直播流
        StartVideoRequest startVideoRequest = new StartVideoRequest();
        startVideoRequest.setVideoUrl(liveUrl);

        getVcsResponse(startVideoRequest);

        return liveUrl;
    }
}
