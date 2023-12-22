
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.common.entity.vo.HouseDesignVo;
import com.aurine.cloudx.open.origin.entity.ProjectHouseDesign;
import com.aurine.cloudx.open.origin.mapper.ProjectHouseDesignMapper;
import com.aurine.cloudx.open.origin.service.ProjectHouseDesignService;
import com.aurine.cloudx.open.origin.service.ProjectHouseInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 项目户型配置表
 *
 * @author pigx code generator
 * @date 2020-05-06 15:22:42
 */
@Service
public class ProjectHouseDesignServiceImpl extends ServiceImpl<ProjectHouseDesignMapper, ProjectHouseDesign>
        implements ProjectHouseDesignService {

    @Resource
    private ProjectHouseInfoService projectHouseInfoService;

    @Resource
    private ProjectHouseDesignMapper projectHouseDesignMapper;


    /**
     * 添加户型
     *
     * @param houseDesign
     * @return
     * @author: 黄阳光
     */
    @Override
    public boolean add(ProjectHouseDesign houseDesign) {
        //判断户型描述是否重复
//        QueryWrapper<ProjectHouseDesign> query = new QueryWrapper<>();
//        query.eq("desginDesc", houseDesign.getDesginDesc());
//        ProjectHouseDesign result = baseMapper.selectOne(query);


//        if (result != null) {
//            //判断面积是否相等
//            if (result.getArea().compareTo(houseDesign.getArea()) == 0) {
//                //相等，抛出异常
//                throw new RuntimeException("户型名称与户型面积重复！");
//            }
//        }
//        boolean complete = baseMapper.insert(houseDesign) == 1;
//        if (!complete) {
//            throw new RuntimeException("未知错误，请联系管理员");
//        }
//        return complete;

        // edit by 王伟
//        int disCount = count(new QueryWrapper<ProjectHouseDesign>().lambda()
//                .eq(ProjectHouseDesign::getProjectId, ProjectContextHolder.getProjectId())
//                .eq(ProjectHouseDesign::getDesginDesc, houseDesign.getDesginDesc())
//                .eq(ProjectHouseDesign::getArea, houseDesign.getArea())
//        );
//        if (disCount >= 1) {
//            throw new RuntimeException("已存在该户型，请重新调整户型或户型面积");
//        }

        return save(houseDesign);
    }

    @Override
    public boolean delete(String houseDesignId) {
        //验证是否已使用，已被使用禁止删除
        if (projectHouseInfoService.checkHouseDesignExist(houseDesignId)) {
            throw new RuntimeException("当前户型已被使用，无法删除");
        }

        return this.removeById(houseDesignId);
    }

    @Override
    public boolean update(ProjectHouseDesign projectHouseDesign) {
        int disCount = count(new QueryWrapper<ProjectHouseDesign>().lambda()
                .eq(ProjectHouseDesign::getProjectId, ProjectContextHolder.getProjectId())
                .eq(ProjectHouseDesign::getDesginDesc, projectHouseDesign.getDesginDesc())
                .eq(ProjectHouseDesign::getArea, projectHouseDesign.getArea())
                .ne(ProjectHouseDesign::getDesignId, projectHouseDesign.getDesignId())
        );
        if (disCount >= 1) {
//            throw new RuntimeException("seq" + projectHouseDesign.getSeq() + " 在本区域中已存在");
            throw new RuntimeException("已存在该户型，请重新调整户型或户型面积");
        }
        return updateById(projectHouseDesign);
    }


    /**
     * 获取首个户型
     *
     * @param projectId
     * @return
     */
    @Override
    public ProjectHouseDesign getTopOne(int projectId) {
        return this.baseMapper.getTopOne(projectId);
    }

    @Override
    public Page<HouseDesignVo> page(Page page, HouseDesignVo vo) {
        ProjectHouseDesign po = new ProjectHouseDesign();
        BeanUtils.copyProperties(vo, po);

        return projectHouseDesignMapper.page(page, po);
    }
}
