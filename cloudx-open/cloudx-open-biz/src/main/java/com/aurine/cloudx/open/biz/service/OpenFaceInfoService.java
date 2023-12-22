package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.FaceInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人脸信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenFaceInfoService {

    /**
     * 根据ID获取人脸信息
     *
     * @param id
     * @return
     */
    R<FaceInfoVo> getById(String id);

    /**
     * 根据ID获取人脸信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<FaceInfoVo>> page(Page page, FaceInfoVo vo);

    /**
     * 新增人脸信息
     *
     * @param vo
     * @return
     */
    R<FaceInfoVo> save(FaceInfoVo vo);

    /**
     * 修改人脸信息
     *
     * @param vo
     * @return
     */
    R<FaceInfoVo> update(FaceInfoVo vo);

    /**
     * 删除人脸信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
