

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectFaceResourceAppPageVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目人脸库，用于项目辖区内的人脸识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:06
 */
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.FACE_INFO)
@Mapper
public interface ProjectFaceResourcesMapper extends BaseMapper<ProjectFaceResources> {
    /**
     * 通过第三方code获取对象
     *
     * @param projectId
     * @param code
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    @SqlParser(filter = true)
    ProjectFaceResources getByCode(@Param("projectId") Integer projectId, @Param("code") String code);
    /**
     * 通过第三方code获取对象
     *
     * @param projectId
     * @param faceId
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    @SqlParser(filter = true)
    ProjectFaceResources getByFaceId(@Param("projectId") Integer projectId, @Param("faceId") String faceId);

    /**
     * 查询业主人脸通行
     *
     * @param page
     * @return
     */
    Page<ProjectFaceResourceAppPageVo> pagePersonFace(Page page, @Param("projectId") Integer projectId, @Param("dlStatus") String dlStatus);

    /**
     * 批量更新面部code
     * @return
     */
    @SqlParser(filter = true)
    Integer updateFaceCodeBatch( @Param("list") List<ProjectFaceResources> faceResourceList);

}
