package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfo {

    private Long PersonID;

    private Long FaceDoforPersonID;

    private String Position;

    private Long SmallPicAttachIndex;

    private Long LargePicAttachIndex;

    private String FeatureVersion;

    private String Feature;

    private RuleInfo RuleInfo;

    private PersonAttr AttributeInfo;
}
