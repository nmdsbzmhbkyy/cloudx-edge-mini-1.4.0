package com.aurine.cloudx.edge.sync.biz.service.parkingremote;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategy;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.common.core.constant.enums.CommandTypeEnum;
import com.aurine.parking.feign.RemoteProjectParkingService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 对第三方车场新增数据
 *
 * @author zouyu
 * @date 2022-08-10 15:29:56
 */
@Slf4j
@Component
public class ParkingInfoAddStrategy implements CommandStrategy {

    @Resource
    private RemoteProjectParkingService remoteProjectParkingService;

    @Override
    public OpenRespVo doHandler(TaskInfoDto taskInfoDto) {
        //调用第三方服务
        try {
            log.info("[车场入云] {} 开始调用第三方的接口:{}",CommandTypeEnum.PARKING_INFO_INSERT.desc,taskInfoDto);
            JSONObject jsonObject = JSONObject.parseObject(taskInfoDto.getData());
            R r = remoteProjectParkingService.parkingInfoAdd(jsonObject, SecurityConstants.FROM_IN);
            return new OpenRespVo(null, r);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        CommandStrategyFactory.registerStrategy(CommandTypeEnum.PARKING_INFO_INSERT.name, this);
    }
}
