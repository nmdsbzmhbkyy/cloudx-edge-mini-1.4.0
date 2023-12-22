package com.aurine.cloudx.estate.open.house.fegin;

import com.aurine.cloudx.estate.entity.ProjectHouseDesign;
import com.aurine.cloudx.estate.open.house.bean.BuildingPage;
import com.aurine.cloudx.estate.vo.BuildingPublicFloorVo;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchVo;
import com.aurine.cloudx.estate.vo.ProjectBuildingInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(contextId = "remoteProjectBuildingInfoService", value = "cloudx-estate-biz")
public interface RemoteProjectBuildingInfoService {

    /**
     * 分页查询
     */
    @GetMapping("/baseBuilding/page")
    R<Page<ProjectHouseDesign>> getBuildingInfoPage(@SpringQueryMap BuildingPage page);

    /**
     * 获取项目下的楼栋列表
     */
    @GetMapping("/baseBuilding/list")
    R list();

    /**
     * 获取项目下的楼栋列表
     */
    @GetMapping("/baseBuilding/listWithGroup")
    R listWithGroup(@RequestParam("name") String name);

    /**
     * 通过楼栋ID获取到楼栋所属区域的信息
     */
    @GetMapping("/baseBuilding/regionByBuildingId")
    R regionByBuildingId(@RequestParam("buildingId") String buildingId);

    /**
     * 通过id查询楼栋
     */
    @GetMapping("/baseBuilding/{buildingid}")
    R getById(@PathVariable("buildingid") String buildingid);

    /**
     * 查询当前项目所有组团列表
     */
    @GetMapping("/baseBuilding/frameList")
    R getFrameList();

    /**
     * 新增楼栋
     */
    @PostMapping("/baseBuilding")
    R save(@RequestBody ProjectBuildingInfoVo building);

    /**
     * 批量新增楼栋
     */
    @PostMapping("/baseBuilding/batch")
    R saveBatch(@RequestBody ProjectBuildingBatchVo vo);

    /**
     * 修改楼栋
     */
    @PutMapping("/baseBuilding")
    R updateById(@RequestBody ProjectBuildingInfoVo building);

    /**
     * 通过id删除楼栋
     */
    @DeleteMapping("/baseBuilding/{buildingid}")
    R removeById(@PathVariable("buildingid") String buildingid);

    /**
     * 统计楼栋总数
     */
    @GetMapping("/baseBuilding/countBuilding")
    R countBuilding();

    /**
     * 根据楼栋id获取其下的房屋名称
     */
    @GetMapping("/baseBuilding/list/house-name/{buildingId}")
    R listHouseNameByBuildingId(@PathVariable("buildingId") String buildingId);

    /**
     * 查询楼栋框架号规则
     */
    @GetMapping("/baseBuildingFrameCfg/getCodeRuleByLevel/{level}")
    R getCodeRuleByLevel(@PathVariable("level") String level);

    @PutMapping("/baseBuilding/public-floor/batch")
    R savePublicFloorsBatch(BuildingPublicFloorVo list);
}
