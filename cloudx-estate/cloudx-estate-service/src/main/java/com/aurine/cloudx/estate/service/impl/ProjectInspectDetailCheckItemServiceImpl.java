package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.entity.ProjectDeviceMonitorConf;
import com.aurine.cloudx.estate.entity.ProjectInspectDetailCheckItem;
import com.aurine.cloudx.estate.entity.ProjectInspectDetailDevice;
import com.aurine.cloudx.estate.mapper.ProjectInspectDetailCheckItemMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceMonitorConfService;
import com.aurine.cloudx.estate.service.ProjectInspectDetailCheckItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 巡检任务明细检查项列表(ProjectInspectDetailCheckItem)表服务实现类
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:12
 */
@Service
public class ProjectInspectDetailCheckItemServiceImpl extends ServiceImpl<ProjectInspectDetailCheckItemMapper,
        ProjectInspectDetailCheckItem> implements ProjectInspectDetailCheckItemService {

    @Resource
    ProjectDeviceMonitorConfService projectDeviceMonitorConfService;

    @Override
    public boolean initDetailCheckItem(List<ProjectInspectDetailDevice> detailDeviceList) {
        List<ProjectInspectDetailCheckItem> checkItemList = new ArrayList<>();
        detailDeviceList.forEach(detailDevice -> {
            String deviceId = detailDevice.getDeviceId();
            List<ProjectDeviceMonitorConf> monitorConfList = projectDeviceMonitorConfService.listCheckItemListByDeviceId(deviceId);
            monitorConfList.forEach(monitorConf -> {
                ProjectInspectDetailCheckItem checkItem = new ProjectInspectDetailCheckItem();
                checkItem.setCheckItemName(monitorConf.getMonitorName());
                checkItem.setRemark(monitorConf.getRemark());
                checkItem.setOperator(0);
                checkItem.setDeviceDetailId(detailDevice.getDeviceDetailId());
                checkItemList.add(checkItem);
            });
        });
        if (CollUtil.isNotEmpty(checkItemList)) {
            return this.saveBatch(checkItemList);
        }
        return true;
    }

    @Override
    public boolean removeDetailCheckItemByDetailDeviceIdList(List<String> detailDeviceIdList) {
        return this.remove(new QueryWrapper<ProjectInspectDetailCheckItem>().lambda()
                .in(ProjectInspectDetailCheckItem::getDeviceDetailId, detailDeviceIdList));
    }

}