

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.CardInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectCard;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:18
 */
@Mapper
public interface ProjectCardMapper extends BaseMapper<ProjectCard> {

    /**
     * 通过第三方code获取对象
     *
     * @param projectId
     * @param code
     * @return
     */
    @SqlParser(filter = true)
    ProjectCard getByCode(@Param("projectId") Integer projectId, @Param("code") String code);

    /**
     * 通过卡号获取对象
     *
     * @param projectId
     * @param cardNo
     * @return
     */
    @SqlParser(filter = true)
    ProjectCard getByCardNo(@Param("projectId") Integer projectId, @Param("cardNo") String cardNo);

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<CardInfoVo> page(Page page, @Param("query") ProjectCard po);
}
