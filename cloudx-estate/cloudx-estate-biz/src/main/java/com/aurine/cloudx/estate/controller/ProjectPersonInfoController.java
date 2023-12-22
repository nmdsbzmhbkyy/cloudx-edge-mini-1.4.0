

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PersonAttrTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ParkingPersonIdDto;
import com.aurine.cloudx.estate.dto.PersonInfoCacheDto;
import com.aurine.cloudx.estate.dto.PersonLabelDto;
import com.aurine.cloudx.estate.entity.ProjectFocusPersonAttr;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonLabel;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.bean.BeanPropertyUtil;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageVo;
import com.aurine.cloudx.estate.vo.ProjectPersonAttrFormVo;
import com.aurine.cloudx.estate.vo.ProjectPersonAttrListVo;
import com.aurine.cloudx.estate.vo.ProjectPersonInfoVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 人员
 *
 * @author 王伟
 * @date 2020-05-11 09:12:50
 */
@RestController
@AllArgsConstructor
@RequestMapping("/basePersonInfo")
@Api(value = "basePersonInfo", tags = "人员管理")
public class ProjectPersonInfoController {

    private final ProjectPersonInfoService projectPersonInfoService;
    private final ProjectPersonLabelService projectPersonLabelService;
    private final ProjectPersonAttrService projectPersonAttrService;
    private final ProjectFocusPersonAttrService projectFocusPersonAttrService;
    private final ProjectFrameInfoService projectFrameInfoService;

    /**
     * 分页查询
     *
     * @param page              分页对象
     * @param projectPersonInfo 人员
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectPersonInfoPage(Page page, ProjectPersonInfo projectPersonInfo) {
        return R.ok(projectPersonInfoService.page(page, Wrappers.query(projectPersonInfo)));
    }


    /**
     * 通过id查询人员
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(poToVo(projectPersonInfoService.getById(id)));
    }

    /**
     * 通过id查询人员
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/inner/{id}")
    @Inner(false)
    public R innerGetById(@PathVariable("id") String id) {
        return R.ok(poToVo(projectPersonInfoService.getById(id)));
    }

    /**
     * 通过telephone查询人员
     *
     * @param telephone telephone
     * @return R
     */
    @ApiOperation(value = "通过telephone查询", notes = "通过telephone查询")
    @GetMapping("/telephone/{telephone}")
    public R getByTelephone(@PathVariable("telephone") String telephone) {
        ProjectPersonInfoVo personInfoVo = poToVo(projectPersonInfoService.getByTelephone(telephone));
        Integer assetsNum = 0;
        // 如果该住户存在,且(assetsNum=0 || assetsNum=null),则将该住户在住户表的信息删除
        if (personInfoVo != null) {
            // 资产(assetsNum) = 车数 + 房数 + 车位数
            assetsNum = projectPersonInfoService.checkPersonAssets(personInfoVo.getPersonId());
            assetsNum = assetsNum != null ? assetsNum : 0;
        }
        // 住户无资产，或住户不存在，返回null，反之返回炒鸡完整的住户信息
        return R.ok(assetsNum != 0 ? personInfoVo : null);
    }

    /**
     * 通过telephone查询人员
     *
     * @param telephone telephone
     * @return R
     */
    @Inner(value = false)
    @ApiOperation(value = "通过telephone查询", notes = "通过telephone查询")
    @GetMapping("/excel/telephone/{redisKey}/{telephone}")
    public R getByTelephone(@PathVariable("redisKey") String redisKey, @PathVariable("telephone") String telephone) {
        ProjectPersonInfoVo personInfoVo = poToVo(projectPersonInfoService.getByTelephone(redisKey, telephone));
        int assetsNum = 0;
        if (personInfoVo != null) {
            // 如果这个住户没有任何资产但是没有被删除则在这里进行删除操作
            Integer num = projectPersonInfoService.checkPersonAssetsParking(personInfoVo.getPersonId());
            assetsNum = num != null ? num : 0;
        }
        return R.ok(assetsNum != 0 ? personInfoVo : null);
    }

