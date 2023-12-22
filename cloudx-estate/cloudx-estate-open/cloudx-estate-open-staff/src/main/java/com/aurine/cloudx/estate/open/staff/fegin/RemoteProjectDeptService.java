package com.aurine.cloudx.estate.open.staff.fegin;

import com.aurine.cloudx.estate.entity.SysProjectDept;
import com.aurine.cloudx.estate.open.staff.bean.SysProjectDeptPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteProjectDeptService", value = "cloudx-estate-biz")
public interface RemoteProjectDeptService {

    /**
     * 分页查询
     *
     * @param page           分页对象
     * @return
     */
    @GetMapping("/projectDept/page")
    R<Page<SysProjectDept>> getSysProjectDeptPage(@SpringQueryMap SysProjectDeptPage page);

    /**
     * 通过id查询项目内部门信息
     *
     * @param id id
     * @return
     */
    @GetMapping("/projectDept/{id}")
    R getById(@PathVariable("id") Integer id);

    /**
     * 新增项目内部门信息
     *
     * @param sysProjectDept 项目内部门信息
     * @return R
     */
    @PostMapping
    R save(@RequestBody SysProjectDept sysProjectDept);

    /**
     * 修改项目内部门信息
     *
     * @param sysProjectDept 项目内部门信息
     * @return R
     */
    @PutMapping
    R updateById(@RequestBody SysProjectDept sysProjectDept);

    /**
     * 通过id删除项目内部门信息
     *
     * @param id id
     * @return R
     */
    @DeleteMapping("/projectDept/{id}")
    R removeById(@PathVariable("id") Integer id);

    /**
     * 分页查询
     *
     * @return
     */
    @GetMapping("/projectDept/list")
    R getSysProjectDeptList();

}
