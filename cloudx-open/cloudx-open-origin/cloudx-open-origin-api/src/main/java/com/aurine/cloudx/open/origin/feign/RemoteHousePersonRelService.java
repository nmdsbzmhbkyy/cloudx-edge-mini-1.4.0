package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.entity.ProjectHousePersonRel;
import com.aurine.cloudx.open.origin.dto.ProjectHouseDTO;
import com.aurine.cloudx.open.origin.dto.ProjectHousePersonRelSearchConditionDTO;
import com.aurine.cloudx.open.origin.vo.KafkaSaveFaceVo;
import com.aurine.cloudx.open.origin.vo.ProjectHousePersonRelVo;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (RemoteHousePersonRelService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 9:03
 */
@FeignClient(contextId = "openRemoteHousePersonRelService", value = "cloudx-estate-biz")
public interface RemoteHousePersonRelService {
    @PutMapping("/baseHousePersonRel/verify")
    public R verify(@RequestBody ProjectHousePersonRelVo projectHousePersonRel);
    @GetMapping("/baseHousePersonRel/sendGeneratedFaceMessage")
     R sendGeneratedFaceMessage(@SpringQueryMap KafkaSaveFaceVo map);
    @GetMapping("/findSaveFace/{relaId}")
    public R<String> findSaveFace(@PathVariable("relaId") String relaId);

    @GetMapping("/baseHousePersonRel/page")
    public R getProjectHousePersonRelPage(@SpringQueryMap ProjectHousePersonRelSearchConditionDTO projectHousePersonRelSearchConditionDTO);

    @DeleteMapping("/baseHousePersonRel/{id}")
    public R removeById(@PathVariable("id") String id);

    @DeleteMapping("/baseHousePersonRel/rel/{id}")
    public R removeRelById(@PathVariable("id") String id);

    @DeleteMapping("/baseHousePersonRel/removeAll")
    public R removeAll(@RequestBody List<String> ids);

    @PostMapping("/baseHousePersonRel/saveRel")
    public R<ProjectHousePersonRel> saveRel(@RequestBody ProjectHousePersonRelVo projectHousePersonRel);

    @GetMapping("/baseHousePersonRel/list-house-by-person-id/{id}")
    R<List<ProjectHouseDTO>> listHouseByPersonId(@PathVariable("id") String id);

    @GetMapping("/baseHousePersonRel/inner/house/{id}")
    R innerGetPersonRelByHouseId(@PathVariable("id") String id,
                                 @RequestHeader(SecurityConstants.FROM) String from);
}
