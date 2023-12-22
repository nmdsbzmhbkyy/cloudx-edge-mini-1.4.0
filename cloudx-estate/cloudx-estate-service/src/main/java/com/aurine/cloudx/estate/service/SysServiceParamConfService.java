package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysServiceParamConf;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 平台设备参数定义表(SysServiceParamConf)表服务接口
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:38
 */
public interface SysServiceParamConfService extends IService<SysServiceParamConf> {

    /**
     * <p>
     *  根据服务ID获取到这些服务ID对应的表单数据
     * </p>
     *
     * <br> [
     * <br>   {
     * <br>         "serviceName":"设备编号对象",
     * <br>         "serviceId":"DeviceNoObj",
     * <br>         "formItems":[
     * <br>             {
     * <br>                 "seq":1,
     * <br>                 "serviceId":"DeviceNoObj",
     * <br>                 "serviceName":"设备编号对象",
     * <br>                 "paramId":"deviceType",
     * <br>                 "paramName":"设备类型",
     * <br>                 "isMandatory":"1",
     * <br>                 "isSync":"0",
     * <br>                 "isModify":"0",
     * <br>                 "columnType":"int",
     * <br>                 "defaultValue":"0",
     * <br>                 "valueRange":null,
     * <br>                 "inputType":"select",
     * <br>                 "dictMap":"0:梯口机,1:区口机",
     * <br>                 "remark":"0：梯口机（默认）1： 区口机",
     * <br>                 "parServId":"0",
     * <br>                 "parParamId":"0",
     * <br>                 "servLevel":1,
     * <br>                 "tenantId":null,
     * <br>                 "createTime":Object{...},
     * <br>                 "updateTime":Object{...},
     * <br>                 "props":"DeviceNoObj/deviceType",
     * <br>                 "rootServiceId":"DeviceNoObj",
     * <br>                 "parParamIds":"/deviceType",
     * <br>                 "formItemList":[
     * <br>
     * <br>                 ]
     * <br>             },
     * <br>             Object{...},
     * <br>             Object{...},
     * <br>             Object{...},
     * <br>             Object{...},
     * <br>             Object{...},
     * <br>             Object{...},
     * <br>             Object{...},
     * <br>             Object{...},
     * <br>             Object{...}
     * <br>         ]
     * <br>     },
     * <br> ]
     *
     * @param serviceIdList 服务ID集合
     * @return 表单的json数据
    */
    String getFormParamDataByServiceId(List<String> serviceIdList);

    List<String> getRebootServiceIdList();
    /**
     * <p>
     *  获取到可以进行参数设置的参数服务ID
     * </p>
     *
     * @param serviceIdList 需要继续筛选的参数服务ID
    */
    List<String> getValidServiceIdList(List<String> serviceIdList);

}