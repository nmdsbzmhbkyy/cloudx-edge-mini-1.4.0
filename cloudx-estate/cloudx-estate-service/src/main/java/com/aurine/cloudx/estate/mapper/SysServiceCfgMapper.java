

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.vo.SysServiceCfgVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统增值服务配置
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:16:57
 */
@Mapper
public interface SysServiceCfgMapper extends BaseMapper<SysServiceCfg> {

    /**
     * 查询平台中的增值服务列表
     *
     * @return
     */
    List<SysServiceCfgVo> selectValueadd();

}
