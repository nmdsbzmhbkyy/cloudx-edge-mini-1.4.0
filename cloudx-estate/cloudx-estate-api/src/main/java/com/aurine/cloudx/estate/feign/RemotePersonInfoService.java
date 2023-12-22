package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.dto.ParkingPersonIdDto;
import com.aurine.cloudx.estate.dto.PersonInfoCacheDto;
import com.aurine.cloudx.estate.dto.PersonLabelDto;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.vo.*;
import com.aurine.cloudx.estate.entity.ProjectPersonLabel;
import com.aurine.cloudx.estate.vo.ProjectPersonInfoVo;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(contextId = "remotePersonInfoService", value = "cloudx-estate-biz")
public interface RemotePersonInfoService {
    @GetMapping("/basePersonInfo/get-owner")
    R<ProjectPersonInfo> getOwner();

    @GetMapping("/basePersonInfo/inner/{id}")
    R innerGetById(@PathVariable("id") String id,
                   @RequestHeader(SecurityConstants.FROM) String from);

    @GetMapping("/basePersonInfo/inner/telephoneParking/{telephone}")
    R<ProjectPersonInfo> getByTelephone(@PathVariable("telephone") String telephone, @RequestHeader(SecurityConstants.FROM) String from);

    @GetMapping("/basePersonInfo/excel/telephone/{redisKey}/{telephone}")
    R<ProjectPersonInfoVo> getByTelephoneCache(@PathVariable("redisKey") String redisKey, @PathVariable("telephone") String telephone);


    @PostMapping("/basePersonInfo/excel/addNewPersonInfoToTheCache")
    R addNewPersonInfoToTheCache(@RequestBody PersonInfoCacheDto dto);

    @DeleteMapping("/basePersonInfo/excel/deletePersonInfoTmpCache/{redisKey}")
    R deletePersonInfoTmpCache(@PathVariable("redisKey") String redisKey);

    @PostMapping("/basePersonInfo/excel/saveFromSystem")
    R saveFromSystem(@RequestBody ProjectPersonInfo personInfo, @RequestHeader(SecurityConstants.FROM) String from);

    @GetMapping("/basePersonInfo/inner/addPersonLabel")
    R addPersonLabel(@RequestBody PersonLabelDto personLabelDto);

    @GetMapping("/basePersonInfo/checkPersonAssets/{personId}")
    R<Integer> checkPersonAssets(@PathVariable("personId") String personId);

    @GetMapping("/basePersonInfo/checkPersonAssets")
    R<List<PersonAssetsNumVo>> checkPersonAssets(@RequestBody ParkingPersonIdDto parkingPersonIdDto);

    @GetMapping("/basePersonInfo/inner/checkPersonAssets/{personId}")
    R<Integer> innerCheckPersonAssets(@PathVariable("personId") String personId, @RequestHeader(SecurityConstants.FROM) String from);

    @PostMapping("/basePersonInfo/savePersonByParking")
    R savePersonByParking(@RequestBody ProjectPersonInfo projectPersonInfo,@RequestHeader(SecurityConstants.FROM) String from);

    @GetMapping("/basePersonInfo/getPersonByPersonId/{personId}")
    R<ProjectPersonInfo> getPersonByPersonId(@PathVariable("personId") String personId);

    @GetMapping("/basePersonInfo/inner/getPersonByPersonId/{personId}")
    R<ProjectPersonInfo> innerGetPersonByPersonId(@PathVariable("personId") String personId,@RequestHeader(SecurityConstants.FROM) String from);

    @PutMapping("/basePersonInfo/updatePersonById")
    R updatePersonById(@RequestBody ProjectPersonInfo projectPersonInfo);

    @PutMapping("/basePersonInfo/inner/updatePersonById")
    R innerUpdatePersonById(@RequestBody ProjectPersonInfo projectPersonInfo,@RequestHeader(SecurityConstants.FROM) String from);

    @PostMapping("/basePersonInfo/addLabel")
    R addLabel(@RequestBody ProjectParkingPlaceManageVo projectParkingPlaceManage);

    @PostMapping("/basePersonInfo/updatePersonAttrList")
    R updatePersonAttrList(@RequestBody ProjectPersonAttrFormVo projectPersonAttrFormVo);


    @GetMapping("/basePersonInfo/listByPersonId/{personId}")
    R<List<ProjectPersonLabel>> listByPersonId(@PathVariable("personId") String personId);

    @GetMapping("/basePersonInfo/getFrameInfoByHouseId/{houseId}")
    R<ProjectFrameInfo> getFrameInfoByHouseId(@PathVariable("houseId") String houseId);

    @GetMapping("/basePersonInfo/getPersonAttrListVo/{personId}")
    R<List<ProjectPersonAttrListVo>> getPersonAttrListVo(@PathVariable("personId") String personId);


    @GetMapping("/basePersonInfo/inner/listByPersonId/{personId}")
    R<List<ProjectPersonLabel>> innerListByPersonId(@PathVariable("personId") String personId);

    @GetMapping("/basePersonInfo/inner/getFrameInfoByHouseId/{houseId}")
    R<ProjectFrameInfo> innerGetFrameInfoByHouseId(@PathVariable("houseId") String houseId);

    @GetMapping("/basePersonInfo/inner/getPersonAttrListVo/{personId}")
    R<List<ProjectPersonAttrListVo>> innerGetPersonAttrListVo(@PathVariable("personId") String personId);

    @GetMapping("/basePersonInfo/inner/telephoneParking2/{telephone}")
    R<ProjectPersonInfo> innerGetByTelephone(@PathVariable("telephone") String telephone);

    @PostMapping("/basePersonInfo/inner/savePersonByParking")
    R innerSavePersonByParking(@RequestBody ProjectPersonInfo projectPersonInfo);

    @PostMapping("/basePersonInfo/inner/addLabel")
    R innerAddLabel(@RequestBody ProjectParkingPlaceManageVo projectParkingPlaceManage);

    @PostMapping("/basePersonInfo/inner/updatePersonAttrList")
    R innerUpdatePersonAttrList(@RequestBody ProjectPersonAttrFormVo projectPersonAttrFormVo);
}
