package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.open.origin.mapper.EdgeCascadeConfMapper;
import com.aurine.cloudx.open.origin.entity.EdgeCascadeConf;
import com.aurine.cloudx.open.origin.service.EdgeCascadeConfService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>项目入云连接码服务</p>
 * @author : 王良俊
 * @date : 2021-12-02 17:34:11
 */
@Slf4j
@Service
public class EdgeCascadeConfServiceImpl extends ServiceImpl<EdgeCascadeConfMapper, EdgeCascadeConf> implements EdgeCascadeConfService {

    @Resource
    EdgeCascadeConfMapper edgeCascadeConfMapper;
    @Override
    public String genCloudConnectCode(Integer projectId) {
        EdgeCascadeConf conf;
        List<EdgeCascadeConf> confList = this.list(new LambdaQueryWrapper<EdgeCascadeConf>().eq(EdgeCascadeConf::getProjectId, projectId)
                .orderByAsc(EdgeCascadeConf::getCreateTime));
        if (CollUtil.isNotEmpty(confList)) {
            conf = confList.get(0);
            if (confList.size() > 1) {
                this.removeByIds(confList.subList(1, confList.size()).stream().map(EdgeCascadeConf::getConfId).collect(Collectors.toList()));
            }
        } else {
            conf = new EdgeCascadeConf();
            conf.setProjectId(projectId);
        }
        if (StrUtil.isEmpty(conf.getConnectCode())) {
            conf.setConnectCode(Integer.toUnsignedString(projectId, 36).toUpperCase());
            log.info("[入云码生成] 入云码信息：{}", conf);
            this.save(conf);
        }
        return conf.getConnectCode();
    }

    @Override
    public String getCloudConnectCode(Integer projectId) {
        EdgeCascadeConf conf = this.getOne(new LambdaQueryWrapper<EdgeCascadeConf>().eq(EdgeCascadeConf::getProjectId, projectId).orderByAsc(EdgeCascadeConf::getCreateTime).last("limit 1"));
        if (conf == null) {
            return this.genCloudConnectCode(projectId);
        }
        return conf.getConnectCode();
    }

    @Override
    public Integer getProjectId(String connectCode) {
        EdgeCascadeConf conf = this.getOne(new LambdaQueryWrapper<EdgeCascadeConf>().eq(EdgeCascadeConf::getConnectCode, connectCode).last("limit 1"));
        return conf != null ? conf.getProjectId() : null;
    }

    @Override
    public void removeByProjectId(Integer projectId) {
        boolean remove = this.remove(new LambdaQueryWrapper<EdgeCascadeConf>().eq(EdgeCascadeConf::getProjectId, projectId));
        if (remove) {
            log.info("[入云连接码] 项目{}连接码删除成功", projectId);
        } else {
            log.error("[入云连接码] 项目{}连接码删除失败", projectId);
        }
    }
}

