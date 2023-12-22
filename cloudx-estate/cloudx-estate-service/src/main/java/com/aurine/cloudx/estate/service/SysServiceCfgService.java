

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.vo.SysServiceCfgClassifyVo;
import com.aurine.cloudx.estate.vo.SysServiceCfgVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统增值服务配置
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:16:57
 */
public interface SysServiceCfgService extends IService<SysServiceCfg> {

    /**
     * 查询平台中的增值服务列表
     *
     * @return
     */
    List<SysServiceCfgVo> selectValueadd();

    /**
     * 获取财务我家云服务的启用状态
     */
    Boolean getWJYStatus (Integer projectId);

}
