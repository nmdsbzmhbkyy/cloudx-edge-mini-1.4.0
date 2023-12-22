package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectHouseInfo;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.impl.ProjectHousePersonRelServiceImpl;
import com.aurine.cloudx.estate.thirdparty.business.platform.BusinessBaseService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20TenementObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums.WR20CredentialTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20RightService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.util.WR20Util;
import com.aurine.cloudx.estate.util.DockModuleConfigUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * WR20
 * 住户服务
 *
 * @ClassName: WR20WebProjectHousePersonRelServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-10 16:51
 * @Copyright:
 */
@Service("wr20ProjectHousePersonRelServiceImplV1")
@Slf4j
public class Wr20ProjectHousePersonRelServiceImplV1 extends ProjectHousePersonRelServiceImpl implements ProjectHousePersonRelService, BusinessBaseService {

    @Resource
    private WR20Util wr20Util;

    @Resource
    private ProjectHouseInfoService projectHouseInfoService;

    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    @Resource
    private DockModuleConfigUtil dockModuleConfigUtil;
    @Resource
    private WR20RemoteService wr20RemoteService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Resource
    private WR20RightService wr20RightService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;


    /**
     * 迁入住户(异步)
     *
     * @param projectHousePersonRel
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ProjectHousePersonRel projectHousePersonRel) {
        log.info("[WR20] 迁入住户");

        //1.init
        ProjectHouseInfo house = projectHouseInfoService.getById(projectHousePersonRel.getHouseId());
        ProjectPersonInfo person = projectPersonInfoService.getById(projectHousePersonRel.getPersonId());

        String frameNo = house.getHouseCode();
        String personName = person.getPersonName();
        String personPhone = person.getTelephone();

        if (StringUtils.isEmpty(frameNo)) {
            log.error("[WR20] 房屋信息错误，无法添加住户，由于 房屋无框架号信息, 房屋={}", house);
            throw new RuntimeException("房屋信息错误，无法添加住户，请联系管理员");
        }


        WR20TenementObj tenementObj = new WR20TenementObj();
        tenementObj.setID(-1L);
        tenementObj.setThirdId(person.getPersonId());
        tenementObj.setFrameNo(frameNo);
        tenementObj.setName(personName);
        tenementObj.setTelephone(personPhone);
        tenementObj.setTelephone2(personPhone);
        tenementObj.setCredentialType(WR20CredentialTypeEnum.getByCloudCode(person.getCredentialType()).wr20Code);
        tenementObj.setCredentialID(person.getCredentialNo());

//        super.save(projectHousePersonRel);

        //连接wr20，获取住户数据
        wr20RemoteService.saveHousePersonRel(ProjectContextHolder.getProjectId(), JSONObject.parseObject(JSONObject.toJSONString(tenementObj)), projectHousePersonRel.getRelaId());
//        projectHousePersonRel.setRelaCode(relaCode);

        return true;
    }

    /**
     * 迁出住户(异步)
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        log.info("[WR20] 迁出住户");

        //1.init
        ProjectHousePersonRel housePersonRel = this.getById(id);


        if (StringUtils.isEmpty(housePersonRel.getRelaCode())) {
            log.error("[WR20] 迁出住户 第三方ID为空, 直接对住户进行迁出处理:{}", housePersonRel);
        } else {
            //判断住户是否是最后一套房，如果不是，需要对该住户刷新授权设备
            int houseCount = projectHousePersonRelService.count(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getPersonId, housePersonRel.getPersonId()));


            JSONObject paramObj = new JSONObject();
            //连接wr20，获取删除住户
//            wr20RemoteService.removeHousePersonRel(ProjectContextHolder.getProjectId(), paramObj, housePersonRel.getRelaId());

            if (houseCount >= 2) {
                paramObj = new JSONObject();
                paramObj.put("teneId", Long.valueOf(housePersonRel.getRelaCode()));
                paramObj.put("thirdId", housePersonRel.getPersonId());

                log.info("[WR20] 删除住户{}套房屋中的一套 刷新当前住户授权，住户PersonID:{} 获取该住户全部权限", houseCount, housePersonRel.getPersonId());
                wr20RemoteService.checkout(ProjectContextHolder.getProjectId(), paramObj, housePersonRel.getPersonId());
//                List<ProjectDeviceInfo> rightDeviceList = wr20RightService.getPersonRightList(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR);
//                String[] deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList()).toArray(new String[rightDeviceList.size()]);
//                projectPersonDeviceService.savePersonDevice(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR, deviceIdList, LocalDateTime.now(), LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                log.info("[WR20] 删除住户 PersonID:{}", housePersonRel.getPersonId());
                paramObj = new JSONObject();
                paramObj.put("userId", Long.valueOf(housePersonRel.getRelaCode()));
                wr20RemoteService.removeHousePersonRel(ProjectContextHolder.getProjectId(), paramObj, housePersonRel.getRelaId());
            }
        }
        return super.removeById(id);
    }

    @Override
    protected Class<ProjectHousePersonRel> currentModelClass() {
        return ProjectHousePersonRel.class;
    }


    @Override
    public boolean reSaveRel(String housePersonRelId) {

        boolean haveRelaCode = false;

        ProjectHousePersonRel projectHousePersonRel = projectHousePersonRelService.getById(housePersonRelId);
        //1、使用查询接口，查询住户情况
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectHousePersonRel.getPersonId());
        ProjectHouseInfo houseInfo = projectHouseInfoService.getById(projectHousePersonRel.getHouseId());

        JSONObject queryJson = new JSONObject();
        queryJson.put("Telephone", projectPersonInfo.getTelephone());
        queryJson.put("FrameNo", houseInfo.getHouseCode());


        HuaweiRespondDTO respondDTO = wr20RemoteService.queryTenementSync(ProjectContextHolder.getProjectId(), queryJson);
        log.info("[WR20] 查询住户第三方ID{}", respondDTO);

        //init
        CallBackData callBackData = respondDTO.getBodyObj().toJavaObject(CallBackData.class);
        if (callBackData == null) {
            log.error("[华为中台] 接收到事件信息格式错误:{}", respondDTO.getBodyObj());
            throw new RuntimeException("[华为中台] 接收到事件信息格式错误");
        }

        List<WR20TenementObj> tenementObjList = callBackData.getOnNotifyData().getObjManagerData().getObjInfo().getJSONArray("list").toJavaList(WR20TenementObj.class);
        String relaCode = "";
        if (CollUtil.isNotEmpty(tenementObjList)) {
            relaCode = tenementObjList.get(0).getID().toString();
            haveRelaCode = true;
        }


        if (haveRelaCode) {
            //将第三方ID存入系统中
            projectHousePersonRel.setAuditStatus(AuditStatusEnum.pass.code);
            projectHousePersonRel.setRelaCode(relaCode);

            return projectHousePersonRelService.updateById(projectHousePersonRel);
        } else {
            // 2、如果没有获取到WR20对应住户数据，重新下发，进入递归迭代
            return this.save(projectHousePersonRel);
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
