

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.vo.ProjectParkingInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 停车区域
 *
 * @author 王伟
 * @date 2020-05-07 09:13:25
 */
@Mapper
public interface ProjectParkingInfoMapper extends BaseMapper<ProjectParkingInfo> {

    IPage<ProjectParkingInfoVo> select(IPage<?> page, @Param("query")ProjectParkingInfoVo parkingInfo);

}
