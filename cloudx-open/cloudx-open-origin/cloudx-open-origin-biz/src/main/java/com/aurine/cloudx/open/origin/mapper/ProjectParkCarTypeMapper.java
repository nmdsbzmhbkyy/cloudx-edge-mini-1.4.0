package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.dto.ProjectParkCarTypeDto;
import com.aurine.cloudx.open.origin.entity.ProjectParkCarType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Classname ProjectParkCarTypeMapper
 * @Description 车辆类型
 * @Date 2022/5/10 14:27
 * @Created by linlx
 */
@Mapper
public interface ProjectParkCarTypeMapper extends BaseMapper<ProjectParkCarType> {

    Page<ProjectParkCarTypeDto> pageCarType(Page page, @Param("query") ProjectParkCarTypeDto projectParkCarTypeDto, @Param("projectId") Integer projectId);

    ProjectParkCarTypeDto getParkCarTypeById(@Param("typeId") String typeId);

    boolean delete(@Param("typeId") String typeId);

    boolean updateIsDisable(@Param("typeId") String typeId, @Param("isDisable") String isDisable);

    List<String> isInRegister(@Param("typeId") String typeId);

    List<ProjectParkCarTypeDto> getAllType(@Param("projectId") Integer projectId);

    ProjectParkCarTypeDto getParkDefault(@Param("parkId") String parkId, @Param("carType") Integer carType);

    List<ProjectParkCarTypeDto> getByParkId(@Param("parkId") String parkId);

}
