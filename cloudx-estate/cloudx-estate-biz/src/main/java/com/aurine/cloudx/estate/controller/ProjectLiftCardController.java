package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.config.LiftCardConfig;
import com.aurine.cloudx.estate.constant.enums.LiftCardTypeEnum;
import com.aurine.cloudx.estate.service.ProjectEntityLevelCfgService;
import com.aurine.cloudx.estate.service.ProjectLiftCardService;
import com.aurine.cloudx.estate.vo.LiftFunctionCardVo;
import com.aurine.cloudx.estate.vo.LiftProprietorCardVo;
import com.aurine.cloudx.estate.vo.LiftStaffCardVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Auther: 黄健杰
 * @Date: 2022/4/6 14:04
 * @Description: 电梯卡读写管理
 */
@RestController
@RequestMapping("/liftCard")
@Api(value = "liftCard", tags = "电梯卡读写管理")
@AllArgsConstructor
public class ProjectLiftCardController {
    @Resource
    private ProjectLiftCardService projectLiftCardService;
    private final ProjectEntityLevelCfgService projectEntityLevelCfgService;
    private final LiftCardConfig liftCardConfig;

    @ApiOperation(value = "获取业主卡写卡数据", notes = "获取业主卡写卡数据")
    @PostMapping("/getProprietorCardData")
    public R getProprietorCardData(@RequestBody LiftProprietorCardVo liftProprietorCardVo) {
        return R.ok(projectLiftCardService.getProprietorCardHex(LiftCardTypeEnum.durationCard.code, liftProprietorCardVo));
    }

    @ApiOperation(value = "获取员工卡写卡数据", notes = "获取员工卡写卡数据")
    @PostMapping("/getStaffCardData")
    public R getStaffCardData(@RequestBody LiftStaffCardVo liftStaffCardVo) {
        return R.ok(projectLiftCardService.getStaffCardHex(LiftCardTypeEnum.staffCard.code, liftStaffCardVo));
    }

    @ApiOperation(value = "获取写卡驱动初始化数据", notes = "获取写卡驱动初始化数据")
    @GetMapping("/getInitDeviceData")
    public R getInitDeviceData() {
        return R.ok(projectLiftCardService.getInitDeviceData());
    }

    @ApiOperation(value = "获取初始化项目卡数据", notes = "获取初始化项目卡数据")
    @GetMapping("/getInitRFData")
    public R getInitRFData() {
        return R.ok(projectLiftCardService.getInitRFData());
    }

    @ApiOperation(value = "获取读取卡数据", notes = "获取读取卡数据")
    @GetMapping("/getReadRFData")
    public R getReadRFData(@RequestParam Integer type) {
        return R.ok(projectLiftCardService.getReadRFData(type));
    }

    @ApiOperation(value = "获取功能卡写卡数据", notes = "获取功能卡写卡数据")
    @PostMapping("/getFunctionCardData")
    public R getFunctionCardData(@RequestBody LiftFunctionCardVo liftFunctionCardVo) {
        return R.ok(projectLiftCardService.getFuctionCardHex(liftFunctionCardVo));
    }

    @ApiOperation(value = "保存写卡记录", notes = "保存写卡记录")
    @PostMapping("/record")
    public R saveCardIssueRecord(@RequestBody LiftFunctionCardVo liftFunctionCardVo) {
        return R.ok(projectLiftCardService.saveCardIssueRecord(liftFunctionCardVo));
    }

    @ApiOperation(value = "分页查询写卡记录", notes = "分页查询写卡记录")
    @GetMapping("/record/page")
    public R pageCardIssueRecord(Page page, LiftFunctionCardVo liftFunctionCardVo) {
        return R.ok(projectLiftCardService.pageCardIssueRecord(page, liftFunctionCardVo));
    }

    @ApiOperation(value = "获取网络参数卡默认数据", notes = "获取网络参数卡默认数据")
    @GetMapping("/networkParams")
    public R getNetworkParams() {
        LiftFunctionCardVo liftFunctionCardVo = new LiftFunctionCardVo();
        liftFunctionCardVo.setCardEncryptionKey(liftCardConfig.getProjectSecret());
        liftFunctionCardVo.setRoomNoLength(String.valueOf(projectEntityLevelCfgService.getCodeRuleByLevel("1")));
        liftFunctionCardVo.setBuildingNoLength(String.valueOf(projectEntityLevelCfgService.getCodeRuleByLevel("2")+projectEntityLevelCfgService.getCodeRuleByLevel("3")));
        liftFunctionCardVo.setCardSectorOffset("1");
        return R.ok(liftFunctionCardVo);
    }
}
