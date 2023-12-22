package com.aurine.cloud.code.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author admin
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ResultCode implements Serializable {


    private static final long serialVersionUID = 1L;

    private Boolean success;
    private String   msg;
}
