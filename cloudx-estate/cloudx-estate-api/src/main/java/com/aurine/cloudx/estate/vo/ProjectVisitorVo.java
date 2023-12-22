

package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectPasswd;
import com.aurine.cloudx.estate.entity.ProjectVisitor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 访客VO
 *
 * @date 2020-06-03 19:42:52
 */
@Data
@ApiModel(value = "访客")
public class ProjectVisitorVo extends ProjectVisitor {
    private static final long serialVersionUID = 1L;


    /**
     * 记录id
     */
    @ApiModelProperty(value = "记录id")
    private String visitId;
    /**
     * 访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务
     */
    @ApiModelProperty(value = "访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务")
    private String visitorType;
    /**
     * 访客姓名（访客申请表的冗余存储）
     */
    @ApiModelProperty(value = "访客姓名")
    private String visitorName;


    /**
     * 被访人手机号
     */
    @ApiModelProperty(value = "被访人手机号")
    private String visitPersonPhone;

    /**
     * 随行人数
     */
    @ApiModelProperty(value = "随行人数")
    private Integer personCount;

    /**
     * 来源
     */
    @ApiModelProperty(value = "来源 1.web 2.小程序  3.app  ")
    private String origin;

    /**
     * 拓展来源来源
     */
    @ApiModelProperty(value = "来源 0:未知 1：物业 2：业主 3：访客")
    private String originEx;

    /**
     * 访问事由
     */
    @ApiModelProperty(value = "事由")
    private String reason;
    /**
     * 通行时间
     */
    @ApiModelProperty(value = "通行开始时间")
    private String passBeginTime;
    /**
     * 通行时间
     */
    @ApiModelProperty(value = "通行结束时间")
    private String passEndTime;
    /**
     * 被访人房屋id
     */
    @ApiModelProperty(value = "被访人房屋id")
    private String visitHouseId;
    /**
     * 被访人id
     */
    @ApiModelProperty(value = "被访人id")
    private String visitPersonId;
    /**
     * 被访人姓名
     */
    @ApiModelProperty(value = "被访人姓名")
    private String beVisitorName;
    /**
     * 到访时间
     */
    @ApiModelProperty(value = "到访时间")
    private String visitTime;
    /**
     * 时间范围可能包含小时看情况
     */
    @ApiModelProperty(value = "时间范围 (某年某月某日-某年某月某日)")
    private String[] timeRange;
    /**
     * 小时范围
     */
    @ApiModelProperty(value = "时段（xx:xx-xx:xx）")
    private String[] cycleRange;

    @ApiModelProperty(value = "车场")
    private String parkId;
    /**
     * 可通行车道
     */
    @ApiModelProperty(value = "可通行车道")
    private String laneList;
    /**
     * 所有门禁卡对象的列表
     */
    @ApiModelProperty(value = "所有门禁卡对象的列表")
    private List<ProjectCard> cardList;
    /**
     * 所有人脸对象的列表
     */
    @ApiModelProperty(value = "所有人脸对象的列表")
    private List<ProjectFaceResources> faceList;
    /**
     * 所有密码对象的列表
     */
    @ApiModelProperty(value = "所有密码对象的列表")
    private List<ProjectPasswd> passwordList;
    /**
     * 可通行设备列表
     */
    @ApiModelProperty(value = "可通行设备列表")
    private String[] deviceIdArray;
    /**
     * 电梯列表
     */
    @ApiModelProperty(value = "可通行电梯列表")
    private List<ProjectDeviceLiftVo> lifts;
    /**
     * 登记类型 1 物业登记 2 住户申请 3 自住申请
     */
    @ApiModelProperty(value = "登记类型 1 物业登记 2 住户申请 3 自住申请")
    private String registerType;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    private String auditStatus;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "拒绝理由")
    private String rejectReason;
    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expTime;
    /**
     * 是否人脸通行 1 是 0 否
     */
    @ApiModelProperty(value = "是否人脸通行 1 是 0 否")
    private String isFace;
    /**
     * 签离状态
     */
    @ApiModelProperty(value = "是否签离 1 是 0 否")
    private char isLeave;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private String projectId;
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 集团id
     */
    @ApiModelProperty(value = "集团id")
    private String companyId;
    /**
     * 集团名称
     */
    @ApiModelProperty(value = "集团名称")
    private String companyName;
    /**
     * 项目组id
     */
    @ApiModelProperty(value = "项目组id")
    private String projectGroupId;
    /**
     * 项目组名称
     */
    @ApiModelProperty(value = "项目组名称")
    private String projectGroupName;

    /**
     * 是否自动延期 1 是 0 否
     */
    @ApiModelProperty(value = "是否自动延期 1 是 0 否")
    private String isAutoDelay;

    /**
     * 有效周期
     */
    @ApiModelProperty(value = "有效周期")
    private String period;
}
