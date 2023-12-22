
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectHouseDesign;
import com.aurine.cloudx.estate.mapper.ProjectHouseDesignMapper;
import com.aurine.cloudx.estate.service.ProjectHouseDesignService;
import com.aurine.cloudx.estate.service.ProjectHouseInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 项目户型配置表
 *
 * @author pigx code generator
 * @date 2020-05-06 15:22:42
 */
@Service
public class ProjectHouseDesignServiceImpl extends ServiceImpl<ProjectHouseDesignMapper, ProjectHouseDesign>
        implements ProjectHouseDesignService {

    @Autowired
    private ProjectHouseInfoService projectHouseInfoService;

    /**
     * 添加户型
     *
     * @param houseDesign
     * @return
     * @author: 黄阳光
     */
    @Override
    public Boolean add(ProjectHouseDesign houseDesign) {
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
        int disCount = count(new QueryWrapper<ProjectHouseDesign>().lambda()
                .eq(ProjectHouseDesign::getProjectId, ProjectContextHolder.getProjectId())
                .eq(ProjectHouseDesign::getDesginDesc, houseDesign.getDesginDesc())
                .eq(ProjectHouseDesign::getArea, houseDesign.getArea())
        );
        if (disCount >= 1) {
            throw new RuntimeException("已存在该户型，请重新调整户型或户型面积");
        }
        return save(houseDesign);
    }

    @Override
    public Boolean update(ProjectHouseDesign projectHouseDesign) {
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

    @Override
    public Boolean delete(String houseDesignId) {
        //验证是否已使用，已被使用禁止删除
        if (projectHouseInfoService.checkHouseDesignExist(houseDesignId)) {
            throw new RuntimeException("当前户型已被使用，无法删除");
        }
        return this.removeById(houseDesignId);
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
}
