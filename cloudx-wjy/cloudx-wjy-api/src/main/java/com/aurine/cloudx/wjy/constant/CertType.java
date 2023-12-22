package com.aurine.cloudx.wjy.constant;

import lombok.Getter;

public enum CertType {
    //证件类型 (0101:大陆身份证,0102:军官证,0103:港澳台,0104:护照,0111:台胞证,0112:香港身份证,
    // 0105:营业执照,0106:税务登记号,0107:组织结构代码,0108:社会信用号码,0199:其它)
    MainlandIdentityCard("0101","大陆身份证"),
    CertificateOfOfficers("0102", "军官证"),
    HongKongMacaoTaiwanPass("0103", "港澳台"),
    Passport("0104", "护照"),
    TaiwanCompatriotsCertificate("0111", "台胞证"),
    HongKongIdentityCard("0112", "香港身份证"),
    TheBusinessLicense("0105", "营业执照"),
    TaxRegistrationNumber("0106", "税务登记号"),
    OrganizationStructureCode("0107", "组织结构代码"),
    SocialCreditNumber("0108", "社会信用号码"),
    OtherCertificate("0199", "其它");

    @Getter
    private final String type;
    @Getter
    private final String desc;


    CertType(String t, String desc) {//String n,
        //name = n;
        this.type = t;

        this.desc = desc;
    }
}
