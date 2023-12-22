

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectMediaAd;
import com.aurine.cloudx.estate.entity.ProjectMediaAdDevCfg;
import com.aurine.cloudx.estate.vo.ProjectMediaAdFormVo;
import com.aurine.cloudx.estate.vo.ProjectMediaAdVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 媒体广告表
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:37:46
 */
public interface ProjectMediaAdService extends IService<ProjectMediaAd> {

    /**
     * 发送媒体广告
     *
     * @param projectMediaAdVo
     *
     * @return
     */
    boolean saveMedia(ProjectMediaAdFormVo projectMediaAdVo);

    /**
     * 格式化媒体设备
     *
     * @param projectMediaAdDevCfgs
     *
     * @return
     */
    boolean cleanMedia(List<ProjectMediaAdDevCfg> projectMediaAdDevCfgs);

    /**
     * 重新发送广告
     *
     * @param adId
     * @param deviceId
     *
     * @return
     */
    boolean resend(String adId, String deviceId);

    /**
     * 分页查询媒体广告
     *
     * @param page
     * @param projectMediaAdFormVo
     *
     * @return
     */
    Page<ProjectMediaAdVo> pageMediaAd(Page page, @Param("query") ProjectMediaAdFormVo projectMediaAdFormVo);

    /**
     * 重新发送所有发送失败的广告
     *
     * @param id
     *
     * @return
     */
    boolean resendAll(String id);

    /**
     * 获取失败的Project集合,即不为成功的状态
     *
     * @param adId
     *
     * @return
     */
    List<ProjectMediaAdDevCfg> projectMediaAdDevCfg(String adId);
}
