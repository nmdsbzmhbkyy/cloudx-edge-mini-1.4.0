

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectMediaAdDevCfg;
import com.aurine.cloudx.estate.vo.ProjectMediaAdDevCfgVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目媒体广告设备配置表
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:36:26
 */
@Mapper
public interface ProjectMediaAdDevCfgMapper extends BaseMapper<ProjectMediaAdDevCfg> {

    /**
     * 分页查询发送设备列表信息
     *
     * @param page
     * @param projectMediaAdDevCfg
     *
     * @return
     */
    Page<ProjectMediaAdDevCfgVo> pageMediaAdDevCfg(Page page, @Param("query") ProjectMediaAdDevCfgVo projectMediaAdDevCfg);
}
