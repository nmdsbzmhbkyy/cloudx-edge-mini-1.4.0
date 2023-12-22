package com.aurine.cloudx.estate.thirdparty.main.entity;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-08-18 16:44
 */

import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import lombok.Data;

/**
 * 人员类型与ID信息
 */
@Data
public class PersonInfo {
    private PersonTypeEnum personTypeEnum;
    private String personId;
    private String personName;

    public PersonInfo(PersonTypeEnum personTypeEnum, String personId, String personName) {
        this.personTypeEnum = personTypeEnum;
        this.personId = personId;
        this.personId = personId;
        this.personName = personName;
    }

    public PersonInfo(PersonTypeEnum personTypeEnum, String personId) {
        this.personTypeEnum = personTypeEnum;
        this.personId = personId;
        this.personId = personId;
        this.personName = null;
    }
}