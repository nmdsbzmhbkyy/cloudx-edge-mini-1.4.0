

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.service.ProjectConfigService;
import com.aurine.cloudx.estate.service.ProjectParCarRegisterService;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceService;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>车位管理</p>
 * @ClassName: ProjectParkingPlaceController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/8 10:40
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/baseParkingPlace" )
@Api(value = "baseParkingPlace", tags = "车位管理")
public class ProjectParkingPlaceController {

    @Autowired
    private final ProjectParkingPlaceService projectParkingPlaceService;
    @Autowired
    private final ProjectParCarRegisterService projectParCarRegisterService;
    @Autowired
    private final ProjectConfigService projectConfigService;


    /**
     * 分页查询
     * @param page 分页对象
     * @param projectParkingPlace 车位
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProjectParkingPlacePage(Page page, ProjectParkingPlaceVo projectParkingPlace) {
        return R.ok(projectParkingPlaceService.findPage(page, projectParkingPlace));
    }

    /**
     * 获取当前项目下所有数据
     *
     * @return
     */
    @ApiOperation(value = "列表", notes = "列表")
    @GetMapping("/list/{parkRegionId}")
    public R list(@PathVariable String parkRegionId) {
        return R.ok(projectParkingPlaceService.listByParkRegionId(parkRegionId));
    }

    /**
     * 通过id查询车位数量
     */
    @ApiOperation(value = "通过id查询数量", notes = "通过id查询数量")
    @GetMapping("/getUseTotal")
    R<Integer> getUseTotal() {
        return R.ok(projectParkingPlaceService.getUseTotal());
    }


