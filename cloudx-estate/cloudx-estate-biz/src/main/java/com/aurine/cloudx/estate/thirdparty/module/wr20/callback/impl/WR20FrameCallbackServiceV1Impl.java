package com.aurine.cloudx.estate.thirdparty.module.wr20.callback.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.TreeUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.BuildingUnitTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceRegion;
import com.aurine.cloudx.estate.entity.ProjectHouseDesign;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.business.entity.constant.ThirdPartyBusinessPlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.callback.WR20FrameCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20FrameObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.tree.FrameObjNode;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 框架数据回调
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-18 14:05
 * @Copyright:
 */
@Service
@Slf4j
public class WR20FrameCallbackServiceV1Impl implements WR20FrameCallbackService {
    @Resource
    private ProjectDockModuleConfService projectDockModuleConfService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;
    @Resource
    private ProjectUnitInfoService projectUnitInfoService;
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;
    @Resource
    private ProjectHouseDesignService projectHouseDesignService;
    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;

    /**
     * 同步框架，组团、楼栋、房屋、单元等
     * 已和3.0研发沟通，虽然有分页，但是同步接口直接一次性传输全部数据
     *
     * @param jsonObject
     * @param gatewayId
     * @return
     */
    @Override
    public void syncFrame(JSONObject jsonObject, String gatewayId) {
        //根据网关，获取项目信息
        ProjectDockModuleConfWR20Vo wr20Config = projectDockModuleConfService.getConfigByThirdCode(ThirdPartyBusinessPlatformEnum.WR20.code, gatewayId, ProjectDockModuleConfWR20Vo.class);
        ProjectContextHolder.setProjectId(wr20Config.getProjectId());
        log.info("[WR20] 开始同步项目：{} 的 框架数据 {}", wr20Config.getProjectId(), jsonObject);

        List<WR20FrameObj> frameJsonObjList = jsonObject.getJSONArray("list").toJavaList(WR20FrameObj.class);
        WR20FrameObj frameHouseObj = frameJsonObjList.stream().filter(e -> e.getFrameType() == 1).findFirst().get();

        //获取房间层级，既为最大框架层级
        int maxLevel = frameHouseObj.getLevelNo();

        //确定各层级对应,WR20最高层级为1，（社区为0，区口为99）

        //将数据转换为树结构
        FrameObjNode frameObjNode;
        List<FrameObjNode> frameObjNodeList = new ArrayList<>();
        for (WR20FrameObj WR20FrameObj : frameJsonObjList) {
            if (WR20FrameObj.getFrameType() == 99) {
                continue;
            }
            frameObjNode = new FrameObjNode();
            frameObjNode.setId(WR20FrameObj.getFrameNo());
            frameObjNode.setParentId(WR20FrameObj.getParentNo());
            frameObjNode.setCreateTime(WR20FrameObj.getCreateTime());
            frameObjNode.setFrameDesc(WR20FrameObj.getFrameDesc());
            frameObjNode.setFrameType(WR20FrameObj.getFrameType());
            frameObjNode.setLevelNo(WR20FrameObj.getLevelNo());
            frameObjNode.setFrameLevel(maxLevel - WR20FrameObj.getLevelNo() + 1);
            frameObjNodeList.add(frameObjNode);
        }
        List<FrameObjNode> frameObjTreeList = TreeUtil.build(frameObjNodeList, "-1");

        //将树递归导入项目中
        this.createOrUpdateFrames(frameObjTreeList, null, wr20Config.getProjectId());
        log.info("[WR20] 同步框架流程结束{}", gatewayId);
        ProjectContextHolder.clear();
    }


    /**
     * 递归同步框架数据
     *
     * @param frameJsonObjList
     */
    private void createOrUpdateFrames(List<FrameObjNode> frameJsonObjList, String puid, int projectId) {
        for (FrameObjNode frameObjNode : frameJsonObjList) {
            //写入数据
            String uid = UUID.randomUUID().toString().replaceAll("-", "");

            //房屋、单元、房屋 业务数据
            if (frameObjNode.getFrameLevel() == 3) {//楼栋
                log.info("[WR20] 楼栋：{}，{},{}", frameObjNode.getFrameLevel(), frameObjNode.getLevelNo(), frameObjNode.getFrameDesc());
                uid = projectFrameInfoService.saveOrUpdateFrameByEntityCode(createFrame(uid, puid, frameObjNode, projectId));
                projectBuildingInfoService.saveOrUpdateByThirdCode(createBuilding(uid, puid, frameObjNode, projectId));
            } else if (frameObjNode.getFrameLevel() == 2) {//单元
                log.info("[WR20] 单元：{}，{},{}", frameObjNode.getFrameLevel(), frameObjNode.getLevelNo(), frameObjNode.getFrameDesc());
                uid = projectFrameInfoService.saveOrUpdateFrameByEntityCode(createFrame(uid, puid, frameObjNode, projectId));
                projectUnitInfoService.saveOrUpdateByThirdCode(createUnit(uid, puid, frameObjNode, projectId));
            } else if (frameObjNode.getFrameLevel() == 1) {//房屋
                log.info("[WR20] 房屋：{}，{},{}", frameObjNode.getFrameLevel(), frameObjNode.getLevelNo(), frameObjNode.getFrameDesc());
                uid = projectFrameInfoService.saveOrUpdateFrameByEntityCode(createFrame(uid, puid, frameObjNode, projectId));
                projectHouseInfoService.saveOrUpdateByThirdCode(createHouse(uid, puid, frameObjNode, projectId));
            } else {//组团
                if (frameObjNode.getLevelNo() >= 1) {
                    log.info("[WR20] 组团：{}，{},{}", frameObjNode.getFrameLevel(), frameObjNode.getLevelNo(), frameObjNode.getFrameDesc());
                    uid = projectFrameInfoService.saveOrUpdateFrameByEntityCode(createFrame(uid, puid, frameObjNode, projectId));
                }
            }

            if (CollUtil.isNotEmpty(frameObjNode.getChildren())) {
                createOrUpdateFrames(frameObjNode.getChildren(), uid, projectId);
            }
        }


    }


