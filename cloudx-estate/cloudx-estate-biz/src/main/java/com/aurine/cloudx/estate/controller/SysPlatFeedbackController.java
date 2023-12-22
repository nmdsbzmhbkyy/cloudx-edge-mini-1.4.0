package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.SysPlatFeedback;
import com.aurine.cloudx.estate.service.SysPlatFeedbackService;
import com.aurine.cloudx.estate.vo.ReplyForm;
import com.aurine.cloudx.estate.vo.SysPlatFeedbackFormVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 平台意见反馈(SysPlatFeedback)表控制层
 *
 * @author xull
 * @since 2021-03-05 09:58:55
 */
@RestController
@RequestMapping("sys-plat-feedback")
@Api(value = "sysPlatFeedback", tags = "平台意见反馈")
public class SysPlatFeedbackController {
    /**
     * 服务对象
     */
    @Resource
    private SysPlatFeedbackService sysPlatFeedbackService;

    /**
     * 分页查询所有数据
     *
     * @param page            分页对象
     * @param sysPlatFeedback 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询sysPlatFeedback所有数据")
    public R selectAll(Page<SysPlatFeedback> page, SysPlatFeedbackFormVo sysPlatFeedback) {
        return R.ok(this.sysPlatFeedbackService.pageAll(page, sysPlatFeedback));
    }


    /**
     * 平台管理员回复
     *
     * @param form
     * @return
     */
    @PutMapping("/reply")
    public R reply(@RequestBody ReplyForm form) {
        SysPlatFeedback sysPlatFeedback = this.sysPlatFeedbackService.getById(form.getFeedbackId());
        sysPlatFeedback.setReplyContent(form.getReplyContent());
        sysPlatFeedback.setStatus("1");
        sysPlatFeedback.setReplyTime(LocalDateTime.now());
        return R.ok(this.sysPlatFeedbackService.updateById(sysPlatFeedback));
    }


    /**
     * 分页查询所有数据
     *
     * @param page            分页对象
     * @param sysPlatFeedback 查询实体
     * @return 所有数据
     */
    @GetMapping("/page/byOwner")
    @ApiOperation(value = "分页查询", notes = "分页查询sysPlatFeedback所有数据")
    public R selectAllByOwner(Page<SysPlatFeedback> page, SysPlatFeedback sysPlatFeedback) {

        return R.ok(this.sysPlatFeedbackService.selectAllByOwner(page, sysPlatFeedback));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询sysPlatFeedback单条数据")
    public R selectOne(@PathVariable String id) {
        return R.ok(this.sysPlatFeedbackService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysPlatFeedback 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增sysPlatFeedback数据")
    public R insert(@RequestBody SysPlatFeedback sysPlatFeedback) {
        sysPlatFeedback.setStatus("0");
        sysPlatFeedback.setFeedbackTime(LocalDateTime.now());
        return R.ok(this.sysPlatFeedbackService.save(sysPlatFeedback));
    }

    /**
     * 修改数据
     *
     * @param sysPlatFeedback 实体对象
     * @return 修改结果
     */
    @PutMapping("{id}")
    @ApiOperation(value = "修改数据", notes = "修改sysPlatFeedback数据")
    public R update(@PathVariable("id") String id, @RequestBody SysPlatFeedback sysPlatFeedback) {
        sysPlatFeedback.setFeedbackId(id);
        return R.ok(this.sysPlatFeedbackService.updateById(sysPlatFeedback));
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除数据", notes = "通过id删除sysPlatFeedback数据")
    public R delete(@PathVariable("id") String id) {
        return R.ok(this.sysPlatFeedbackService.removeById(id));
    }


}
