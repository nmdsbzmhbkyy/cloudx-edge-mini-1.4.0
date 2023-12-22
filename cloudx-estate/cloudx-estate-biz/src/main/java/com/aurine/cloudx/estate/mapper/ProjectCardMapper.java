

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectCardVo;
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
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.CARD_INFO)
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
     * 分页查询门禁卡
     *
     * @param page
     * @param projectCardVo
     * @return
     */
    @SqlParser(filter = true)
    Page<ProjectCardVo> cardPage(Page page, @Param("query") ProjectCardVo projectCardVo, @Param("projectId") Integer projectId);


    /**
     * 查询住户/员工权限是否到期和禁用
     *
     * @param personId
     * @return
     */
    Integer getPersonStatus(@Param("personId") String personId);

    /**
     * 查询访客权限是否到期
     *
     * @param personId
     * @return
     */
    Integer getVisitorStatus(@Param("personId") String personId);


    ProjectCardVo getVisitorInfo(@Param("cardNo") String cardNo);

}
