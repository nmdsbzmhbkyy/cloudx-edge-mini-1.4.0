package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.impl.ProjectCardServiceImpl;
import com.aurine.cloudx.estate.thirdparty.business.platform.BusinessBaseService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiEventEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.wr20.constant.WR20PersonTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20CardManageObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20DeviceRightObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20FaceManageObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20TenementObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20PersonService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20RightService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (WebProjectPersonDeviceServiceImpl)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/8 17:48
 */
@Service("wr20ProjectCardServiceImplV1")
@Slf4j
public class Wr20ProjectCardServiceImplV1 extends ProjectCardServiceImpl implements ProjectCardService, BusinessBaseService {

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

    /**
     * 保存卡片
     *
     * @param card
     * @return
     * @author: 王伟
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCard(ProjectCard card) {
        log.info("[WR20] 添加卡片");
        //保存卡片
        super.saveCard(card);


        //下发wr20
//        {"TeneID":"01010101","CardNo":"12345678"}
        ProjectCard cardEntity = super.getCardByNo(card.getCardNo());
//        JSONObject param = new JSONObject();


        //如果是住户
        if (PersonTypeEnum.PROPRIETOR.code.equals(card.getPersonType())) {
            //针对首套房添加卡片
            ProjectHousePersonRel housePersonRel = wr20PersonService.getFitstHouseRel(card.getPersonId());
            if (housePersonRel != null) {
                if (StringUtil.isNotEmpty(housePersonRel.getRelaCode())) {

                    WR20CardManageObj cardManage = new WR20CardManageObj();
                    cardManage.setTeneID(housePersonRel.getRelaCode());
                    cardManage.setCardNo(card.getCardNo());
                    cardManage.setPersonType(WR20PersonTypeEnum.getByCloudCode(card.getPersonType()).code);

                    wr20RemoteService.addCard(ProjectContextHolder.getProjectId(), JSONObject.parseObject(JSONObject.toJSONString(cardManage)), card.getCardId());
                }
            }


            List<ProjectHousePersonRel> personRelList = webProjectHousePersonRelService.listHousePersonByPersonId(card.getPersonId());
            //将该人员的所有住户关系都进行卡添加，其中只有一条关系可以添加成功，其他的将失败，失败的业务不进行处理。

//
//            int i = 0;
//            for (ProjectHousePersonRel housePersonRel : personRelList) {
//                if (StringUtil.isNotEmpty(housePersonRel.getRelaCode())) {
//
////                    if (i == 0) {
////                        //通过TeneId查询，并解析WR20住户权限，再有权限的设备中创建下载中的凭证记录 @since 2021-08-11 王伟
////                        JSONObject queryJson = new JSONObject();
////                        queryJson.put("TeneID", housePersonRel.getRelaCode());
////                        HuaweiRespondDTO respondDTO = wr20RemoteService.queryTenementSync(ProjectContextHolder.getProjectId(), queryJson);
////                        log.info("[WR20] 添加住户卡片获取到住户权限 {}", respondDTO.getBodyObj());
////
////                        CallBackData callBackData = respondDTO.getBodyObj().toJavaObject(CallBackData.class);
////                        if (callBackData == null) {
////                            log.error("[WR20] 接收到事件信息格式错误:{}", respondDTO.getBodyObj());
////                            throw new RuntimeException("接收到数据信息格式错误，请联系管理员");
////                        }
////
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
////                            wr20RightService.createDownloadingCert(ProjectContextHolder.getProjectId(), HuaweiEventEnum.CERT_CARD_ADD_ING, deviceCodeList, card, null, true);
////                        }
////
////                    }
//
//
//                    WR20CardManageObj cardManage = new WR20CardManageObj();
//                    cardManage.setTeneID(housePersonRel.getRelaCode());
//                    cardManage.setCardNo(card.getCardNo());
//                    cardManage.setPersonType(WR20PersonTypeEnum.getByCloudCode(card.getPersonType()).code);
//
////                    param = new JSONObject();
////                    param.put("TeneID", housePersonRel.getRelaCode());
////                    param.put("CardNo", card.getCardNo());
////                    param.put("PersonType", WR20PersonTypeEnum.getByCloudCode(card.getPersonType()).code);
//                    wr20RemoteService.addCard(ProjectContextHolder.getProjectId(), JSONObject.parseObject(JSONObject.toJSONString(cardManage)), card.getCardId());
//                    i++;
//                }
//            }

        } else if (PersonTypeEnum.STAFF.code.equals(card.getPersonType())) {
            // 员工
            ProjectStaff staff = projectStaffService.getById(card.getPersonId());

            WR20CardManageObj cardManage = new WR20CardManageObj();
            if (StringUtils.isEmpty(staff.getStaffCode())) {
                log.error("[WR20] 员工添加卡片失败,由于员工无第三方id: {} ", staff);
                throw new RuntimeException("员工参数不正确，请联系管理员");
            }

//            //在所有区口机设备中创建下载中的凭证记录 @since 2021-08-11 王伟
//            List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.listDeviceByType(ProjectContextHolder.getProjectId(), DeviceTypeEnum.GATE_DEVICE.getCode());
//
//            if (CollUtil.isNotEmpty(deviceInfoList)) {
//                List<String> deviceCodeList = deviceInfoList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
//                //模拟WR20返回，下载中数据
//                wr20RightService.createDownloadingCert(ProjectContextHolder.getProjectId(), HuaweiEventEnum.CERT_CARD_ADD_ING, deviceCodeList, card, null, false);
//            }


            cardManage.setTeneID(staff.getStaffCode());
            cardManage.setCardNo(card.getCardNo());
            cardManage.setPersonType(WR20PersonTypeEnum.getByCloudCode(card.getPersonType()).code);

//            param = new JSONObject();
//            param.put("TeneID", staff.getStaffCode());
//            param.put("CardNo", card.getCardNo());
//            param.put("PersonType", WR20PersonTypeEnum.getByCloudCode(card.getPersonType()).code);
            wr20RemoteService.addCard(ProjectContextHolder.getProjectId(), JSONObject.parseObject(JSONObject.toJSONString(cardManage)), card.getCardId());
        } else {//访客 WR20暂无访客卡片下发

        }

        //更新卡片第三方id
        //wr20卡片没有第三方编号
        return true;
    }

    /**
     * 删除卡
     *
     * @param cardId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delCard(String cardId) {

        ProjectCard card = this.getById(cardId);

        /**
         * WR20
         */
//        JSONObject param = new JSONObject();

