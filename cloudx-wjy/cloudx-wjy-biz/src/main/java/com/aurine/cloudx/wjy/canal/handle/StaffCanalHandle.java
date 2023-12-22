package com.aurine.cloudx.wjy.canal.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.aurine.cloudx.estate.feign.RemoteStaffService;
import com.aurine.cloudx.wjy.canal.utils.CanalDataUtil;
import com.aurine.cloudx.wjy.constant.CertType;
import com.aurine.cloudx.wjy.constant.Constant;
import com.aurine.cloudx.wjy.constant.CustomerType;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.pojo.PushData;
import com.aurine.cloudx.wjy.service.ProjectService;
import com.aurine.cloudx.wjy.utils.RedisUtil;
import com.aurine.cloudx.wjy.vo.CustomerStandardVo;
import com.aurine.cloudx.wjy.vo.WorkerVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/5/12
 * @description： 房间数据变化处理
 */
@Component
@Slf4j
public class StaffCanalHandle extends AbstractCanalHandler {

    private final String TABLE_NAME = "project_staff";
    @Resource
    ProjectService projectService;

    @Override
    public boolean handle(CanalEntry.Entry canalEntry) {
        //init
        String tableName = canalEntry.getHeader().getTableName();
        CanalEntry.RowChange rowChange = CanalDataUtil.getRowChange(canalEntry);
        CanalEntry.EventType eventType = rowChange.getEventType();

        //----------------条件筛选 开始----------------
        if (!StringUtils.equalsIgnoreCase(tableName, TABLE_NAME)) {
            return next();
        }

        if (!(eventType == CanalEntry.EventType.INSERT || eventType == CanalEntry.EventType.DELETE)) {
            return next();
        }
        //----------------条件筛选 结束----------------

        super.logCanalEntry(canalEntry, eventType);

        //记录行
        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            //获取指定记录列，并转换为JSON数据
            List<CanalEntry.Column> columnList = null;
            String projectId = null;
            if (eventType == CanalEntry.EventType.INSERT){
                columnList = this.getListByName(rowData.getAfterColumnsList(), "staffId", "staffCode", "mobile", "staffName");
                projectId = getProjectId(rowData.getAfterColumnsList());
            }else if(eventType == CanalEntry.EventType.DELETE){
                columnList = this.getListByName(rowData.getBeforeColumnsList(), "mobile");
                projectId = getProjectId(rowData.getBeforeColumnsList());
            }
            if (CollUtil.isNotEmpty(columnList)) {
                JSONObject dataJson = new JSONObject();
                for (CanalEntry.Column column : columnList) {
                    dataJson.put(column.getName(), column.getValue());
                }

                if(!RedisUtil.sExistValue("wjy_project_set",projectId)){//判断该项目是否需要同步我家云
                    System.out.println("未开启同步不处理");
                    return next();
                }
                if(StringUtils.isBlank(dataJson.getString("mobile"))){
                    System.out.println("手机号不存在不处理");
                    return next();
                }

                //封装员工数据
                WorkerVo workerVo = new WorkerVo();
                //workerVo
                //cus.setBirthday("1999-01-09");
                workerVo.setNumber(dataJson.getString("mobile"));
                workerVo.setName(dataJson.getString("staffName"));
                workerVo.setPhone(dataJson.getString("mobile"));
                workerVo.setSourceID(dataJson.getString("staffId"));
                workerVo.setSourceSystem(Constant.source);
                Project project = projectService.getByProjectId(Integer.valueOf(projectId));
                if(project == null || project.getOrgName() == null){
                    System.out.println("组织不存在不处理");
                    return next();
                }
                workerVo.setOrgUnitNames(project.getOrgName());
                workerVo.setProjectId(Integer.valueOf(projectId));
                //构造缓存数据
                PushData pushData = new PushData();
                pushData.setDataType(PushData.DataType.Worker.getCode());
                pushData.setTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
                if (eventType == CanalEntry.EventType.INSERT){
                    pushData.setOperateType(PushData.OperateType.Insert.getCode());
                    pushData.setData(JSONObject.toJSONString(workerVo));
                }else if(eventType == CanalEntry.EventType.DELETE){
                    pushData.setOperateType(PushData.OperateType.Delete.getCode());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone",dataJson.getString("mobile"));
                    pushData.setData(jsonObject.toJSONString());
                }

                log.info("客户数据存入缓存："+JSONObject.toJSONString(pushData));
                RedisUtil.lSet("wjy_project"+projectId,JSONObject.toJSONString(pushData));
            }

        }


        return done();
    }
}