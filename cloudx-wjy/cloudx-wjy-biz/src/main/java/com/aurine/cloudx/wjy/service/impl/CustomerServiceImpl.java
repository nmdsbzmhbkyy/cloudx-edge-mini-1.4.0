package com.aurine.cloudx.wjy.service.impl;

import com.aurine.cloudx.wjy.entity.Customer;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.entity.Room;
import com.aurine.cloudx.wjy.pojo.RDataList;
import com.aurine.cloudx.wjy.service.CustomerService;
import com.aurine.cloudx.wjy.mapper.CustomerMapper;
import com.aurine.cloudx.wjy.service.ProjectService;
import com.aurine.cloudx.wjy.service.RoomService;
import com.aurine.cloudx.wjy.service.WjyCustomerService;
import com.aurine.cloudx.wjy.vo.BindCustomerVo;
import com.aurine.cloudx.wjy.vo.CustomerStandardVo;
import com.aurine.cloudx.wjy.vo.WjyCustomer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/4/26
 * @description： 客户接口实现类
 */
@Service
public class CustomerServiceImpl  extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    @Resource
    CustomerMapper customerMapper;
    @Resource
    ProjectService projectService;
    @Resource
    WjyCustomerService wjyCustomerService;
    @Resource
    RoomService roomService;

    @Override
    public Customer getByNameAndPhone(String name, String phone) {
        return customerMapper.selectOne(new QueryWrapper<Customer>().lambda().eq(Customer::getName,name).eq(Customer::getPhone,phone));
    }

    public R addStandardCus(CustomerStandardVo customerVo){

        Project project = projectService.getByProjectId(customerVo.getProjectId());
        if(project == null){
            return R.failed("未找到项目信息");
        }

        List<CustomerStandardVo> customerStandardVos = new ArrayList<>();
        customerStandardVos.add(customerVo);
        Boolean isSave = wjyCustomerService.customerStandardAdd(project, customerStandardVos);
        if (!isSave){
            return R.failed("添加客户失败");
        }else{
            Customer customer = getByNameAndPhone(customerVo.getName(),customerVo.getPhone());
            if(customer == null){
                customer = new Customer();
            }
            customer.setCusId(customerVo.getSourceID());
            customer.setName(customerVo.getName());
            customer.setPhone(customerVo.getPhone());
            customer.setProjectId(project.getProjectId());
            if(customer.getWjyCusId() == null){
                customer.setWjyCusId("unknown");
            }
            if(StringUtils.isNotBlank(customerVo.getNewName())){
                customer.setName(customerVo.getNewName());
            }
            if(StringUtils.isNotBlank(customerVo.getNewPhone())){
                customer.setPhone(customerVo.getNewPhone());
            }
            saveOrUpdate(customer);
        }
        return R.ok();
    }
    public R bindCus(CustomerStandardVo customerVo,BindCustomerVo bindCustomerVo){
        Project project = projectService.getByProjectId(bindCustomerVo.getProjectId());
        if(project == null){
            return R.failed("未找到项目信息");
        }
        List<BindCustomerVo> bindCustomerVos = new ArrayList<>();
        bindCustomerVos.add(bindCustomerVo);
        Boolean isSave = wjyCustomerService.roomBindCustomer(project, bindCustomerVos);
        if (!isSave){
            return R.failed("添加客户房间关系绑定失败");
        }
        com.aurine.cloudx.wjy.pojo.R<RDataList<WjyCustomer>> r = wjyCustomerService.customerGetList(1,10,project,null,null,customerVo.getName(),null);
        if(r != null && r.isSuccess()){
            try {
                List<WjyCustomer> wjyCustomers = r.getData().getList();
                if(wjyCustomers != null && wjyCustomers.size()>0){

                    for (WjyCustomer wjyCustomer:
                            wjyCustomers) {
                        if(wjyCustomer.getName().equals(customerVo.getName())){
                            Customer customer = getByNameAndPhone(customerVo.getName(),customerVo.getPhone());
                            if(customer == null){
                                customer = new Customer();
                            }
                            customer.setCusId(customerVo.getSourceID());
                            customer.setName(customerVo.getName());
                            customer.setPhone(customerVo.getPhone());
                            customer.setWjyCusId(wjyCustomer.getId());
                            customer.setProjectId(project.getProjectId());
                            if(StringUtils.isNotBlank(customerVo.getNewName())){
                                customer.setName(customerVo.getNewName());
                            }
                            if(StringUtils.isNotBlank(customerVo.getNewPhone())){
                                customer.setPhone(customerVo.getNewPhone());
                            }
                            saveOrUpdate(customer);
                            return R.ok();
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                return R.failed("无法读取客户信息");
            }
        }

        return R.failed();
    }

    public R joinOutCus(Integer projectId,
                        String roomId,
                        String cusId){
        Project project = projectService.getByProjectId(projectId);
        if(project == null){
            return R.failed("未找到项目信息");
        }
        Room room = roomService.getByRoomId(roomId);
        if(room == null){
            return R.failed("未找到房间信息");
        }
        Customer customer = getByCusId(cusId);
        if(customer == null){
            return R.failed("未找到客户信息");
        }
        Boolean isSave = wjyCustomerService.customerStandardCheckout(project,room.getRoomName(),customer.getPhone());
        if (!isSave){
            return R.failed("删除客户房间关系绑定失败");
        }
        return R.ok();
    }

    @Override
    public Customer getByCusId(String cusId) {
        return customerMapper.selectOne(new QueryWrapper<Customer>().lambda().eq(Customer::getCusId,cusId));
    }
}