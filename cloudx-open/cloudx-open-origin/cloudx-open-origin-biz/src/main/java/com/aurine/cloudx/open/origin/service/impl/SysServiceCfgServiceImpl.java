
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.vo.SysServiceCfgVo;
import com.aurine.cloudx.open.origin.mapper.SysServiceCfgMapper;
import com.aurine.cloudx.open.origin.entity.SysServiceCfg;
import com.aurine.cloudx.open.origin.service.SysServiceCfgService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统增值服务配置
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:16:57
 */
@Service
public class SysServiceCfgServiceImpl extends ServiceImpl<SysServiceCfgMapper, SysServiceCfg> implements SysServiceCfgService {

    @Override
    public List<SysServiceCfgVo> selectValueadd() {
        List<SysServiceCfgVo> sysServiceCfgVos = baseMapper.selectValueadd();
        sysServiceCfgVos.forEach(e -> {
            String valueaddName = e.getValueaddName();
            List<SysServiceCfg> manufacturerList = super.list(Wrappers.lambdaQuery(SysServiceCfg.class)
                    .eq(SysServiceCfg::getServiceName, valueaddName));
            e.setManufacturerList(manufacturerList);
/*            String manufacturers = e.getManufacturers();
            String[] manufacturerArr = manufacturers.split(",");
            if (ArrayUtil.isNotEmpty(manufacturerArr)) {
                //厂商列表
                List<String> manufacturerList = new ArrayList<>();
                for (String manufacturer : manufacturerArr) {
                    manufacturerList.add(manufacturer);
                    e.setManufacturerList(manufacturerList);
                }
            }*/
        });
        return sysServiceCfgVos;
    }
}
