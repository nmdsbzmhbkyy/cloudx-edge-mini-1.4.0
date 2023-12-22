package com.aurine.cloudx.estate.open.parking.controller;

import com.aurine.cloudx.estate.entity.ProjectCarPreRegister;
import com.aurine.cloudx.estate.open.parking.bean.CarPreRegisterSearchConditionPage;
import com.aurine.cloudx.estate.open.parking.fegin.RemoteProjectCarPreRegisterService;
import com.aurine.cloudx.estate.vo.ProjectCarPreRegisterAuditVo;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 车辆预登记表，保存车辆登记前的原始数据及审核意见，如审核通过则转存入正式业务(ProjectCarPreRegister)表控制层
 *
 * @author makejava
 * @since 2021-03-05 14:50:49
 */
@RestController
@RequestMapping("car-pre-register")
@Api(value = "car-pre-register", tags = "车辆预登记表，保存车辆登记前的原始数据及审核意见，如审核通过则转存入正式业务")
public class ProjectCarPreRegisterController {
//    /**
//     * 服务对象
//     */
//    @Resource
//    private RemoteProjectCarPreRegisterService projectCarPreRegisterService;
//
//    /**
//     * 分页查询车辆预登记数据
//     *
//     * @param  page 查询条件
//     * @param
//     * @return 所有数据
//     */
//    @GetMapping("/page")
//    @ApiOperation(value = "分页查询车辆预登记数据", notes = "分页查询车辆预登记数据")
//    public R fetchList(CarPreRegisterSearchConditionPage page) {
//        return this.projectCarPreRegisterService.fetchList(page);
//    }
//
//    /**
//     * 通过预登记ID获取车辆审核页面需要的数据（如果有空闲车位则带有车位相关信息）
//     *
//     * @param preRegId 预登记ID
//     */
//    @GetMapping("{preRegId}")
//    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectCarPreRegister单条数据")
//    @ApiImplicitParam(name = "id", value = "车辆预登记id", paramType = "path", required = true)
//    public R selectOne(@PathVariable String preRegId) {
//        return this.projectCarPreRegisterService.selectOne(preRegId);
//    }
//
//    /**
//     * 通过预登记ID获取预登记的信息（车辆审核查看页使用）
//     *
//     * @param preRegId 预登记ID
//     */
//    @GetMapping("/getAuditInfo/{preRegId}")
//    @ApiOperation(value = "通过预登记ID获取预登记的信息（车辆审核查看页使用）", notes = "通过预登记ID获取预登记的信息（车辆审核查看页使用）")
//    @ApiImplicitParam(name = "preRegId", value = "预登记ID", paramType = "path", required = true)
//    public R getAuditInfo(@PathVariable String preRegId) {
//        return this.projectCarPreRegisterService.getAuditInfo(preRegId);
//    }
//
//    /**
//     * 拒绝车辆预登记申请
//     *
//     * @param carPreRegister (申请ID和拒绝原因为必填)
//     * @return 处理结果
//     */
//    @PostMapping("/rejectAudit")
//    @ApiOperation(value = "拒绝车辆预登记申请", notes = "拒绝车辆预登记申请")
//    public R rejectAudit(@RequestBody ProjectCarPreRegister carPreRegister) {
//        return this.projectCarPreRegisterService.rejectAudit(carPreRegister);
//    }
//
//    /**
//     * 拒绝车辆预登记申请
//     *
//     * @param carPreRegister (申请ID和拒绝原因为必填)
//     * @return 处理结果
//     */
//    @GetMapping("/checkHasBeenApplied/{plateNumber}")
//    @ApiOperation(value = "拒绝车辆预登记申请", notes = "拒绝车辆预登记申请")
//    public R checkHasBeenApplied(@PathVariable("plateNumber") String plateNumber) {
//        return this.projectCarPreRegisterService.checkHasBeenApplied(plateNumber);
//    }
//
//    /**
//     * 通过车辆预登记申请
//     *
//     * @param preRegisterAuditVo 包含了车辆预登记ID的车辆登记信息
//     * @return 处理结果
//     */
//    @PostMapping("/passAudit")
//    @ApiOperation(value = "通过车辆预登记申请", notes = "通过车辆预登记申请")
//    public R passAudit(@RequestBody ProjectCarPreRegisterAuditVo preRegisterAuditVo) {
//        return this.projectCarPreRegisterService.passAudit(preRegisterAuditVo);
//    }
//
//    /**
//     * 车辆预登记申请
//     *
//     * @param projectCarPreRegister 车辆预登记申请（人员ID、车牌号为必填）
//     * @return 申请结果
//     */
//    @PostMapping
//    @ApiOperation(value = "车辆预登记", notes = "车辆预登记")
//    public R insert(@RequestBody ProjectCarPreRegister projectCarPreRegister) {
//        return this.projectCarPreRegisterService.insert(projectCarPreRegister);
//    }
//
//    /**
//     * 修改数据
//     *
//     * @param projectCarPreRegister 实体对象
//     * @return 修改结果
//     */
//    @PutMapping
//    @ApiOperation(value = "修改数据", notes = "修改projectCarPreRegister数据")
//    public R update(@RequestBody ProjectCarPreRegister projectCarPreRegister) {
//        return this.projectCarPreRegisterService.update(projectCarPreRegister);
//    }
//
//    /**
//     * 删除数据
//     *
//     * @param idList 主键结合
//     * @return 删除结果
//     */
//    @DeleteMapping
//    @ApiOperation(value = "删除数据", notes = "通过id删除projectCarPreRegister数据")
//    public R delete(@RequestParam("idList") List<Integer> idList) {
//        return this.projectCarPreRegisterService.delete(idList);
//    }


}
