

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.SysCompany;
import com.aurine.cloudx.estate.entity.SysGroup;
import com.aurine.cloudx.estate.service.SysCompanyService;
import com.aurine.cloudx.estate.service.SysGroupService;
import com.aurine.cloudx.estate.service.SysInfoService;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.util.SerialNumberUtil;
import com.aurine.cloudx.estate.util.SpringContextUtil;
import com.aurine.cloudx.estate.vo.SysGroupFormVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;


/**
 * 校验证书
 * @author cjw
 */
@RestController
@RequestMapping("/sysVerify")
@Api(value = "sysVerify", tags = "授权管理")
public class SysVerifyController {
    @Resource
    SysInfoService sysInfoService;

    /**
     * 上传授权文件并校验
     * @param file
     * @return
     */
    @ApiOperation(value = "上传授权文件")
    @PostMapping("/upload")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = " 集团id 项目组id 或 项目id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @Inner(false)
    public R upload(@RequestPart("file") MultipartFile file) {

        return R.ok(sysInfoService.uploadFile(file));
    }

    @ApiOperation(value = "获取授权信息并且校验")
    @GetMapping("/getSysInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = " 集团id 项目组id 或 项目id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @Inner(false)
    public R getSysInfo() {
        // 获取当前激活的应用程序配置文件
        if("dev".equals(SpringContextUtil.getProfile())) {
            return R.ok();
        }
        // 从Redis中获取预先存储的授权信息
        String systemInfo = (String) RedisUtil.get("systemInfo");
        if(null == systemInfo) {
            // 获取所有授权信息
            return  sysInfoService.verifiedAll();
        }
        // 将上面获取所有授权信息，存储到Redis中
        return R.ok(RedisUtil.get("systemInfo"));
    }
}
