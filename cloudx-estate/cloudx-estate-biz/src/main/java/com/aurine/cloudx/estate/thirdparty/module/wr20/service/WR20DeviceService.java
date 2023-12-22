

package com.aurine.cloudx.estate.thirdparty.module.wr20.service;


/**
 * WR20 设备接口
 *
 * @ClassName: WR20FrameService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04 10:53
 * @Copyright:
 */
public interface WR20DeviceService extends BaseService {

    /**
     * 同步设备
     *
     * @param projectId WR20 ID
     * @param type      设备类型    0：全部 1：区口 2：梯口,4:室内
     * @return
     */
    boolean syncDevice(int projectId, String type);


}
