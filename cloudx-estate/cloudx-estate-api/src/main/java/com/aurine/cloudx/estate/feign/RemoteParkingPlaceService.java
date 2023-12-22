package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.entity.ProjectParkRegion;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * 车位管理
 */
@FeignClient(contextId = "remoteParkingPlaceService", value = "cloudx-estate-biz")
public interface RemoteParkingPlaceService {
    /**
     * 获取这个用户所拥有的车位类型
     * @author: 王良俊
     *  parkId 车场ID
     *  personId 人员ID
     * @return
     */
    @GetMapping("/baseParkingPlace/getPlaceRelTypeByPersonId" )
    R<List<String>> getPlaceRelTypeByPersonId(@SpringQueryMap Map<String,Object> map);

    /**
     * <p>
     *  获取 车位区域列表
     * </p>
     *
     * @return
     * @throws
     */
    @GetMapping("/projectParkRegion/listPersonAttrParkingRegionByRelType")
    R<List<ProjectParkRegion>> getList(@SpringQueryMap Map<String,Object> map);


    /**
     * 通过车位区域id和人员id获取到这个人在这个区域拥有的车位列表
     * @author: 王良俊
     *  projectParkingPlace 车位对象
     * @return
     */
    @GetMapping("/baseParkingPlace/listParkPlaceByRelType" )
    R<List<ProjectParkingPlace>> listParkPlaceByRelType(@SpringQueryMap Map<String,Object> map);


}
