package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.constant.KafkaConstant;
import com.aurine.cloudx.edge.sync.biz.mq.enums.TypeEnum;
import com.aurine.cloudx.edge.sync.biz.mq.kafka.KafkaProducer;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategy;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.common.core.constant.enums.CommandTypeEnum;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 设备删除指令
 *
 * @author：zy
 * @data: 2022-08-23 09:07:18
 */
@Slf4j
@Component
public class DeviceInfoDeleteStrategy implements CommandStrategy {


    @Override
    public OpenRespVo doHandler(TaskInfoDto taskInfoDto) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", TypeEnum.DEVICE_DELETE.value);
        jsonObject.put("data", taskInfoDto.getData());
        jsonObject.put("projectUUID",taskInfoDto.getProjectUUID());
        KafkaProducer.sendMessage(KafkaConstant.EDGE_INTERCOM_TOPIC,jsonObject);
        try {
            R res = new R();
            res.setCode(0);
            return new OpenRespVo(null, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CommandStrategyFactory.registerStrategy(CommandTypeEnum.DELETE_DEVICE.name, this);
    }
}
