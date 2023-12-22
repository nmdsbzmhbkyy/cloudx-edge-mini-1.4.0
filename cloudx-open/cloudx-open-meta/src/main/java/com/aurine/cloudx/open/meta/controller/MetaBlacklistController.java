package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.meta.service.MetaBlacklistService;
import com.aurine.cloudx.open.origin.entity.ProjectBlacklist;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 黑名单管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/blacklist")
@Api(value = "metaBlacklist", tags = {"v1", "黑名单管理"}, hidden = true)
@Inner
@Slf4j
public class MetaBlacklistController {

    @Resource
    private MetaBlacklistService metaBlacklistService;

//    Blacklist-黑名单-blacklist
//    CarInfo-车辆信息-car_info
//    CarPreRegister-车辆登记记录-car_pre_register
//    EntryExitLane-出入口车道-entry_exit_lane
//    OpenLaneHis-开关闸记录-open_lane_his
//    ParCarRegister-车辆登记-par_car_register
//    ParkBillingInfo-缴费记录-park_billing_info
//    ParkBillingRule-车场计费规则-park_billing_rule
//    ParkCarType-车辆类型-park_car_type
//    ParkEntranceHis-车行记录-park_entrance_his
//    ParkingInfo-车场信息-parking_info
//    ParkingPlace-车位-parking_place
//    ParkingPlaceHis-车位变动记录-parking_place_his
//    ParkRegion-车位区域-park_region
//    PlateNumberDevice-设备车牌号下发情况-plate_number_device
//    VehiclesEntryExit-车辆出入口信息-vehicles_entry_exit

    /**
     * 新增黑名单
     *
     * @param model 黑名单
     * @return R 返回新增后的黑名单
     */
    @AutoInject
    @ApiOperation(value = "新增黑名单", notes = "新增黑名单", hidden = true)
    @SysLog("新增黑名单")
    @PostMapping
    public R<ProjectBlacklist> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectBlacklist> model) {
        log.info("[MetaBlacklistController - save]: 新增黑名单, model={}", JSONConvertUtils.objectToString(model));
        return metaBlacklistService.save(model.getData());
    }

//    /**
//     * 修改黑名单
//     *
//     * @param model 黑名单
//     * @return R 返回修改后的黑名单
//     */
//    @AutoInject
//    @ApiOperation(value = "修改黑名单", notes = "修改黑名单", hidden = true)
//    @SysLog("通过id修改黑名单")
//    @PutMapping
//    public R<ProjectBlacklist> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectBlacklist> model) {
//        log.info("[MetaBlacklistController - update]: 修改黑名单, model={}", JSONConvertUtils.objectToString(model));
//        return metaBlacklistService.update(model.getData());
//    }

    /**
     * 通过id删除黑名单
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除黑名单", notes = "通过id删除黑名单", hidden = true)
    @SysLog("通过id删除黑名单")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaBlacklistController - delete]: 通过id删除黑名单, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaBlacklistService.delete(id);
    }
}
