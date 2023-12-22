package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.vo.ProjectFrameInfoTreeVo;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/5/13
 * @description： 楼栋管理接口
 */
@FeignClient(contextId = "remoteFrameInfoService", value = "cloudx-estate-biz")
public interface RemoteFrameInfoService {

    /**
     * 获取子系统树，带根节点
     *
     * @return
     */
    @GetMapping("/baseBuildingFrame/findTreeWithRoot")
    R<List<ProjectFrameInfoTreeVo>> findTreeWithRoot();


    @GetMapping("/baseBuildingFrame/excel/checkHouseIsCorrect/{address}/{isGroup}")
    R checkHouseIsCorrect(@PathVariable("address") String address, @PathVariable("isGroup") boolean isGroup);

}