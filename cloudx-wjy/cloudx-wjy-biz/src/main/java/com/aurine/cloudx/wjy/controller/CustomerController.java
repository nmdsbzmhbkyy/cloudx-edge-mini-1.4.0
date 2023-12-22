package com.aurine.cloudx.wjy.controller;

import com.aurine.cloudx.wjy.entity.Customer;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.entity.Room;
import com.aurine.cloudx.wjy.service.RoomService;
import com.aurine.cloudx.wjy.vo.*;
import com.aurine.cloudx.wjy.service.CustomerService;
import com.aurine.cloudx.wjy.service.ProjectService;
import com.aurine.cloudx.wjy.service.impl.CustomerServiceImpl;
import com.aurine.cloudx.wjy.service.impl.ProjectServiceImpl;
import com.aurine.cloudx.wjy.pojo.RDataList;
import com.aurine.cloudx.wjy.service.WjyCustomerService;
import com.aurine.cloudx.wjy.service.impl.WjyCustomerServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cus")
@Api(value = "cus", tags = "客户接口")
public class CustomerController {
    @Resource
    CustomerService customerService;

    /**
     * 功能描述: 添加/更新客户信息
     *
     * @author huangjj
     * @date 2021/4/14
     * @param customerVo 客户信息对象
     * @return 返回添加结果
    */
    @PostMapping("/standard/add")
    @Inner(value = false)
    @ApiOperation(value = "标准客户添加接口", notes = "标准客户添加接口")
    public R addStandardCus(@RequestBody CustomerStandardVo customerVo){
        return customerService.addStandardCus(customerVo);
    }
    @PostMapping("/add")
    @Inner(value = false)
    @ApiOperation(value = "客户添加接口", notes = "客户添加接口")
    public R addCus(@RequestBody CustomerVo customerVo){

        return null;
    }

    @PostMapping("/room/bind")
    @Inner(value = false)
    @ApiOperation(value = "房间绑定接口", notes = "房间绑定接口")
    public R bindCus(@RequestBody BindCustomer2Vo bindCustomer2Vo){
        return customerService.bindCus(bindCustomer2Vo.getCustomerStandardVo(),bindCustomer2Vo.getBindCustomerVo());
    }

    @PostMapping("/room/joinout")
    @Inner(value = false)
    @ApiOperation(value = "客户迁出接口", notes = "客户迁出接口")
    public R joinOutCus(@RequestParam(value = "projectId") Integer projectId,
                        @RequestParam(value = "roomId") String roomId,
                        @RequestParam(value = "personId") String personId){
        return customerService.joinOutCus(projectId,roomId,personId);
    }
}
