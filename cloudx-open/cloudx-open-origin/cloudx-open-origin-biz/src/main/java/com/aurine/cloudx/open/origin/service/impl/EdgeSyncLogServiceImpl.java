package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.entity.EdgeSyncLog;
import com.aurine.cloudx.open.origin.mapper.EdgeSyncLogMapper;
import com.aurine.cloudx.open.origin.service.EdgeSyncLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EdgeSyncLogServiceImpl extends ServiceImpl<EdgeSyncLogMapper, EdgeSyncLog> implements EdgeSyncLogService {
}
