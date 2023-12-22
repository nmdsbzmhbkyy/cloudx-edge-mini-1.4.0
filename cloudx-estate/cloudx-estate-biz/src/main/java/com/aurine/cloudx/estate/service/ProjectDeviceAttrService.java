

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.DeviceAttrDto;
import com.aurine.cloudx.estate.vo.ProjectDeviceAttrFormVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceAttrListVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectDeviceAttr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备拓展属性表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 15:18:23
 */
public interface ProjectDeviceAttrService extends IService<ProjectDeviceAttr> {

    List<ProjectDeviceAttrListVo> getDeviceAttrListVo(Integer projectId,String  type, String deviceId);

    boolean updateDeviceAttrList(ProjectDeviceAttrFormVo projectDeviceAttrFormVo);

    /**
     * <p>
     * 如果没有这个拓展属性则会增加 attrName 的拓展属性，同时保存拓展属性数据，否则会更新拓展属性数据
     * </p>
     *
     * @param attrName 拓展属性名
     * @param attrNameCn 拓展属性中文名
     * @param deviceId 设备ID
     * @param attrValue 拓展属性数据
     * @author: 王良俊
     */
    void saveDeviceAttr(String deviceId, String attrName, String attrNameCn, String attrValue);
/**
 * @description 根据第三方编码 拓展字段 拓展字段值查询设备是否有值
 * @param String thirdpartyCode
 * @param String attrCode
 * @param String attrValue
 * @return java.util.List<com.aurine.cloudx.estate.dto.DeviceAttrDto>
 * @throws
 * @author cyw
 * @time 2023-04-26 17:04
 */
    List<DeviceAttrDto> getDeviceAttrKeyAndValue(String thirdpartyCode, String attrCode,String attrValue);
}
