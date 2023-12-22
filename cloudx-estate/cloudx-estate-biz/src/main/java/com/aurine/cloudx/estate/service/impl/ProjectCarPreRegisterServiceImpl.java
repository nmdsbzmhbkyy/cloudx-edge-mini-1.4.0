package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.CarAuditStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectCarPreRegister;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.util.MessageTextUtil;
import com.aurine.cloudx.estate.vo.ProjectCarPreRegisterInfoVo;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.mapper.ProjectCarPreRegisterMapper;
import com.aurine.cloudx.estate.service.ProjectCarPreRegisterService;
import com.aurine.cloudx.estate.service.ProjectParCarRegisterService;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceService;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.vo.CarPreRegisterSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectCarPreRegisterAuditVo;
import com.aurine.cloudx.estate.vo.ProjectCarPreRegisterVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 车辆登记表
 */
@Service
public class ProjectCarPreRegisterServiceImpl extends ServiceImpl<ProjectCarPreRegisterMapper, ProjectCarPreRegister> implements ProjectCarPreRegisterService {

    @Resource
    private ProjectParCarRegisterService projectParCarRegisterService;
    @Resource
    private ProjectParkingPlaceService projectParkingPlaceService;
    @Resource
    private NoticeUtil noticeUtil;
    @Resource
    private ProjectStaffService projectStaffService;

    // 车辆审核菜单ID
    private final Integer carAuditMenuId = 11053;

    @Override
    public Page<ProjectCarPreRegisterVo> fetchList(Page page, CarPreRegisterSearchCondition query) {
        return this.baseMapper.fetchList(page, query, ProjectContextHolder.getProjectId());
    }

    @Override
    public R application(ProjectCarPreRegister carPreRegister) {
        if (StrUtil.isEmpty(carPreRegister.getPersonId()) || StrUtil.isEmpty(carPreRegister.getPlateNumber())) {

            String plateNumber = carPreRegister.getPlateNumber().trim().replaceAll(" ", "");
            if (plateNumber.length() < 7) {
                return R.failed("车牌号长度不符合要求，至少为7位");
            }
            int registerNum = projectParCarRegisterService.count(new QueryWrapper<ProjectParCarRegister>().lambda()
                    .eq(ProjectParCarRegister::getPlateNumber, plateNumber));
            if (registerNum != 0) {
                return R.failed("车牌号已被登记");
            }
            carPreRegister.setPlateNumber(plateNumber);
            carPreRegister.setCommitTime(LocalDateTime.now());
            carPreRegister.setAuditStatus(CarAuditStatusEnum.inAudit.code);
            carPreRegister.setOperator(SecurityUtils.getUser().getId());
            // 这里是避免重复提交相同的申请
            int preRegNum = this.count(new QueryWrapper<ProjectCarPreRegister>().lambda()
                    .eq(ProjectCarPreRegister::getAuditStatus, CarAuditStatusEnum.inAudit.code)
                    .eq(ProjectCarPreRegister::getPlateNumber, plateNumber)
                    .eq(ProjectCarPreRegister::getPersonId, carPreRegister.getPersonId()));
            boolean result;
            if (preRegNum == 0) {
                result = this.save(carPreRegister);
            } else {
                result = true;
            }
            if (result) {
                this.sendNotice(plateNumber);
                return R.ok("申请成功");
            } else {
                return R.failed("申请失败");
            }
        }
        return R.failed("信息填写不完整");
    }

