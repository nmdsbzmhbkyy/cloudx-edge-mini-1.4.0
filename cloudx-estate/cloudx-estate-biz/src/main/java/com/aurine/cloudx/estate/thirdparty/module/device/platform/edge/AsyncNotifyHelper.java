package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Data
@Service
public class AsyncNotifyHelper {
    private Map<String,Object> asyncNotifyMap=new HashMap<>();
}
