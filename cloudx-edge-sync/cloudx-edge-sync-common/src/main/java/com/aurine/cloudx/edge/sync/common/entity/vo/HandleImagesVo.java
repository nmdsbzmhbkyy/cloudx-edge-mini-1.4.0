package com.aurine.cloudx.edge.sync.common.entity.vo;

import lombok.Data;

import java.io.InputStream;

/**
 * 处理图片vo
 *
 * @author:zy
 * @data:2022/12/19 9:17 上午
 */
@Data
public class HandleImagesVo {

    /**
     * 桶名
     */
    private String bucketName;
    /**
     * 文件名
     */
    private String objectName;
    /**
     * 图片流
     */
    private byte[] byteArray;
    /**
     * 图片长度
     */
    private long length;

}
