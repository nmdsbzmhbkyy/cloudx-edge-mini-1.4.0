

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.SysServiceCfgVo;
import com.aurine.cloudx.open.origin.entity.SysServiceCfg;
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

}
