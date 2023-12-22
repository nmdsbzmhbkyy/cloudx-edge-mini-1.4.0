//package com.aurine.cloudx.estate.rabbitMq;
//
//import com.alibaba.fastjson.JSONObject;
//import com.aurine.cloudx.common.data.project.ProjectContextHolder;
//import com.aurine.cloudx.estate.service.ProjectPaymentRecordService;
//import com.aurine.cloudx.estate.vo.ProjectPaymentRecordVo;
//import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component //路由模式
//@Slf4j
// public class rabbitConsumerTopic {
//    @Autowired
//    private ProjectPaymentRecordService projectPaymentRecordService;
//
//    @RabbitListener(bindings = {
//            @QueueBinding(value = @Queue,//临时队列
//                    exchange =@Exchange(value= "topics",type = "topic"),//绑定交换机
//                    key = {"order.save"}
//            )
//    })
//    public void payBillSaveListener(String message){
//        log.info("接收到订单生成消息");
//        ProjectPaymentRecordVo projectPaymentRecordVo = JSONObject.parseObject(message, ProjectPaymentRecordVo.class);
//        try {
//            TenantContextHolder.setTenantId(1);
//            projectPaymentRecordService.savePaymentNotPaid(projectPaymentRecordVo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//
//    }
//
//    @RabbitListener(bindings = {
//            @QueueBinding(value = @Queue,//临时队列
//                    exchange =@Exchange(value= "topics",type = "topic"),//绑定交换机
//                    key = {"order.update"}
//            )
//    })
//    public void payBillUpdateListener(Map message){
//        log.info("接收到订单更新");
//        try {
//            TenantContextHolder.setTenantId(1);
//            ProjectContextHolder.setProjectId(ProjectContextHolder.getProjectId());
//            projectPaymentRecordService.updateOrderStatus(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//
//    }
// }