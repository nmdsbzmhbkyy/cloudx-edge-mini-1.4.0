package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.vo.ProjectBillPromotionVo;
import com.aurine.cloudx.open.origin.vo.ProjectBillingInfoVo;
import com.aurine.cloudx.open.origin.dto.ProjectBillingInfoFormDTO;
import com.aurine.cloudx.open.origin.vo.AppProjectBillingInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ： yz
 * @date ： 2021/7/6
 * @description： 房屋账单信息
 */
@FeignClient(contextId = "openRemoteBillingInfoService", value = "cloudx-estate-biz")
public interface RemoteBillingInfoService {


    @GetMapping("/projectBillingInfo/listOnPromotionApp/{id}")
    R<HashMap<String, Map<String, List<ProjectBillPromotionVo>>>> listOnPromotionApp(@PathVariable("id") String id);

    @GetMapping("/projectBillingInfo/pageApp")
    R<Page<ProjectBillingInfoVo>> selectAll(@SpringQueryMap ProjectBillingInfoFormDTO projectBillingInfo);

    @GetMapping("/projectBillingInfo/getDict")
    R<List<Map<String, String>>> getDict();


    @GetMapping("/projectBillingInfo/getSumFeeByToday/{orderNo}")
    R<List<AppProjectBillingInfoVo>> getDetailByOrder(@PathVariable("orderNo") String orderNo);

     @GetMapping("/projectBillingInfo/call/{houseId}")
     R callByHouseId(@PathVariable("houseId") String houseId);

     @GetMapping("/projectBillingInfo/getAppMoneyByBillingNo/{billingNo}")
     R getAppMoneyByBillingNo(@PathVariable("billingNo") String billingNo);

}