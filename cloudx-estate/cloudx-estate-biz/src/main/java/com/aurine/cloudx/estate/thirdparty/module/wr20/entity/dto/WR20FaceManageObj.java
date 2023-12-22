package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto;

import lombok.Data;

/**
 * WR20 住户 面部识别信息 对象
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-23 15:04
 * @Copyright:
 */
@Data
public class WR20FaceManageObj {
    /**
     * 住户id
     */
    private String teneId;
    /**
     * 人脸id
     */
    private String faceID;
    /**
     * 源类型  1：第三方（默认）2：云平台
     */
    private String srcType;

    /**
     * 人脸图片
     */
    private String faceImage;
    /**
     * 人脸描述	 默认为住户姓名
     */
    private String photoDesc;
    /**
     * 人员类型 0 住户 10 员工
     */
    private String personType;

}
