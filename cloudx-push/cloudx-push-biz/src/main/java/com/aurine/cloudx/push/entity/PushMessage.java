package com.aurine.cloudx.push.entity;

import com.aurine.cloudx.push.constant.PushSystemEnum;
import com.aurine.cloudx.push.constant.PushTypeEnum;
import com.cloudx.common.push.constant.OSTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * <p>推送消息对象</p>
 *
 * @ClassName: PushMessage
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/30 11:30
 * @Copyright:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "推送信息对象", description = "暂存要推送的元信息")
public class PushMessage {

    @ApiModelProperty(value = "推送目标 本地系统,E-MAIL,SMS,APP")
    private PushTypeEnum pushType;

    @ApiModelProperty(value = "推送目标的识别信息，本地为user-uid,email地址、电话号码、APP ID等")
    private String targetId;

    @ApiModelProperty(value = "群推目标ID")
    private List<String> targetIdList;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "信息内容")
    private String message;

    @ApiModelProperty(value = "推送使用的平台")
    private PushSystemEnum appPushSystem;

    @ApiModelProperty(value = "要推送的app产品")
    private String appName;

    @ApiModelProperty(value = "被推送系统类型，安卓/IOS")
    private OSTypeEnum osType;

    @ApiModelProperty(value = "被推送系统类型，安卓/IOS 列表")
    private List<OSTypeEnum> osTypeList;

    @ApiModelProperty(value = "其他扩展用参数")
    private Map<String, Object> paramMap;

    @ApiModelProperty(value = "用户的unionId")
    private List<String> unionId;

    @ApiModelProperty(value = "模板数据")
    private Map<String, Object> data;

    @ApiModelProperty(value = "模板Id")
    private String templateId;




    /**
     * 单推SMS,MAIL
     *
     * @param pushType
     * @param targetId
     * @param title
     * @param message
     */
    public PushMessage(PushTypeEnum pushType, String targetId, String title, String message, Map<String, Object> paramMap) {
        this.pushType = pushType;
        this.targetId = targetId;
        this.title = title;
        this.message = message;
        this.paramMap = paramMap;
    }

    /**
     * 群推SMS,MAIL
     *
     * @param pushType
     * @param targetIdList
     * @param title
     * @param message
     */
    public PushMessage(PushTypeEnum pushType, List<String> targetIdList, String title, String message, Map<String, Object> paramMap) {
        this.pushType = pushType;
        this.targetIdList = targetIdList;
        this.title = title;
        this.message = message;
        this.paramMap = paramMap;
    }

    /**
     * 单推APP
     *
     * @param pushType
     * @param targetId
     * @param message
     */
    public PushMessage(PushTypeEnum pushType, String targetId, String message, PushSystemEnum appPushSystem, String appId, OSTypeEnum osType, Map<String, Object> paramMap) {
        this.pushType = pushType;
        this.targetId = targetId;
        this.message = message;
        this.appPushSystem = appPushSystem;
        this.appName = appId;
        this.osType = osType;
        this.paramMap = paramMap;
    }

    /**
     * 群推APP
     *
     * @param pushType
     * @param targetIdList
     * @param message
     */
    public PushMessage(PushTypeEnum pushType, List<String> targetIdList, String message, PushSystemEnum appPushSystem, String appId, List<OSTypeEnum> osTypeList, Map<String, Object> paramMap) {
        this.pushType = pushType;
        this.targetIdList = targetIdList;
        this.message = message;
        this.appPushSystem = appPushSystem;
        this.appName = appId;
        this.osTypeList = osTypeList;
        this.paramMap = paramMap;
    }


    /**
     * 推送公众号
     *
     * @param pushType
     * @param unionId
     * @param data
     * @param templateId
     */
    public PushMessage(PushTypeEnum pushType, List<String> unionId, Map<String, Object> data,String templateId) {
        this.pushType = pushType;
        this.unionId = unionId;
        this.data = data;
        this.templateId= templateId;
    }
}
