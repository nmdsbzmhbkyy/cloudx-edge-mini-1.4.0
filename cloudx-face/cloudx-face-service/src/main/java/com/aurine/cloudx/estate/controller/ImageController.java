
package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.FaceConstant;
import com.aurine.cloudx.estate.service.FileService;
import com.aurine.cloudx.estate.util.DetectFaceResultCode;
import com.aurine.cloudx.estate.util.DetectResult;
import com.aurine.cloudx.estate.util.FaceDetect;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.minio.service.MinioTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 王伟
 * @date 2021-03-01 14:29
 */
@RestController
@RequestMapping("/images")
@Api(value = "images", tags = "上传图片")
public class ImageController {
    @Resource
    private FileService fileService;


    @ApiOperation(value = "上传图片")
    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile upload) {
        return fileService.uploadFile(upload);
    }

    @ApiOperation(value = "上传图片,并附添加水印")
    @PostMapping("/upload-with-water-mark")
    public R uploadWithWaterMark(@RequestParam("file") MultipartFile upload) throws IOException {
        return fileService.uploadFileWithWaterMark(upload, "", "");
    }

    @ApiOperation(value = "上传图片,并附添加水印")
    @PostMapping("/upload-with-water-mark-template/{template}")
    public R uploadWithWaterMarkTemplate(@RequestParam("file") MultipartFile upload, @PathVariable("template") String template) throws IOException {
        return fileService.uploadFileWithWaterMark(upload, "", template);
    }

    @ApiOperation(value = "base64方式上传图片,并附添加水印")
    @PostMapping("/upload-by-base64-with-water-mark")
    public String uploadWithWaterMarkTemplate(@RequestBody String base64) throws IOException {
        return fileService.uploadFileWithWaterMarkBase64(base64, "", "");
    }

    @ApiOperation(value = "上传图片,并附添加水印")
    @PostMapping("/upload-by-base64-with-water-mark-template/{template}")
    public String uploadWithWaterMarkTemplate(@RequestBody String base64, @PathVariable("template") String template) throws IOException {
        return fileService.uploadFileWithWaterMarkBase64(base64, "", template);
    }


}
