package com.aurine.cloudx.common.uniview.model;

public class GetVideoResponse extends VcsResponse<GetVideo> {
    public final static String STREAM_TYPE_RTMP = "rtmp";
    public final static String STREAM_TYPE_HLS = "hls";
    public final static String STREAM_TYPE_FLV = "flv";

    /**
     * 获取流类型
     * @param steamType
     * @return
     */
    public String getLiveUrl(String steamType) {
        if (STREAM_TYPE_RTMP.equals(steamType)) {
            return this.getData().getRtmpUrl();
        } else if (STREAM_TYPE_FLV.equals(steamType)) {
            return this.getData().getFlvUrl();
        } else {
            return this.getData().getHlsUrl();
        }
    }
}
