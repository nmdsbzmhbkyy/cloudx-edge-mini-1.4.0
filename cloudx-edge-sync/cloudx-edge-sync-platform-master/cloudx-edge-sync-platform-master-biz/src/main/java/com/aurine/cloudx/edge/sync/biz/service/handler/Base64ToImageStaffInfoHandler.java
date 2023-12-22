package com.aurine.cloudx.edge.sync.biz.service.handler;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.componments.chain.AbstractHandler;
import com.aurine.cloudx.edge.sync.common.componments.chain.annotation.ChainHandler;
import com.aurine.cloudx.edge.sync.common.constant.PublicConstants;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.edge.sync.common.utils.ImgConvertUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Base64转图片
 *
 * @Author: wrm
 * @Date: 2022/3/2 16:03
 * @Version: 1.0
 * @Remarks: 员工信息
 **/
@ChainHandler(chainClass = Base64ToImageChain.class)
@Component
@Slf4j
public class Base64ToImageStaffInfoHandler extends AbstractHandler<TaskInfoDto> {
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

        if (!ServiceNameEnum.STAFF_INFO.name.equals(handleEntity.getServiceName())) {
            return false;
        }

        log.info("[边缘网关同步服务] Base64ToImage员工信息图片数据转换");

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
        String base64Code = jsonObj.getString(PublicConstants.PIC_BASE64_KEY);
        String url = jsonObj.getString("picUrl");
        if (base64Code != null) {
            String picUrl = imgConvertUtil.base64ToMinio(base64Code, url);
            jsonObj.remove(PublicConstants.PIC_BASE64_KEY);
            jsonObj.put("picUrl", picUrl);
            handleEntity.setData(jsonObj.toString());
        }
        return true;
    }
}
