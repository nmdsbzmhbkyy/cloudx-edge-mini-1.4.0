package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.dto.OpenApiProjectVisitorDto;
import com.aurine.cloudx.estate.service.OpenApiProjectVisitorService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/05/18 10:29
 * @Package: com.aurine.openv2.controller
 * @Version: 1.0
 * @Remarks:
 **/
@RestController
@RequestMapping("/open/visit/inner")
@Api(value = "visit", tags = "访客信息管理", hidden = true)
public class OpenApiProjectVisitorController {

	@Resource
	private OpenApiProjectVisitorService projectVisitorService;

	/**
	 * 物业登记或住户申请
	 * 物业登记直接通过无需审核
	 *
	 * @param projectVisitorDto 访客vo对象
	 * @return R
	 */
	@ApiOperation(value = "新增访客", notes = "输入访客类型、姓名、手机号、性别、车辆信息、被访人、访问时间、人脸照片等信息新增访客，并返回新增对象")
	@SysLog("访客信息管理 - 新增访客")
	@PostMapping
    @Inner
	public R<OpenApiProjectVisitorDto> visitorRegister(@RequestBody OpenApiProjectVisitorDto projectVisitorDto) {
        try {
            return projectVisitorService.visitSave(projectVisitorDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
	}

	/**
	 * 重新物业登记或住户申请
	 * 物业登记直接通过无需审核
	 *
	 * @param visitorReAddVo 访客vo对象
	 * @return R
	 */
	@ApiOperation(value = "重新申请", notes = "指定访客记录Id，重新审请来访登记，输入项与新增类同")
	@SysLog("访客信息管理 - 重新申请")
	@PutMapping
    @Inner
	public R<OpenApiProjectVisitorDto> reRegister(@RequestBody OpenApiProjectVisitorDto visitorReAddVo) {
        try {
            return projectVisitorService.reRegister(visitorReAddVo);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
	}

	/**
	 * 物业登记或住户申请
	 * 物业登记直接通过无需审核
	 *
	 * @param visitIdList 访客记录Id列表
	 * @return R
	 */
	@ApiOperation(value = "手动签离", notes = "指定访客Id，将状态置成已签离")
	@SysLog("访客信息管理 - 手动签离")
	@DeleteMapping
    @Inner
	public R<List<String>> signOff(@RequestBody List<String> visitIdList) {
        try {
            return projectVisitorService.signOff(visitIdList);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
	}

}
