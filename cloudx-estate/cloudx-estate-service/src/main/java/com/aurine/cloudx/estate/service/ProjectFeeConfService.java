package com.aurine.cloudx.estate.service;
import com.aurine.cloudx.estate.entity.ProjectFeeConf;
import com.aurine.cloudx.estate.vo.ProjectFeeConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 费用设置(ProjectFeeConf)表服务接口
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
public interface ProjectFeeConfService extends IService<ProjectFeeConf> {
    /**
     * 分页查询费用
     * @param page 分页
     * @param projectFeeConf 查询条件
     * @return 分页费用列表
     */
    IPage<ProjectFeeConfVo> pageFee(Page<ProjectFeeConfVo> page, ProjectFeeConf projectFeeConf);

    /**
     * 查询所有费用列表
     * @return 费用列表
     */
    List<ProjectFeeConfVo> listFee(String type,String status);


}