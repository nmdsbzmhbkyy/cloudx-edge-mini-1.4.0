package com.aurine.cloudx.estate.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
//import lombok.Getter;
//import sun.plugin2.message.Serializer;

import java.io.Serializable;

/** 识别结果类
 * 数据项@field res 成功识别到人脸返回0，否则返回错误代码，错误代码定义如下：
 * 	 5		其他未知状态
 * 	 4		人脸尺寸不合格
 * 	 3		黑白照片（不合规）
 * 	 2		检测到多张人脸
 * 	 1		检测不到人脸
 * 	 0		成功（检测正常人脸）
 * 	 -1		指定输入的jpg文件不存在或无法打开
 * 	 -2		无法确定输入的jpg文件大小
 * 	 -3		输入的jpg文件大小为0
 * 	 -4		分配jpg空间错误
 * 	 -5		读取输入的jpg文件错误
 * 	 -6		无法初始化jpg编解码
 * 	 -7		无法读取输入的jpg文件头信息或非jpg文件
 * 	 -8		输入的jpg文件格式无法支持
 *
 */
@Data
public class DetectResult implements Serializable {

    //@JsonIgnore
    //private IFaceDetect detector;

    @JsonIgnore
    private byte[]jpgBuf;

    @JsonIgnore
    private int sizeBuf;

    private int x;
    private int y;
    private int w;
    private int h;
    private int result;

    private String url;
    private String urlSrc;
    private String urlOut;

    //@JsonIgnore
    private DetectFaceResultCode code;

    public DetectResult(){
        //detector = IFaceDetect.Instance;
        sizeBuf = IFaceDetect.GetOutJpgSize().intValue();
        jpgBuf = new byte[sizeBuf];
        x = y = w = h = result = 0;
    }
    public DetectResult(int result){
        this();
        this.result = result;
    }

    public void setResult(int result){
        this.result = result;
        code = DetectFaceResultCode.create(result);
    }
}
