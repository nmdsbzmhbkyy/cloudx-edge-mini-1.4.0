package com.aurine.cloudx.open.mix.controller;

import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 混合查询XX模板
 *
 * 注：
 * 1、这个包下的类，初步定义用于数据库表的混合查询；
 * 2、这个类只是用于定义一个模板，并没有实际用途；
 * 3、这个类的“XX”，相当于是要混合查询的业务名称。
 *   如住户就是HousePersonInfo，户型就是HouseDesign，项目就是ProjectInfo。
 *
 * @author : Qiu
 * @date : 2022 01 10 14:08
 */

@RestController
@RequestMapping("/v1/mix/xx")
// 注释是为了防止被swagger识别
//@Api(value = "openMixXX", tags = {"v1", "混合查询相关", "混合查询XX"})
@Inner
@Slf4j
public class MixXXController {
}
