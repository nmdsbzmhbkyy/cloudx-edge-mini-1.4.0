package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectInfo {

    private Long FaceNum;

    private List<FaceInfo> FaceInfoList;

    private Long PersonNum;

    private List<PersonInfo> PersonInfoList;


}