    /**
     * 构建框架对象
     *
     * @param uid
     * @param puid
     * @param frameObjNode
     * @param projectId
     * @return
     */
    private ProjectFrameInfoVo createFrame(String uid, String puid, FrameObjNode frameObjNode, int projectId) {
        ProjectFrameInfoVo frameInfoVo = new ProjectFrameInfoVo();
        frameInfoVo.setProjectId(projectId);
        frameInfoVo.setTenantId(1);
        frameInfoVo.setEntityId(uid);
        frameInfoVo.setPuid(puid);

        frameInfoVo.setLevel(frameObjNode.getFrameLevel());
        frameInfoVo.setEntityName(frameObjNode.getFrameDesc());
        frameInfoVo.setIsBuilding(frameObjNode.getFrameLevel() == 3 ? "1" : "0");
        frameInfoVo.setIsUnit(frameObjNode.getFrameLevel() == 2 ? "1" : "0");
        frameInfoVo.setIsHouse(frameObjNode.getFrameLevel() == 1 ? "1" : "0");
        //房间对象时，使用当前层级框架号作为房间名称
        if (frameObjNode.getFrameLevel() == 1) {
            frameInfoVo.setEntityName(getFrameNo(frameObjNode));
        }
        frameInfoVo.setEntityCode(frameObjNode.getId());
        frameInfoVo.setFrameNo(getFrameNo(frameObjNode));

        return frameInfoVo;
    }

    /**
     * 构建楼栋对象
     *
     * @param uid
     * @param puid
     * @param frameObjNode
     * @param projectId
     * @return
     */
    private ProjectBuildingInfoVo createBuilding(String uid, String puid, FrameObjNode frameObjNode, int projectId) {
        ProjectBuildingInfoVo buildingInfo = new ProjectBuildingInfoVo();
        buildingInfo.setProjectId(projectId);
        buildingInfo.setTenantId(1);
        buildingInfo.setBuildingId(uid);
        buildingInfo.setPuid(puid);

        buildingInfo.setBuildingCode(frameObjNode.getId());
        buildingInfo.setBuildingName(frameObjNode.getFrameDesc());
        buildingInfo.setFrameNo(getFrameNo(frameObjNode));

        //设置单元
        buildingInfo.setUnitNameType(BuildingUnitTypeEnum.NUMBER.code);
        if (frameObjNode.getChildren() != null) {
            buildingInfo.setUnitTotal(frameObjNode.getChildren().size());
        } else {
            buildingInfo.setUnitTotal(0);
        }

        List<ProjectDeviceRegion> regionList = projectDeviceRegionService.list(new QueryWrapper<ProjectDeviceRegion>().lambda().eq(ProjectDeviceRegion::getParRegionId, "1"));
        if (CollUtil.isNotEmpty(regionList)) {
            buildingInfo.setRegionId(regionList.get(0).getRegionId());
        }
        return buildingInfo;
    }

    /**
     * 构建单元对象
     *
     * @param uid
     * @param puid
     * @param frameObjNode
     * @param projectId
     * @return
     */
    private ProjectUnitInfoVo createUnit(String uid, String puid, FrameObjNode frameObjNode, int projectId) {
        ProjectUnitInfoVo unitInfo = new ProjectUnitInfoVo();
        unitInfo.setProjectId(projectId);
        unitInfo.setTenantId(1);
        unitInfo.setUnitId(uid);

        unitInfo.setUnitName(frameObjNode.getFrameDesc());
        unitInfo.setUnitCode(frameObjNode.getId());
        unitInfo.setFrameNo(getFrameNo(frameObjNode));
        return unitInfo;
    }

    /**
     * 构建房屋对象
     *
     * @param uid
     * @param puid
     * @param frameObjNode
     * @param projectId
     * @return
     */
    private ProjectHouseInfoVo createHouse(String uid, String puid, FrameObjNode frameObjNode, int projectId) {
        ProjectHouseInfoVo houseInfo = new ProjectHouseInfoVo();
        houseInfo.setProjectId(projectId);
        houseInfo.setTenantId(1);
        houseInfo.setHouseId(uid);
        houseInfo.setBuildingUnit(puid);

//        houseInfo.setHouseName(frameObjNode.getFrameDesc());
        houseInfo.setHouseName(getFrameNo(frameObjNode));//房屋名称既当前层级的框架号
        houseInfo.setHouseCode(frameObjNode.getId());

        //配置房屋的默认户型
        ProjectHouseDesign projectHouseDesign = projectHouseDesignService.getTopOne(projectId);
        if (projectHouseDesign != null) {
            houseInfo.setHouseDesginId(projectHouseDesign.getDesignId());
            houseInfo.setHouseDesginName(projectHouseDesign.getDesginDesc());
        }
        return houseInfo;
    }

    /**
     * 获取当前层级框架号
     *
     * @return
     */
    private String getFrameNo(FrameObjNode frameObjNode) {
        String fullNo = frameObjNode.getId();
        String expNo = frameObjNode.getParentId();

        if (StringUtils.equals("0", expNo)) {//首个层级，当前层级框架号既累加框架号
            return fullNo;
        } else {
            return fullNo.replaceFirst(expNo, "");
        }

    }


    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }


}
