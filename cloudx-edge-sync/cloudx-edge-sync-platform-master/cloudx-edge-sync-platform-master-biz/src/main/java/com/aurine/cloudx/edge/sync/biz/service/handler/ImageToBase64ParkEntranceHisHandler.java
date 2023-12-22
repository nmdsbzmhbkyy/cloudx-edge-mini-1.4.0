package com.aurine.cloudx.edge.sync.biz.service.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.common.componments.chain.AbstractHandler;
import com.aurine.cloudx.edge.sync.common.componments.chain.annotation.ChainHandler;
import com.aurine.cloudx.edge.sync.common.constant.PublicConstants;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.utils.ImageBase64ConvertUtils;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.origin.entity.ProjectParkEntranceHis;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片转Base64
 *
 * @Author: zy
 * @Date: 2022-09-30 10:02:29
 * @Remarks: 车行记录
 **/
@ChainHandler(chainClass = ImageToBase64Chain.class)
@Component
@Slf4j
public class ImageToBase64ParkEntranceHisHandler extends AbstractHandler<TaskInfoDto> {
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

        log.info("[边缘网关同步服务] ImageToBase64车行记录图片数据转换");
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
        Map<String, String> base64Map = new HashMap<>();
        if (StrUtil.isNotEmpty(outPicUrl)) {
            try {
                String base64Code = ImageBase64ConvertUtils.urlToFullBase64(Constants.imgUriPrefix + outPicUrl);
                base64Map.put("outPicUrl", base64Code);

            } catch (Exception e) {
                e.printStackTrace();
                jsonObj.put("outPicUrl", null);
            }
        }
        if (StrUtil.isNotEmpty(enterPicUrl)) {
            try {
                String base64Code = ImageBase64ConvertUtils.urlToFullBase64(Constants.imgUriPrefix + enterPicUrl);
                base64Map.put("enterPicUrl", base64Code);
            } catch (Exception e) {
                e.printStackTrace();
                jsonObj.put("enterPicUrl", null);
            }
        }
        jsonObj.put(PublicConstants.PIC_BASE64_KEY, base64Map);
        handleEntity.setData(jsonObj.toString());

        return true;
    }
}
