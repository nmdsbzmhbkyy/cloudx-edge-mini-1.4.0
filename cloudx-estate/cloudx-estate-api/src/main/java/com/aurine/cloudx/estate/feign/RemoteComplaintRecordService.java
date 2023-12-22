package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.entity.ProjectComplaintRecord;
import com.aurine.cloudx.estate.vo.ProjectComplainRecordResultFormVo;
import com.aurine.cloudx.estate.vo.ProjectComplaintRecordInfoVo;
import com.aurine.cloudx.estate.vo.ProjectComplaintRecordVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(contextId = "remoteComplaintRecordService", value = "cloudx-estate-biz")
public interface RemoteComplaintRecordService {
    @GetMapping("/projectComplaintRecord/page")
    R<Page<ProjectComplaintRecordVo>> pageByComplainRecordList(@SpringQueryMap Map<String, Object> query);

    @GetMapping("/projectComplaintRecord/{id}")
    R<ProjectComplaintRecordInfoVo> getComplainRecordInfo(@PathVariable("id") String complaintId);

    @PutMapping("/projectComplaintRecord")
    R updateById(@RequestBody ProjectComplaintRecord projectComplaintRecord);

    @PostMapping("/projectComplaintRecord")
    R save(@RequestBody ProjectComplaintRecord projectComplaintRecord);

    /**
     * 派单
     *
     * @param projectComplaintRecord
     * @return R
     */
    @PutMapping("/projectComplaintRecord/sendOrder")
    public R sendOrder(@RequestBody ProjectComplaintRecord projectComplaintRecord);

    /**
     * 抢单
     *
     * @param projectComplaintRecord
     * @return R
     */
    @PutMapping("/projectComplaintRecord/robOrder")
    public R robOrder(@RequestBody ProjectComplaintRecord projectComplaintRecord);

    /**
     * 完成订单
     *
     * @return R
     */
    @PutMapping("/projectComplaintRecord/doneOrder")
    public R doneOrder(@RequestBody ProjectComplainRecordResultFormVo projectComplainRecordResultFormVo);
}
