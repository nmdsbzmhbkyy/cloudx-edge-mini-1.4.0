package com.aurine.cloudx.dashboard.controller;//package com.aurine.dashboard.controller;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.canal.CanalClient;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.handler.chain.DashboardHandleChain;
import com.aurine.cloudx.dashboard.service.DashboardServicePolicyFactory;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import com.aurine.cloudx.dashboard.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-22
 * @Copyright:
 */
@RestController
@RequestMapping("/dashboard")
@Slf4j
public class DashboardController {

    @Resource
    private CanalClient canalClient;
    @Resource
    private DashboardHandleChain dashboardHandleChain;


    /**
     * 查询服务
     *
     * @param serviceName
     * @param version
     * @return
     */
    @RequestMapping("/{projectId}/{serviceName}/{version}")
    public Result<JSONObject> getData(@PathVariable("projectId") String projectId, @PathVariable("serviceName") String serviceName, @PathVariable("version") String version, HttpServletRequest request) {
        DashboardRequestVo vo = new DashboardRequestVo();
        vo.setProjectId(projectId);
        vo.setServiceName(serviceName);
        vo.setVersion(version);
        vo.setParams(this.getParams(request));


        log.info("[dashboard] 获取请求：{}", vo);
        try {

            return Result.ok(DashboardServicePolicyFactory.getService(vo.getServiceName(), vo.getVersion()).getData(vo), vo.getProjectId(), vo.getServiceName(), vo.getVersion());

        } catch (DashboardException de) {
            return Result.failed(de.getErrorType(), vo);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("服务器错误", vo);
        }


    }

    /**
     * 查询服务
     *
     * @param vo
     * @return
     */
    @PostMapping()
    public Result getByService(@RequestBody DashboardRequestVo vo) {

        log.info("[dashboard] 获取请求：{}", vo);
        try {

            return Result.ok(DashboardServicePolicyFactory.getService(vo.getServiceName(), vo.getVersion()).getData(vo), vo.getProjectId(), vo.getServiceName(), vo.getVersion());

        } catch (DashboardException de) {
            return Result.failed(de.getErrorType(), vo);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("服务器错误", vo);
        }

    }


    /**
     * 获取HTTP请求的参数
     */
    public JSONObject getParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        for (Enumeration<String> e = params; e.hasMoreElements(); ) {
            String key = e.nextElement().toString();
            jsonObject.put(key, request.getParameter(key));
        }
        return jsonObject;
    }

}
