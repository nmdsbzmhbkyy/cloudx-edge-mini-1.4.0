

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectPasswd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目密码库
 *
 * @author 王良俊
 * @date 2020-06-04 18:16:17
 */
@Mapper
public interface ProjectPasswdMapper extends BaseMapper<ProjectPasswd> {
    /**
     * 通过第三方code获取对象
     *
     * @param projectId
     * @param code
     * @author: 王伟
     * @since 2020-08-20
     * @return
     */
    ProjectPasswd getByCode(@Param("projectId") Integer projectId, @Param("code") String code);
}
