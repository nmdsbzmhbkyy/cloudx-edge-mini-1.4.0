package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectBlacklistAttr;
import com.aurine.cloudx.estate.vo.BlacklistAttrSearchCondition;
import com.aurine.cloudx.estate.vo.CarPreRegisterSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectBlacklistAttrVo;
import com.aurine.cloudx.estate.vo.ProjectCarPreRegisterVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 黑名单属性(ProjectBlacklistAttr)表服务接口
 *
 * @author 顾文豪
 * @since 2023-11-9 10:32:48
 */
public interface ProjectBlacklistAttrService  extends IService<ProjectBlacklistAttr> {

    /**
     * 根据人脸id获取人脸黑名单
     * @param faceId 人脸库faceID
      */
    ProjectBlacklistAttr getByFaceId(String faceId);


    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page 分页信息
     * @param query 查询条件
     * @author: 顾文豪
     * @return 分页查询结果
     */
    Page<ProjectBlacklistAttrVo> fetchList(Page page, BlacklistAttrSearchCondition query);

}
