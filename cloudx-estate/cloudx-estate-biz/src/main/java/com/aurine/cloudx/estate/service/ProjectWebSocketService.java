

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectAlarmHandle;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 *
 *
 * @author 黄阳光
 * @date 2020-06-04 08:31:21
 */
public interface ProjectWebSocketService{


    R findNumByProjectId();

}
