package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.vo.AppProjectPaymentRecordFromVo;
import com.aurine.cloudx.open.origin.vo.ProjectPaymentRecordVo;
import com.aurine.cloudx.open.origin.vo.AppProjectPaymentRecordVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 调用账单信息
 */
@FeignClient(contextId = "openRemotePaymentRecordController", value = "cloudx-estate-biz")
public interface RemotePaymentRecordService {




    /**
     * 分页查询所有数据 物业端
     */
    @GetMapping("/projectPaymentRecord/pageApp")
    R<Page<AppProjectPaymentRecordVo>> selectAllApp(@SpringQueryMap AppProjectPaymentRecordFromVo appProjectPaymentRecordVo);


    @PostMapping(value = "/projectPaymentRecord/checkCode")
    R checkCode(@RequestBody ProjectPaymentRecordVo projectPaymentRecord);

    @GetMapping(value = "/projectPaymentRecord/checkOrderStatus/{payOrderNo}")
    R checkOrderStatus(@PathVariable("payOrderNo") String payOrderNo);




}