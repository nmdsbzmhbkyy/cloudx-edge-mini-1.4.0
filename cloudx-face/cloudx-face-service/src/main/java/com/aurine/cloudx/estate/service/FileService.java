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

package com.aurine.cloudx.estate.service;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件管理
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    R uploadFile(MultipartFile file);



    /**
     * 上传文件,且带有水印
     *
     * @param file
     * @param waterMark     水印内容
     * @param templateName  模板名称
     * @return
     */
    R uploadFileWithWaterMark(MultipartFile file, String waterMark, String templateName) throws IOException;

    /**
     * 上传文件,且带有水印
     *
     * @param base64
     * @param waterMark     水印内容
     * @param templateName  模板名称
     * @return
     */
    String uploadFileWithWaterMarkBase64(String base64, String waterMark, String templateName) throws IOException;

}
