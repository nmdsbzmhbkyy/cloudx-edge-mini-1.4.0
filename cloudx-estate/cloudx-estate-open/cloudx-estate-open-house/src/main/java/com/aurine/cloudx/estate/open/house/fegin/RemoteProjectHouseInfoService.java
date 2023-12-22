package com.aurine.cloudx.estate.open.house.fegin;

import com.aurine.cloudx.estate.open.house.bean.HousePage;
import com.aurine.cloudx.estate.open.house.bean.NamePage;
import com.aurine.cloudx.estate.open.house.bean.ProjectHouseInfoPage;
import com.aurine.cloudx.estate.vo.ProjectHouseInfoVo;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteProjectHouseInfoService", value = "cloudx-estate-biz")
public interface RemoteProjectHouseInfoService {

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/baseHouse/page")
    R getProjectHouseInfoPage(@SpringQueryMap ProjectHouseInfoPage page);


    /**
     * 通过id查询房屋
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/baseHouse/{id}")
    R getById(@PathVariable("id") String id);

    /**
     * 新增房屋
     *
     * @param projectHouseInfo 房屋
     * @return R
     */
    @ApiOperation(value = "新增房屋", notes = "新增房屋")
    @PostMapping("/baseHouse")
    R save(@RequestBody ProjectHouseInfoVo projectHouseInfo);

    /**
     * 修改房屋
     *
     * @param projectHouseInfo 房屋
     * @return R
     */
    @ApiOperation(value = "修改房屋", notes = "修改房屋")
    @PutMapping("/baseHouse")
    R updateById(@RequestBody ProjectHouseInfoVo projectHouseInfo);

    /**
     * 通过id删除房屋
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id删除房屋", notes = "通过id删除房屋")
    @DeleteMapping("/baseHouse/{id}")
    R removeById(@PathVariable("id") String id);

    /**
     * <p>
     * 查询该房屋的所有住户
     * </p>
     *
     * @param page 分页信息
     */
    @ApiOperation(value = "房屋住户查询")
    @GetMapping("/baseHouse/getHouseResident")
    R getHouseResident(@SpringQueryMap HousePage page);

    /**
     * <p>getHouseResident
     * 查询服务住户记录
     * </p>
     *
     */
    @ApiOperation(value = "房屋住户查询")
    @GetMapping("/baseHouse/getHouseRecord")
    R getHouseRecord(@SpringQueryMap HousePage page);

    /**
     * 统计房屋总数
     *
     * @return
     */
    @ApiOperation(value = "统计房屋总数")
    @GetMapping("/baseHouse/countHouse")
    R countHouse();

    /**
     * 根据房屋的用途进行分类统计
     *
     * @return
     */
    @ApiOperation(value = "根据房屋的用途进行分类统计")
    @GetMapping("/baseHouse/countByType")
    R countByType();

    /**
     * 根据房屋名称查询框架树信息
     * @param page
     * @param name
     * @return
     */
    @ApiOperation(value = "根据房屋名称查询框架树信息")
    @GetMapping("/baseHouse/findIndoorByName")
    R findIndoorByName(@SpringQueryMap NamePage page);
}
