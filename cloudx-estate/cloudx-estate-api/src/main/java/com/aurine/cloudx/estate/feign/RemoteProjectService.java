package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.vo.ProjectVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 项目配置
 * @author ：huangjj
 * @date ：2021/4/15
 * @description：项目配置路由
 */
@FeignClient(contextId = "remoteProjectService", value = "cloudx-wjy-biz")
public interface RemoteProjectService {
    /**
     * 功能描述: 配置项目参数
     *
     * @author huangjj
     * @date 2021/4/15
     * @param projectVo 项目参数对象
     * @return 返回添加结果
     */
    @PostMapping("/project/config")
    public R projectConfig(@RequestBody ProjectVo projectVo);
    @PostMapping("/project/enable")
    public R projectEnable(@RequestParam("projectId") Integer projectId, @RequestParam("enable") boolean enable);
    @GetMapping("/project/info")
    public R projectInfo(@RequestParam("projectId") Integer projectId);
}
