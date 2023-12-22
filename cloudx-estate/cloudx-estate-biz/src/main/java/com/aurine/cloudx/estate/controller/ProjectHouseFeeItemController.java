package com.aurine.cloudx.estate.controller;
import com.aurine.cloudx.estate.constant.FeeConstant;
import com.aurine.cloudx.estate.vo.ProjectHouseFeeItemUpdateBatchVo;
import com.aurine.cloudx.estate.vo.ProjectHouseFeeItemUpdateVo;
import com.pig4cloud.pigx.common.core.util.R;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper; 
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.entity.ProjectHouseFeeItem;
import com.aurine.cloudx.estate.service.ProjectHouseFeeItemService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 房屋费用设置(ProjectHouseFeeItem)表控制层
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@RestController
@RequestMapping("projectHouseFeeItem")
@Api(value="projectHouseFeeItem",tags="房屋费用设置")
public class ProjectHouseFeeItemController  {
    /**
     * 服务对象
     */
    @Resource
    private ProjectHouseFeeItemService projectHouseFeeItemService;


    /**
     * 新增数据
     *
     * @param projectHouseFeeItem 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectHouseFeeItem数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R insert(@RequestBody ProjectHouseFeeItemUpdateVo projectHouseFeeItem) {
        return  this.projectHouseFeeItemService.updateVo(projectHouseFeeItem);
    }


    /**
     * 批量新增数据
     *
     * @param projectHouseFeeItemUpdateBatchVo 实体对象
     * @return 新增结果
     */
    @PostMapping("/batch")
    @ApiOperation(value = "批量新增数据", notes = "新增projectHouseFeeItem数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R insertBatch(@RequestBody ProjectHouseFeeItemUpdateBatchVo projectHouseFeeItemUpdateBatchVo) {
        return this.projectHouseFeeItemService.updateBatchVo(projectHouseFeeItemUpdateBatchVo);
    }
//    /**
//     * 修改数据
//     *
//     * @param projectHouseFeeItem 实体对象
//     * @return 修改结果
//     */
//    @PutMapping
//    @ApiOperation(value = "修改数据", notes = "修改projectHouseFeeItem数据")
//    public R update(@RequestBody ProjectHouseFeeItem projectHouseFeeItem) {
//        return R.ok(this.projectHouseFeeItemService.updateById(projectHouseFeeItem));
//    }
//    /**
//     * 删除数据
//     *
//     * @param idList 主键结合
//     * @return 删除结果
//     */
//    @DeleteMapping
//    @ApiOperation(value = "删除数据", notes = "通过id删除projectHouseFeeItem数据")
//    public R delete(@RequestParam("idList") List<Integer> idList) {
//        return R.ok(this.projectHouseFeeItemService.removeByIds(idList));
//    }

    /**
     * 获取房屋费用列表
     * @param houseId 房屋id
     * @return
     */
    @GetMapping("/listByHouseId/{houseId}")
    @ApiOperation(value = "获取房屋费用列表",notes = "获取房屋费用列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "houseId", value = "房屋id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R listByHouseId(@PathVariable("houseId") String houseId) {
        return R.ok(this.projectHouseFeeItemService.listHouseFeeItemConf(houseId,null, Arrays.asList(FeeConstant.FIXED_CHARGE),null));
    }



}