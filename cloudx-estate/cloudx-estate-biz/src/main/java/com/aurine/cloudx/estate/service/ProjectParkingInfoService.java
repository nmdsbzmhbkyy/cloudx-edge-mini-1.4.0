

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

    /**
    * <p>
    * 初始化项目车场
    * </p>
    *
    * @param projectId 项目ID
    * @author: 王良俊
    */
    void init(String projectName, Integer projectId, Integer tenantId);

    boolean syncPark(String parkName,Integer projectId);

    /**
    * <p>
    * 根据车场名获取到车场ID
    * </p>
    *
    * @param redisKey redis的键
    * @param parkName 车场名
    * @return 车场ID，如果未找到则返回null
    * @author: 王良俊
    */
    String getParkIdByParkName(String redisKey, String parkName, Integer projectId);

    /**
    * <p>
    * 删除车场临时字典缓存
    * </p>
    *
    * @param redisKey 缓存的键
    * @author: 王良俊
    */
    void deleteParkTmpCache(String redisKey);
}
