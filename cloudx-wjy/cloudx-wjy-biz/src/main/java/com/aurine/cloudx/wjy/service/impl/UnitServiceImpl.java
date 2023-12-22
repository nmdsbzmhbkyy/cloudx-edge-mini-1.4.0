package com.aurine.cloudx.wjy.service.impl;

import com.aurine.cloudx.wjy.entity.Unit;
import com.aurine.cloudx.wjy.mapper.UnitMapper;
import com.aurine.cloudx.wjy.service.UnitService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ： huangjj
 * @date ： 2021/4/25
 * @description： 单元接口实现类
 */
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {

    @Resource
    UnitMapper unitMapper;
    @Override
    public Unit getByProjectIdAndUnitName(Integer projectId, String unitName) {
        return unitMapper.selectOne(new QueryWrapper<Unit>().lambda().eq(Unit::getProjectId,projectId).eq(Unit::getUnitName,unitName));
    }
}