package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectPersonDeviceMapper;
import com.aurine.cloudx.estate.mapper.ProjectRightDeviceMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.CertVo;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectRightDeviceOptsAccessSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectRightDeviceOptsAccessVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 权限设备关系表，记录权限（认证介质）的下发状态
 *
 * @author 王良俊
 * @date 2020-05-21 09:52:28
 */
@Service
@Slf4j
public class ProjectRightDeviceServiceImpl extends ServiceImpl<ProjectRightDeviceMapper, ProjectRightDevice> implements ProjectRightDeviceService {

    @Override
    public boolean updateStateByIds(List<String> certIdList, String deviceId, PassRightCertDownloadStatusEnum statusEnum) {
        baseMapper.updateStateByIds(certIdList, deviceId, statusEnum.code);
        return true;
    }

    @Override
    public Page<ProjectRightDeviceOptsAccessVo> pageByDeviceId(Page page, ProjectRightDeviceOptsAccessSearchConditionVo query, String deviceId) {
        return baseMapper.pageByDeviceId(page, query, deviceId);
    }

    /**
     * <p>
     * 返回存储人脸、指纹、卡片的id列表
     * </p>
     *
     * @param personId 人员id
     * @return 返回介质id列表（所有介质的）
     * @author: 王良俊
     */
    @Override
    public List<CertVo> listCertmediaVoByPersonId(String personId) {
        return baseMapper.listCertByPersonId(personId);
    }


}
