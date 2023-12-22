

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.PasswordInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectPasswd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    ProjectPasswd getByCode(@Param("projectId") Integer projectId, @Param("code") String code);

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<PasswordInfoVo> page(Page page, @Param("query") ProjectPasswd po);
}
