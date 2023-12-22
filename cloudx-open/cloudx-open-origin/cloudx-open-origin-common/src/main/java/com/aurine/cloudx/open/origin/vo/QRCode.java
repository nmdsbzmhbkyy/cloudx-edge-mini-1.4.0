package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName: QrcCodeVo
 * @author: 王良俊 <>
 * @date:  2020年10月28日 下午01:58:02
 * @Copyright:
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "巡点二维码vo对象")
public class QRCode {

    /**
     * 二维码logo如果没有该图片则默认为无logo版
     * */
    private MultipartFile logoFile;

    /**
     * 二维码宽
     * */
    private Integer width;

    /**
     * 二维码高
     * */
    private Integer height;

    /**
     * 巡检点ID列表
     * */
    private List<String> pointIdList;

    /**
     * 二维码颜色
     * */
    private String rgbStr;

}