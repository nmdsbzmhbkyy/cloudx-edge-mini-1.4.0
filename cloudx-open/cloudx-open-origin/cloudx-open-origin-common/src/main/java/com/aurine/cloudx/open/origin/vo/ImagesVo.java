package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagesVo {

    /**
     * 图片路径
     */
    private String accessPath;

    /**
     * 图片名称
     */

    private String picName;
    private String picId;
}
