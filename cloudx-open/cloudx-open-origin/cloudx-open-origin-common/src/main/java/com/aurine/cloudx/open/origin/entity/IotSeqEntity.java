package com.aurine.cloudx.open.origin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : 王良俊
 * @date : 2021-12-17 09:25:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IotSeqEntity {


    private Integer projectId;

    private String deviceType;

    private Integer seq;

}
