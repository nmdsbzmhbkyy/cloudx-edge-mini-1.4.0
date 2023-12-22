

package com.aurine.cloudx.estate.controller;


import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.feign.RemoteParkingPlaceService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>车位管理</p>
 * @ClassName: ProjectParkingPlaceController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/8 10:40
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/baseParkingPlace" )
@Api(value = "baseParkingPlace", tags = "车位管理")
public class ProjectParkingPlaceController {
    @Resource
    private RemoteParkingPlaceService remoteParkingPlaceService;

    /**
     * 获取这个用户所拥有的车位类型
     * @author: 王良俊
     * @param projectParkingPlace 车位对象
     * @return
     */
    @ApiOperation(value = "获取这个用户所拥有的车位类型")
    @GetMapping("/getPlaceRelTypeByPersonId" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "停车场ID", required = true, paramType = "param"),
            @ApiImplicitParam(name = "personId", value = "人员ID", required = true, paramType = "param"),
    })
    public R<List<String>> getPlaceRelTypeByPersonId(ProjectParkingPlace projectParkingPlace) {
        Map<String, Object> query = new HashMap<>();
        query.put("personId", projectParkingPlace.getPersonId());
        query.put("parkId", projectParkingPlace.getParkId());

        return remoteParkingPlaceService.getPlaceRelTypeByPersonId(query);
    }
}
