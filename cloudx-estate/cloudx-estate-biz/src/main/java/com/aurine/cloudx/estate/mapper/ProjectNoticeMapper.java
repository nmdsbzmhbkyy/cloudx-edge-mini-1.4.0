

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectNotice;
import com.aurine.cloudx.estate.entity.ProjectNoticeDevice;
import com.aurine.cloudx.estate.vo.ProjectNoticeFormVo;
import com.aurine.cloudx.estate.vo.ProjectNoticeVo;
import com.aurine.cloudx.estate.vo.ProjectPersonNoticeVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 信息发布
 *
 * @author guhl@aurine.cn
 * @date 2020-05-20 11:52:46
 */
@Mapper
public interface ProjectNoticeMapper extends BaseMapper<ProjectNotice> {

    /**
     * 分页查询信息
     *
     * @param page
     * @param projectNoticeFormVo
     * @return
     */
    Page<ProjectNoticeVo> pageNotice(Page page, @Param("query") ProjectNoticeFormVo projectNoticeFormVo);

    /**
     *根据设备id批量查询
     *
     * @param deviceIds
     * @return
     */
    List<ProjectNoticeDevice> projectNoticeDeviceList(@Param("deviceIds") List<String> deviceIds);

    /**
     * 分页查询业主消息
     * @param page
     * @param personId
     * @return
     */
    Page<ProjectPersonNoticeVo> pageByPerson(Page page,@Param("personId") String personId);
}
