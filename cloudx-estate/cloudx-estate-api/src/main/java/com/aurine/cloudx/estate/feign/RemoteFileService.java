/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.config.MultipartSupportConfig;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 王伟
 * @date 2020-11-05 8:34
 * <p>
 * 远程文件上传调用接口
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.UPMS_SERVICE, configuration = MultipartSupportConfig.class)
public interface RemoteFileService {

    /**
     * 上传文件
     * 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
     *
     * @param file 资源
     * @return R(/ admin / bucketName / filename)
     */
    @PostMapping(value = "/sys-file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R upload(@RequestParam("file") MultipartFile file);



    /**
     * 上传文件
     * 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
     *
     * @param file 资源
     * @return R(/ admin / bucketName / filename)
     */
    @PostMapping("/sys-file/byBucket")
    @ApiOperation(value = "根据桶名和分类路径上传文件", notes = "根据桶名和分类路径上传文件，返回值为")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucket", value = "桶名", required = true, paramType = "header"),
            @ApiImplicitParam(name = "path", value = "分类路径(租户id+'/'+集团id+’/‘+项目组id+'/'+'项目id')", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "file", value = "文件", required = true, paramType = "form")
    })
    R uploadByBucket(@RequestHeader("bucket") String bucket, @RequestHeader(value = "path", required = false) String path, @RequestParam("file") MultipartFile file);



    /**
     * 获取文件
     *
     * @param bucket   桶名称
     * @param fileName 文件空间/名称
     * @return
     */
    @GetMapping("/sys-file/{bucket}/{fileName}")
    @ApiOperation(value = "获取文件", notes = "获取文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucket", value = "桶名", required = true, paramType = "path"),
            @ApiImplicitParam(name = "path", value = "分类路径(租户id+'/'+集团id+’/‘+项目组id+'/'+'项目id')", paramType = "query"),
            @ApiImplicitParam(name = "fileName", value = "文件名", required = true, paramType = "path")
    })
    public ResponseEntity file(@PathVariable(value = "bucket") String bucket, @PathVariable(value = "fileName") String fileName, @RequestParam(value = "path", required = false) String path);
}
