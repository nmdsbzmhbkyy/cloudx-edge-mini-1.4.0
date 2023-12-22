package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleInfo {

    private Long RuleType;

    private Long TrigerType;

    private Long PointNum;

    private List PointList;

    private Long X;

    private Long Y;

}
