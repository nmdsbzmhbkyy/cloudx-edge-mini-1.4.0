

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.vo.ProjectHouseStatusVo;
import com.aurine.cloudx.estate.vo.ProjectPersonInfoVo;
import com.aurine.cloudx.estate.vo.ProjectUserHouseInfoVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 人员
 *
 * @author 王伟
 * @date 2020-05-11 09:12:50
 */
@Mapper
public interface ProjectPersonInfoMapper extends BaseMapper<ProjectPersonInfo> {
    /**
     * 统计人员类型
     * @return
     */
    List<Map<String, Object>> groupByPersonType();

    /**
     * 根据用户id变更手机号
     * @param phone
     * @param userId
     */
    @SqlParser(filter = true)
    void updatePhoneByUserId(@Param("phone") String phone,@Param("userId") Integer userId);

    /**
     * 根据手机号变更用户id
     * @param phone
     * @param userId
     */
    @SqlParser(filter = true)
    void updateUserIdByPhone(@Param("phone") String phone,@Param("userId") Integer userId);

    List<ProjectHouseStatusVo> getListPersonById(@Param("userId") Integer userId);

    List<ProjectUserHouseInfoVo> getListUserHouseInfoByUserId(@Param("userId") Integer userId);

    @SqlParser(filter = true)
    List<ProjectPersonInfo> getPersonByTelephone(@Param("phone") String phone);

}
