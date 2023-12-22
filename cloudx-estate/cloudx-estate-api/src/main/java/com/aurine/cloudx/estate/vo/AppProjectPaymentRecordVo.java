package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectPaymentRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "账单信息vo")
public class AppProjectPaymentRecordVo  extends ProjectPaymentRecord {



    @ApiModelProperty(value = "楼栋+房号名称")
     private  String roomNumber;




}
