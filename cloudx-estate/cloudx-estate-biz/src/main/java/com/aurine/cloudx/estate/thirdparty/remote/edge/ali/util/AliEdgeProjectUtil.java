package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.util;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectEntryExitLane;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 阿里边缘网关项目工具
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-11
 * @Copyright:
 */

@Component
public class AliEdgeProjectUtil {

    @Resource
    private ProjectInfoService projectInfoService;
    /**
     * 通过4.0项目ID获取阿里边缘项目ID
     *
     * @param projectId
     * @return
     */
    public String getAliEdgeProject(Integer projectId) {
        return "a124zqUIBWbYpNmB";
    }

    public List<ProjectEntryExitLane> getBarrierList(JSONObject jsonObject){
        return new ArrayList<ProjectEntryExitLane>();

    }

    public List<String> getBarrierListId( List<ProjectEntryExitLane> projectEntryExitLanes){
        return new ArrayList<String>();

    }
}
