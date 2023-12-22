package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.entity.ProjectRepairRecord;
import com.aurine.cloudx.estate.vo.ProjectRepairRecordResultFormVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(contextId = "remoteRepairRecordService", value = "cloudx-estate-biz")
public interface RemoteRepairRecordService {

    /**
     * 抢单
     *
     * @param projectRepairRecord
     * @return R
     */
    @PutMapping("/projectRepairRecord/robOrder")
    public R robOrder(@RequestBody ProjectRepairRecord projectRepairRecord);

    /**
     * 派单
     *
     * @param projectRepairRecord
     * @return R
     */
    @PutMapping("/projectRepairRecord/sendOrder")
    public R sendOrder(@RequestBody ProjectRepairRecord projectRepairRecord);

    /**
     * 完成订单
     *
     * @param projectRepairRecordResultFormVo
     * @return R
     */
    @PutMapping("/projectRepairRecord/doneOrder")
    public R doneOrder(@RequestBody ProjectRepairRecordResultFormVo projectRepairRecordResultFormVo);
}
