package com.aurine.cloudx.estate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IotSeqEntity {


    private Integer projectId;

    private String deviceType;

    private Integer seq;

}
