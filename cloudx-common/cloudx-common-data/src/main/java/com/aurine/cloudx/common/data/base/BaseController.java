package com.aurine.cloudx.common.data.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/***
 * @title BaseController
 * @description 基础视图层 定义基础CRUD方法
 * 业务试图层继承时 需要满足实现BaseApi接口
 * Attention ：使用时需检查 业务视图层/自定义接口 是否带有@RequestMapping 注解
 * @author cyw
 * @version 1.0.0
 * @create 2023/6/9 10:48
 **/
public class BaseController<T extends BaseEntity,Q extends PageParam , S extends IService<T> & BaseService<T,Q>> implements BaseApi<T,Q> {
    private final S service;

    public BaseController(S service) {
        this.service = service;
    }

//    public R<IPage<T>> page(Q param) {
//        return R.ok(service.queryPage(param));
//    }

    public R<T> get(Long id) {
        return R.ok(service.getById(id));
    }

    public R<Object> save(T t) {
        return R.ok(service.save(t));
    }

    public R<Object> update(T t) {
        T getById = service.getById(t.getId());
        BeanUtil.copyPropertiesIgnoreNull(t, getById);
        return R.ok(service.updateById(getById));
    }

    public R<Object> delete(Long campusId) {
        return R.ok(service.removeById(campusId));
    }

    public R<Object> deleteByIds(List<Long> campusIds) {
        return R.ok(service.removeByIds(campusIds));
    }

    public S getService(){
        return this.service;
    }
}
