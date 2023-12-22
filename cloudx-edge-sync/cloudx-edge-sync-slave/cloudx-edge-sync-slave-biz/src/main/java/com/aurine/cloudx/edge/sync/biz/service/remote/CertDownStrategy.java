package com.aurine.cloudx.edge.sync.biz.service.remote;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.cert.remote.RemoteCertAdownService;
import com.aurine.cloudx.estate.cert.vo.CertAdownRequestVO;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategy;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategyFactory;
import com.aurine.cloudx.edge.sync.common.constant.PublicConstants;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.common.core.constant.enums.CommandTypeEnum;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/03/03 8:50
 * @Package: com.aurine.cloudx.edge.sync.biz.service.remote
 * @Version: 1.0
 * @Remarks: 凭证下发，只在 slave 端和 platform-master 端有此操作
 **/
@Slf4j
@Component
public class CertDownStrategy implements CommandStrategy {

    @Resource
    private RemoteCertAdownService remoteCertAdownService;

    @Override
    public OpenRespVo doHandler(TaskInfoDto taskInfoDto) {
        // 调用下发服务
        List<CertAdownRequestVO> certAdownRequestVOList = JSONObject.parseArray(JSONObject.parseObject(taskInfoDto.getData()).getString("certData"), CertAdownRequestVO.class);
        try {
            Object r = remoteCertAdownService.requestList(certAdownRequestVOList, PublicConstants.FROM_IN);
            R res = new R();
            BeanUtil.copyProperties(r, res);
            return new OpenRespVo(null, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CommandStrategyFactory.registerStrategy(CommandTypeEnum.DOWN.name, this);
        CommandStrategyFactory.registerStrategy(CommandTypeEnum.DELETE.name, this);
    }
}
