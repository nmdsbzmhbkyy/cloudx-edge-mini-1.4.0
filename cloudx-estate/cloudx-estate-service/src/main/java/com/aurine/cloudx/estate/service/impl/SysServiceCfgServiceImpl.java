
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.entity.SysServiceDeviceClassify;
import com.aurine.cloudx.estate.mapper.SysServiceCfgMapper;
import com.aurine.cloudx.estate.service.SysServiceCfgService;
import com.aurine.cloudx.estate.service.SysServiceDeviceClassifyService;
import com.aurine.cloudx.estate.vo.SysServiceCfgClassifyVo;
import com.aurine.cloudx.estate.vo.SysServiceCfgVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Override
    public Boolean getWJYStatus(Integer projectId) {
        List<SysServiceCfg> manufacturerList = super.list(Wrappers.lambdaQuery(SysServiceCfg.class)
                .eq(SysServiceCfg::getServiceType, "CW")
                .eq(SysServiceCfg::getIsActive, "1"));
        return manufacturerList.size() > 0;
    }
}
