package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.door;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/18 14:13
 * @Copyright:
 */
@Data
public class DoorAccessObj {
    /**
     *通行权限
     */
    private List<DoorKeyObj> doorAccess;
}
