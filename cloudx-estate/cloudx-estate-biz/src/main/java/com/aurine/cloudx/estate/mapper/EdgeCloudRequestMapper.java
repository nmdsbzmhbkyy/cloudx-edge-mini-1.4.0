

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.AdminUserInfo;
import com.aurine.cloudx.estate.entity.EdgeCloudRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>入云申请mapper</p>
 * @author : 王良俊
 * @date : 2021-12-08 11:08:02
 */
@Mapper
public interface EdgeCloudRequestMapper extends BaseMapper<EdgeCloudRequest> {

}
