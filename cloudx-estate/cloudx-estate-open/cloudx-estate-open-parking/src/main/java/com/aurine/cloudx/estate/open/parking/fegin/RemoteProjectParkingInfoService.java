package com.aurine.cloudx.estate.open.parking.fegin;

import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.open.parking.bean.ParkingPage;
import com.aurine.cloudx.estate.vo.ProjectParkingInfoVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteProjectParkingInfoService", value = "cloudx-estate-biz")
public interface RemoteProjectParkingInfoService {

    /**
     * 分页查询
     * @param page
     * @return
     */
    @GetMapping("/baseParkingArea/page")
    R getParkingInfoPage(@SpringQueryMap ParkingPage page);

    /**
     * 获取当前项目下所有数据
     * @return
     */
    @GetMapping("/baseParkingArea/list")
    R<ProjectParkingInfoVo> list();

    /**
     * 通过id查询停车场
     * @param parkId
     * @return
     */
    @GetMapping("/baseParkingArea/{parkId}")
    R<ProjectParkingInfoVo> getById(@PathVariable("parkId") String parkId);

    /**
     * 停车场信息配置
     * @param parkingInfo
     * @return
     */
    @PutMapping("/baseParkingArea")
    R updateById(@RequestBody ProjectParkingInfo parkingInfo);

    /**
     * 通过id删除停车场
     * @param parkId
     * @param projectId
     * @return
     */
    @DeleteMapping("/baseParkingArea/{parkId}/{projectId}")
    R removeById(@PathVariable("parkId") String parkId, @PathVariable("projectId") Integer projectId);

    /**
     * 通过id查询停车场支付二维码
     * @param parkId
     * @return
     */
    @GetMapping("/baseParkingArea/payImgUrl/{parkId}")
    R<String> getPayUrlById(@PathVariable("parkId") String parkId);
}
