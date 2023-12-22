package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StructureDataInfo {

  private ObjectInfo ObjInfo;

  private Long ImageNum;

  private List<ImageInfo> ImageInfoList;

  private Long FinishFaceNum;

  private List FinishFaceList;

}
