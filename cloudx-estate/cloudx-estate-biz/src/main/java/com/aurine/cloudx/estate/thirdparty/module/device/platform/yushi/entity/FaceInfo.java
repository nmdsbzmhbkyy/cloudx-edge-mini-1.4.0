package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceInfo {

    private Long FaceID;

    private Long FaceDoforPersonID;

    private Long FaceDoforNonMotorVehicleID;

    private String Position;

    private String AppearTime;

    private String DisAppearTime;

    private Long Confidence;

    private Long DirectionV;

    private Long DirectionH;

    private Long Rotation;

    private Long SmallPicAttachIndex;

    private Long LargePicAttachIndex;

    private String FeatureVersion;

    private String Feature;

    private FaceAttr AttributeInfo;
}
