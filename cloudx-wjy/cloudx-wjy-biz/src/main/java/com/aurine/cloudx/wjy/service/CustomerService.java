package com.aurine.cloudx.wjy.service;

import com.aurine.cloudx.wjy.entity.Customer;
import com.aurine.cloudx.wjy.vo.BindCustomerVo;
import com.aurine.cloudx.wjy.vo.CustomerStandardVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ： huangjj
 * @date ： 2021/4/25
 * @description： 客户接口
 */
public interface CustomerService extends IService<Customer> {
    Customer getByNameAndPhone(String name, String phone);
    R addStandardCus(CustomerStandardVo customerVo);
    R bindCus(CustomerStandardVo customerVo,BindCustomerVo bindCustomerVo);
    R joinOutCus(Integer projectId,
                 String roomId,
                 String cusId);
    Customer getByCusId(String cusId);
}
