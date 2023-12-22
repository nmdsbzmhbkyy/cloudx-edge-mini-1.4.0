package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.impl.ProjectFaceResourcesServiceImpl;
import com.aurine.cloudx.estate.thirdparty.business.platform.BusinessBaseService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiEventEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.wr20.constant.WR20PersonTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20DeviceRightObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20FaceManageObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20TenementObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20PersonService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20RightService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service("wr20ProjectFaceResourcesServiceImplV1")
@Slf4j
@RefreshScope
public class Wr20ProjectFaceResourcesServiceImplV1 extends ProjectFaceResourcesServiceImpl implements ProjectFaceResourcesService, BusinessBaseService {

    @Resource
    private ProjectHousePersonRelService webProjectHousePersonRelService;
    @Resource
    private ProjectStaffService projectStaffService;

    @Resource
    private WR20RemoteService wr20RemoteService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private WR20RightService wr20RightService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private WR20PersonService wr20PersonService;

    @Value("${server.base-uri}")
    private String baseUriPath;

    /**
     * 保存
     *
     * @param faceResources
     * @return
     * @author: 王伟
     */
    @Override
    public boolean saveFace(ProjectFaceResources faceResources) {
        String uid = UUID.randomUUID().toString().replace("-", "");
        faceResources.setFaceId(uid);
        String faceCode = "";

        //下发wr20
//        {"TeneID":"01010101","CardNo":"12345678"}
//        JSONObject param = new JSONObject();


        //如果是住户
        if (PersonTypeEnum.PROPRIETOR.code.equals(faceResources.getPersonType())) {

//            针对首套房添加人脸
            ProjectHousePersonRel housePersonRel  = wr20PersonService.getFitstHouseRel(faceResources.getPersonId());
            if(housePersonRel != null){
                if (StringUtil.isNotEmpty(housePersonRel.getRelaCode())) {
                    WR20FaceManageObj faceManage = new WR20FaceManageObj();
                    faceManage.setTeneId(housePersonRel.getRelaCode());
                    faceManage.setSrcType("2");
                    faceManage.setFaceImage(baseUriPath + faceResources.getPicUrl());
                    faceManage.setPersonType(WR20PersonTypeEnum.getByCloudCode(faceResources.getPersonType()).code);

                    wr20RemoteService.addFace(ProjectContextHolder.getProjectId(), JSONObject.parseObject(JSONObject.toJSONString(faceManage)), faceResources.getFaceId());
                }
            }

//            List<ProjectHousePersonRel> personRelList = webProjectHousePersonRelService.listHousePersonByPersonId(faceResources.getPersonId());
//            //将该人员的所有住户关系都进行卡添加，其中只有一条关系可以添加成功，其他的将失败，失败的业务不进行处理。
//            int i = 0;
//            for (ProjectHousePersonRel housePersonRel : personRelList) {
//                if (StringUtil.isNotEmpty(housePersonRel.getRelaCode())) {
////                    if (i == 0) {
////                        //通过TeneId查询，并解析WR20住户权限，再有权限的设备中创建下载中的凭证记录 @since 2021-08-11 王伟
////                        JSONObject queryJson = new JSONObject();
////                        queryJson.put("TeneID", housePersonRel.getRelaCode());
////                        HuaweiRespondDTO respondDTO = wr20RemoteService.queryTenementSync(ProjectContextHolder.getProjectId(), queryJson);
////                        log.info("[WR20] 添加住户人脸获取到住户权限 {}", respondDTO.getBodyObj());
////
////                        CallBackData callBackData = respondDTO.getBodyObj().toJavaObject(CallBackData.class);
////                        if (callBackData == null) {
////                            log.error("[WR20] 接收到事件信息格式错误:{}", respondDTO.getBodyObj());
////                            throw new RuntimeException("接收到数据信息格式错误，请联系管理员");
////                        }
////
////                        List<WR20TenementObj> tenementObjList = callBackData.getOnNotifyData().getObjManagerData().getObjInfo().getJSONArray("list").toJavaList(WR20TenementObj.class);
////
////                        if (CollUtil.isNotEmpty(tenementObjList)) {
////                            List<String> deviceCodeList = new ArrayList<>();
////                            if (CollUtil.isNotEmpty(tenementObjList.get(0).getRights())) {
////                                for (WR20DeviceRightObj wr20DeviceRightObj : tenementObjList.get(0).getRights()) {
////                                    deviceCodeList.add(wr20DeviceRightObj.getDeviceNo());
////                                }
////                            }
////
////                            //模拟WR20返回，下载中数据
////                            wr20RightService.createDownloadingCert(ProjectContextHolder.getProjectId(), HuaweiEventEnum.CERT_FACE_ADD_ING, deviceCodeList, null, faceResources, true);
////                        }
////                    }
//
//
//                    WR20FaceManageObj faceManage = new WR20FaceManageObj();
//                    faceManage.setTeneId(housePersonRel.getRelaCode());
//                    faceManage.setSrcType("2");
//                    faceManage.setFaceImage(baseUriPath + faceResources.getPicUrl());
//                    faceManage.setPersonType(WR20PersonTypeEnum.getByCloudCode(faceResources.getPersonType()).code);
//
////                    param = new JSONObject();
////                    param.put("ID", housePersonRel.getRelaCode());
////                    param.put("Photo", faceResources.getPicUrl());
////                    param.put("SrcType", "2");
////                    param.put("PersonType", WR20PersonTypeEnum.getByCloudCode(faceResources.getPersonType()).code);
//
//
//                    wr20RemoteService.addFace(ProjectContextHolder.getProjectId(), JSONObject.parseObject(JSONObject.toJSONString(faceManage)), faceResources.getFaceId());
//                    i++;
//                }
//            }

        } else if (PersonTypeEnum.STAFF.code.equals(faceResources.getPersonType())) {
            // 员工
            ProjectStaff staff = projectStaffService.getById(faceResources.getPersonId());

            WR20FaceManageObj faceManage = new WR20FaceManageObj();

            if (StringUtils.isEmpty(staff.getStaffCode())) {
                log.error("[WR20] 员工添加人脸失败,由于员工无第三方id: {} ", staff);
                throw new RuntimeException("员工参数不正确，请联系管理员");
            }

//            //在所有区口机设备中创建下载中的凭证记录 @since 2021-08-11 王伟
//            List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.listDeviceByType(ProjectContextHolder.getProjectId(), DeviceTypeEnum.GATE_DEVICE.getCode());
//
//            if (CollUtil.isNotEmpty(deviceInfoList)) {
//                List<String> deviceIdList = deviceInfoList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
//                //模拟WR20返回，下载中数据
//                wr20RightService.createDownloadingCert(ProjectContextHolder.getProjectId(), HuaweiEventEnum.CERT_FACE_ADD_ING, deviceIdList, null, faceResources, false);
//            }

            faceManage.setTeneId(staff.getStaffCode());
            faceManage.setSrcType("2");
            faceManage.setFaceImage(baseUriPath + faceResources.getPicUrl());
            faceManage.setPersonType(WR20PersonTypeEnum.getByCloudCode(faceResources.getPersonType()).code);

//            param = new JSONObject();
//            param.put("ID", staff.getStaffCode());
//            param.put("Photo", faceResources.getPicUrl());
//            param.put("SrcType", "2");
//            param.put("PersonType", WR20PersonTypeEnum.getByCloudCode(faceResources.getPersonType()).code);
            wr20RemoteService.addFace(ProjectContextHolder.getProjectId(), JSONObject.parseObject(JSONObject.toJSONString(faceManage)), faceResources.getFaceId());
        } else {//访客 WR20暂无访客人脸下发

        }

        //更新第三方id
//        faceResources.setFaceCode(faceCode);
        return super.save(faceResources);
    }

