package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.ProjectPromotionConfFormVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfOnFeeIdVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfPageVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectPromotionConf;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 优惠活动设置(ProjectPromotionConf)表服务接口
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
public interface ProjectPromotionConfService extends IService<ProjectPromotionConf> {

    /**
     * 保存优惠配置
     * @param projectPromotionConfVo 优惠配置视图
     * @return R
     */
    R savePromotionConf(ProjectPromotionConfVo projectPromotionConfVo);

    /**
     * 保存优惠配置
     * @param projectPromotionConfVo 优惠配置视图
     * @return R
     */
    R updatePromotionConf(ProjectPromotionConfVo projectPromotionConfVo);

    /**
     * 删除优惠配置
     * @param id 优惠表id
     * @return 是否删除
     */
    boolean removePromotionConf(String id);

    /**
     * 分页查询
     * @param page 分页
     * @param projectPromotionConf 优惠查询视图
     * @return 分页信息
     */
    IPage pageByForm(Page<ProjectPromotionConfPageVo> page, ProjectPromotionConfFormVo projectPromotionConf);



    /**
     * 根据类型查询优惠列表
     *
     * @return 优惠信息列表
     **/
    List<ProjectPromotionConfOnFeeIdVo> listConfByType(List<String> types);

    /**
     * 根据类型查询优惠列表
     *
     * @return 优惠信息列表
     **/
    List<ProjectPromotionConfOnFeeIdVo> listConfById(List<String> ids);


    /**
     * 获取优惠配置信息
     * @param id id
     * @return 优惠配置信息
     */
    ProjectPromotionConfVo getPromotion(String id);
}