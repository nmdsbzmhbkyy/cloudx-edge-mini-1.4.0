package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>边缘网关的媒体广告对象</p>
 * <p>
 * 【说明】媒体广告下发、删除、启用/停用时使用该对象
 * 1、添加媒体广告使用ADD命令
 * 2、删除媒体广告使用DELETE命令
 * 3、设置媒体广告启用/体用使用SET命令
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-10-13 17:02:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("MultimediaObj")
public class AurineEdgeMultimediaDTO {

    /**
     * 播放列表ID
     * <p>
     * 媒体广告下发、删除时必选
     */
    private Long playListId;

    /**
     * 播放列表
     * <p>
     * 媒体广告下发时必选
     * 参数说明：
     * 内容中包括一个或多个json对象数组,json对象详见以下
     * "播放列表play_list json对象"说明
     */
    private List<PlayListItem> playList;

    /**
     * 播放周期
     * <p>
     * 媒体广告下发时必选
     * 参数说明：
     * 由cron表达式构成，以 *组成，代表秒、分、小时、日、
     * 月、周、年构成，按cron表达式实现周期播放
     */
    private String playCycle;

    /**
     * 播放周期
     * <p>
     * 媒体广告启用/停用时必选
     * 1：启用   0：停用
     */
    private Integer playEnable;


    /**
     * <p>播放列表playList对象</p>
     *
     * @author : 王良俊
     * @date : 2021-10-13 16:55:28
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayListItem {

        /**
         * 资源ID
         */
        private Long resourceId;

        /**
         * 资源类型
         * <p>
         * 1、图片；2、视频；3、音频
         */
        private String resourceType;

        /**
         * 资源格式
         * <p>
         * png、wav、mp4等格式
         */
        private String resourceFmt;

        /**
         * 资源url
         */
        private String resourceUrl;

        /**
         * 播放顺序
         * <p>
         * 取值1-N，值为0时不关心顺序，按设备端一定顺序播放
         */
        private Integer playOrder;

        /**
         * 播放时间
         * <p>
         * 以秒为单位，值为0时不关心播放时间，按设备端播放时间
         */
        private Integer playTime;

        /**
         * 间隔时间
         * <p>
         * 以秒为单位，指当本资源播放完成后，继续播放下个资源的间隔时间，
         * 值为0时不关心间隔，按设备端间隔时间
         */
        private Integer interval;
    }

    public static String getObjectName() {
        return "ADMediaObj";
    }
}
