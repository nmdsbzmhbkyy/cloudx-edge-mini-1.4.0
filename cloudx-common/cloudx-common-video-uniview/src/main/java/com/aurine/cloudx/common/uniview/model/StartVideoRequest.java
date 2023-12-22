package com.aurine.cloudx.common.uniview.model;

import lombok.Data;

@Data
public class StartVideoRequest extends VcsRequest<StartVideoResponse> {
    // 视频播放地址
    private String videoUrl;
}
