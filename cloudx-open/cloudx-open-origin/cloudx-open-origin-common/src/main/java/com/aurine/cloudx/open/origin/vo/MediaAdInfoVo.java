package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectMediaRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>媒体广告信息对象</p>
 * @author : 王良俊
 * @date : 2021-10-14 11:02:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaAdInfoVo {

    /**
     * 一般取表自增序列 资源ID
     */
    private Long mediaId;

    /**
     * 媒体广告要播放的周期cron表达式
     */
    private String cron;

    /**
     * 具体的资源列表
     */
    List<ProjectMediaRepo> resourceList;
}
