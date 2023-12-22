package com.aurine.cloudx.wjy.controller;

import com.aurine.cloudx.wjy.vo.Hello;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
@Api(value = "hello", tags = "你好")
public class HelloWorld {
    @RequestMapping("/test")
    @ApiOperation(value = "你好測試", notes = "你好測試")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "測試名稱", required = true, paramType = "query")
    })
    @Inner(value = false)
    public R<Hello> register(@RequestParam String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return R.ok(hello);
    }
}
