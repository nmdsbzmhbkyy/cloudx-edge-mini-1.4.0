

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectFingerprints;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 记录项目辖区内允许通行的指纹信息，供辖区内已开放通行权限的指纹识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:20:22
 */
@Mapper
public interface ProjectFingerprintsMapper extends BaseMapper<ProjectFingerprints> {
    /**
     * 通过第三方code获取对象
     *
     * @param projectId
     * @param code
     * @author: 王伟
     * @since 2020-08-20
     * @return
     */
    ProjectFingerprints getByCode(@Param("projectId") Integer projectId, @Param("code") String code);
}
