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
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.service.FileService;
import com.aurine.cloudx.estate.util.ImageUtil;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.minio.service.MinioTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件管理
 *
 * @author Luckly
 * @date 2019-06-18 17:18:42
 */
@Slf4j
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    @Resource
    private MinioTemplate minioTemplate;
    @Resource
    private ImageUtil imageUtil;


    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @Override
    public R uploadFile(MultipartFile file) {
        String fileName = IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
        Map<String, String> resultMap = new HashMap<>(4);
        resultMap.put("bucketName", CommonConstants.BUCKET_NAME);
        resultMap.put("fileName", fileName);

        resultMap.put("url", String.format("/admin/sys-file/%s/%s", CommonConstants.BUCKET_NAME, fileName));


        try {
            minioTemplate.putObject(CommonConstants.BUCKET_NAME, fileName, file.getInputStream(), file.getSize(), file.getContentType());
        } catch (Exception e) {
            log.error("上传失败", e);
            return R.failed(e.getLocalizedMessage());
        }
        return R.ok(resultMap);
    }

    /**
     * 上传文件，BASE64
     *
     * @param base64
     * @return
     */
    private String uploadFileBase64(String base64, String extName) throws IOException {

        if (base64 == null || base64 == "") {
            return null;
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        BASE64Decoder decoder = new BASE64Decoder();//base64转为二进制
        byte[] date_blob = decoder.decodeBuffer(base64.substring(base64.indexOf(",") + 1));
        for (int i = 0; i < date_blob.length; ++i) {
            if (date_blob[i] < 0) {
                date_blob[i] += 256;
            }
        }

        InputStream intStream = new ByteArrayInputStream(date_blob);

//        String fileName = String.format("/admin/sys-file/%s/%s", CommonConstants.BUCKET_NAME, fileNameDest);
        String fileName = uuid + StrUtil.DOT + extName;

        Map<String, String> resultMap = new HashMap<>(4);
        resultMap.put("bucketName", CommonConstants.BUCKET_NAME);
        resultMap.put("fileName", fileName);

        resultMap.put("url", String.format("/admin/sys-file/%s/%s", CommonConstants.BUCKET_NAME, fileName));


        try {
            minioTemplate.putObject(CommonConstants.BUCKET_NAME, fileName, intStream, date_blob.length, MediaType.IMAGE_JPEG_VALUE);
        } catch (ProtocolException e) {
            log.error("图片转存本地失败");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败", e);
            return "";
        } finally {
            intStream.close();
        }

//        return R.ok(resultMap);
        return resultMap.get("url");


    }


    /**
     * 上传文件,且带有水印
     *
     * @param file
     * @return
     */
    @Override
    public R uploadFileWithWaterMark(MultipartFile file, String waterMark, String templateName) throws IOException {
        return this.uploadFile(imageUtil.addWorkMarkToMultipartFile(file, waterMark, templateName));
    }

    /**
     * 上传文件,且带有水印
     *
     * @param base64
     * @param waterMark    水印内容
     * @param templateName 模板名称
     * @return
     */
    @Override
    public String uploadFileWithWaterMarkBase64(String base64, String waterMark, String templateName) throws IOException {
        if (base64 == null || base64 == "") {
            return null;
        }

        String extName = this.getBase64FileExtension(base64);

        return this.uploadFileBase64(imageUtil.addWorkMarkToBase64(base64, extName, waterMark, templateName), extName);

    }


    /**
     * 获取base64扩展名
     *
     * @param base64
     * @return
     */
    private String getBase64FileExtension(String base64) {
//        data:image/png;base64,
        if (base64.indexOf("/") == -1 || base64.indexOf(";") == -1) {
            return "";
        }
        String ext = base64.substring(base64.indexOf("/") + 1, base64.indexOf(";"));
        return ext;

    }


}
