package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.estate.entity.ProjectAttendancePoint;
import com.aurine.cloudx.estate.mapper.ProjectAttendancePointMapper;
import com.aurine.cloudx.estate.service.ProjectAttendancePointService;
import com.aurine.cloudx.estate.util.GpsUtil;
import com.aurine.cloudx.estate.vo.ProjectAttendancePointForm;
import com.aurine.cloudx.estate.vo.ProjectAttendancePointPlaceVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 考勤点设置(ProjectAttendancePoint)表服务实现类
 *
 * @author xull
 * @since 2021-03-03 10:52:25
 */
@Service
public class ProjectAttendancePointServiceImpl extends ServiceImpl<ProjectAttendancePointMapper, ProjectAttendancePoint> implements ProjectAttendancePointService {

    @Override
    public Boolean addBatch(ProjectAttendancePointForm form) {

        List<ProjectAttendancePoint> pointList = new ArrayList<>();

        for (ProjectAttendancePointPlaceVo vo:form.getPlaceVo() ) {
            ProjectAttendancePoint point = new ProjectAttendancePoint();
            BeanUtil.copyProperties(vo, point);
            point.setPointPrecision(form.getPointPrecision());
            pointList.add(point);
        }
        return saveBatch(pointList);
    }

    @Override
    public ProjectAttendancePoint getAttendancePointInfo(Double lat, Double lon) {
        List<ProjectAttendancePoint> attendancePointList =  this.list();
        for (ProjectAttendancePoint attendancePoint : attendancePointList) {
            if (GpsUtil.getDistance(lat, lon, attendancePoint.getLat(), attendancePoint.getLon()) < attendancePoint.getPointPrecision()) {
                return attendancePoint;
            }
        }
        return null;
    }
}
