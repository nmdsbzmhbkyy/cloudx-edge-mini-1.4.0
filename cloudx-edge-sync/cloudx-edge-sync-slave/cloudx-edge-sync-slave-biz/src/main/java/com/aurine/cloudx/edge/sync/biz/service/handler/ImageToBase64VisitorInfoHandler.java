package com.aurine.cloudx.edge.sync.biz.service.handler;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.common.componments.chain.AbstractHandler;
import com.aurine.cloudx.edge.sync.common.componments.chain.annotation.ChainHandler;
import com.aurine.cloudx.edge.sync.common.constant.PublicConstants;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.utils.ImageBase64ConvertUtils;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.origin.entity.ProjectVisitor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 图片转Base64
 *
 * @Author: wrm
 * @Date: 2022/3/2 16:07
 * @Version: 1.0
 * @Remarks: 访客信息
 **/
@ChainHandler(chainClass = ImageToBase64Chain.class)
@Component
@Slf4j
public class ImageToBase64VisitorInfoHandler extends AbstractHandler<TaskInfoDto> {
    /**
     * handler校验函数，不满足该函数则跳过处理
     *
     * @param handleEntity
     * @return
     */
    @Override
    public boolean filter(TaskInfoDto handleEntity) {

        if (!ServiceNameEnum.VISITOR_INFO.name.equals(handleEntity.getServiceName())) {
            return false;
        }

        log.info("[边缘网关同步服务] {} ImageToBase64访客信息图片数据转换", handleEntity);

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
        String picUrl = JSONObject.toJavaObject(jsonObj, ProjectVisitor.class).getPicUrl();
        if (!StringUtils.isEmpty(picUrl)) {
            try {
                String base64Code = ImageBase64ConvertUtils.urlToFullBase64(Constants.imgUriPrefix + picUrl);
                jsonObj.put(PublicConstants.PIC_BASE64_KEY, base64Code);
            } catch (Exception e) {
                e.printStackTrace();
                jsonObj.put("picUrl", null);
            }
            handleEntity.setData(jsonObj.toString());
        }
        return true;
    }
}
