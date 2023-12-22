package com.aurine.cloudx.edge.sync.biz.util;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.entity.vo.HandleImagesVo;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.pig4cloud.pigx.common.minio.service.MinioTemplate;
import io.minio.ObjectStat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

/**
 * @author:zy
 * @data:2022/12/16 3:50 下午
 */
@Component
@Slf4j
@EnableScheduling
public class ImageScheduleTask {

    @Resource
    private MinioTemplate minioTemplate;

    @Resource
    private HttpConnectorUtil httpConnectorUtil;

    @Value("${cloud-url}")
    private String cloudUrl;

    private Integer count = 0;

    @Resource
    private ImageHandleUtil imageHandleUtil;

    /**
     * 处理图片任务 3秒
     */
    @Scheduled(fixedDelay = 5000)
    private void scheduled3(){
        try {
            long size = RedisUtil.lGetListSize(ImageHandleUtil.KEY);
            if(size == 0){
                return;
            }
            count++;
            List<Object> urlList = RedisUtil.lGet(ImageHandleUtil.KEY, 0, -1);
            log.info("[图片处理] 上传给云端的图片urlList:{}",JSONObject.toJSONString(urlList));
            for (Object o : urlList) {
                String url = o.toString();
                //获取存储桶和文件路径
                String replace = url.replace(ImageHandleUtil.URL, "");
                int index = replace.indexOf("/");
                String bucketName = replace.substring(0,index);
                String objectName = replace.substring(index + 1);

                //从minio取出图片流
                InputStream inputStream = minioTemplate.getObject(bucketName, objectName);
                byte[] byteArray = imageHandleUtil.toByteArray(inputStream);

                //获取文件大小
                ObjectStat objectInfo = minioTemplate.getObjectInfo(bucketName, objectName);
                long length = objectInfo.length();

                //传输对象
                HandleImagesVo handleImagesVo = new HandleImagesVo();
                handleImagesVo.setBucketName(bucketName);
                handleImagesVo.setObjectName(objectName);
                handleImagesVo.setByteArray(byteArray);
                handleImagesVo.setLength(length);

                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(handleImagesVo));

                //http请求云端接口保存图片
                JSONObject result = httpConnectorUtil.post(cloudUrl + "/estate/projectConnectRequest/handleImage", jsonObject, null);
                boolean bool = result.getBooleanValue("data");
                if(bool){
                    RedisUtil.leftPopKey(ImageHandleUtil.KEY);
                    count = 0;
                }
            }
        } catch (Exception e) {
            log.info("[图片处理] 出现异常:{} 处理次数:{}",e.getMessage(),count);
            if(count >= 3){
                RedisUtil.leftPopKey(ImageHandleUtil.KEY);
                log.info("[图片处理] 重试次数:{} 跳过这个图片文件",count);
                count = 0;
            }
        }
    }
}
