

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.PersonAssetsNumVo;
import com.aurine.cloudx.open.origin.vo.ProjectUserHouseInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectPersonInfo;
import com.aurine.cloudx.open.origin.vo.ProjectHouseStatusVo;
import com.aurine.cloudx.open.common.entity.vo.PersonInfoVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    void updatePhoneByUserId(@Param("phone") String phone, @Param("userId") Integer userId);

    /**
     * 根据手机号变更用户id
     * @param phone
     * @param userId
     */
    @SqlParser(filter = true)
    void updateUserIdByPhone(@Param("phone") String phone, @Param("userId") Integer userId);

    List<ProjectHouseStatusVo> getListPersonById(@Param("userId") Integer userId);

    List<ProjectUserHouseInfoVo> getListUserHouseInfoByUserId(@Param("userId") Integer userId);

    ProjectPersonInfo selectPersonById(@Param("relaId") String relaId);
    /**
     * <p>
     * 根据人员ID获取到这个人员在系统中有的资产数（车辆、车位、房屋）
     * </p>
     *
     * @param personId 人员ID
     * @author: 王良俊
    */
    Integer getAssetsNum(@Param("personId") String personId);

    /**
     * <p>
     * 根据人员ID获取到这个人员在系统中有的资产数（车辆、车位、房屋）
     * </p>
     *
     * @param personId 人员ID
     * @author: 王良俊
    */
    List<PersonAssetsNumVo> getAssetsNumVoList(@Param("personIdList") List<String> personIdList);

    /**
     * 分页查询
     *
     * @param page 分页信息
     * @param po 查询条件
     * @return
     */
    Page<PersonInfoVo> page(Page page, @Param("query") ProjectPersonInfo po);

}
