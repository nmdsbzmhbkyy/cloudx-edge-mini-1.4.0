package com.aurine.cloudx.wjy.config;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.wjy.canal.config.CanalBoot;
import com.aurine.cloudx.wjy.constant.Constant;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.runnable.DataToWjy;
import com.aurine.cloudx.wjy.service.impl.ProjectServiceImpl;
import com.aurine.cloudx.wjy.utils.RedisUtil;
import com.aurine.cloudx.wjy.utils.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author ： huangjj
 * @date ： 2021/5/17
 * @description： 项目初始化加载
 */
@Slf4j
public class AppInit implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {


        if(event.getApplicationContext().getParent().getParent() == null){
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
            ProjectServiceImpl projectService = event.getApplicationContext().getBean(ProjectServiceImpl.class);
            //服务启动时校对一次数据库数据，存入redis
            List<Project> list = projectService.getList();
            System.out.println("=====我家云项目存入缓存===="+ JSON.toJSONString(list));
            if(list != null && list.size() > 0){
                for (Project p:
                        list) {
                    if(p.getEnable() == 1){
                        //判断是否要插入缓存中
                        DataToWjy.setQueue(p.getProjectId());
                        if(!RedisUtil.sExistValue("wjy_project_set",p.getProjectId().toString())){//判断该项目是否需要同步我家云
                            System.out.println("插入缓存："+p.getProjectId().toString());
                            RedisUtil.sSet("wjy_project_set",p.getProjectId().toString());
                        }
                    }
                }
            }
            //初始化线程
            DataToWjy dataToWjy = new DataToWjy(5);
            dataToWjy.start();

            WjyConfig wjyConfig = event.getApplicationContext().getBean(WjyConfig.class);
            Constant.baseUrl = wjyConfig.getBaseUrl();
            Constant.productName = wjyConfig.getProductName();

            //启动canal
            CanalBoot canalBoot = event.getApplicationContext().getBean(CanalBoot.class);
            canalBoot.start();
        }


    }
}