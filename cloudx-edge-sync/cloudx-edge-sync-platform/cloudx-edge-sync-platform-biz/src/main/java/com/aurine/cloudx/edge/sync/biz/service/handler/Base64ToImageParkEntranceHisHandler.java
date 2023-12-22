package com.aurine.cloudx.edge.sync.biz.service.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.componments.chain.AbstractHandler;
import com.aurine.cloudx.edge.sync.common.componments.chain.annotation.ChainHandler;
import com.aurine.cloudx.edge.sync.common.constant.PublicConstants;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.utils.ImgConvertUtil;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.origin.entity.ProjectParkEntranceHis;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Base64转图片
 *
 * @Author: zy
 * @Date: 2022-09-30 10:15:58
 * @Version: 1.0
 * @Remarks: 车行记录
 **/
@ChainHandler(chainClass = Base64ToImageChain.class)
@Component
@Slf4j
public class Base64ToImageParkEntranceHisHandler extends AbstractHandler<TaskInfoDto> {
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

        if (!ServiceNameEnum.PARK_ENTRANCE_HIS.name.equals(handleEntity.getServiceName())) {
            return false;
        }

        log.info("[边缘网关同步服务] Base64ToImage车行记录图片数据转换");

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
        String outPicUrl = JSONObject.toJavaObject(jsonObj, ProjectParkEntranceHis.class).getOutPicUrl();
        String enterPicUrl = JSONObject.toJavaObject(jsonObj, ProjectParkEntranceHis.class).getEnterPicUrl();

        Map map = jsonObj.getObject(PublicConstants.PIC_BASE64_KEY, Map.class);
        //base64
        Object outPicUrlBase64 = map.get("outPicUrl");
        Object enterPicUrlBase64 = map.get("enterPicUrl");

        if(StrUtil.isNotEmpty(outPicUrl)){
            imgConvertUtil.base64ToMinio(outPicUrlBase64.toString(), outPicUrl);
        }
        if(StrUtil.isNotEmpty(enterPicUrl)){
            imgConvertUtil.base64ToMinio(enterPicUrlBase64.toString(), enterPicUrl);
        }
        jsonObj.remove(PublicConstants.PIC_BASE64_KEY);
        handleEntity.setData(jsonObj.toString());

        return true;
    }
}
