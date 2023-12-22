package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterSeachConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 车辆登记
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
public interface ProjectParCarRegisterService extends IService<ProjectParCarRegister> {

    /**
     * 分页查询信息
     *
     * @param searchConditionVo
     * @return
     */
    Page<ProjectParCarRegisterRecordVo> pageCarRegister(Page<ProjectParCarRegisterRecordVo> page, @Param("query") ProjectParCarRegisterSeachConditionVo searchConditionVo);

    Boolean pageCarRegisterExport(Page<ProjectParCarRegisterRecordVo> page, @Param("query") ProjectParCarRegisterSeachConditionVo searchConditionVo,HttpServletResponse httpServletResponse);

    /**
     * 更新、车辆、以及新的人员信息
     *
     * @param parCarRegisterVo
     * @return
     */
    boolean updateCarRegister(ProjectParCarRegisterVo parCarRegisterVo);


    /**
     * 通过id获取注册信息VO,包含车辆信息、人员信息、车位信息、缴费信息等
     *
     * @param id
     * @return
     */
    ProjectParCarRegisterVo getVo(String id);

    /**
     * <p>
     * 判断车位是否已被登记
     * </p>
     *
     * @param placeId 车位id
     * @return 处理结果
     */
    boolean checkHasRegister(String placeId);


    /**
     * 保存车辆登记、车辆、以及新的人员信息
     *
     * @param parCarRegisterVo
     * @return
     */
    boolean saveCarRegister(ProjectParCarRegisterVo parCarRegisterVo);

    /**
     * 与saveCarRegister(ProjectParCarRegisterVo parCarRegisterVo)的唯一区别就是需要额外传入操作者ID，车辆登记导入需要使用
     * 保存车辆登记、车辆、以及新的人员信息(同上方法不过需要手动传入操作人ID)
     *
     * @param parCarRegisterVo
     * @return
     */
    boolean saveCarRegister(ProjectParCarRegisterVo parCarRegisterVo, Integer operatorId);


    /**
     * 保存车辆登记、车辆、以及新的人员信息 （Excel导入专用）
     *
     * @param parCarRegisterVoList
     * @return
     */
    boolean saveCarRegisterBatch(List<ProjectParCarRegisterVo> parCarRegisterVoList);

    /**
     * 撤销车辆注销
     *
     * @param registerId
     * @return
     */
    boolean cancelCarRegister(String registerId);

    /**
     * 撤销车辆注销-批量
     *
     * @param registerIdList 要进行注销的车辆登记ID
     */
    boolean cancelCarRegister(List<String> registerIdList);


    /**
     * <p>
     * 通过id获取注册信息VO,包含车辆信息、人员信息、车位信息、缴费信息等
     * </p>
     *
     * @param carRegisterVo 车辆登记vo对象
     * @return 处理结果
     * @author: 王良俊
     */
    boolean delay(ProjectParCarRegisterVo carRegisterVo);

    /**
     * <p>
     * 导入车辆登记信息
     * </p>
     *
     * @param file Excel文件对象
     * @return 处理结果
     */
    R importExcel(MultipartFile file, String type);

    /**
     * 获取失败名单
     *
     * @param name 文件名
     * @return 导入结果
     * @author: 许亮亮
     */
    void errorExcel(String name, HttpServletResponse httpServletResponse) throws IOException;

    /**
     * 获取模板文件
     *
     * @return 导入结果
     * @author: 许亮亮
     */
    void modelExcel(String type, HttpServletResponse httpServletResponse) throws IOException;

    /**
    * <p>
    * 判断是否已经开启了一位多车
    * </p>
    *
    * @author: 王良俊
    */
    boolean isAlreadyAMultiCar();

    /**
     * 返回同一车位上第一辆车的有效期和收费方式
     * @param projectParCarRegister
     * @return
     */
    ProjectParCarRegister getValidityByPlaceId(ProjectParCarRegisterVo projectParCarRegister);

    Map<String,Integer> getRegisterAndExpire();
}