    @Inner(value = false)
    @ApiOperation(value = "excel导入使用", notes = "excel导入使用")
    @PostMapping("/excel/addNewPersonInfoToTheCache")
    public R addNewPersonInfoToTheCache(@RequestBody PersonInfoCacheDto dto) {
        ProjectPersonInfo personInfo = new ProjectPersonInfo();
        BeanPropertyUtil.copyProperty(personInfo, dto);
        projectPersonInfoService.addNewPersonInfoToTheCache(dto.getRedisKey(), dto.getTelephone(), personInfo);
        return R.ok();
    }

    @Inner(value = false)
    @ApiOperation(value = "excel导入使用", notes = "excel导入使用")
    @DeleteMapping("/excel/deletePersonInfoTmpCache/{redisKey}")
    public R deletePersonInfoTmpCache(@PathVariable("redisKey") String redisKey) {
        projectPersonInfoService.deletePersonInfoTmpCache(redisKey);
        return R.ok();
    }

    @Inner(value = false)
    @ApiOperation(value = "excel导入使用", notes = "excel导入使用")
    @PostMapping("/excel/saveFromSystem")
    public R saveFromSystem(@RequestBody ProjectPersonInfo personInfo) {
        projectPersonInfoService.saveFromSystem(personInfo);
        return R.ok();
    }

    /**
     * 通过telephone查询人员  提供给车场remote
     *
     * @param telephone telephone
     * @return R
     */
    @ApiOperation(value = "通过telephone查询", notes = "通过telephone查询")
    @GetMapping("/telephoneParking/{telephone}")
    public R telephoneParking(@PathVariable("telephone") String telephone) {
        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(telephone);
        return R.ok(personInfo);
    }

    /**
     * 通过telephone查询人员  提供给车场remote
     *
     * @param telephone telephone
     * @return R
     */
    @Inner(value = false)
    @ApiOperation(value = "通过telephone查询", notes = "通过telephone查询")
    @GetMapping("/inner/telephoneParking/{telephone}")
    public R innerTelephoneParking(@PathVariable("telephone") String telephone) {
        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(telephone);
        return R.ok(personInfo);
    }
    /**
     * 通过personId查询  提供给车场remote
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过personId查询", notes = "通过telephone查询")
    @GetMapping("/getPersonByPersonId/{personId}")
    public R getPersonByPersonId(@PathVariable("personId") String personId) {
        ProjectPersonInfo personInfo = projectPersonInfoService.getById(personId);
        return R.ok(personInfo);
    }

    @ApiOperation(value = "通过personId查询", notes = "通过telephone查询")
    @GetMapping("/inner/getPersonByPersonId/{personId}")
    @Inner(false)
    public R innerGetPersonByPersonId(@PathVariable("personId") String personId) {
        ProjectPersonInfo personInfo = projectPersonInfoService.getById(personId);
        return R.ok(personInfo);
    }

    /**
     * 通过personId查询  提供给车场remote
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "通过personId查询", notes = "通过telephone查询")
    @GetMapping("/listByPersonId/{personId}")
    public R listByPersonId(@PathVariable("personId") String personId) {
        List<ProjectPersonLabel> projectPersonLabels = projectPersonLabelService.listByPersonId(personId);
        return R.ok(projectPersonLabels);
    }

    /**
     * 通过houseId查询  提供给车场remote
     *
     * @param houseId
     * @return
     */
    @ApiOperation(value = "通过houseId查询", notes = "通过houseId查询")
    @GetMapping("/getFrameInfoByHouseId/{houseId}")
    public R getFrameInfoByHouseId(@PathVariable("houseId") String houseId) {
        ProjectFrameInfo frame = projectFrameInfoService.getById(houseId);
        return R.ok(frame);
    }

    /**
     * 通过personId查询  提供给车场remote
     *
     * @param personId
     * @return
     */
    @ApiOperation(value = "通过personId查询", notes = "通过telephone查询")
    @GetMapping("/getPersonAttrListVo/{personId}")
    public R getPersonAttrListVo(@PathVariable("personId") String personId) {
        List<ProjectPersonAttrListVo> personAttrListVo = projectPersonAttrService.getPersonAttrListVo(ProjectContextHolder.getProjectId(), PersonAttrTypeEnum.PLACE_MANAGE.code, personId);
        return R.ok(personAttrListVo);
    }
    /**
     * 修改人员  remote
     *
     * @param projectPersonInfo 人员
     * @return R
     */
    @ApiOperation(value = "修改人员", notes = "修改人员")
    @SysLog("修改人员")
    @PutMapping("/updatePersonById")
    public R updatePersonById(@RequestBody ProjectPersonInfo projectPersonInfo) {
        return R.ok(projectPersonInfoService.updateById(projectPersonInfo));
    }