        //如果是住户
        if (PersonTypeEnum.PROPRIETOR.code.equals(card.getPersonType())) {
            List<ProjectHousePersonRel> personRelList = webProjectHousePersonRelService.listHousePersonByPersonId(card.getPersonId());
            //将该人员的所有住户关系都进行卡添加，其中只有一条关系可以添加成功，其他的将失败，失败的业务不进行处理。
            for (ProjectHousePersonRel housePersonRel : personRelList) {
                if (StringUtil.isNotEmpty(housePersonRel.getRelaCode())) {
                    WR20CardManageObj cardManage = new WR20CardManageObj();
                    cardManage.setTeneID(housePersonRel.getRelaCode());
                    cardManage.setCardNo(card.getCardNo());
                    cardManage.setPersonType(WR20PersonTypeEnum.getByCloudCode(card.getPersonType()).code);
//                    param = new JSONObject();
//                    param.put("TeneID", housePersonRel.getRelaCode());
//                    param.put("CardNo", card.getCardNo());
//                    param.put("PersonType", WR20PersonTypeEnum.getByCloudCode(card.getPersonType()).code);
                    wr20RemoteService.delCard(ProjectContextHolder.getProjectId(), JSONObject.parseObject(JSONObject.toJSONString(cardManage)));
                }
            }

        } else if (PersonTypeEnum.STAFF.code.equals(card.getPersonType())) {
            // 员工
            ProjectStaff staff = projectStaffService.getById(card.getPersonId());
            WR20CardManageObj cardManage = new WR20CardManageObj();
            cardManage.setTeneID(staff.getStaffCode());
            cardManage.setCardNo(card.getCardNo());
            cardManage.setPersonType(WR20PersonTypeEnum.getByCloudCode(card.getPersonType()).code);

//            param = new JSONObject();
//            param.put("TeneID", staff.getStaffCode());
//            param.put("CardNo", card.getCardNo());
//            param.put("PersonType", WR20PersonTypeEnum.getByCloudCode(card.getPersonType()).code);
            wr20RemoteService.delCard(ProjectContextHolder.getProjectId(), JSONObject.parseObject(JSONObject.toJSONString(cardManage)));
        } else {//访客 WR20暂无访客卡片下发

        }

        //清空卡关联
        card.setCardId(cardId);
        card.setStatus(PassRightTokenStateEnum.UNUSE.code);
        card.setPersonType("");
        card.setPersonId("");
        //如果是实时删除数据库中的卡片与人员的关系则要删除其与设备的关系
//        webProjectRightDeviceService.removeCertmedia(cardId);
        this.updateById(card);


        return true;
    }


    @Override
    protected Class<ProjectCard> currentModelClass() {
        return ProjectCard.class;
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
