

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectMediaAdFormVo;
import com.aurine.cloudx.open.origin.vo.ProjectMediaAdVo;
import com.aurine.cloudx.open.origin.entity.ProjectMediaAd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 媒体广告表
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:37:46
 */
@Mapper
public interface ProjectMediaAdMapper extends BaseMapper<ProjectMediaAd> {

    /**
     * 分页查询媒体广告信息
     *
     * @param page
     * @param projectMediaAdFormVo
     *
     * @return
     */
    Page<ProjectMediaAdVo> pageMediaAd(Page page, @Param("query") ProjectMediaAdFormVo projectMediaAdFormVo);

    /**
     * 获取媒体名称
     * @param adName
     * @return
     */
    List<ProjectMediaAd> getMediaAdByAdName(@Param("adName") String adName);
}
