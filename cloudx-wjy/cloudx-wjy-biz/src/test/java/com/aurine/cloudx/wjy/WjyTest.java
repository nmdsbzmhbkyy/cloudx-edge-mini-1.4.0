package com.aurine.cloudx.wjy;

import com.aurine.cloudx.wjy.constant.Constant;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.service.*;
import com.aurine.cloudx.wjy.service.impl.ProjectServiceImpl;
import com.aurine.cloudx.wjy.service.impl.WjyBuildingServiceImpl;
import com.aurine.cloudx.wjy.service.impl.WjyCustomerServiceImpl;
import com.aurine.cloudx.wjy.service.impl.WjyTokenServiceImpl;
import com.aurine.cloudx.wjy.vo.Organization;
import com.aurine.cloudx.wjy.vo.WorkerVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/4/22
 * @description： 我家云接口测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WjyTest {
    @Resource
    WjyTokenService wjyTokenService;
    @Resource
    ProjectService projectService;
    @Resource
    WjyBuildingService wjyBuildingService;
    @Resource
    WjyCustomerService wjyCustomerService;
    @Resource
    WjyOrgService wjyOrgService;
    @Test
    public void getTicket(){

        System.out.println(wjyTokenService.getTicket(projectService.getById(1), Constant.wyWebType));
    }

    @Test
    public void getBuilding(){
        Project p = new Project();
        p.setProjectId(1);
        p.setPid("fa6b067415114e71b096631ae3daf490");
        p.setGjAppid("96doy1yf7caaalbm");
        p.setGjAppkey("z5c5fx7x5tec6tgj");
        p.setGjAppsecret("xc03srvxbv5e4yoq7m45d7779mszkkhz");
        p.setWyAppid("kingdeeAuth");
        p.setWyAppkey("qne7w8s7iae17xwm");
        p.setWyAppsecret("n26z6pfrly8ea778snqdlc9rejduc1b1");
        p.setWjAppid("fbtkgre11r15hnbu");
        p.setWjAppkey("vq22dprv7vddf32z");
        p.setWjAppsecret("jibtmtzwvlvyqq4e18gdjodj1fz0psje");
        p.setPhone("13799340817");
        System.out.println(wjyBuildingService.buildingGetByPage(1, 1, p, ""));
    }

    @Test
    public void getCus(){
        Project p = new Project();
        p.setProjectId(1);
        p.setPid("a03fe881ef6f431687322d835d308d57");
        p.setGjAppid("96doy1yf7caaalbm");
        p.setGjAppkey("z5c5fx7x5tec6tgj");
        p.setGjAppsecret("xc03srvxbv5e4yoq7m45d7779mszkkhz");
        p.setWyAppid("kingdeeAuth");
        p.setWyAppkey("qne7w8s7iae17xwm");
        p.setWyAppsecret("n26z6pfrly8ea778snqdlc9rejduc1b1");
        p.setWjAppid("fbtkgre11r15hnbu");
        p.setWjAppkey("vq22dprv7vddf32z");
        p.setWjAppsecret("jibtmtzwvlvyqq4e18gdjodj1fz0psje");
        p.setPhone("13799340817");
        System.out.println(wjyCustomerService.customerGetList(1,10,p,null,null,"黄建杰",null));
    }

    @Test
    public void saveOrg(){
        Project p = new Project();
        p.setProjectId(1000000232);
        p.setPid("4e3f5d7bba2142c384b53e94b2b42748");
        p.setGjAppid("96doy1yf7caaalbm");
        p.setGjAppkey("z5c5fx7x5tec6tgj");
        p.setGjAppsecret("xc03srvxbv5e4yoq7m45d7779mszkkhz");
        p.setWyAppid("kingdeeAuth");
        p.setWyAppkey("qne7w8s7iae17xwm");
        p.setWyAppsecret("n26z6pfrly8ea778snqdlc9rejduc1b1");
        p.setWjAppid("fbtkgre11r15hnbu");
        p.setWjAppkey("vq22dprv7vddf32z");
        p.setWjAppsecret("jibtmtzwvlvyqq4e18gdjodj1fz0psje");
        p.setPhone("13799340817");

        List<Organization> orgs = new ArrayList<>();
        Organization organization = new Organization();
        organization.setName("gl-hjjcs");
        organization.setNumber("1000000232");
        organization.setSimpleName("cs");
        organization.setIsLeaf(0);
        organization.setLevel(2);
        organization.setLongNumber("f486960c850d49efb8d24d56e210f640");
        organization.setIsCompany("1"); //是否公司(0区域 1公司 2部门 3备用公司)
        organization.setSourceID("1000000232");
        organization.setSourceSystem("aurine");
        organization.setParentID("f486960c850d49efb8d24d56e210f640");
        organization.setDescription("测试");
        orgs.add(organization);

        System.out.println(wjyOrgService.orgSyncSave(p, orgs));
    }

    @Test
    public void saveWorker(){
        WorkerVo workerVo = new WorkerVo();
        workerVo.setName("许志航");
        workerVo.setNumber("17746071554");
        workerVo.setPhone("17746071554");
        workerVo.setSourceID("685a85ee5df72d60adeee7c7ef229b9e");
        workerVo.setSourceSystem(Constant.source);
        workerVo.setOrgUnitNames("gl基地");
        List<WorkerVo> workerVos = new ArrayList<>();
        workerVos.add(workerVo);
        System.out.println(wjyOrgService.workerAdd(projectService.getByProjectId(1000001167), workerVos));
    }
    @Test
    public void delWorker(){

        wjyOrgService.workerDelete(projectService.getByProjectId(1000001167),"17746071554");
    }


    @Test
    public void delHouseCus(){
        Project p = new Project();
        p.setProjectId(1);
        p.setPid("a03fe881ef6f431687322d835d308d57");
        p.setGjAppid("96doy1yf7caaalbm");
        p.setGjAppkey("z5c5fx7x5tec6tgj");
        p.setGjAppsecret("xc03srvxbv5e4yoq7m45d7779mszkkhz");
        p.setWyAppid("kingdeeAuth");
        p.setWyAppkey("qne7w8s7iae17xwm");
        p.setWyAppsecret("n26z6pfrly8ea778snqdlc9rejduc1b1");
        p.setWjAppid("fbtkgre11r15hnbu");
        p.setWjAppkey("vq22dprv7vddf32z");
        p.setWjAppsecret("jibtmtzwvlvyqq4e18gdjodj1fz0psje");
        p.setPhone("13799340817");
        System.out.println(wjyCustomerService.customerStandardCheckout(p,"1栋01单元0101","18950462413"));
    }
}