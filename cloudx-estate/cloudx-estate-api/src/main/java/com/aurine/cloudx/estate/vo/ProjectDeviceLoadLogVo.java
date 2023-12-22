package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLog;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 设备导入日志Vo对象
 *
 * @author 王良俊
 * @since 2021-06-03 10:49:35
 */
@Data
@NoArgsConstructor
public class ProjectDeviceLoadLogVo extends ProjectDeviceLoadLog {

    /*
    * 失败数
    * */
    private String failedNum;
}
