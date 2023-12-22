package com.aurine.cloudx.edge.sync.biz.service.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.componments.chain.AbstractHandler;
import com.aurine.cloudx.edge.sync.common.componments.chain.annotation.ChainHandler;
import com.aurine.cloudx.edge.sync.common.constant.PublicConstants;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.utils.ImgConvertUtil;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.origin.entity.ProjectSnapRecord;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Base64转图片
 *
 * @Author: wrm
 * @Date: 2022/3/2 16:03
 * @Version: 1.0
 * @Remarks: 抓拍记录
 **/
@ChainHandler(chainClass = Base64ToImageChain.class)
@Component
@Slf4j
public class Base64ToImageSnapRecordHandler extends AbstractHandler<TaskInfoDto> {
    @Resource
    private ImgConvertUtil imgConvertUtil;
    /**
     * handler校验函数，不满足该函数则跳过处理
     *
     * @param handleEntity
     * @return
     */
    @Override
    public boolean filter(TaskInfoDto handleEntity) {

        if (!ServiceNameEnum.SNAP_RECORD.name.equals(handleEntity.getServiceName())) {
            return false;
        }

        log.info("[边缘网关同步服务] Base64ToImage人员信息图片数据转换");

        return true;
    }


    /**
     * 执行方法，执行后返回调用next继续下一组handle，调用done则结束处理
     *
     * @param handleEntity
     */
    @SneakyThrows
    @Override
    public boolean doHandle(TaskInfoDto handleEntity) {
        JSONObject jsonObj = JSONObject.parseObject(handleEntity.getData());
        String snapSmalImage = JSONObject.toJavaObject(jsonObj, ProjectSnapRecord.class).getSnapSmalImage();
        String snapBigImage = JSONObject.toJavaObject(jsonObj, ProjectSnapRecord.class).getSnapBigImage();

        Map map = jsonObj.getObject(PublicConstants.PIC_BASE64_KEY, Map.class);
        //base64
        Object snapSmalImageBase64 = map.get("snapSmalImage");
        Object snapBigImageBase64 = map.get("snapBigImage");

        if(StrUtil.isNotEmpty(snapSmalImage)){
            imgConvertUtil.base64ToMinio(snapSmalImageBase64.toString(), snapSmalImage);
        }
        if(StrUtil.isNotEmpty(snapBigImage)){
            imgConvertUtil.base64ToMinio(snapBigImageBase64.toString(), snapBigImage);
        }
        jsonObj.remove(PublicConstants.PIC_BASE64_KEY);
        handleEntity.setData(jsonObj.toString());

        return true;
    }
}
