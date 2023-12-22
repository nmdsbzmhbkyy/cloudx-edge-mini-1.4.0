
package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.open.common.entity.vo.UnitInfoVo;
import com.aurine.cloudx.open.origin.constant.enums.FrameTypeEnum;
import com.aurine.cloudx.open.origin.entity.ProjectFrameInfo;
import com.aurine.cloudx.open.origin.entity.ProjectUnitInfo;
import com.aurine.cloudx.open.origin.mapper.ProjectUnitInfoMapper;
import com.aurine.cloudx.open.origin.service.ProjectFrameInfoService;
import com.aurine.cloudx.open.origin.service.ProjectHouseInfoService;
import com.aurine.cloudx.open.origin.service.ProjectUnitInfoService;
import com.aurine.cloudx.open.origin.vo.ProjectUnitFileVo;
import com.aurine.cloudx.open.origin.vo.ProjectUnitInfoCountVo;
import com.aurine.cloudx.open.origin.vo.ProjectUnitInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单元
 *
 * @author 王伟
 * @date 2020-06-10 11:10:40
 */
@Service
public class ProjectUnitInfoServiceImpl extends ServiceImpl<ProjectUnitInfoMapper, ProjectUnitInfo> implements ProjectUnitInfoService {
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;

    /**
     * 保存单元极其框架信息
     *
     * @param unitInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUnit(ProjectUnitInfo unitInfo, String buildingId) {
        //init
//        String uid = UUID.randomUUID().toString().replaceAll("-", "");
//        unitInfo.setUnitId(uid);
        ProjectFrameInfo frameinfo = new ProjectFrameInfo();


        String unitFrameNo = StringUtils.replace(unitInfo.getUnitName(), "单元", "");


        String unitCode = null;
        //获取单元上一级的信息
        List<ProjectFrameInfo> unitList = projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().eq("entityId", buildingId));
        if (CollectionUtil.isNotEmpty(unitList)) {
            ProjectFrameInfo projectFrameInfo = unitList.get(0);
            //保存单元编码和框架编码
            unitCode = projectFrameInfo.getEntityCode() + unitFrameNo;
            unitInfo.setFrameNo(unitFrameNo);
            //设置单元框架信息
            frameinfo.setEntityCode(unitCode);
            frameinfo.setFrameNo(unitFrameNo);

            unitInfo.setUnitCode(unitCode);

        }
        //保存单元
        this.save(unitInfo);
        //保存单元框架
        frameinfo.setEntityId(unitInfo.getUnitId());
        frameinfo.setEntityName(unitInfo.getUnitName());
        frameinfo.setIsBuilding("0");
        frameinfo.setIsHouse("0");
        frameinfo.setIsUnit("1");
        frameinfo.setPuid(buildingId);

        //frameinfo.setEntityCode(unitInfo.getUnitCode());
        frameinfo.setLevel(FrameTypeEnum.UNIT.code);


        return projectFrameInfoService.save(frameinfo);
    }

    /**
     * 根据框架号和项目号，保存或修改单元信息
     *
     * @param projectUnitInfoVo
     */
    @Override
    public boolean saveOrUpdateByThirdCode(ProjectUnitInfoVo projectUnitInfoVo) {
        ProjectUnitInfoVo projectUnitInfo = this.baseMapper.getByCode(projectUnitInfoVo.getUnitCode(), projectUnitInfoVo.getProjectId());
        if (projectUnitInfo == null) {
            return this.baseMapper.saveUnit(projectUnitInfoVo) >= 1;
        } else {
            projectUnitInfoVo.setUnitId(projectUnitInfo.getUnitId());
            return this.updateById(projectUnitInfoVo);
        }
    }

