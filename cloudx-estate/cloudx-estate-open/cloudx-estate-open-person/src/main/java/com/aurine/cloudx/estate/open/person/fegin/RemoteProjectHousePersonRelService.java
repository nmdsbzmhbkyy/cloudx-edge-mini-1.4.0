package com.aurine.cloudx.estate.open.person.fegin;


import com.aurine.cloudx.estate.open.person.bean.ProjectHousePersonRelSearchConditionVoPage;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(contextId = "remoteProjectHousePersonRelController", value = "cloudx-estate-biz")
public interface RemoteProjectHousePersonRelService {
    @GetMapping("/baseHousePersonRel/person/page")
    R<IPage<ProjectHousePersonRelRecordVo>> getProjectHousePersonRelPage(@SpringQueryMap ProjectHousePersonRelSearchConditionVoPage page);

    @GetMapping("/baseHousePersonRel/info/{relaId}")
    R<ProjectHouseParkPlaceInfoVo> getInfo(@PathVariable("relaId") String relaId);

    @GetMapping("/baseHousePersonRel/page")
    R<IPage<ProjectHousePersonRelRecordVo>> getProjectHousePersonRelPage(Page page);


    @GetMapping("/baseHousePersonRel/house/{id}")
    R<List<ProjectHousePersonRelRecordVo>> getPersonRelByHouseId(@PathVariable("id") String id);


    @GetMapping("/baseHousePersonRel/getHouseResident/{houseId}")
    R<List<ProjectHouseResidentVo>> getHouseResident(@PathVariable("houseId") String houseId,
                                                     @RequestParam(value = "phone", required = false) String phone);

    @GetMapping("/baseHousePersonRel/{id}")
    R<ProjectHousePersonRelVo> getById(@PathVariable("id") String id);


    @GetMapping("/baseHousePersonRel/checkHouseRel/{houseId}")
    R<ProjectHousePersonRelVo> checkHouseRel(@PathVariable("houseId") String houseId,
                                             @RequestParam("personName") String personName,
                                             @RequestParam("houseHoldType") String houseHoldType,
                                             @RequestParam("personId") String personId);

    @GetMapping("/baseHousePersonRel/more/{id}")
    R<ProjectHousePersonRelVo> getMoreInfoById(@PathVariable("id") String id);


    @GetMapping("/baseHousePersonRel/owner/{houseId}")
    R<Boolean> haveOwner(@PathVariable("houseId") String houseId);

    @GetMapping("/baseHousePersonRel/info")
    R<List<ProjectHouseHisRecordVo>> findPerson(@Param("name") String name);


    @PostMapping("/baseHousePersonRel")
    R save(@RequestBody ProjectHousePersonRelRequestVo projectHousePersonRel);


    @PutMapping("/baseHousePersonRel/requestAgain")
    R requestAgain(@RequestBody ProjectHousePersonRelRequestAgainVo projectHousePersonRelRequestAgainVo);

    @PutMapping("/baseHousePersonRel")
    R updateById(@RequestBody ProjectHousePersonRelVo projectHousePersonRel);


    @DeleteMapping("/baseHousePersonRel/{id}")
    R removeById(@PathVariable("id") String id);


    @DeleteMapping("/baseHousePersonRel/rel/{id}")
    R removeRelById(@PathVariable("id") String id);

    @DeleteMapping("/baseHousePersonRel/removeAll")
    R removeById(@RequestBody List<String> ids);


    @GetMapping("/baseHousePersonRel/pageIdentity")
    R<IPage<ProjectHousePersonRelRecordVo>> pageIdentity(@SpringQueryMap ProjectHousePersonRelSearchConditionVoPage page);

    @PutMapping("/baseHousePersonRel/verify")
    R verify(@RequestBody ProjectHousePersonRelVo projectHousePersonRel);


    @PostMapping("/baseHousePersonRel/byStaff")
    R<String> save(@RequestBody ProjectHousePersonRelVo projectHousePersonRel);


}
