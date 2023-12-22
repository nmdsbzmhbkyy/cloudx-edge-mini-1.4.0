

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.SysDeviceTypeModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 设备产品和设备类型映射
 * </p>
 * @author : 王良俊
 * @date : 2021-07-22 18:30:40
 */
@Mapper
//@CacheNamespace
public interface SysDeviceTypeModelMapper extends BaseMapper<SysDeviceTypeModel> {

}