    /**
     * 修改人员  remote
     *
     * @param projectPersonInfo 人员
     * @return R
     */
    @ApiOperation(value = "修改人员", notes = "修改人员")
    @SysLog("修改人员")
    @PutMapping("inner/updatePersonById")
    @Inner(false)
    public R innerUpdatePersonById(@RequestBody ProjectPersonInfo projectPersonInfo) {
        return R.ok(projectPersonInfoService.updateById(projectPersonInfo));
    }

    /**
     * 添加人员标签  remote
     * @param projectParkingPlaceManageVo
     * @return
     */
    @ApiOperation(value = "添加人员标签", notes = "添加人员标签")
    @SysLog("添加人员标签")
    @PostMapping("/addLabel")
    public R addLabel(@RequestBody ProjectParkingPlaceManageVo projectParkingPlaceManageVo) {
        return R.ok(projectPersonLabelService.addLabel(projectParkingPlaceManageVo.getTagArray(),projectParkingPlaceManageVo.getPersonId()));
    }

    /**
     * 新增扩展属性 remote
     * @param projectPersonAttrFormVo
     * @return
     */
    @ApiOperation(value = "新增扩展属性", notes = "新增扩展属性")
    @SysLog("新增扩展属性")
    @PostMapping("/updatePersonAttrList")
    public R updatePersonAttrList(@RequestBody ProjectPersonAttrFormVo projectPersonAttrFormVo) {
        return R.ok(projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo));
    }


    /**
     * 对当前人员进行处理-判断是否需要删除该人员 提供给车场remote
     *
     * @param personId
     * @return
     */
    @ApiOperation(value = "对当前人员进行处理-判断是否需要删除该人员", notes = "对当前人员进行处理-判断是否需要删除该人员")
    @GetMapping("/checkPersonAssets/{personId}")
    public R checkPersonAssets(@PathVariable("personId") String personId) {
        return R.ok(projectPersonInfoService.checkPersonAssetsParking(personId));
    }

    /**
     * 对当前人员进行处理-判断是否需要删除该人员 提供给车场 remote
     *
     * @param parkingPersonIdDto
     * @return
     */
    @ApiOperation(value = "对当前人员进行处理-判断是否需要删除该人员", notes = "对当前人员进行处理-判断是否需要删除该人员")
    @PostMapping("/checkPersonAssets")
    public R checkPersonAssets(@RequestBody ParkingPersonIdDto parkingPersonIdDto) {
        return R.ok(projectPersonInfoService.checkPersonAssets(parkingPersonIdDto.getPersonIdList()));
    }

    /**
     * 对当前人员进行处理-判断是否需要删除该人员 提供给车场remote
     *
     * @param personId
     * @return
     */
    @ApiOperation(value = "对当前人员进行处理-判断是否需要删除该人员", notes = "对当前人员进行处理-判断是否需要删除该人员")
    @GetMapping("/inner/checkPersonAssets/{personId}")
    @Inner(false)
    public R innerCheckPersonAssets(@PathVariable("personId") String personId) {
        return R.ok(projectPersonInfoService.checkPersonAssetsParking(personId));
    }

    /**
     * 新增人员 提供给车场remote
     *
     * @param projectPersonInfo 人员
     * @return R
     */
    @ApiOperation(value = "新增人员", notes = "新增人员")
    @SysLog("新增人员")
    @PostMapping("/savePersonByParking")
//    @PreAuthorize("@pms.hasPermission('estate_projectpersoninfo_add')")
    @Inner
    public R savePersonByParking(@RequestBody ProjectPersonInfo projectPersonInfo) {
        return R.ok(projectPersonInfoService.saveFromSystem(projectPersonInfo));
    }

    /**
     * 修改人员 提供给车场remote
     *
     * @param projectPersonInfo 人员
     * @return R
     */
    @ApiOperation(value = "修改人员", notes = "修改人员")
    @SysLog("修改人员")
    @PostMapping("/updatePersonByParking")
