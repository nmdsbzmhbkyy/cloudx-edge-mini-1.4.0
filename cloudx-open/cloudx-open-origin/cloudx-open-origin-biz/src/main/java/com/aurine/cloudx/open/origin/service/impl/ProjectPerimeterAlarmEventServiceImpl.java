package com.aurine.cloudx.open.origin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.constant.enums.PerimeterAlarmEnum;
import com.aurine.cloudx.open.origin.vo.ProjectPerimeterAlarmEventVo;
import com.aurine.cloudx.open.origin.mapper.ProjectHousePersonRelMapper;
import com.aurine.cloudx.open.origin.mapper.ProjectPerimeterAlarmEventMapper;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.open.origin.entity.ProjectPerimeterAlarmEvent;
import com.aurine.cloudx.open.common.core.util.MessageTextUtil;
import com.aurine.cloudx.open.origin.service.ProjectDeviceInfoService;
import com.aurine.cloudx.open.origin.service.ProjectPerimeterAlarmAreaService;
import com.aurine.cloudx.open.origin.service.ProjectPerimeterAlarmEventService;
import com.aurine.cloudx.open.origin.service.ProjectWebSocketService;
import com.aurine.cloudx.open.origin.util.NoticeUtil;
import com.aurine.cloudx.open.origin.util.WebSocketNotifyUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 周界报警
 *
 * @author 邹宇
 * @date 2021-6-8 14:30:55
 */

@Service
@Slf4j
public class ProjectPerimeterAlarmEventServiceImpl extends ServiceImpl<ProjectPerimeterAlarmEventMapper, ProjectPerimeterAlarmEvent> implements ProjectPerimeterAlarmEventService {


    @Resource
    private ProjectPerimeterAlarmAreaService projectPerimeterAlarmAreaService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Resource
    private NoticeUtil noticeUtil;

    @Resource
    private ProjectHousePersonRelMapper projectHousePersonRelMapper;


    @Resource
    private ProjectWebSocketService projectWebSocketService;

    // 周界报警菜单ID
    private final Integer alarmMenuId = 11059;

    /**
     * 分页查询
     *
     * @param projectPerimeterAlarmEventVo
     * @return
     */
    @Override
    public Page<ProjectPerimeterAlarmEventVo> pagePerimeterAlarmEvent(ProjectPerimeterAlarmEventVo projectPerimeterAlarmEventVo) {
        if (projectPerimeterAlarmEventVo.getSize() != null && projectPerimeterAlarmEventVo.getCurrent() != null) {
            projectPerimeterAlarmEventVo.setCurrent((projectPerimeterAlarmEventVo.getCurrent() - 1) * projectPerimeterAlarmEventVo.getSize());
        }

        List<ProjectPerimeterAlarmEventVo> list = baseMapper.selectAll(projectPerimeterAlarmEventVo);
        Page<ProjectPerimeterAlarmEventVo> projectPerimeterAlarmEventPage = new Page<ProjectPerimeterAlarmEventVo>();
         projectPerimeterAlarmEventPage.setCurrent(projectPerimeterAlarmEventVo.getCurrent());
         projectPerimeterAlarmEventPage.setSize(projectPerimeterAlarmEventVo.getSize());
        projectPerimeterAlarmEventPage.setRecords(list);
        projectPerimeterAlarmEventPage.setTotal(baseMapper.getCount(projectPerimeterAlarmEventVo));
        return projectPerimeterAlarmEventPage;
    }


    /**
     * 查询当月报警数
     *
     * @return
     */
    @Override
    public Integer countPolice() {
        List<String> time = getTime();
        String startTime = time.get(0);
        String endTime = time.get(1);
        QueryWrapper<ProjectPerimeterAlarmEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("alaramTime", startTime).le("alaramTime", endTime);
        return count(queryWrapper);

    }


    /**
     * 添加报警记录
     *
     * @param sn
     * @param channel
     * @param type
     * @return
     */
    @Override
    public Boolean saveAlarm(String sn, String channel, String type) {
        ProjectPerimeterAlarmEvent projectPerimeterAlarmEvent = baseMapper.getAlarm(sn, channel);
        if (projectPerimeterAlarmEvent != null) {
            String id = UUID.randomUUID().toString().replace("-", "");
            projectPerimeterAlarmEvent.setEventId(id);
            projectPerimeterAlarmEvent.setAlaramTime(new Date());
            projectPerimeterAlarmEvent.setAlaramType(type);

            //给拥有周界报警菜单的员工发送消息
            sendAssignNotice(projectPerimeterAlarmEvent);

            return save(projectPerimeterAlarmEvent);
        } else {
            return false;
        }
    }