    /**
     * 删除人脸
     *
     * @param faceId
     * @return
     */
    @Override
    public boolean removeFace(String faceId) {

        ProjectFaceResources face = this.getById(faceId);


        /**
         * WR20
         */
//        JSONObject param = new JSONObject();

        //如果是住户
        if (PersonTypeEnum.PROPRIETOR.code.equals(face.getPersonType())) {
            List<ProjectHousePersonRel> personRelList = webProjectHousePersonRelService.listHousePersonByPersonId(face.getPersonId());
            //清空该住户名下全部第三方住户的人脸信息
            for (ProjectHousePersonRel housePersonRel : personRelList) {
                if (StringUtil.isNotEmpty(housePersonRel.getRelaCode())) {

                    JSONObject faceManage = new JSONObject();
                    faceManage.put("faceID", face.getFaceCode());

                    HuaweiRespondDTO respondDTO = wr20RemoteService.delFace(ProjectContextHolder.getProjectId(), faceManage);
                }
            }


        } else if (PersonTypeEnum.STAFF.code.equals(face.getPersonType())) {
            // 员工
            ProjectStaff staff = projectStaffService.getById(face.getPersonId());

            JSONObject faceManage = new JSONObject();
            faceManage.put("faceID", face.getFaceCode());

//            param = new JSONObject();
//            param.put("ID", face.getFaceCode());
            wr20RemoteService.delFace(ProjectContextHolder.getProjectId(), faceManage);
        } else {//访客 WR20暂无访客卡片下发

        }

        return this.removeById(faceId);
    }


    @Override
    protected Class<ProjectFaceResources> currentModelClass() {
        return ProjectFaceResources.class;
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

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.BUSINESS_WR20.code;
    }
}