//    @PreAuthorize("@pms.hasPermission('estate_projectpersoninfo_add')")
    public R updatePersonByParking(@RequestBody ProjectPersonInfo projectPersonInfo) {
        return R.ok(projectPersonInfoService.updateById(projectPersonInfo));
    }

    /**
     * 新增人员
     *
     * @param projectPersonInfo 人员
     * @return R
     */
    @ApiOperation(value = "新增人员", notes = "新增人员")
    @SysLog("新增人员")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projectpersoninfo_add')")
    public R save(@RequestBody ProjectPersonInfo projectPersonInfo) {
        return R.ok(projectPersonInfoService.save(projectPersonInfo));
    }

    /**
     * 修改人员
     *
     * @param projectPersonInfo 人员
     * @return R
     */
    @ApiOperation(value = "修改人员", notes = "修改人员")
    @SysLog("修改人员")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projectpersoninfo_edit')")
    public R updateById(@RequestBody ProjectPersonInfo projectPersonInfo) {
        return R.ok(projectPersonInfoService.updateById(projectPersonInfo));
    }

    /**
     * 通过id删除人员
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除人员", notes = "通过id删除人员")
    @SysLog("通过id删除人员")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_projectpersoninfo_del')")
    public R removeById(@PathVariable String id) {
        return R.ok(projectPersonInfoService.removeById(id));
    }

    /**
     * 统计人员类型
     *
     * @return R
     */
    @ApiOperation(value = "统计人员类型", notes = "统计人员类型")
    @GetMapping("/groupByPersonType")
    public R groupByPersonType() {
        return R.ok(projectPersonInfoService.groupByPersonType());
    }

    /**
     * 统计人员类型
     *
     * @return R
     */
    @Inner(value = false)
    @ApiOperation(value = "统计人员类型", notes = "统计人员类型")
    @PostMapping("/inner/addPersonLabel")
    public R addPersonLabel(@RequestBody PersonLabelDto personLabelDto) {
        return R.ok(projectPersonLabelService.addLabel(personLabelDto.getLabelUidArray(), personLabelDto.getPersonId()));
    }

    @GetMapping("/identify/{relaId}")
    public R<ProjectPersonInfo> getPerson(@PathVariable("relaId") String relaId) {
        return R.ok(projectPersonInfoService.getPersonById(relaId));
    }

    /**
     * 获取当前登录业主信息
     * @return
     */
    @GetMapping("/get-owner")
    public R<ProjectPersonInfo> getOwner() {
        return R.ok(projectPersonInfoService.getPersonByOwner());
    }
    /**
     * po 转换 vo ，拼接tag数组
     *
     * @param personInfo
     * @return
     */
    private ProjectPersonInfoVo poToVo(ProjectPersonInfo personInfo) {
        ProjectPersonInfoVo personInfoVo = new ProjectPersonInfoVo();
        if (BeanUtil.isNotEmpty(personInfo)) {
            BeanUtils.copyProperties(personInfo, personInfoVo);
            //转换tag

            //转换人员标签
            List<ProjectPersonLabel> lableList = projectPersonLabelService.listByPersonId(personInfo.getPersonId());
            String[] lableArray = lableList.stream().map(ProjectPersonLabel::getLabelId).collect(Collectors.toList()).toArray(new String[lableList.size()]);
            personInfoVo.setTagArray(lableArray);

            // 获取人员拓展属性
            List<ProjectPersonAttrListVo> projectPersonAttrListVos = projectPersonAttrService.getPersonAttrListVo(ProjectContextHolder.getProjectId(), PersonTypeEnum.PROPRIETOR.code, personInfo.getPersonId());
            personInfoVo.setProjectPersonAttrListVoList(projectPersonAttrListVos);

            // 获取重点人员信息如果有的话
            ProjectFocusPersonAttr focusPersonAttr = projectFocusPersonAttrService.getFocusPersonAttrByPersonId(personInfo.getPersonId());
            BeanUtil.copyProperties(focusPersonAttr, personInfoVo);
            personInfoVo.setPersonId(personInfo.getPersonId());
            return personInfoVo;
        } else {
            return null;
        }
    }

    /**
     * 内部通过personId查询  提供给车场remote
     *
     * @param personId personId
     * @return R
     */
    @ApiOperation(value = "内部通过personId查询", notes = "内部通过telephone查询")
    @GetMapping("/inner/listByPersonId/{personId}")
    @Inner(value = false)
    public R innerListByPersonId(@PathVariable("personId") String personId) {
        List<ProjectPersonLabel> projectPersonLabels = projectPersonLabelService.listByPersonId(personId);
        return R.ok(projectPersonLabels);
    }

    /**
     * 内部通过houseId查询  提供给车场remote
     *
     * @param houseId
     * @return
     */
    @ApiOperation(value = "内部通过houseId查询", notes = "内部通过houseId查询")
    @GetMapping("/inner/getFrameInfoByHouseId/{houseId}")
    @Inner(value = false)
    public R innerGetFrameInfoByHouseId(@PathVariable("houseId") String houseId) {
        ProjectFrameInfo frame = projectFrameInfoService.getById(houseId);
        return R.ok(frame);
    }

    /**
     * 通过personId查询  提供给车场remote
     *
     * @param personId
     * @return
     */
    @ApiOperation(value = "内部通过personId查询", notes = "内部通过personId查询")
    @GetMapping("/inner/getPersonAttrListVo/{personId}")
    @Inner(value = false)
    public R innerGetPersonAttrListVo(@PathVariable("personId") String personId) {
        List<ProjectPersonAttrListVo> personAttrListVo = projectPersonAttrService.getPersonAttrListVo(ProjectContextHolder.getProjectId(), PersonAttrTypeEnum.PLACE_MANAGE.code, personId);
        return R.ok(personAttrListVo);
    }

    /**
     * 内部通过telephone查询人员
     *
     * @param telephone telephone
     * @return R
     */
    @ApiOperation(value = "内部通过telephone查询", notes = "内部通过telephone查询")
    @GetMapping("/inner/telephoneParking2/{telephone}")
    @Inner(value = false)
    public R innerGetByTelephone(@PathVariable("telephone") String telephone) {
        ProjectPersonInfoVo personInfoVo = poToVo(projectPersonInfoService.getByTelephone(telephone));
        int assetsNum = 0;
        if (personInfoVo != null) {
            // 如果这个住户没有任何资产但是没有被删除则在这里进行删除操作
            Integer num = projectPersonInfoService.checkPersonAssets(personInfoVo.getPersonId());
            assetsNum = num != null ? num : 0;
        }
        return R.ok(assetsNum != 0 ? personInfoVo : null);
    }

    /**
     * 内部新增人员 提供给车场remote
     *
     * @param projectPersonInfo 人员
     * @return R
     */
    @ApiOperation(value = "内部新增人员", notes = "内部新增人员")
    @SysLog("新增人员")
    @PostMapping("/inner/savePersonByParking")
