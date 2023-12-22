package com.aurine.cloudx.wjy;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.service.ProjectService;
import com.aurine.cloudx.wjy.service.impl.ProjectServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author ： huangjj
 * @date ： 2021/4/22
 * @description： 数据库测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DBTest {
    @Resource
    ProjectService projectService;
    @Test
    public void addProject(){
        Project p = new Project();
        p.setProjectId(1);
        p.setPid("62076e66f4524620823f47b1f6d9be8a");
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
        p.setEnable(1);
        p.setSeq(1);
        projectService.saveOrUpdate(p);
        System.out.println(JSONObject.toJSONString(projectService.getById(1)));
    }
}