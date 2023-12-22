

package com.aurine.cloudx.estate.thirdparty.module.wr20.service;


/**
 * WR20 框架同步接口
 *
 * @ClassName: WR20FrameService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04 10:53
 * @Copyright:
 */
public interface WR20FrameService extends BaseService {

    /**
     * 同步组团、楼栋、单元等信息
     *
     * @param projectId WR20的ID号
     * @return
     */
    boolean syncFrame(int projectId);




}
