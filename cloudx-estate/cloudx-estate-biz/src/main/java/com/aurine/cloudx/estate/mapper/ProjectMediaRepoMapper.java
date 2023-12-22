

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectMediaRepo;
import com.aurine.cloudx.estate.vo.ProjectMediaRepoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目媒体资源库
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:35:57
 */
@Mapper
public interface ProjectMediaRepoMapper extends BaseMapper<ProjectMediaRepo> {

    List<ProjectMediaRepo> queryObj(String repoId);

    /**
     * <p>根据广告ID获取所有广告资源列表</p>
     *
     * @param adSeq 广告自增序列
     * @return 广告资源列表
     * @author: 王良俊
     */
    List<ProjectMediaRepo> listMediaRepoByAdSeq(@Param("adSeq") Long adSeq);

    /**
     * 获取资源具体信息
     * @param repoIds
     * @return
     */
    List<ProjectMediaRepoVo> getMediaRepoList(@Param("query") List repoIds);
}