    private void sendNotice(String plateNumber) {
        List<String> staffIdList = projectStaffService.getStaffIdListByMenuId(carAuditMenuId);
        if (CollUtil.isNotEmpty(staffIdList)) {
            noticeUtil.send(true, "车辆审核通知",
                    MessageTextUtil.init()
                            .append("有新的车辆申请，请尽快审核")
                            .p("车牌号：%s", plateNumber == null ? "" : plateNumber)
                            .toString(),
                    staffIdList
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectAudit(String preRegisterId, String reason) {
        ProjectCarPreRegister preRegister = this.getOne(new QueryWrapper<ProjectCarPreRegister>().lambda()
                .eq(ProjectCarPreRegister::getPreRegId, preRegisterId));
        Date date = new Date();
        String time = DateFormatUtils.format(date.getTime(), "yyyy年MM月dd日 HH时mm分");
        // 发送消息给移动端
//        String content = "您认证的车辆：" + preRegister.getPlateNumber()
//                        + "，未通过审核(" + reason + ")";
        String content = "您申请登记的" + preRegister.getPlateNumber() + "车辆,未通过审核<br>审核结果：未通过<br>审核时间：" + time + "" +
                "<br>备注：" + (StrUtil.isEmpty(reason) ? "" : "" + reason + "");

        Boolean flag = this.update(new UpdateWrapper<ProjectCarPreRegister>().lambda()
                .eq(ProjectCarPreRegister::getPreRegId, preRegisterId)
                .set(ProjectCarPreRegister::getAuditRemark, reason)
                .set(ProjectCarPreRegister::getAuditStatus, CarAuditStatusEnum.notPass.code)
                .set(ProjectCarPreRegister::getAuditor, SecurityUtils.getUser().getId())

        );
        noticeUtil.send(false, "车辆认证审核未通过", content, preRegister.getPersonId());
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R passAudit(ProjectCarPreRegisterAuditVo carPreRegisterAuditVo) {
        ProjectParCarRegisterVo parCarRegisterVo = new ProjectParCarRegisterVo();
        BeanUtil.copyProperties(carPreRegisterAuditVo, parCarRegisterVo);
        parCarRegisterVo.setSource("audit");
        Date date = new Date();
        String time = DateFormatUtils.format(date.getTime(), "yyyy年MM月dd日 HH时mm分");
        boolean saveResult;
        try {
            saveResult = projectParCarRegisterService.saveCarRegister(parCarRegisterVo);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        if (saveResult) {
            boolean updateResult = this.update(new UpdateWrapper<ProjectCarPreRegister>().lambda()
                    .eq(ProjectCarPreRegister::getPreRegId, carPreRegisterAuditVo.getPreRegId())
                    .set(ProjectCarPreRegister::getAuditStatus, CarAuditStatusEnum.pass.code)
                    .set(ProjectCarPreRegister::getAuditor, SecurityUtils.getUser().getId())
            );

            String content = "您申请登记的" + carPreRegisterAuditVo.getPlateNumber() + "车辆,已通过审核<br>审核结果：已通过<br>审核时间：" + time + "" +
                    "<br>备注：";
            noticeUtil.send(false, "审核结果通知", content, carPreRegisterAuditVo.getPersonId());

            return R.ok(updateResult);
        }

        return R.failed();
    }

    @Override
    public R getObj(String preRegId) {
        ProjectCarPreRegisterAuditVo carPreRegisterAuditVo = this.baseMapper.getAuditObj(preRegId);
        // 获取这个人拥有的所有车位
        List<ProjectParkingPlace> placeList = projectParkingPlaceService.getFreeParkingSpace(carPreRegisterAuditVo.getPersonId());
        // 如果空闲车位列表为非空说明有可用的车位
        if (CollUtil.isNotEmpty(placeList)) {
            ProjectParkingPlace parkingPlace = placeList.get(0);
            carPreRegisterAuditVo.setParkId(parkingPlace.getParkId());
            carPreRegisterAuditVo.setParkRegionId(parkingPlace.getParkRegionId());
            carPreRegisterAuditVo.setParkPlaceId(parkingPlace.getPlaceId());
            carPreRegisterAuditVo.setRelType(parkingPlace.getRelType());
        }
        return R.ok(carPreRegisterAuditVo);
    }

    @Override
    public R getPreRegisterInfo(String preRegId) {
        return R.ok(this.baseMapper.getAuditInfo(preRegId));
    }

    @Override
    public String checkHasBeenApplied(String plateNumber) {
        List<ProjectCarPreRegister> list = this.list(new LambdaQueryWrapper<ProjectCarPreRegister>().eq(ProjectCarPreRegister::getPlateNumber, plateNumber)
                .eq(ProjectCarPreRegister::getAuditStatus, CarAuditStatusEnum.inAudit.code));
        if (CollUtil.isNotEmpty(list)) {
            ProjectCarPreRegister carPreRegister = list.get(0);
            return carPreRegister.getPreRegId();
        }
        return "";
    }

    @Override
    public Integer countByOff() {
        return baseMapper.countByOff();
    }
}
