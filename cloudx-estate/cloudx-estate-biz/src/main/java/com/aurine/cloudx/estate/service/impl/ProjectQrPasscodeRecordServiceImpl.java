package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.base.BeanUtil;
import com.aurine.cloudx.common.data.base.PageParam;
import com.aurine.cloudx.estate.dto.qrPasscode.QrPasscodePageDto;
import com.aurine.cloudx.estate.entity.ProjectQrPasscodeRecord;
import com.aurine.cloudx.estate.mapper.ProjectQrPasscodeRecordMapper;
import com.aurine.cloudx.estate.service.ProjectQrPasscodeRecordService;
import com.aurine.cloudx.estate.vo.qrPasscode.SaveProjectQrPasscodeRecordVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectQrPasscodeRecordServiceImpl extends ServiceImpl<ProjectQrPasscodeRecordMapper, ProjectQrPasscodeRecord> implements ProjectQrPasscodeRecordService {
    @Override
    @Transactional
    public void saveRecord(SaveProjectQrPasscodeRecordVo vo) {
        ProjectQrPasscodeRecord one = lambdaQuery().eq(ProjectQrPasscodeRecord::getUniqueCode, vo.getUniqueCode()).one();
        if (Objects.isNull(one)) {
            one = new ProjectQrPasscodeRecord();
        }
        one.setPassenger(vo.getPassenger());
        one.setPhone(vo.getPhone());
        one.setCredentialNo(vo.getCredentialNo());
        one.setStartTime(vo.getStartTime());
        one.setEndTime(vo.getEndTime());
        one.setTimes(vo.getTimes());
        one.setUniqueCode(vo.getUniqueCode());

        saveOrUpdate(one);
    }

    @Override
    public boolean deleteRecord(String uniqueCode) {
        return this.remove(new LambdaQueryWrapper<ProjectQrPasscodeRecord>().eq(ProjectQrPasscodeRecord::getUniqueCode, uniqueCode));
    }

    @Override
    public Page<QrPasscodePageDto> queryPage(Page<Object> page) {
        Page<QrPasscodePageDto> qrPasscodePageDtoPage = getBaseMapper().queryPage(page);
        qrPasscodePageDtoPage.getRecords().forEach(QrPasscodePageDto::initTimeStr);
        return qrPasscodePageDtoPage;
    }
}
