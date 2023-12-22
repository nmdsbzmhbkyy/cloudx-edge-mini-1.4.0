

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectMediaRepo;
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

    /**
     * <p>根据广告ID获取所有广告资源列表</p>
     *
     * @param adSeq 广告自增序列
     * @return 广告资源列表
     * @author: 王良俊
     */
    List<ProjectMediaRepo> listMediaRepoByAdSeq(@Param("adSeq") Long adSeq);

}
