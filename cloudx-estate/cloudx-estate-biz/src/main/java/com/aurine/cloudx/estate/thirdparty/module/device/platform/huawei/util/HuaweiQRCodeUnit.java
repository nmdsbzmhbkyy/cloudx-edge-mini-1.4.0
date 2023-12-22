package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.util;

import cn.hutool.core.date.DateUtil;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.util.security.*;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-27
 * @Copyright:
 */
public class HuaweiQRCodeUnit {

    private static HuaweiQRCodeUnit instance = null;

    private final static String SIGN = "@u^$OPlx";

    public static final String ENCRYPTIO_TYPE_AES_BASE64 = "3";//新版本AES base64编码

    public static final String V2_CBC_IV = "3zcI#vOkxL8uJLZ8";

    public static final String V2_PUBLIC_KEY = "eimKGGm!B3Fn47TD";

    static final byte[] HEX_CHAR_TABLE = {
            (byte) '0', (byte) '1', (byte) '2', (byte) '3',
            (byte) '4', (byte) '5', (byte) '6', (byte) '7',
            (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
            (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
    };


    private HuaweiQRCodeUnit() {

    }

    public static HuaweiQRCodeUnit getInstance() {
        if (instance == null) {
            instance = new HuaweiQRCodeUnit();
        }
        return instance;
    }

    /**
     * 新版二位码生成，V2.0
     * <p>
     * //     * @param type          二维码类型，1、业主二维码，2、临时分享二维码（时间段），3、临时分享二维码（N次）,5、临时分享二维码（时段+次数）
     *
     * @param userNo        用户码，8位字符长度
     * @param roomNos       房屋编号集合 集合长度最大支持16
     * @param startTime     开始时间  13位时间戳
     * @param effectiveTime 有效时间，单位：分钟
     * @param times         次数
     * @param businessId    业务ID    访客等（设备目前没有返回 2021 04 26）
     * @param extStr        扩展字段
     * @return
     */
    public String getQrcodeV2(String personType, Integer communityCode, String userNo, List<String> roomNos, Long startTime, Integer effectiveTime, Integer times, String businessId, String extStr) {

        //加密方式：电子钥匙数据域：校验串：扩展标识
        String dataFormat = "%s:%s:%s:%s";
        Integer type = 0;

        startTime = startTime / 1000;

        if (StringUtils.equals(personType, PersonTypeEnum.PROPRIETOR.code)) {
            type = 1;
        } else {
            if (effectiveTime == null && (times != null && times > 0)) {
                type = 4;
            } else if (effectiveTime != null && effectiveTime > 0 && times == null) {
                type = 3;
            } else {
                type = 5;
            }
        }

        String electronicKeyDataField = getElectronicKeyDataV2(Integer.valueOf(type), String.valueOf(communityCode), userNo, startTime, effectiveTime, times, roomNos, businessId);

        System.out.println("电子数据域加密前：" + electronicKeyDataField);

        byte[] bytes = hexToByteArry(electronicKeyDataField);

        String waitAES = null;

        //waitAES = byteArrayToHexStr(bytes);
        try {
            waitAES = new String(bytes, "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            electronicKeyDataField = AES128.encrypt(waitAES, "iso-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("电子数据域AES加密后：" + electronicKeyDataField);

        //electronicKeyDataField = Base64.encodeBase64String(electronicKeyDataField.getBytes());

        //System.out.println("电子数据域Base64加密后："+electronicKeyDataField);

        System.out.println("MD加密前：" + ENCRYPTIO_TYPE_AES_BASE64 + ":" + electronicKeyDataField + ":" + SIGN);

        String md5Str = MD5Util.MD5(ENCRYPTIO_TYPE_AES_BASE64 + ":" + electronicKeyDataField + ":" + SIGN);

        System.out.println("MD5数据：" + md5Str);

        String checkStr = md5Str.substring(5, 9);

        return String.format(dataFormat, ENCRYPTIO_TYPE_AES_BASE64, electronicKeyDataField, checkStr, extStr);
    }

    /**
     * 验证二维码是正确
     *
     * @param qrcodeStr
     * @param communityId
     * @return
     */
    public String qrcodeVerify(String qrcodeStr, Integer communityId, String deviceCode) {
        try {
            System.out.println("=======" + qrcodeStr);
            String content = qrcodeStr.split(":")[1];
            String type = qrcodeStr.split(":")[0];


//            String electronicKeyData = SecurityCore.getInstance().decrypt(content);
            String waitAES = AES128.decrypt(content, "iso-8859-1");
            System.out.println(waitAES);
            byte[] bytes = new byte[0];
            try {
                bytes = waitAES.getBytes("iso-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(bytes.length);
            String result = "";
            String hexStr = "";
            String bStr = "";
            System.out.println("tString:" + bStr);
            for (byte b : bytes) {
//                bStr += Integer.toBinaryString((b & 0xFF) + 0x100).substring(1) + " ";
                System.out.print(hexStr += Integer.toHexString(b & 0xff) + ",");
            }

            return hexStr;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 解析
     *
     * @param bStr
     * @return
     */
    private String analysis(String bStr) {
        String result = "";
        String[] bStrArray = bStr.split(" ");

        Integer version = Integer.parseInt(bStrArray[1], 2);
        result += " 版本：" + version;

        Integer type = Integer.parseInt(bStrArray[2].substring(0, 4), 2);
        String typeStr = "";
        switch (type) {
            case 1:
                typeStr = "业主的二维码";
                break;
            case 2:
                typeStr = "临时分享二维码（时间段）";
                break;
            case 3:
                typeStr = "临时分享的二维码(N次)";
                break;
            default:
                typeStr = "临时分享时间段+ 次数";
        }
        result += " 类型：" + typeStr;

        Integer roomCount = Integer.parseInt(bStrArray[2].substring(4, 8), 2);
        result += " 房屋数量：" + roomCount;

        Long createtime = Long.parseLong(bStrArray[2].substring(4, 8), 2);


        return result;
    }

    public String getHexString(byte[] raw, String charsetName)
            throws UnsupportedEncodingException {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;

        for (byte b : raw) {
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        return new String(hex, charsetName);
    }


    /**
     * 电子钥匙数据V2
     *
     * @param type
     * @param communityCode
     * @param userNo
     * @param effectiveTime
     * @param roomNos
     * @param businessId
     * @return
     */
    private String getElectronicKeyDataV2(Integer type, String communityCode, String userNo, Long startTime, Integer effectiveTime, Integer times, List<String> roomNos, String businessId) {

        //char[] electronicKeyData = new char[24+4*roomNos.size()];

        String head = "FA";//固定头 1B

        String ver = "01";//版本 1B

        String qrcodeType = type.toString();//二维码类型 4bit


        String roomNum = Integer.toHexString(roomNos.size());//房号个数 4bit

        System.out.println("房号个数:" + roomNum);

        String userCode = "";//用户通行码 8B

        communityCode = Sm3Util.encrypt(communityCode);
        System.out.println("社区ID进行SM3加密:" + communityCode);
        communityCode = communityCode.substring(communityCode.length() - 6, communityCode.length());
        System.out.println("取后3个字节:" + communityCode);
        communityCode = String.valueOf(Integer.parseInt(communityCode, 16));
        System.out.println("转10进制:" + communityCode);

        Integer luhn = luhn(communityCode + userNo);

        //计算社区代码HEX
        String communityCodeHex = Long.toHexString(Long.valueOf(communityCode));

        communityCodeHex = parchLeftZero(communityCodeHex, 6);

        communityCodeHex = reverseHex(communityCodeHex).toUpperCase();

        //计算用户码HEX
        String userNoHex = Long.toHexString(Long.valueOf(userNo));

        userNoHex = parchLeftZero(userNoHex, 8);

        userNoHex = reverseHex(userNoHex).toUpperCase();

        //计算校验码HEX
        String luhnHex = Integer.toHexString(luhn);

        luhnHex = parchLeftZero(luhnHex, 2);

        userCode = communityCodeHex + userNoHex + luhnHex;

        System.out.println("用户通行码：" + userCode);

        String timeStamp;// = startTime;//开始时间

        timeStamp = Long.toHexString(startTime);

        timeStamp = parchLeftZero(timeStamp, 8);

        timeStamp = reverseHex(timeStamp).toUpperCase();

        if (effectiveTime == null) {
            effectiveTime = 0;
        }

        System.out.println("开始时间：" + timeStamp);

        String minute = String.valueOf(effectiveTime);

        if ("3".equals(qrcodeType)) {
            minute = times.toString();
            minute = Long.toHexString(Long.valueOf(minute));
            minute = parchLeftZero(minute, 8);
            minute = reverseHex(minute).toUpperCase();
        } else if ("5".equals(qrcodeType)) {
            minute = parchLeftZero(Integer.toHexString(times), 2);
            minute = minute + reverseHex(parchLeftZero(Integer.toHexString(effectiveTime), 6));
            minute = minute.toUpperCase();
        } else {
            minute = Long.toHexString(Long.valueOf(minute));
            minute = parchLeftZero(minute, 8);
            minute = reverseHex(minute).toUpperCase();
        }


        System.out.println("有效时间：" + minute);

        String roomAuth = "";

        for (String str :
                roomNos) {
            roomAuth += roomCodeToHex(str);
        }

        //String electronicKeyDataV2 =  ver + qrcodeType + roomNum + userCode + timeStamp + minute + roomAuth + businessId;

        businessId = Long.toHexString(Long.valueOf(businessId));
        businessId = parchLeftZero(businessId, 8);
        businessId = reverseHex(businessId).toUpperCase();
        System.out.println("businessId=" + businessId);

        String electronicKeyDataV2 = ver + qrcodeType + roomNum + userCode + timeStamp + minute + roomAuth + businessId;

        byte[] bytes = hexToByteArry(electronicKeyDataV2);

        //String waitCRC = String.valueOf(bytes);

        int crc = CRCUtil.CRC8(bytes, 0, bytes.length);

        String crcStr = Integer.toHexString(crc);
        crcStr = parchLeftZero(crcStr, 2).toUpperCase();

        System.out.println("crc校验码：" + crcStr);


        return head + electronicKeyDataV2 + crcStr;
    }

    //位数不足往左补0
    private static String parchLeftZero(String str, Integer len) {

        if (str.length() >= len) {
            return str;
        }

        for (int i = 0, size = len - str.length(); i < size; i++) {
            str = "0" + str;
        }
        return str;
    }

    //高低位转换
    public static String reverseHex(String hex) {
        char[] charArray = hex.toCharArray();
        int length = charArray.length;
        int times = length / 2;
        for (int c1i = 0; c1i < times; c1i += 2) {
            int c2i = c1i + 1;
            char c1 = charArray[c1i];
            char c2 = charArray[c2i];
            int c3i = length - c1i - 2;
            int c4i = length - c1i - 1;
            charArray[c1i] = charArray[c3i];
            charArray[c2i] = charArray[c4i];
            charArray[c3i] = c1;
            charArray[c4i] = c2;
        }
        return new String(charArray);
    }

    //16进制字符串转byte[]
    private static byte[] hexToByteArry(String str) {
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0, item = str.length() / 2; i < item; i++) {
            Integer a = Integer.parseInt(str.substring(i * 2, 2 * i + 2), 16);
            bytes[i] = (byte) a.intValue();
        }
        return bytes;
    }

    //房号转十六进制
    private static String roomCodeToHex(String roomCode) {
        String newStr = "";
        for (int i = 0, item = roomCode.length() / 2; i < item; i++) {
            String str = Integer.toHexString(Integer.valueOf(roomCode.substring(i * 2, 2 * i + 2)));
            str = parchLeftZero(str, 2);
            newStr += str;

        }
        return newStr;
    }

    //模10算法
    private Integer luhn(String src) {

        if (src.length() % 2 != 0) {
            src = src + "0";
        }

        Integer sumOdd = 0;
        Integer sumEven = 0;
        //System.out.println(src.length() / 2);
        for (int i = 0; i < src.length() / 2; i++) {

            Integer odd = Integer.valueOf(src.substring(src.length() - 2 * i - 1, src.length() - 2 * i));
            Integer even = Integer.valueOf(src.substring(src.length() - 2 * i - 2, src.length() - 2 * i - 1));
            //System.out.println("odd:"+odd);
            //System.out.println("even:"+even);
            if (even * 2 > 9) {
                sumEven += even * 2 - 9;
            } else {
                sumEven += even * 2;
            }
            sumOdd += odd;

        }

        //System.out.println("奇数和：" + sumOdd);
        //System.out.println("偶数和：" + sumEven);

        return 10 - (sumOdd + sumEven) % 10 == 10 ? 0 : 10 - (sumOdd + sumEven) % 10;
    }


    private String getSeriaNo(String devMac, String userid) {

        Random random = new Random();
        int i = random.nextInt(1000);

        String seriaNo = devMac + "|" + userid + "|" + (i - (System.currentTimeMillis() / 1000));
        return MD5Util.MD5(seriaNo).toLowerCase().substring(3, 12);

    }

//    public static String getQrCodeV2() {
//
//        List<String> roomNos = new ArrayList<>();
//        roomNos.add("01010101");
//
//        return HuaweiQRCodeUnit.getInstance().getQrcodeV2(5, 1000000962, "12234332", roomNos,
//                1614652138L, 5, 5, "888", "extStr");
//    }


//    public static void main(String[] args) {
//
//        //System.out.println(DateFormatUtils.format(new Date(), "Z").substring(0, 3));
//
//        try {
//            List<String> houseList = new ArrayList<>();
//            houseList.add("01010201");
//            System.out.println(HuaweiQRCodeUnit.getInstance().getQrcodeV2(1,"1000000696","11111111",houseList, String.valueOf(System.currentTimeMillis()),2,999,"0",""));
//            //System.out.println(QrcodeCore.getInstance().luhn("88512234332"));
//            //System.out.println(QrcodeCore.getInstance().getQrcode("张三","01010101", 1555420281, 1555430281, 10, 2));
////            System.out.println(SecurityCore.getInstance().decrypt("5d/ctISdM2fP95hXKUtK/KdbSv8+Sx94X+cavjcSTL5bmz0IDusSn1nqugDTKS7+pN58yAfFGR8qaY9i0RIqa0QWLwk8sJ+p8nS8xU0aJg4="));
//            //System.out.println(getQrCodeV2());
//            //System.out.println(roomCodeToHex("16151401").toUpperCase());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    //test
//        public static void main(String[] args) throws Exception {
////			// 需要加密的字串
////			String cSrc = "123";
////
////			// 加密
////			long lStart = System.currentTimeMillis();
//            String enString = SecurityCore.class.newInstance().encrypt("啦啦啦");
//            System.out.println("加密后的字串是：" + enString);
////
////			long lUseTime = System.currentTimeMillis() - lStart;
////			System.out.println("加密耗时：" + lUseTime + "毫秒");
////			// 解密
////			lStart = System.currentTimeMillis();
//            Date now = new Date(Long.valueOf("1604012400000"));
//            ZoneId zoneId = ZoneId.systemDefault();
//            LocalDateTime thisDate = LocalDateTime.ofInstant(now.toInstant(), zoneId);
//            System.out.println(thisDate.toString());
//            int nextTime = (int) (DateUtil.parse("2020-10-30 18:00:00").getTime() / 1000);
//            Date nextDateTime = new Date(Long.valueOf(nextTime + "000"));
//            LocalDateTime nextDate = LocalDateTime.ofInstant(nextDateTime.toInstant(), zoneId);
//
//            Map<String, String> params = new HashMap<>();
//            params.put("visitName", "1");
//            String m = MessageFormat.format("visitName: {visitName} ", params);
//            System.out.println(m);
//            System.out.println(nextDate.toString());
//
//            System.out.println((nextTime - 1604012400) / 60);
//
//            String DeString = SecurityCore.AES128CBCStringDecoding(base64StringDecoding("6J0ZovTDdCkjGefShk+kxQ"), sKey, ivParameter);
//            System.out.printf(DeString);
////			System.out.println("解密后的字串是：" + DeString);
////			lUseTime = System.currentTimeMillis() - lStart;
////			System.out.println("解密耗时：" + lUseTime + "毫秒");
//        }
    private String getMD5(String str) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = str.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char strs[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                strs[k++] = hexDigits[byte0 >>> 4 & 0xf];

                strs[k++] = hexDigits[byte0 & 0xf];

            }
            return new String(strs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
