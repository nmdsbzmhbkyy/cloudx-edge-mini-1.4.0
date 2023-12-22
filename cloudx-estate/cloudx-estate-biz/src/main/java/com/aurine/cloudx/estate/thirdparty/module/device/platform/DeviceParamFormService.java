package com.aurine.cloudx.estate.thirdparty.module.device.platform;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.vo.DeviceParamDataVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

/**
 * <p>设备参数服务</p>
 *
 * @author : 王良俊
 * @date : 2021-10-19 17:01:58
 */
public interface DeviceParamFormService extends BaseParamService {


    /**
     * <p>根据服务ID获取到这些服务ID对应的表单数据</p>
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
     * <br>             Object{...}
     * <br>         ]
     * <br>     },
     * <br> ]
     *
     * @param serviceIdList 服务ID集合
     * @return 表单的json数据
     */
    String getParamFormJson(List<String> serviceIdList, String deviceId) throws JsonProcessingException;

    /**
     * <p>获取参数数据</p>
     *
     * @param deviceId 设备ID
     * @param productId 设备产品ID
     * @return 设备参数数据Vo对象
     */
    DeviceParamDataVo getParamByDeviceId(String deviceId, String productId);

    PlatformEnum getPlateForm();
}