    /**
     * 消除报警
     *
     * @param eventId
     * @return
     */
    @Override
    public Boolean deleteByEventId(String eventId) {
        ProjectPerimeterAlarmEvent projectPerimeterAlarmEvent = getOne(Wrappers.lambdaQuery(ProjectPerimeterAlarmEvent.class)
                .eq(ProjectPerimeterAlarmEvent::getEventId, eventId));

        if (projectPerimeterAlarmEvent==null){
            return Boolean.FALSE;
        }

        if ("1".equals(projectPerimeterAlarmEvent.getExecStatus())){
            return Boolean.FALSE;
        }
        ProjectPerimeterAlarmArea perimeterAlarmArea = projectPerimeterAlarmAreaService.getOne(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
                .eq(ProjectPerimeterAlarmArea::getInfoUid, projectPerimeterAlarmEvent.getDeviceId())
                .eq(ProjectPerimeterAlarmArea::getChannelNo, projectPerimeterAlarmEvent.getChannelId()));
        if (perimeterAlarmArea==null){
            return Boolean.FALSE;
        }
        ProjectDeviceInfo deviceInfoServiceOne = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getDeviceId, perimeterAlarmArea.getDeviceId()));
        if ("2".equals(deviceInfoServiceOne.getStatus())) {
            throw new RuntimeException("设备离线");
        }
//        boolean bool = DeviceFactoryProducer.getFactory(perimeterAlarmArea.getDeviceId()).getPerimeterAlarmDeviceService().clearAlarm(perimeterAlarmArea);
//        if (!bool) {
//            return bool;
//        }
        boolean update = update(Wrappers.lambdaUpdate(ProjectPerimeterAlarmEvent.class)
                .set(ProjectPerimeterAlarmEvent::getExecStatus, "1")
                .set(ProjectPerimeterAlarmEvent::getOperator, SecurityUtils.getUser().getId())
                .eq(ProjectPerimeterAlarmEvent::getEventId, eventId));

        WebSocketNotifyUtil.sendMessgae(ProjectContextHolder.getProjectId().toString(), JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));
        return update;
    }

    @Override
    public Integer countByOff() {
        return this.count(new QueryWrapper<ProjectPerimeterAlarmEvent>().eq("execStatus", "0"));
    }

    @Override
    public Integer countByoffxj() {
        return baseMapper.countByoffxj();
    }

    /**
     * 发送周界报警消息通知
     */
    private void sendAssignNotice(ProjectPerimeterAlarmEvent projectPerimeterAlarmEvent) {
        try {
            Integer projectId = ProjectContextHolder.getProjectId();
            List<String> staffIdListByMenuId = projectHousePersonRelMapper.getProjectStaff(alarmMenuId, projectId);
            String alarmName = PerimeterAlarmEnum.getEnum(projectPerimeterAlarmEvent.getAlaramType()).getName();
            Date date = new Date(projectPerimeterAlarmEvent.getAlaramTime().getTime());
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(gc.getTime());

            if (CollectionUtils.isNotEmpty(staffIdListByMenuId)) {
                String message = MessageTextUtil.init()
                        .append("有新的周界报警，请尽快确认处理")
                        .p("报警名称：%s", alarmName)
                        .p("报警编号：%s", projectPerimeterAlarmEvent.getAlaramType())
                        .p("报警时间：%s", time)
                        .p("报警位置：%s", projectPerimeterAlarmEvent.getRegionName())
                        .toString();
                noticeUtil.send(true, "周界报警提醒", message, staffIdListByMenuId);
            }
        } catch (Exception e) {
            log.warn("消息发送异常");
        }
    }


    /**
     * 获取系统当前月的第一天和最后一天
     *
     * @return
     */
    private List<String> getTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH));
        c2.set(Calendar.HOUR_OF_DAY, 23);
        c2.set(Calendar.MINUTE, 59);
        c2.set(Calendar.SECOND, 59);
        c2.set(Calendar.MILLISECOND, 999);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDate = sdf.format(c.getTime());
        String endDate = sdf.format(c2.getTime());
        List<String> list = new ArrayList<>();
        list.add(startDate);
        list.add(endDate);
        return list;
    }
}
