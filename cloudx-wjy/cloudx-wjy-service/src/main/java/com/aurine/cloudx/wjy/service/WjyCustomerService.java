package com.aurine.cloudx.wjy.service;

import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.vo.BindCustomerVo;
import com.aurine.cloudx.wjy.vo.CustomerStandardVo;
import com.aurine.cloudx.wjy.pojo.RDataList;
import com.aurine.cloudx.wjy.pojo.RDataRowsPager;
import com.aurine.cloudx.wjy.pojo.R;
import com.aurine.cloudx.wjy.pojo.RDataSuccessCount;
import com.aurine.cloudx.wjy.vo.WjyCustomer;
import com.aurine.cloudx.wjy.vo.WjyRoomCustomer;

import java.util.Date;
import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/4/20
 * @description： 我家云客户管理接口
 */
public interface WjyCustomerService {
    R<RDataRowsPager<WjyRoomCustomer>> roomGetInListCustomer(int current,
                                                             int rowCount,
                                                             Project project,
                                                             String pids,
                                                             String buildingIds,
                                                             String roomIds,
                                                             String roomNames,
                                                             String searchPhrase,
                                                             String idCardNos);
    boolean customerStandardAdd(Project project, List<CustomerStandardVo> customer);
    R<RDataList<WjyCustomer>> customerGetList(int current, int rowCount, Project project, Date updateBegin, Date updateEnd,
                                              String likeName, String areaName);
    boolean roomBindCustomer(Project project, List<BindCustomerVo> customers);
    boolean customerStandardCheckout(Project project, String room, String phone);
}
