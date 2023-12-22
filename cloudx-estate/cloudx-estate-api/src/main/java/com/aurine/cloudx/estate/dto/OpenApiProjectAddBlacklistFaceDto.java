package com.aurine.cloudx.estate.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 顾文豪
 * @Date: 2023/11/8 17:27
 * @Package: com.aurine.openv2.dto
 * @Version: 1.0
 * @Remarks:
 **/
@Data
@ApiModel(value = "新增黑名单人脸Dto")
public class OpenApiProjectAddBlacklistFaceDto {
    /**
     * 黑名单人脸图片地址
     */
    @ApiModelProperty(value = "黑名单人脸图片地址")
    private String picUrl;
    /**
     * 第三方人脸ID
     */
    @JSONField(name = "thirdFaceId")
    @JsonProperty(value = "thirdFaceId")
    @ApiModelProperty(value = "第三方人脸ID")
    private String thirdFaceId;
    /**
     * 手机号
     */
    @JSONField(name = "mobile")
    @JsonProperty(value = "mobile")
    @ApiModelProperty(value = "手机号")
    private String mobile;
    /**
     * 姓名
     */
    @JSONField(name = "name")
    @JsonProperty(value = "name")
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 身份证号码
     */
    @JSONField(name = "credentialNo")
    @JsonProperty(value = "credentialNo")
    @ApiModelProperty(value = "身份证号码")
    private String credentialNo;
}
