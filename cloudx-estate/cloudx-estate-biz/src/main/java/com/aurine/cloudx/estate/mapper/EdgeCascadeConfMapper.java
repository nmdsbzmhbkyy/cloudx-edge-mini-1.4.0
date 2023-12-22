

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.EdgeCascadeConf;
import com.aurine.cloudx.estate.vo.EdgeCascadeConfVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>项目连接码配置服务</p>
 * @author : 王良俊
 * @date : 2021-12-10 15:39:34
 */
@Mapper
public interface EdgeCascadeConfMapper extends BaseMapper<EdgeCascadeConf> {

    /**
     * <p>获取级联入云配置VO对象</p>
     *
     * @param projectId 项目ID
     */
    EdgeCascadeConfVo getConfVo(@Param("projectId") Integer projectId);
}
