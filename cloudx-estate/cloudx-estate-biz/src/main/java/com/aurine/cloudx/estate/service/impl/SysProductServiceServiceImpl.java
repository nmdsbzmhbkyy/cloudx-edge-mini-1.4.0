package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.SysProductService;
import com.aurine.cloudx.estate.mapper.SysProductServiceMapper;
import com.aurine.cloudx.estate.service.SysProductServiceService;
import com.aurine.cloudx.estate.vo.SysServiceParamConfVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 以产品为维度进行平台设备参数配置项管理(SysProductParamCategory)表服务实现类
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:58
 */
@Service
public class SysProductServiceServiceImpl extends ServiceImpl<SysProductServiceMapper, SysProductService>
        implements SysProductServiceService {

    @Override
    public List<SysServiceParamConfVo> listParamConf(List<String> serviceIdList, String deviceId) {
        return baseMapper.listParamConf(serviceIdList, deviceId);
    }
}