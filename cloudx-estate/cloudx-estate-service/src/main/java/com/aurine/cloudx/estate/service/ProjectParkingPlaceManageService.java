

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 车位
 *
 * @author 王伟
 * @date 2020-05-08 10:24:42
 */
public interface ProjectParkingPlaceManageService extends IService<ProjectParkingPlace> {

    /**
     * 查询住户
     *
     * @param page
     * @param searchConditionVo 查询条件
     * @return
     */
    IPage<ProjectParkingPlaceManageRecordVo> findPage(IPage<ProjectParkingPlaceManageRecordVo> page, ProjectParkingPlaceManageSearchConditionVo searchConditionVo);

    /**
     * 迁入车主
     *
     * @param projectParkingPlaceManageVo
     * @return
     */
    String save(ProjectParkingPlaceManageVo projectParkingPlaceManageVo);

    /**
     * <p>
     * 批量迁入车主
     * </p>
     *
     * @param parkingPlaceManageVoList 车位和人员关系对象列表
     * @return 是否保存成功
     */
    boolean saveBatch(List<ProjectParkingPlaceManageVo> parkingPlaceManageVoList);

    /**
     * 更新车主信息
     *
     * @param projectParkingPlaceManageVo
     * @return
     */
    boolean updateById(ProjectParkingPlaceManageVo projectParkingPlaceManageVo);



    /**
     * 获取
     *
     * @param id
     * @return
     */
    ProjectParkingPlaceManageVo getVoById(String id);

    /**
     * 确认车位上是否已存在用户
     *
     * @param id
     * @return
     */
    boolean checkPersonExits(String id);

    /**
     * 用户公共车位分配
     *
     * @param parkId     车场id
     * @param personId   人员id
     * @param personName 人员姓名
     * @return 返回车位ID
     * @author: 王良俊
     */
    String allocationPersonPublicParkingPlace(String parkId, String personId, String personName);


    /**
     * 用户公共车位分配(同时设置收费规则)
     *
     * @param parkId     车场id
     * @param personId   人员id
     * @param personName 人员姓名
     * @param ruleId     收费规则Id 这里如果有传入则分配车位的时候也会设置其收费规则
     * @return 返回车位ID
     * @author: 王良俊
     */
    String allocationPersonPublicParkingPlace(String parkId, String personId, String personName, String ruleId);

    /**
     * <p>时间格式yyyy-MM-dd</p>
     * 租赁时间更新
     *
     * @param placeId 车位id
     * @param endTime 可能要进行更新的租赁时间
     * @return
     */
    boolean updateRentTime(String placeId, String endTime);


    /**
     * <p>
     * 检查车位和车位区域以及车场的对应关系是否正确
     * </p>
     *
     * @param parkingName    车场名
     * @param parkRegionName 车位区域名
     * @param placeName      车位号
     * @return 车位ID
     * @throws com.alibaba.excel.exception.ExcelAnalysisException Excel异常对象
     */
    String checkParkingPlaceIsCorrect(String parkingName, String parkRegionName, String placeName);

}
