package com.aurine.cloudx.estate.open.parking.fegin;

import com.aurine.cloudx.estate.open.parking.bean.ProjectParCarRegisterSeachConditionVoPage;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(contextId = "remoteProjectParCarRegisterService", value = "cloudx-estate-biz")
public interface RemoteProjectParCarRegisterService {

    @GetMapping("/page")
    R getProjectParCarRegisterPage(@SpringQueryMap ProjectParCarRegisterSeachConditionVoPage page);

    @GetMapping("/{registerId}")
    R getById(@PathVariable("registerId") String registerId);

    @GetMapping("/getCarVoByPlateNumber/{plateNumber}")
    R getCarVoByPlateNumber(@PathVariable("plateNumber") String plateNumber);

    @PostMapping
    R register(@RequestBody ProjectParCarRegisterVo projectParCarRegister);

    @PostMapping("/importExcel/{type}")
    R importExcel(@PathVariable("type") String type, @RequestParam("file") MultipartFile file);

    @PutMapping
    R updateById(@RequestBody ProjectParCarRegisterVo projectParCarRegister);

    @DeleteMapping("/{id}")
    R removeById(@PathVariable("id") String id);

    @GetMapping("/cancelCarRegister/{registerId}")
    R cancelCarRegister(@PathVariable("registerId") String registerId);

    @PostMapping("/cancelCarRegisterList")
    R cancelCarRegisterList(@RequestBody List<String> cancelRegisterIdList);

    @PostMapping("/delay")
    R delay(@RequestBody ProjectParCarRegisterVo vo);

    @GetMapping("/checkHasRegister/{placeId}")
    R checkHasRegister(@PathVariable("placeId") String placeId);

    @GetMapping("/checkIsRegister/{plateNumber}")
    R checkIsRegister(@PathVariable("plateNumber") String plateNumber);

    @GetMapping("/isAlreadyAMultiCar")
    R isAlreadyAMultiCar();
}
