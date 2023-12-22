package com.aurine.cloudx.wjy.service;

import com.aurine.cloudx.wjy.entity.Unit;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author ： huangjj
 * @date ： 2021/4/25
 * @description： 单元接口
 */
public interface UnitService extends IService<Unit> {
    Unit getByProjectIdAndUnitName(Integer projectId,String unitName);
}
