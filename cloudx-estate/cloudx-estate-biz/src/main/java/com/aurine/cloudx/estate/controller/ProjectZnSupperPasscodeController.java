package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectZnPasscodeService;
import com.aurine.cloudx.estate.vo.zn.SetSupperPasscodeVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zn/passcode")
@RequiredArgsConstructor
public class ProjectZnSupperPasscodeController {
    private final ProjectZnPasscodeService projectZnPasscodeService;

    @PostMapping("/set")
    @Inner(false)
    public R<Object> set(@RequestBody @Validated SetSupperPasscodeVo vo) {
        projectZnPasscodeService.setSupperPasscode(vo);
        return R.ok();
    }


}
