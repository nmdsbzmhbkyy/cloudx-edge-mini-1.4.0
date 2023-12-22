package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlateNumberQueryVo extends Page {

    /*
     * 设备ID
     **/
    private String deviceId;
}
