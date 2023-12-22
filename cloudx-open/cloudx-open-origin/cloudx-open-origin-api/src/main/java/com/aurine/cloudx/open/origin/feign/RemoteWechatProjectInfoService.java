package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.vo.ProjectInfoPageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;


/**
 * (remoteUmsTokenService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 11:17
 */
@FeignClient(contextId = "openRemoteWechatProjectInfoService", value = "cloudx-estate-wechat")
public interface RemoteWechatProjectInfoService {
    /**
     * 分页查询当前员工负责的小区列表
     *
     * @return 放回小区分页信息
     */
    @GetMapping("/projectInfo/page/byStaff")
    R<Page<ProjectInfoPageVo>> pageProjectByStaff(@SpringQueryMap Map<String, Object> map);
}
