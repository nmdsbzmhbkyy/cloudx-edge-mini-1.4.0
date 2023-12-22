package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectHouseService;
import com.aurine.cloudx.estate.vo.ProjectHouseServiceInfoVo;
import com.aurine.cloudx.estate.vo.ServiceHouseIdsSaveVo;
import com.aurine.cloudx.estate.vo.ServiceHouseSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 房屋增值服务
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 15:23:14
 */
public interface ProjectHouseServiceService extends IService<ProjectHouseService> {

    /**
     * 关闭到期的房屋增值服务
     */
    void removeExpireHouseService();

    /**
     * 关闭远端当前房屋下的所有住户的增值服务
     *
     * @param houseId
     */
    void removeRemoteHouseService(String houseId, List<String> serviceIds);

    /**
     *
     * 新增房屋增值服务
     * @param serviceHouseSaveVo
     *
     * @return
     */
    Boolean saveByHouse(ServiceHouseSaveVo serviceHouseSaveVo);

    /**
     *
     * 批量新增房屋增值服务
     * @param serviceHouseIdsSaveVo
     * @return
     */
    Boolean saveByHouseIds(ServiceHouseIdsSaveVo serviceHouseIdsSaveVo);

    /**
     *
     * 根据房屋Id查询该房屋增值服务
     * @param
     * @return
     */
    List <ProjectHouseServiceInfoVo> getHouseServiceByHouseId(String houseId);


}
