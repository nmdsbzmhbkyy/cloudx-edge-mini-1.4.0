package com.aurine.cloudx.edge.sync.biz.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.enums.ImagesEnum;
import com.aurine.cloudx.edge.sync.common.utils.ImgConvertUtil;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: ImageHandleUtil
 * @author: zouyu
 * @date: 2022-12-16 13:51:46
 * @Copyright:
 */
@Slf4j
@Component
public class ImageHandleUtil {

    public final static String URL = "/admin/sys-file/";

    public final static String KEY = "cloux-edge-sync-platform-master-biz:imageHandle";

    @Value("${cloud-url}")
    public String cloudUrl;

    @Resource
    private ImgConvertUtil imgConvertUtil;

    /**
     * 处理图片上传
     *
     * @param data
     * @param serviceName
     */
    public void edgeHandle(String data, String serviceName) {
        //不包含图片直接返回
        if (!data.contains(URL)) {
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(data);
        //获取对应的图片字段
        List<String> fieIdList = ImagesEnum.getFieId(serviceName);
        if (CollUtil.isNotEmpty(fieIdList)) {
            //报警事件处理是用逗号拼接图片 处理方式不同
            if (serviceName.equals(ImagesEnum.ALARM_HANDLE.serviceName)) {
                String picUrl = jsonObject.getString(fieIdList.get(0));
                if (picUrl.contains(",")) {
                    String[] split = picUrl.split(",");
                    for (int i = 0; i < split.length; i++) {
                        RedisUtil.lSet(KEY, split[i]);
                    }
                } else {
                    RedisUtil.lSet(KEY, picUrl);
                }
            } else {
                for (String e : fieIdList) {
                    String picUrl = jsonObject.getString(e);
                    //不为空并且在集合里不存在
                    if (StrUtil.isNotEmpty(picUrl)) {
                        RedisUtil.lSet(KEY, picUrl);
                    }
                }
            }
        }
    }

    /**
     * 处理图片下载
     *
     * @param data
     * @param serviceName
     */
    public void downloadImages(String data, String serviceName) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(data);
        List<String> fieIdList = ImagesEnum.getFieId(serviceName);
        if (CollUtil.isNotEmpty(fieIdList)) {
            //报警事件处理是用逗号拼接图片 处理方式不同
            if (serviceName.equals(ImagesEnum.ALARM_HANDLE.serviceName)) {
                String picUrl = jsonObject.getString(fieIdList.get(0));
                if(StrUtil.isNotEmpty(picUrl)){
                    if (picUrl.contains(",")) {
                        String[] split = picUrl.split(",");
                        for (String url : Arrays.asList(split)) {
                            //获取存储桶和文件路径
                            String bucketName = getBucketName(url);
                            String objectName = getObjectName(url);
                            imgConvertUtil.saveToMinio(cloudUrl + url, bucketName, objectName);
                        }
                    } else {
                        String bucketName = getBucketName(picUrl);
                        String objectName = getObjectName(picUrl);
                        imgConvertUtil.saveToMinio(cloudUrl + picUrl, bucketName, objectName);
                    }
                }
            } else {
                for (String e : fieIdList) {
                    String url = jsonObject.getString(e);
                    if (StrUtil.isNotEmpty(url)) {
                        String bucketName = getBucketName(url);
                        String objectName = getObjectName(url);
                        imgConvertUtil.saveToMinio(cloudUrl + url, bucketName, objectName);
                    }
                }
            }
        }
    }

    public byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    private String getBucketName(String url) {
        //获取存储桶
        String replace = url.replace(ImageHandleUtil.URL, "");
        int index = replace.indexOf("/");
        return replace.substring(0, index);
    }

    private String getObjectName(String url) {
        //获取存储桶
        String replace = url.replace(ImageHandleUtil.URL, "");
        int index = replace.indexOf("/");
        return replace.substring(index + 1);
    }
}
