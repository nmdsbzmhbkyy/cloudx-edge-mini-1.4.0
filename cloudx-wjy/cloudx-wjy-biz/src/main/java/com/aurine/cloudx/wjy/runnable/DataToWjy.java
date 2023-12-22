package com.aurine.cloudx.wjy.runnable;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.wjy.controller.BuildingController;
import com.aurine.cloudx.wjy.controller.CustomerController;
import com.aurine.cloudx.wjy.controller.RoomController;
import com.aurine.cloudx.wjy.pojo.PushData;
import com.aurine.cloudx.wjy.service.BuildingService;
import com.aurine.cloudx.wjy.service.CustomerService;
import com.aurine.cloudx.wjy.service.OrgService;
import com.aurine.cloudx.wjy.service.RoomService;
import com.aurine.cloudx.wjy.service.impl.BuildingServiceImpl;
import com.aurine.cloudx.wjy.service.impl.CustomerServiceImpl;
import com.aurine.cloudx.wjy.service.impl.OrgServiceImpl;
import com.aurine.cloudx.wjy.service.impl.RoomServiceImpl;
import com.aurine.cloudx.wjy.utils.RedisUtil;
import com.aurine.cloudx.wjy.utils.SpringUtil;
import com.aurine.cloudx.wjy.vo.*;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class DataToWjy{

    //待处理小区队列
    private static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
    //正在处理小区队列
    private static BlockingQueue<Integer> queueDoing = new LinkedBlockingQueue<>();

    private BuildingService buildingService;

    private RoomService roomService;

    private CustomerService customerService;

    private OrgService orgService;

    private Integer num;

    public DataToWjy(Integer num){
        buildingService = SpringUtil.getBean(BuildingServiceImpl.class);
        roomService = SpringUtil.getBean(RoomServiceImpl.class);
        customerService = SpringUtil.getBean(CustomerServiceImpl.class);
        orgService = SpringUtil.getBean(OrgServiceImpl.class);
        this.num = num;
    }

    public void start(){
        createRunnable(num);
    }

    //待处理小区加入队列
    public static void setQueue(Integer id) {
        if (!queue.contains(id) && !queueDoing.contains(id)) {
            //System.out.println("加入队列："+id);
            queue.add(id);
        }
    }

    private boolean createRunnable(Integer num){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        ExecutorService executorService = Executors.newFixedThreadPool(num);
        for (int i = 1; i <= num; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    RequestContextHolder.setRequestAttributes(servletRequestAttributes,true);//设置子线程共享
                    while (true){
                        deal();
                    }
                }
            });
        }
        return true;
    }


    private void deal() {
        Integer projectId = null;
        try {
            //log.info("等待队列取数据");
            projectId = queue.take();
            //log.info("处理项目："+projectId);
            queueDoing.add(projectId);
            while (true){
                Thread.sleep(100);

                PushData pushData = null;

                Object object = RedisUtil.lPop("wjy_project"+projectId.toString());

                if(object  == null){
                    break;
                }

                try {
                    JSONObject pushDataJson = (JSONObject) JSONObject.parse(object.toString());
                    pushData = pushDataJson.toJavaObject(PushData.class);
                    log.info("正在处理数据"+JSONObject.toJSONString(pushData));
                    /*if(pushData.getSuccess() != null &&  pushData.getSuccess() == 1){
                        rollback(projectId,JSONObject.toJSONString(pushData));
                        continue;
                    }*/

                    if(pushData.getOperateType() == PushData.OperateType.Insert.getCode()){
                        if(pushData.getDataType() == PushData.DataType.Building.getCode()){
                            BuildingVo buildingVo = JSONObject.parseObject(pushData.getData(),BuildingVo.class);
                            R r = buildingService.addBuilding(buildingVo);
                            if(r.getCode() != 0){
                                pushData.setSuccess(1);
                                rollback(projectId,JSONObject.toJSONString(pushData));
                                break;
                            }
                        }else if(pushData.getDataType() == PushData.DataType.Room.getCode()){
                            RoomStandardVo roomStandardVo = JSONObject.parseObject(pushData.getData(),RoomStandardVo.class);
                            R r = roomService.addRoom(roomStandardVo);
                            log.info(JSONObject.toJSONString(r));
                            if(r.getCode() != 0){
                                pushData.setSuccess(1);
                                //rollback(projectId,JSONObject.toJSONString(pushData));
                                break;
                            }
                        }else if(pushData.getDataType() == PushData.DataType.Customer.getCode()){
                            CustomerStandardVo customerStandardVo = JSONObject.parseObject(pushData.getData(),CustomerStandardVo.class);
                            R r = customerService.addStandardCus(customerStandardVo);
                            log.info(JSONObject.toJSONString(r));
                            if(r.getCode() != 0){
                                pushData.setSuccess(1);
                                //rollback(projectId,JSONObject.toJSONString(pushData));
                                break;
                            }
                        }else if(pushData.getDataType() == PushData.DataType.Relationship.getCode()){
                            BindCustomer2Vo bindCustomer2Vo = JSONObject.parseObject(pushData.getData(),BindCustomer2Vo.class);
                            R r = customerService.bindCus(bindCustomer2Vo.getCustomerStandardVo(),bindCustomer2Vo.getBindCustomerVo());
                            log.info(JSONObject.toJSONString(r));
                            if(r.getCode() != 0){
                                pushData.setSuccess(1);
                                //rollback(projectId,JSONObject.toJSONString(pushData));
                                break;
                            }
                        }else if(pushData.getDataType() == PushData.DataType.Worker.getCode()){
                            WorkerVo workerVo = JSONObject.parseObject(pushData.getData(),WorkerVo.class);
                            R r = orgService.addWorker(workerVo);
                            log.info(JSONObject.toJSONString(r));
                            if(r.getCode() != 0){
                                pushData.setSuccess(1);
                                //rollback(projectId,JSONObject.toJSONString(pushData));
                                break;
                            }
                        }
                    }else if(pushData.getOperateType() == PushData.OperateType.Delete.getCode()){
                        if(pushData.getDataType() == PushData.DataType.Relationship.getCode()){
                            JSONObject jsonObject = JSONObject.parseObject(pushData.getData());
                            String roomId = jsonObject.getString("roomId");
                            String personId = jsonObject.getString("personId");
                            R r = customerService.joinOutCus(projectId,roomId,personId);
                            log.info(JSONObject.toJSONString(r));
                            if(r.getCode() != 0){
                                pushData.setSuccess(1);
                                //rollback(projectId,JSONObject.toJSONString(pushData));
                                break;
                            }
                        }else if(pushData.getDataType() == PushData.DataType.Worker.getCode()){
                            JSONObject jsonObject = JSONObject.parseObject(pushData.getData());
                            String phone = jsonObject.getString("phone");
                            R r = orgService.delWorker(projectId,phone);
                            log.info(JSONObject.toJSONString(r));
                            if(r.getCode() != 0){
                                pushData.setSuccess(1);
                                //rollback(projectId,JSONObject.toJSONString(pushData));
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    rollback(projectId,object);
                    e.printStackTrace();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(projectId != null){
                queueDoing.remove(projectId);
                if(RedisUtil.sExistValue("wjy_project_set",projectId.toString())){
                    setQueue(projectId);
                }
            }

        }
    }
    private void rollback(Integer projectId, Object object){
        log.info("失败回滚");
        if(RedisUtil.sExistValue("wjy_project_set",projectId.toString())){
            RedisUtil.lLeftSet("wjy_project"+projectId,object);
        }
    }
}
