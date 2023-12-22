package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ProjectCertStateVo{


    /**
     * 状态
     */
    private String state;


    /**
     * 数量
     */
    private Integer num;


}