package com.aurine.cloudx.open.origin.mapper;


import com.aurine.cloudx.open.origin.dto.ProjectCarPreRegisterAuditDto;
import com.aurine.cloudx.open.origin.dto.ProjectCarPreRegisterDto;
import com.aurine.cloudx.open.origin.entity.ProjectCarPreRegister;
import com.aurine.cloudx.open.origin.vo.ProjectCarPreRegisterInfoVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectCarPreRegisterMapper extends BaseMapper<ProjectCarPreRegister> {

    /**
     * <p>
     * 分页查询设备预登记页
     * (这里如果自动注入项目ID会导致无法查询到审核人信息)
     * </p>
     *
     * @param page  分页信息
     * @param query 查询条件
     * @author: 王良俊
     */
    @SqlParser(filter = true)
    Page<ProjectCarPreRegisterDto> fetchList(Page page, @Param("query") ProjectCarPreRegisterDto query, @Param("projectId") Integer projectId);

    /**
     * <p>
     * 根据预登记ID获取预登记审核页需要的部分信息
     * </p>
     *
     * @param preRegId 预登记ID
     * @author: 王良俊
     */
    ProjectCarPreRegisterAuditDto getAuditObj(@Param("preRegId") String preRegId);

    /**
     * <p>
     * 根据预登记ID获取到这个已审核通过的登记信息（包含车场名等）
     * </p>
     *
     * @param preRegId 预登记ID
     * @author: 王良俊
     */
    ProjectCarPreRegisterInfoVo getAuditInfo(@Param("preRegId") String preRegId);

    Integer countByOff();

}