    /**
     * 批量保存单元信息极其框架
     * 单元数据不会过多为前提的逻辑策略
     *
     * @param unitInfoList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchUnit(List<ProjectUnitInfo> unitInfoList, String buildingId) {
        for (ProjectUnitInfo unitInfo : unitInfoList) {
            this.saveUnit(unitInfo, buildingId);
        }
        return true;
    }

    /**
     * 保存修改极其框架信息
     *
     * @param unitInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUnit(ProjectUnitInfo unitInfo) {
        //修改单元数据
        this.updateById(unitInfo);

        //修改框架数据
        ProjectFrameInfo frameinfo = projectFrameInfoService.getById(unitInfo.getUnitId());
        frameinfo.setEntityName(unitInfo.getUnitName());
        return this.projectFrameInfoService.updateById(frameinfo);
    }

    /**
     * 批量修改单元信息极其框架
     *
     * @param unitInfoList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchUnit(List<ProjectUnitInfoVo> unitInfoList) {
        for (ProjectUnitInfoVo unitInfoVo : unitInfoList) {
            ProjectUnitInfo unitInfo = new ProjectUnitInfo();
            BeanUtil.copyProperties(unitInfoVo, unitInfo);
            //TODO:这里后面要讲图片url数组拆分成三个图片url
            unitInfo.setPicUrl1("");
            unitInfo.setPicUrl2("");
            unitInfo.setPicUrl3("");
            String[] imgArr = unitInfoVo.getImgArr();
            int i = 1;
//            String url = "http://localhost:8080";
            if (ArrayUtil.isNotEmpty(imgArr)) {
                for (String imgUrl : imgArr) {
                    switch (i) {
                        case 1:
                            unitInfo.setPicUrl1(imgUrl);
                            break;
                        case 2:
                            unitInfo.setPicUrl2(imgUrl);
                            break;
                        default:
                            unitInfo.setPicUrl3(imgUrl);
                    }
                    i++;
                }
            }
            this.updateUnit(unitInfo);
        }
        return true;
    }

    /**
     * 通过楼栋ID获取所属的单元
     *
     * @param buildingId
     * @return
     */
    @Override
    public List<ProjectUnitInfoVo> listUnit(String buildingId) {
        List<ProjectUnitInfo> untiList = new ArrayList<>();
        List<String> unitIdList;

        List<ProjectFrameInfo> frameList = this.projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda()
                .eq(ProjectFrameInfo::getLevel, FrameTypeEnum.UNIT.code)
                .eq(ProjectFrameInfo::getPuid, buildingId));

        unitIdList = frameList.stream().map(ProjectFrameInfo::getEntityId).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(frameList)) {
            untiList = list(new QueryWrapper<ProjectUnitInfo>().lambda().in(ProjectUnitInfo::getUnitId, unitIdList).orderByAsc(ProjectUnitInfo::getUnitName));
        }
        List<ProjectUnitInfoVo> unitInfoVoList = new ArrayList<>();
        untiList.forEach(s -> {
            ProjectUnitInfoVo projectUnitInfoVo = new ProjectUnitInfoVo();
            BeanUtil.copyProperties(s, projectUnitInfoVo);
            List<ProjectUnitFileVo> fileList = new ArrayList<>();
            ProjectUnitFileVo fileVo;
            // 前端回显图片列表
            if (StrUtil.isNotBlank(projectUnitInfoVo.getPicUrl1())) {
                fileVo = new ProjectUnitFileVo();
                fileVo.setUrl(projectUnitInfoVo.getPicUrl1());
                fileList.add(fileVo);
                if (StrUtil.isNotBlank(projectUnitInfoVo.getPicUrl2())) {
                    fileVo = new ProjectUnitFileVo();
                    fileVo.setUrl(projectUnitInfoVo.getPicUrl2());
                    fileList.add(fileVo);
                    if (StrUtil.isNotBlank(projectUnitInfoVo.getPicUrl3())) {
                        fileVo = new ProjectUnitFileVo();
                        fileVo.setUrl(projectUnitInfoVo.getPicUrl3());
                        fileList.add(fileVo);
                    }
                }
            }
            String[] imgArr = new String[fileList.size()];
            if (CollUtil.isNotEmpty(fileList)) {
                for (int i = 0; i < fileList.size(); i++) {
                    imgArr[i] = fileList.get(i).getUrl();
                }
            }
            projectUnitInfoVo.setImgArr(imgArr);
            projectUnitInfoVo.setFileList(fileList);

            unitInfoVoList.add(projectUnitInfoVo);
        });

        return unitInfoVoList;
    }

    /**
     * 根据楼栋ID删除单元极其框架数据
     *
     * @param buildingId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByBuildingId(String buildingId) {

        //检查名下是否存在楼栋
//        boolean haveHouse = projectFrameInfoService.checkBuildingHaveHouse(buildingId);
//
//        if (!haveHouse) {
//            return false;
//        }


        List<ProjectFrameInfo> unitFrameList = projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getPuid, buildingId));
        List<String> idList = unitFrameList.stream().map(ProjectFrameInfo::getEntityId).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(unitFrameList)) {
            return true;
        }

        //删除房屋
        for (String unitId : idList) {
            projectHouseInfoService.deleteHouseByUnitId(unitId);
        }

        projectFrameInfoService.removeByIds(idList);
        return removeByIds(idList);
    }

    @Override
    public List<ProjectUnitInfoCountVo> listUnitInfo(String buildingId) {
        return baseMapper.listUnitInfo(buildingId);
    }

    @Override
    public String getBuildingIdByUnitId(String unitId) {
        return projectFrameInfoService.getPuidByEntityId(unitId);
    }

    @Override
    public Page<UnitInfoVo> page(Page page, UnitInfoVo vo) {
        ProjectUnitInfo po = new ProjectUnitInfo();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }
}
