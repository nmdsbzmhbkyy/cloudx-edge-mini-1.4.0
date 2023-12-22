package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.OpenApiProjectCardDto;
import com.aurine.cloudx.estate.service.OpenApiProjectCardService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/06/28 15:18
 * @Package: com.aurine.cloudx.estate.controller
 * @Version: 1.0
 * @Remarks:
 **/
@RestController
@RequestMapping("/open/card/inner")
@Api(value = "card", tags = "门禁卡管理", hidden = true)
public class OpenApiProjectCardController {

    @Resource
    private OpenApiProjectCardService openApiProjectCardService;

    /**
     * 新增员工门禁卡
     *
     * @param projectCardDto 员工卡信息
     * @return R
     */
    @ApiOperation(value = "新增员工门禁卡", notes = "指定员工Id，更新门禁卡信息，不存在该卡则新增")
    @SysLog("新增员工门禁卡资源信息")
    @PostMapping(value = "/staff")
    @Inner
    public R<OpenApiProjectCardDto> saveOrUpdateStaffCard(@RequestBody OpenApiProjectCardDto projectCardDto) {
        projectCardDto.setPersonType(PersonTypeEnum.STAFF.code);

        try {
            return openApiProjectCardService.saveCardInfo(projectCardDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 删除员工门禁卡
     *
     * @param projectCardDto 员工卡信息
     * @return R
     */
    @ApiOperation(value = "删除员工门禁卡", notes = "指定员工Id，删除员工门禁卡信息, 返回员工Id")
    @SysLog("删除员工门禁卡资源信息")
    @DeleteMapping(value = "/staff")
    @Inner
    public R<String> removeStaffCard(@RequestBody OpenApiProjectCardDto projectCardDto) {
        projectCardDto.setPersonType(PersonTypeEnum.STAFF.code);

        try {
            return openApiProjectCardService.unbindPersonCardRelation(projectCardDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 新增住户门禁卡
     *
     * @param projectCardDto 住户卡信息
     * @return R
     */
    @ApiOperation(value = "新增住户门禁卡", notes = "指定住户Id，更新门禁卡信息，不存在该卡则新增")
    @SysLog("新增住户门禁卡资源信息")
    @PostMapping(value = "/household")
    @Inner
    public R<OpenApiProjectCardDto> saveOrUpdateProprietorCard(@RequestBody OpenApiProjectCardDto projectCardDto) {
        projectCardDto.setPersonType(PersonTypeEnum.PROPRIETOR.code);

        try {
            return openApiProjectCardService.saveCardInfo(projectCardDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 删除住户门禁卡
     *
     * @param projectCardDto 住户卡信息
     * @return R
     */
    @ApiOperation(value = "删除住户门禁卡", notes = "指定住户Id，删除住户门禁卡信息, 返回住户Id")
    @SysLog("删除住户门禁卡资源信息")
    @DeleteMapping(value = "/household")
    @Inner
    public R<String> removeProprietorCard(@RequestBody OpenApiProjectCardDto projectCardDto)  {
        projectCardDto.setPersonType(PersonTypeEnum.PROPRIETOR.code);

        try {
            return openApiProjectCardService.unbindPersonCardRelation(projectCardDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        } 
    }
}
