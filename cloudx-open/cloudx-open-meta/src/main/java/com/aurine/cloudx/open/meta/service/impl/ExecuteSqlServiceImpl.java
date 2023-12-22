package com.aurine.cloudx.open.meta.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.core.util.RedisUtils;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.meta.mapper.ExecuteSqlRequestMapper;
import com.aurine.cloudx.open.meta.service.ExecuteSqlService;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-报警事件管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
@Slf4j
public class ExecuteSqlServiceImpl implements ExecuteSqlService {


    @Resource
    private ExecuteSqlRequestMapper executeSqlRequestMapper;


    @Override
    public R<Boolean> save(OpenApiModel model) {
        log.info("model{},", model);
        String sql = (String) JSONObject.parseObject(JSONObject.toJSONString(model.getData())).get("sql");
        RedisUtils.set("sql", true);
        boolean result = false;
        try {
            result = executeSqlRequestMapper.addSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.del("sql");
        }

        if (!result) return Result.fail(false, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(true);
    }

    @Override
    public R<Boolean> update(OpenApiModel model) {
        log.info("model{},", model);
        String sql = (String) JSONObject.parseObject(JSONObject.toJSONString(model.getData())).get("sql");

        boolean result = false;
        try {
            RedisUtils.set("sql", true);
            result = executeSqlRequestMapper.updateSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.del("sql");
        }

        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }

    @Override
    public R<Boolean> delete(OpenApiModel model) {
        log.info("model{},", model);
        String sql = (String) JSONObject.parseObject(JSONObject.toJSONString(model.getData())).get("sql");

        boolean result = false;
        try {
            RedisUtils.set("sql", true);
            result = executeSqlRequestMapper.deleteSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.del("sql");
        }

        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }

//    public static void main(String[] args) {
//        String a ="{\"data\":{\"sql\":\"UPDATE project_device_info SET deviceRegionId='0a98c0e7a04e43f8abd9e2aa029156b4' WHERE (deviceId IN (null))\"},\"header\":{\"appId\":\"2\",\"projectId\":1000000708,\"projectUUID\":\"698542830A9D4B04AEB46EE7DA8D2333\",\"tenantId\":1}}";
//        OpenApiModel o = (OpenApiModel)JSONObject.parseObject(a, OpenApiModel.class);
//        String sql = (String)JSONObject.parseObject(JSONObject.toJSONString(o.getData())).get("sql");
//        System.out.println(sql);
//    }
}
