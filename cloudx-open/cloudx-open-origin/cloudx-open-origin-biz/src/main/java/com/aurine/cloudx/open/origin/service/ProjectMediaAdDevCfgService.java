

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectMediaAdDevCfg;
import com.aurine.cloudx.open.origin.vo.ProjectMediaAdDevCfgVo;
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

    /**
     * <p>删除设备和媒体广告的关联关系</p>
     *
     * @param deviceId 设备ID
     * @author: 王良俊
     */
    void removeRel(String deviceId);
}
