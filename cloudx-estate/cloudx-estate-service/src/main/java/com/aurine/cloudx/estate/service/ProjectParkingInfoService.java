

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.vo.ProjectParkingInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 停车场
 *
 * @author 王伟
 * @date 2020-05-07 09:13:25
 */
public interface ProjectParkingInfoService extends IService<ProjectParkingInfo> {

    IPage<ProjectParkingInfoVo> findPage(IPage<ProjectParkingInfoVo> page, ProjectParkingInfoVo parkingInfoVo);

    /**
     * <p>
     *  保存停车场信息
     * </p>
     *
     * @author: 王良俊
     * @param parkingInfo 停车场场po对象
     * @return
     * @throws
    */
    boolean saveParkInfo(ProjectParkingInfo parkingInfo);

    boolean updateParkInfo(ProjectParkingInfo parkingInfo);

    /**
     * 根据第三方编号，获取车场信息
     * @author: 王伟
     * @param code
     * @return
     */
    ProjectParkingInfo getByThirdCode(String code);

    /**
     * 验证第三方编号是否重复
     * @param parkingId
     * @param code
     * @return
     */
    boolean checkThirdCodeRepeat(String parkingId,String code);

    /**
     * <p>
     *  通过车场ID删除车场
     * </p>
     *
     * @param parkId 车场ID
     * @return 删除结果
    */
    boolean removeParkById(String parkId);
}
