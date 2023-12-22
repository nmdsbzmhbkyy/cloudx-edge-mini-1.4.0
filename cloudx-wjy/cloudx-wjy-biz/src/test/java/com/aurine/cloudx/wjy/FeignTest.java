package com.aurine.cloudx.wjy;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.feign.RemoteBuildingInfoService;
import com.aurine.cloudx.estate.feign.RemoteStaffService;
import com.aurine.cloudx.estate.feign.RemoteUnitInfoService;
import com.aurine.cloudx.wjy.constant.GjModuleType;
import com.aurine.cloudx.wjy.constant.WjModuleType;
import com.aurine.cloudx.wjy.constant.WyModuleType;
import com.aurine.cloudx.wjy.feign.*;
import com.aurine.cloudx.wjy.vo.CustomerStandardVo;
import com.aurine.cloudx.wjy.vo.BuildingVo;
import com.aurine.cloudx.wjy.vo.RoomStandardVo;
import com.aurine.cloudx.wjy.vo.UnitVo;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeignTest {
    @Resource
    RemoteHelloService remoteHelloService;
    @Resource
    RemoteBuildingService remoteBuildingService;
    @Resource
    RemoteRoomService remoteRoomService;
    @Resource
    RemoteCustomerService remoteCustomerService;
    @Resource
    RemoteAppUserH5Service remoteAppUserH5Service;
    @Resource
    RemoteAppEngineerH5Service remoteAppEngineerH5Service;
    @Resource
    RemoteWebH5Service remoteWebH5Service;
    @Resource
    RemoteProjectService remoteProjectService;
    @Resource
    RemoteBuildingInfoService remoteBuildingInfoService;
    @Resource
    RemoteStaffService remoteStaffService;
    @Resource
    RemoteUnitInfoService remoteUnitInfoService;

    @Test
    public void hello(){
        R r = remoteHelloService.hello("666");

        Assert.assertEquals(0, r.getCode());
    }
    @Test
    public void addBuilding(){
        BuildingVo buildingVo = new BuildingVo();
        buildingVo.setName("A栋");
        buildingVo.setNumber("01");
        buildingVo.setSourceID("1");
        buildingVo.setSourceSystem("冠林源");
        buildingVo.setOvergroundCount(17);
        buildingVo.setUndergroundCount(2);
        buildingVo.setProjectId(1000000232);
        buildingVo.setCatalog(1);
        List<UnitVo> unitVos = new ArrayList<>();
        UnitVo unitVo = new UnitVo();
        unitVo.setName("A栋1单元");
        unitVo.setNumber("0101");
        unitVo.setSeq(101);
        unitVo.setSourceID("1");
        unitVo.setSourceSystem("冠林源");
        unitVos.add(unitVo);
        buildingVo.setBuildingUnitVoList(unitVos);
        R r = remoteBuildingService.addBuilding(buildingVo);
        System.out.println(JSONObject.toJSONString(r));
    }
    @Test
    public void addRoom(){
        RoomStandardVo roomVo = new RoomStandardVo();
        roomVo.setRoomName("1栋1单元101");
        roomVo.setRoomNumber("01010101");
        roomVo.setFloor("1");
        roomVo.setBuildingName("A栋");
        roomVo.setBuildUnitName("A栋1单元");
        roomVo.setProperty(0);
        roomVo.setBuildingArea(128);
        roomVo.setRoomArea(109);
        roomVo.setProductName("门禁系统");
        roomVo.setAttributeName("A户型-3房1室1厅");
        roomVo.setSourceID("1");
        roomVo.setSourceSystem("冠林源");
        roomVo.setFeeStatus(4);
        roomVo.setStatus(1);
        roomVo.setProjectId(1000000232);
        roomVo.setUnitId("1");
        R r = remoteRoomService.addRoom(roomVo);
        System.out.println(JSONObject.toJSONString(r));
    }
    @Test
    public void addCus(){
        CustomerStandardVo cus = new CustomerStandardVo();
        cus.setName("黄健杰");
        cus.setPhone("18950462412");
        cus.setType("P");
        cus.setCertType("0101");
        cus.setCertNo("350783199901099090");
        cus.setSex("0");
        cus.setBirthday("1999-01-09");
        cus.setProjectId(1000000232);
        R r = remoteCustomerService.addStandardCus(cus);
        System.out.println(JSONObject.toJSONString(r));
    }
    @Test
    public void getUrl(){
        R r = remoteAppUserH5Service.getModule(1000001167, WjModuleType.User.getType(),"18950462412");
        System.out.println(JSONObject.toJSONString(r));
        R r2 = remoteAppEngineerH5Service.getModule(1000001167, GjModuleType.Query.getType(),"18950462412");
        System.out.println(JSONObject.toJSONString(r2));
        R r3 = remoteWebH5Service.getModule(1000001167, WyModuleType.toMain.getType());
        System.out.println(JSONObject.toJSONString(r3));

    }
    @Test
    public void getProjectInfo(){
        R r = remoteProjectService.projectInfo(1000000232);
        System.out.println(JSONObject.toJSONString(r));
    }
    @Test
    public void projectEnable(){
        R r = remoteProjectService.projectEnable(1000001167,false);
        System.out.println(JSONObject.toJSONString(r));
    }

    @Test
    public void getBuilding(){
        R r = remoteBuildingInfoService.innerListByProjectId(1000001167, SecurityConstants.FROM_IN);
        System.out.println(JSONObject.toJSONString(r));
    }

    @Test
    public void getStaff(){
        R r = remoteStaffService.innerListByProjectId(1000001167, SecurityConstants.FROM_IN);
        System.out.println(JSONObject.toJSONString(r));
    }

    @Test
    public void getBuildingId(){
        R r = remoteUnitInfoService.innerGetBuildingId("10d06d56c78c4d93bc68d41411ced602", SecurityConstants.FROM_IN);
        System.out.println(JSONObject.toJSONString(r));
    }
}
