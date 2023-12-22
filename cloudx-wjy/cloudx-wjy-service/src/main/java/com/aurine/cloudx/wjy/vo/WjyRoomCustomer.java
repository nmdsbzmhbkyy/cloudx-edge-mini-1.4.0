package com.aurine.cloudx.wjy.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 房屋-客户关系实体
 * 附：由于我家云在线文档不全，只能根据接口测试后以下返回的字段定义本实体属性
 *  诸多字段意义未知，具体返回的json在本文档最尾端
 */
@Data
public class WjyRoomCustomer implements Serializable {
    private String id;
    private String roomID;
    private String roomVo;

    private String customerID;
    private String mainCusID;
    private String customerVo;
    private String customerType;
    private String customerNumber;
    private String tranBankAccount;
    private String isMain;
    private Integer isDefault;
    private String isCharge;
    private String joinInDate;
    private String outDate;
    private String rentStartDate;
    private String rentEndDate;
    private String agency;
    private String telephone;
    private String relation;
    private String status;
    private String creator;
    private String createTime;
    private String projectID;
    private String projectName;
    private String ecID;
    private String roomids;
    private String province;
    private String city;
    private String area;
    private String outDescription;
    private String joinInDescription;
    private String backUpPhone;
    private String planOutDate;
    private String operator;
    private String outOperator;
    private String type;
    private String certificateType;
    private String certificateName;
    private String certificateNumber;
    private Integer hasRsa;
    private String phone;
    private String sex;
    private String ecName;
    private String roomName;
    private String roomNumber;
    private String customerName;
    private String certificateNo;
    private String buildingID;
    private String buildingName;
    private String buildUnitID;
    private String buildUnitName;
    private String buildingArea;
    private String roomArea;
    private String productTypeName;
    private String roomModelName;
    private String customerIsOwner;
    private String remark;
    private String moneystandardName;
    private String instrumentName;
    private String roomStatus;
    private String feeStatus;
    private String number;
    private Integer auditStatus;
    private String customerList;
    private String thumbUrl;
    private String nationality;
    private Integer nation;
    private String residenceAddress;
    private String birthdayStr;
    private String taxpayerType;
    private String custMakeOutName;
    private String specInvCountryTaxCode;
    private String businessAddress;
    private String businessPhone;
    private String bankName;
    private String bankCount;
    private String activeTime;
    private String userIsActive;
    private String customerPhone;
    private String customerSpell;
    private String rmfname;
    private String crfname;
    private String authCode;
    private String userID;
    private String updateCus;
    private String oldCustomerId;
    private String changeType;
    private String cusType;
    private String propertyLabels;
    private String catalog;
    private String relationRoomID;
    private String cusLabelList;
    private String fileUrl;
    private String email;
    private String category;
    private String educationLevel;
    private String phoneType;
    private String formCodeId;
    private String deleteTime;
    private String isDelete;
    private String updateTime;
    private String partAId;
    private String partAName;
    private String avatar;
    private String loginUserAccount;
    private String buildName;
}


// 我家云在线文档不全，只能根据接口测试后以下返回的字段定义本实体属性
//{"id":"25892e9676334da08d845e32fd306887","roomID":"818d948697fb4153a13d1e3b0c767dfe","roomVo":null,
//"customerID":"959e34e6d5164f22bad88db972b2618b","mainCusID":null,"customerVo":null,"customerType":"2",
//"customerNumber":"13699340825","tranBankAccount":null,"isMain":null,"isDefault":0,"isCharge":null,
//"joinInDate":"2021-01-07 00:00:00","outDate":null,"rentStartDate":null,"rentEndDate":null,"agency":null,
//"telephone":null,"relation":null,"status":null,"creator":null,"createTime":"2021-01-07 12:01:48",
//"projectID":"62076e66f4524620823f47b1f6d9be8a","projectName":"西锦城","ecID":null,"roomids":null,
//"province":null,"city":null,"area":null,"outDescription":null,"joinInDescription":null,"backUpPhone":"",
//"planOutDate":null,"operator":null,"outOperator":null,"type":"P","certificateType":"","certificateName":"",
//"certificateNumber":"","hasRsa":0,"address":"","phone":null,"sex":"0","ecName":null,"roomName":"GLTest-01栋01单元0101房",
//"roomNumber":"GLTest-01010101","customerName":"张四三","certificateNo":null,"buildingID":"09afd1c913f940dfa12d63ae53e12aed",
//"buildingName":"01栋","buildUnitID":"a95833af63104be78ac6e5471f4946e5","buildUnitName":"","buildingArea":null,
//"roomArea":null,"productTypeName":null,"roomModelName":null,"customerIsOwner":null,"remark":null,
//"moneystandardName":null,"instrumentName":null,"roomStatus":null,"feeStatus":null,"number":null,"auditStatus":2,
//"customerList":null,"thumbUrl":null,"nationality":null,"nation":0,"residenceAddress":null,"birthdayStr":null,
//"taxpayerType":null,"custMakeOutName":null,"specInvCountryTaxCode":null,"businessAddress":null,"businessPhone":null,
//"bankName":null,"bankCount":null,"activeTime":null,"userIsActive":null,"customerPhone":"13699340825","customerSpell":null,
//"rmfname":null,"crfname":null,"authCode":null,"userID":null,"updateCus":null,"oldCustomerId":null,"changeType":null,
//"cusType":null,"propertyLabels":null,"catalog":null,"relationRoomID":null,"cusLabelList":null,"fileUrl":null,"email":null,
//"category":null,"educationLevel":null,"phoneType":"cn","formCodeId":null,"deleteTime":null,"isDelete":null,"updateTime":null,
//"partAId":"","partAName":null,"avatar":null,"loginUserAccount":null}

