

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectMediaAdDevCfg;
import com.aurine.cloudx.estate.vo.ProjectMediaAdDevCfgVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目媒体广告设备配置表
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:36:26
 */
public interface ProjectMediaAdDevCfgService extends IService<ProjectMediaAdDevCfg> {

    /**
     * 根据设备id查询发送设备列表信息
     *
     * @param deviceIds
     *
     * @return
     */
    List<ProjectMediaAdDevCfg> getMediaAdDevCfgByDeviceIds(List<String> deviceIds);

    List<ProjectMediaAdDevCfg> getMediaAdDevCfgByAdId(String adId);

    /**
     * 分页查询媒体广告发送设备列表信息
     *
     * @param page
     * @param projectMediaAdDevCfg
     *
     * @return
     */
    Page<ProjectMediaAdDevCfgVo> pageMediaAdDevCfg(Page page, @Param("query") ProjectMediaAdDevCfgVo projectMediaAdDevCfg);
}
