package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YushiCallBackObj {

  private String Reference;

  private Long TimeStamp;

  private Long Seq;

  private Long SrcID;

  private String SrcName;

  private Long NotificationType;

  private String DeviceID;

  private String RelatedID;

  private StructureDataInfo StructureInfo;


}
