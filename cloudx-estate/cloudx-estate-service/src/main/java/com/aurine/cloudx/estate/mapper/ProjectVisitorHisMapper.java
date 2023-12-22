

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectVisitorHis;
import com.aurine.cloudx.estate.vo.ProjectVisitorVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 访客记录
 *
 * @author 王伟
 * @date 2020-06-03 19:43:06
 */
@Mapper
public interface ProjectVisitorHisMapper extends BaseMapper<ProjectVisitorHis> {

    Integer countVisitor();

    /**
     * 获取30天内的最高访客数量
     * @return
     */
    Map<String,Object> count30DayVisitor();

    /**
     * <p>
     *  获取当前时间不包括之前可进行介质下发的任务 (最早只追溯回一天内为下发介质的申请)
     * </p>
     *
     * @param time 如 1234-01-02 03:04
    */
    List<String> getCurrentUnSendCertList(@Param("time") String time);

    /**
     * 获取审核超时的访客数据
     * @return
     */
    @SqlParser(filter=true)
    List<ProjectVisitorVo> getTimeOutVisitor();


    void updateAuditStatusTimeOut();
}
