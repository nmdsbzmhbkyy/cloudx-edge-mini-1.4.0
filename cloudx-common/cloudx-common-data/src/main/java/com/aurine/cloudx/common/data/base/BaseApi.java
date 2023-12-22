package com.aurine.cloudx.common.data.base;

import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
/**
 * @description
 * 基础定义API 自定义API继承即可
 * 如果使用openFeign 只需要将openFeign对应接口继承自定义API
 * @author cyw
 * @time 2023/6/9 10:56
 */
public interface BaseApi <T,P extends PageParam>{
//    @ApiOperation("分页查询")
//    @GetMapping("/page")
//    R<IPage<T>> page(@SpringQueryMap P param);

    @ApiOperation("根据ID查询")
    @GetMapping("/{id}")
    R<T> get(@PathVariable("id") Long id);


    @ApiOperation("保存")
    @PostMapping
    R<Object> save(@Validated(ValidationGroups.Save.class ) @RequestBody T t);

    @ApiOperation("更新")
    @PutMapping
    R<Object> update(@Validated(ValidationGroups.Update.class ) @RequestBody T t);

    @ApiOperation("根据ID删除")
    @DeleteMapping("/{id}")
    R<Object> delete(@PathVariable("id") @Validated @NotNull(message = "删除ID不可为空") Long ids);
    @ApiOperation("根据IDS删除")
    @DeleteMapping("/{ids}")
    R<Object> deleteByIds(@PathVariable("ids") @Validated @Size(message = "删除IDS不可为空",min = 1) List<Long> ids);
}