//    @PreAuthorize("@pms.hasPermission('estate_projectpersoninfo_add')")
    @Inner(value = false)
    public R innerSavePersonByParking(@RequestBody ProjectPersonInfo projectPersonInfo) {
        return R.ok(projectPersonInfoService.saveFromSystem(projectPersonInfo));
    }

    /**
     * 内部添加人员标签  remote
     * @param projectParkingPlaceManageVo
     * @return
     */
    @ApiOperation(value = "内部添加人员标签", notes = "内部添加人员标签")
    @SysLog("内部添加人员标签")
    @PostMapping("/inner/addLabel")
    @Inner(value = false)
    public R innerAddLabel(@RequestBody ProjectParkingPlaceManageVo projectParkingPlaceManageVo) {
        return R.ok(projectPersonLabelService.addLabel(projectParkingPlaceManageVo.getTagArray(),projectParkingPlaceManageVo.getPersonId()));
    }

    /**
     * 内部新增扩展属性 remote
     * @param projectPersonAttrFormVo
     * @return
     */
    @ApiOperation(value = "内部新增扩展属性", notes = "内部新增扩展属性")
    @SysLog("内部新增扩展属性")
    @PostMapping("/inner/updatePersonAttrList")
    @Inner(value = false)
    public R innerUpdatePersonAttrList(@RequestBody ProjectPersonAttrFormVo projectPersonAttrFormVo) {
        return R.ok(projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo));
    }
}
