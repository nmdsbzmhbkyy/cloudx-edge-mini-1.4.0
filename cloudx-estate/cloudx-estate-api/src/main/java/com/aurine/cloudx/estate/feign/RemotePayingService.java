package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.vo.ProjectPaymentRecordVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * @author ： cjw
 * @date ：
 * @description： 调用支付接口
 */
@FeignClient(contextId = "remotePayingService", value = "paying-biz")
public interface RemotePayingService {




     @PostMapping("/wxPay/miniAppYzPay")
     R miniAppYzPay(@RequestBody ProjectPaymentRecordVo projectPaymentRecordVo);



     @PostMapping("/wxPay/miniAppWyPay")
     R miniAppWyPay(@RequestBody ProjectPaymentRecordVo projectPaymentRecordVo);

     @PostMapping("/wxPay/appWyPay")
     R AppWyPay(@RequestBody ProjectPaymentRecordVo projectPaymentRecordVo);

     @PostMapping("/wxPay/platformPay")
     R platformPay (@RequestBody ProjectPaymentRecordVo projectPaymentRecordVo);

     @GetMapping ("/wxPay/queryOrder")
     R queryOrder (@RequestParam(value = "transactionId", required = false) String transactionId, @RequestParam(value = "outTradeNo", required = false) String outTradeNo,
                   @RequestParam(value = "projectId", required = true) Integer projectId);


     @GetMapping ("/wxPay/getOpenId/{code}")
     R getOpenId (@PathVariable(value = "code") String code);


     @PostMapping("/wxPay/closeOrder")
     R closeOrder (@RequestBody ProjectPaymentRecordVo projectPaymentRecordVo);
}