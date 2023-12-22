package com.aurine.cloudx.estate.util;

/**
 * 请求返回的状态码
 */
public enum DetectFaceResultCode {
    /**
     * 返回状态码
     */
    SUCCESS("操作成功", 0),

    ERROR_NO_FACE_DETECTION("检测不到人脸", 1),
    ERROR_MULTIPLE_FACE_DETECTED("检测到多张人脸", 2),
    ERROR_BW_PHOTO("黑白照片（不合规）", 3),
    ERROR_FACE_SIZE("人脸尺寸不合格", 4),
    ERROR_DETECT_UNKNOWN("其他未知状态", 5),
    ERROR_KEYPOINTS_STATE_ILLEGALANGLE("人脸角度不合格", 6),
    ERROR_QUALITY_STATE_ILLEGALANGLE("人脸清晰度不合格", 7),

    ERROR_JPG_FILE_NOT_EXISTS("指定输入的jpg文件不存在或无法打开", -1),
    ERROR_JPG_FILE_SIZE("无法确定输入的jpg文件大小", -2),
    ERROR_JPG_FILE_SIZE_ZERO("输入的jpg文件大小为0", -3),
    ERROR_JPG_ALLOCATE("分配jpg空间错误", -4),
    ERROR_JPG_READ("读取输入的jpg文件错误", -5),
    ERROR_JPG_ENCODER("无法初始化jpg编解码", -6),
    ERROR_JPG_HEADER("无法读取输入的jpg文件头信息或非jpg文件", -7),
    ERROR_JPG_FORMAT_NOT_SUPPORT("jpg文件格式无法支持", -8),

    ERROR_JPG_WIDTH_ODD("jpg宽度不能为奇数", -100),
    ERROR_JPG_HEIGHT_IDD("jpg高度不能为奇数", -101);
    private String msg;
    private int code;

    private DetectFaceResultCode(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public static DetectFaceResultCode create(int code){
        DetectFaceResultCode[]list = values();
        for(DetectFaceResultCode item: list){
            if(item.code == code){
                return item;
            }
        }
        return null;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
