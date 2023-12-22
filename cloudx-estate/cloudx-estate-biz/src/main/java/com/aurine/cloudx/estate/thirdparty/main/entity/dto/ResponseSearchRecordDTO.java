package com.aurine.cloudx.estate.thirdparty.main.entity.dto;

import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import lombok.Data;

import java.util.*;

/**
 * @description: 查询数据结果 DTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */

@Data
public class ResponseSearchRecordDTO extends BaseDTO {

    /**
     * 设备列表
     */
    List<ProjectDeviceInfoProxyVo> deviceList;
}