    /**
     * 通过id查询车位
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(projectParkingPlaceService.getById(id));
    }

    /**
     * 新增车位
     * @param projectParkingPlace 车位
     * @return R
     */
    @ApiOperation(value = "新增车位", notes = "新增车位")
    @SysLog("新增车位" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projectparkingplace_add')" )
    public R save(@RequestBody ProjectParkingPlace projectParkingPlace) {
//        projectParkingPlace.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectParkingPlaceService.add(projectParkingPlace));
    }

    /**
     * 修改车位
     * @param projectParkingPlace 车位
     * @return R
     */
    @ApiOperation(value = "修改车位", notes = "修改车位")
    @SysLog("修改车位" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projectparkingplace_edit')" )
    public R updateById(@RequestBody ProjectParkingPlace projectParkingPlace) {
        //修改车位地址
        ProjectParCarRegister projectParCarRegister = projectParCarRegisterService.getOne(new LambdaQueryWrapper<ProjectParCarRegister>()
                .eq(ProjectParCarRegister::getParkPlaceId, projectParkingPlace.getPlaceId()));
        if(projectParCarRegister != null){
            List<String> split = StrSpliter.split(projectParCarRegister.getParkPlaceName(), '-',0, true, true);
            String parkPlaceName;
            if (split.size() == 3){
                parkPlaceName = split.get(0)+"-"+split.get(1)+"-"+projectParkingPlace.getPlaceName();
                projectParCarRegister.setParkPlaceName(parkPlaceName);
                projectParCarRegisterService.updateById(projectParCarRegister);
            }
        }
        return R.ok(projectParkingPlaceService.update(projectParkingPlace));
    }

    /**
     * 通过id删除车位
     * @param  id
     * @return R
     */
    @ApiOperation(value = "通过id删除车位", notes = "通过id删除车位")
    @SysLog("通过id删除车位" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('estate_projectparkingplace_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(projectParkingPlaceService.removeById(id));
    }


    /**
     * 通过id删除车位
     * @param  parkingPlaceVo 车位vo对象
     * @return R
     */
    @ApiOperation(value = "通过id列表删除车位", notes = "通过id列表删除车位")
    @SysLog("通过id删除车位" )
    @PostMapping("/checkPlaceListIsUse" )
    public R checkPlaceIsUse(@RequestBody ProjectParkingPlaceVo parkingPlaceVo) {
        return R.ok(projectParkingPlaceService.checkPlaceNameExist(parkingPlaceVo.getPlaceNameList(), parkingPlaceVo.getParkRegionId()));
    }

    /**
     * 通过id删除车位
     * @param  placeIdList 车位ID列表
     * @return R
     */
    @ApiOperation(value = "通过id列表删除车位", notes = "通过id列表删除车位")
    @SysLog("通过id删除车位" )
    @PostMapping("/removeBatch" )
    @PreAuthorize("@pms.hasPermission('estate_projectparkingplace_del')" )
    public R removeBatch(@RequestBody List<String> placeIdList) {
        if (CollUtil.isNotEmpty(placeIdList)){
            return R.ok(projectParkingPlaceService.remove(new QueryWrapper<ProjectParkingPlace>().lambda().in(ProjectParkingPlace::getPlaceId, placeIdList)));
        }
        return R.ok();
    }

    /**
     * 通过id删除车位
     * @param  placeVo 车位vo对象
     * @return R
     */
    @ApiOperation(value = "批量添加车位", notes = "批量添加车位")
    @SysLog("批量添加车位" )
    @PostMapping("/savePlaceBatch" )
    @PreAuthorize("@pms.hasPermission('estate_projectparkingplace_add')" )
    public R savePlaceBatch(@RequestBody ProjectParkingPlaceVo placeVo) {
        return R.ok(projectParkingPlaceService.savePlaceBatch(placeVo));
    }

    /**
     * 获取车位归属信息
     * @author: 王良俊
     * @param placeId 车位id
     * @return
     */
    @ApiOperation(value = "获取车位归属信息")
    @GetMapping("/getAssignment/{placeId}" )
    public R getParkingAttribution(@PathVariable("placeId")String placeId) {
        return R.ok(projectParkingPlaceService.getParkingAttribution(placeId));
    }

    /**
     * 获取车位记录信息
     * @author: 王良俊
     * @param placeId 车位id
     * @return
     */
    @ApiOperation(value = "获取车位记录")
    @GetMapping("/getParkingHis/{placeId}" )
    public R getParkingHis(@PathVariable("placeId")String placeId) {
        return R.ok(projectParkingPlaceService.getParkUseHis(placeId));
    }

    /**
     * 通过车位id获取到该车位同区域的所有车位列表
     * @author: 王良俊
     * @param placeId 车位id
     * @return
     */
    @ApiOperation(value = "获取同区域所有车位列表并返回所属区域parkId")
    @GetMapping("/getCurrentListAndPuid/{placeId}" )
    public R getCurrentListAndPuid(@PathVariable("placeId")String placeId) {
        return R.ok(projectParkingPlaceService.getCurrentListAndPuidMap(placeId));
    }

    /**
     * 通过车位区域id和人员id获取到这个人在这个区域拥有的车位列表
     * @author: 王良俊
     * @param projectParkingPlace 车位对象
     * @return
     */
    @ApiOperation(value = "获取同区域所有车位列表并返回所属区域parkId")
    @GetMapping("/listParkPlaceByRelType" )
    public R listParkPlaceByRelType(ProjectParkingPlace projectParkingPlace) {
        List<ProjectParkingPlace> projectParkingPlaces = projectParkingPlaceService.listParkPlaceByRelType(projectParkingPlace.getParkRegionId()
                , projectParkingPlace.getPersonId()
                , projectParkingPlace.getRelType()
                , projectParkingPlace.getPlaceId());
        return R.ok(projectParkingPlaces);
    }

    /**
     * 获取这个用户所拥有的车位类型
     * @author: 王良俊
     * @param parkId 车场ID
     * @param personId 人员ID
     * @return
     */
    @ApiOperation(value = "获取这个用户所拥有的车位类型")
    @GetMapping("/getPlaceRelTypeByPersonId" )
    public R getPlaceRelTypeByPersonId(String parkId, String personId) {
        List<String> removeIdList = new ArrayList<>();
        if (!projectConfigService.isEnableMultiCarsPerPlace(ProjectContextHolder.getProjectId())) {
            List<ProjectParCarRegister> list = projectParCarRegisterService.list();
            // 这里获取到已经登记了车辆的车位id
            removeIdList = list.stream().map(ProjectParCarRegister::getParkPlaceId).collect(Collectors.toList());
        }
        return R.ok(projectParkingPlaceService.getPlaceRelTypeByPersonId(parkId
                , personId, removeIdList));
    }

    /**
     * 检查收费规则是否正在被车位使用中
     * @author: 王良俊
     * @param ruleId 收费规则ID
     */
    @ApiOperation(value = "检查收费规则是否正在被车位使用中")
    @GetMapping("/checkRuleUseStatus/{ruleId}")
    public R checkRuleUseStatus(@PathVariable("ruleId") String ruleId) {
        return R.ok(projectParkingPlaceService.checkRuleUseStatus(ruleId));
    }

    /**
     * 统计车位总数
     * @return
     */
    @ApiOperation(value = "统计车位总数", notes = "统计车位总数")
    @GetMapping("/countPlace")
    public R countPlace() {
        return R.ok(projectParkingPlaceService.countPlace());
    }

    /**
     * 统计非公共车场车位总数
     * @return
     */
    @ApiOperation(value = "统计非公共车场车位总数", notes = "统计非公共车场车位总数")
    @GetMapping("/countPrivatePlace")
    public R countPrivatePlace() {
        return R.ok(projectParkingPlaceService.countPrivatePlace());
    }
}
