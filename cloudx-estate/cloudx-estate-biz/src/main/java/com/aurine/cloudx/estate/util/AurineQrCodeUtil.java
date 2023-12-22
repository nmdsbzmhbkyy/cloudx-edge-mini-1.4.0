package com.aurine.cloudx.estate.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.util.security.AES128;
import com.aurine.cloudx.estate.util.security.CRCUtil;
import com.aurine.cloudx.estate.util.security.MD5Util;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @Auther: hjj
 * @Date: 2023/3/16 08:50
 * @Description:
 */
@Data
@Component
public class AurineQrCodeUtil {
    private static final String SIGN = "@u^$OPlx";

    public static final String ENCRYPTIO_TYPE_AES_BASE64 = "3";//新版本AES base64编码

    public static final String V2_CBC_IV = "3zcI#vOkxL8uJLZ8";

    public static final String V2_PUBLIC_KEY = "eimKGGm!B3Fn47TD";

    static final byte[] HEX_CHAR_TABLE = {
            (byte) '0', (byte) '1', (byte) '2', (byte) '3',
            (byte) '4', (byte) '5', (byte) '6', (byte) '7',
            (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
            (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
    };

//    public static void main(String[] args) {
//        String json="{\"EdgeSysParamObj\":{\"faceEnable\":1,\"ftpPathBlacklist\":\"/PersonPhoto/SnapShot/Blacklist/\",\"cloudTalkEnable\":1,\"ftpPathStranger\":\"/PersonPhoto/SnapShot/Stranger/\",\"ftpIp\":\"10.110.16.191\",\"ftpPort\":21,\"ftpUser\":\"admin\",\"ftpPwd\":\"admin\",\"cloudAreaId\":\"\",\"manufacturer\":0},\"DeviceNoObj\":{\"devSubDesc\":[\"楼栋\",\"单元\",\"房屋\"],\"devNoRule\":{\"useCellNo\":1,\"roomNoLen\":4,\"cellNoLen\":2,\"subSection\":\"224\",\"stairNoLen\":4}}}";
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        JSONObject edgeSysParamObj = jsonObject.getJSONObject("EdgeSysParamObj");
//        System.out.println(edgeSysParamObj);
//        String string = edgeSysParamObj.getString("cloudAreaId");
//        System.out.println(string);
//    }

//    public static void main(String[] args) {
//        String qr = getQrcodeV2(2,
//                "1000000708",
//                "112233",
//                new ArrayList<String>() {{
//                    add("01010101");
//                    add("01010102");
//                }},
//                String.valueOf(new Date().getTime() / 1000),
//                1,
//                2,
//                "223344",
//                "789",
//                "2222"
//        );
//        System.out.println("----------------------------------------");
//        System.out.println(qr);
//        QrCode analysis = new AurineQrCodeUtil().analysis(qr, "224", 2);
//        System.out.println(analysis);
//    }

    /**
     * 新版二位码生成，V2.0
     *
     * @param type          二维码类型，1、业主二维码，2、临时分享二维码（时间段），3、临时分享二维码（N次）,5、临时分享二维码（时段+次数）
     * @param userNo        用户码，8位字符长度
     * @param roomNos       房屋编号集合 集合长度最大支持16
     * @param startTime     开始时间
     * @param effectiveTime 有效时间，单位：分钟
     * @param times         次数
     * @param businessId    业务ID
     * @param TStr          第三方梯控字段
     * @param rule          房号规则，例如：2222代表2位楼栋，2位单元，2位楼层，2位房间；22222代表2位组团，2位楼栋，2位单元，2位楼层，2位房间
     * @return
     */
    public static String getQrcodeV2(Integer type, String communityCode, String userNo, List<String> roomNos, String startTime,
                                     Integer effectiveTime, Integer times, String businessId, String TStr, String rule) {

        //加密方式：电子钥匙数据域：校验串：梯控：扩展标识
        String dataFormat = "%s:%s:%s:%s:%s";

        String electronicKeyDataField = getElectronicKeyDataV2(type, communityCode, userNo, startTime, effectiveTime, times, roomNos, rule, businessId);

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

        if (TStr == null) {
            TStr = "";
        }
        String extStr = "";

        //包含组团数据
        if (rule.length() == 5) {
            //增加组团扩展数据
            byte[] keyG = "G:".getBytes();
            byte[] extStrBytes = ArrayUtils.addAll(keyG);
            for (String str :
                    roomNos) {
                String groupCode = getHexGroupString(str, rule);
                if (StringUtils.isNotBlank(groupCode)) {
                    byte[] groupCodeBytes = {(byte) Integer.parseInt(groupCode, 16)};
                    System.out.println(groupCodeBytes[0]);
                    extStrBytes = ArrayUtils.addAll(extStrBytes, groupCodeBytes);
                }
            }

            //增加组团扩展数据R
            byte[] keyR = "|R:".getBytes();
            extStrBytes = ArrayUtils.addAll(extStrBytes, keyR);
            for (String str :
                    roomNos) {
                String groupCode = getGroupString(str, rule);
                if (StringUtils.isNotBlank(groupCode)) {
                    groupCode = groupCode + ",";
                    extStrBytes = ArrayUtils.addAll(extStrBytes, groupCode.getBytes());
                }
            }

            try {
                extStr = new String(Base64.getEncoder().encode(extStrBytes), "iso-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        /*byte[] bs = Base64.getDecoder().decode(extStr);
        byte a = bs[2];
        System.out.println(a&0xFF);*/

        //return String.format(dataFormat, ENCRYPTIO_TYPE_AES_BASE64, electronicKeyDataField, checkStr, extStr, TStr);
        if (communityCode.equals("S1000000563")) { // 修改getQrcodeV2方法，为了兼容风华里项目开梯控二维码内容，去掉第四段组团extStr屏蔽，临时应对方法，后续再讨论合理处理方法 by chensl [2022-04-03]
            dataFormat = "%s:%s:%s:%s";
            return String.format(dataFormat, ENCRYPTIO_TYPE_AES_BASE64, electronicKeyDataField, checkStr, TStr);
        }
        return String.format(dataFormat, ENCRYPTIO_TYPE_AES_BASE64, electronicKeyDataField, checkStr, extStr, TStr);
    }

    //获取10进制组团号
    public static String getGroupString(String code, String rule) {
        String groupCode = "";
        if (rule.length() == 5) {
            Integer groupRule = Integer.valueOf(rule.substring(0, rule.length() - 4));
            //System.out.println("groupRule:"+groupRule);
            groupCode = code.substring(0, groupRule);
            //System.out.println("groupCode:"+groupCode);
        }
        return groupCode;
    }

    //获取16进制组团号
    public static String getHexGroupString(String code, String rule) {
        System.out.println(code + ":" + rule);
        String groupCode = "";
        if (rule.length() == 5) {
            Integer groupRule = Integer.valueOf(rule.substring(0, rule.length() - 4));
            //System.out.println("groupRule:"+groupRule);
            groupCode = code.substring(0, groupRule);
            //System.out.println("groupCode:"+groupCode);
            groupCode = parchLeftZero(Integer.toHexString(Integer.valueOf(groupCode)), 2);
        }
        return groupCode;
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
    private static String getElectronicKeyDataV2(
            Integer type,
            String communityCode,
            String userNo,
            String startTime,
            Integer effectiveTime,
            Integer times,
            List<String> roomNos,
            String rule,
            String businessId) {

        //char[] electronicKeyData = new char[24+4*roomNos.size()];

        String head = "FA";//固定头 1B

        String ver = "01";//版本 1B

        String qrcodeType = type.toString();//二维码类型 4bit


        String roomNum = Integer.toHexString(roomNos.size());//房号个数 4bit

        System.out.println("房号个数:" + roomNum);

        String userCode = "";//用户通行码 8B
        userNo = parchLeftZero(userNo, 8);

        communityCode = Sm3Utils.encrypt(communityCode);
        System.out.println("社区ID进行SM3加密:" + communityCode);
        communityCode = communityCode.substring(communityCode.length() - 6, communityCode.length());
        System.out.println("取后3个字节:" + communityCode);
        communityCode = String.valueOf(Integer.parseInt(communityCode, 16));
        System.out.println("转10进制:" + communityCode);
        System.out.println(communityCode + userNo);
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

        String timeStamp = startTime;//开始时间

        timeStamp = Long.toHexString(Long.valueOf(timeStamp));

        timeStamp = parchLeftZero(timeStamp, 8);

        timeStamp = reverseHex(timeStamp).toUpperCase();

        if (effectiveTime == null) {
            effectiveTime = 0;
        }

        System.out.println("开始时间：" + timeStamp);

        String minute = String.valueOf(effectiveTime);

        if (qrcodeType.equals("3")) {
            minute = times.toString();
            minute = Long.toHexString(Long.valueOf(minute));
            minute = parchLeftZero(minute, 8);
            minute = reverseHex(minute).toUpperCase();
        } else if (qrcodeType.equals("5")) {
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

        //默认房号规则为2222，即两位楼栋号，两位单元号，两位层号，两位房间号
        if (StringUtils.isBlank(rule)) {
            rule = "2222";
        }

        /*for (String str:
                roomNos) {
            roomAuth += roomCodeToHex(str);
        }*/
        for (String str :
                roomNos) {
            if ("0000".equals(str)) {
                roomAuth = str;
            } else {
                String roomCode = changeRoomCode(str, rule);
                roomAuth += roomCode;
            }
        }
        roomAuth = roomAuth.toUpperCase();
        System.out.println("房号集合：" + roomAuth);

        //String electronicKeyDataV2 =  ver + qrcodeType + roomNum + userCode + timeStamp + minute + roomAuth + businessId;

        try {
            if (businessId != null) {
                businessId = Long.toHexString(Long.valueOf(businessId));
                businessId = parchLeftZero(businessId, 8);
                businessId = reverseHex(businessId).toUpperCase();
            } else {
                businessId = "00000000";
            }
        } catch (NumberFormatException e) {
            businessId = "00000000";
            e.printStackTrace();
        }


        String electronicKeyDataV2 = ver + qrcodeType + roomNum + userCode + timeStamp + minute + roomAuth + businessId;

        byte[] bytes = hexToByteArry(electronicKeyDataV2);

        //String waitCRC = String.valueOf(bytes);

        int crc = CRCUtil.CRC8(bytes, 0, bytes.length);

        String crcStr = Integer.toHexString(crc);
        crcStr = parchLeftZero(crcStr, 2).toUpperCase();

        System.out.println("crc校验码：" + crcStr);


        return head + electronicKeyDataV2 + crcStr;
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

    //十进制房号，根据规则转16进制
    public static String changeRoomCode(String code, String rule) {
        String HexCode = "";
        String roomCode = "0";
        String floorCode = "0";
        String unitCode = "0";
        String bulidingCode = "0";
        //String groupCode = "";
        //System.out.println(rule.length());
        //if(rule.length() == 5 || rule.length() == 4){
        Integer roomRule = Integer.valueOf(rule.substring(rule.length() - 1));
        Integer floorRule = Integer.valueOf(rule.substring(rule.length() - 2, rule.length() - 1));
        Integer unitRule = Integer.valueOf(rule.substring(rule.length() - 3, rule.length() - 2));
        Integer bulidingRule = Integer.valueOf(rule.substring(rule.length() - 4, rule.length() - 3));
        if ((roomRule + floorRule + unitRule + bulidingRule) > code.length()) {
            roomRule = roomRule - 1;
        }
        roomCode = code.substring(code.length() - roomRule);
        //System.out.println(roomCode);
        floorCode = code.substring(code.length() - (roomRule + floorRule), code.length() - roomRule);
        //System.out.println(floorCode);
        unitCode = code.substring(code.length() - (roomRule + floorRule + unitRule), code.length() - (roomRule + floorRule));
        //System.out.println(unitCode);
        bulidingCode = code.substring(code.length() - (roomRule + floorRule + unitRule + bulidingRule), code.length() - (roomRule + floorRule + unitRule));
        //System.out.println(bulidingCode);

//        if(rule.length() == 5){
//            Integer groupRule = Integer.valueOf(rule.substring(0,rule.length()-4));
//            System.out.println("groupRule:"+groupRule);
//            groupCode = code.substring(code.length() - (roomRule+floorRule+unitRule+bulidingRule+groupRule),code.length() - (roomRule+floorRule+unitRule+bulidingRule));
//            System.out.println("groupCode:"+groupCode);
//            groupCode = parchLeftZero(Integer.toBinaryString(Integer.valueOf(groupCode)&0x0F),4);
//        }
        //}
        //System.out.println(groupCode);
        bulidingCode = parchLeftZero(Integer.toBinaryString(Integer.valueOf(bulidingCode)), 8);
        //System.out.println(bulidingCode);
        unitCode = parchLeftZero(Integer.toBinaryString(Integer.valueOf(unitCode)), 8);
        //System.out.println(unitCode);
        if (StringUtils.isEmpty(floorCode)) {
            floorCode = "0";
        }
        floorCode = parchLeftZero(Integer.toBinaryString(Integer.valueOf(floorCode)), 8);
        //System.out.println(floorCode);
        roomCode = parchLeftZero(Integer.toBinaryString(Integer.valueOf(roomCode)), 8);
        //System.out.println(roomCode);
        String str = /*groupCode +*/ bulidingCode + unitCode + floorCode + roomCode;
        //System.out.println(str);
        for (int i = 0; i < str.length() / 8; i++) {
            String binary = str.substring(8 * i, 8 * (i + 1));
            //System.out.println(binary);
            HexCode += parchLeftZero(Integer.toHexString(Integer.parseInt(binary, 2)), 2);
        }
        //System.out.println(HexCode);
        return HexCode;
    }

    //模10算法
    private static Integer luhn(String src) {

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

    public QrCode analysis(String qrString) {
        if (StrUtil.isBlank(qrString)) {
            return null;
        }
        String[] item = qrString.split(":");
        if (item.length < 3) {
            return null;
        }
        if (!StrUtil.equals("3", item[0])) {
            return null;
        }
        byte[] bytes = null;
        try {
            bytes = AES128.decrypt(item[1], "iso-8859-1").getBytes("iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        String hexStr = "";
        for (byte b :
                bytes) {
            hexStr += parchLeftZero(Integer.toHexString(b & 0xff), 2);
        }
        QrCode qrCode = new QrCode();
        qrCode.setType(hexStr.substring(4, 5));
        qrCode.setUserNo(String.valueOf(Integer.parseInt(reverseHex(hexStr.substring(12, 20)), 16)));
        qrCode.setStartTime(Long.parseLong(reverseHex(hexStr.substring(22, 30)), 16));
        if (StrUtil.equals(qrCode.getType(), "3")) {
            qrCode.setTimes(Integer.parseInt(reverseHex(hexStr.substring(30, 38)), 16));
        } else if (StrUtil.equals(qrCode.getType(), "5")) {
            qrCode.setEffectiveTime(Integer.parseInt(reverseHex(hexStr.substring(32, 38)), 16));
            qrCode.setTimes(Integer.parseInt(reverseHex(hexStr.substring(30, 32)), 16));
        } else {
            qrCode.setEffectiveTime(Integer.parseInt(reverseHex(hexStr.substring(30, 38)), 16));
        }
//        Integer roomSize = Integer.valueOf(hexStr.substring(5, 6));
//        List<String> roomList = new ArrayList<>();
//        for (int i = 0; i < roomSize; i++) {
//            int baseIndex = 38 + i * 8;
//            String buildingNo = String.valueOf(Integer.parseInt(hexStr.substring(baseIndex, baseIndex + 2), 16));
//            String unitNo = String.valueOf(Integer.parseInt(hexStr.substring(baseIndex + 2, baseIndex + 4), 16));
//            String roomNo = String.valueOf(Integer.parseInt(hexStr.substring(baseIndex + 4, baseIndex + 8), 16));
//            roomList.add(buildingNo + unitNo + roomNo);
//        }
//        qrCode.setRoomNos(roomList);
//        qrCode.setBusinessId(Long.parseLong(reverseHex(hexStr.substring(38 + roomSize * 8, 38 + (roomSize + 1) * 8)), 16));
        System.out.println(JSONUtil.toJsonStr(qrCode));
        return qrCode;
    }

    public static QrCode analysis(String qrString, String rule, Integer floorLen) {
        if (StrUtil.isBlank(qrString)) {
            return null;
        }
        String[] item = qrString.split(":");
        if (item.length < 3) {
            return null;
        }
        if (!StrUtil.equals("3", item[0])) {
            return null;
        }
        byte[] bytes = null;
        try {
            bytes = AES128.decrypt(item[1], "iso-8859-1").getBytes("iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        String hexStr = "";
        for (byte b :
                bytes) {
            hexStr += parchLeftZero(Integer.toHexString(b & 0xff), 2);
        }
        QrCode qrCode = new QrCode();
        qrCode.setType(hexStr.substring(4, 5));
        int communityCode = Integer.parseInt(reverseHex(hexStr.substring(6, 12)), 16);
        qrCode.setCommunityCode(String.valueOf(communityCode));
        qrCode.setUserNo(String.valueOf(Integer.parseInt(reverseHex(hexStr.substring(12, 20)), 16)));
        qrCode.setStartTime(Long.parseLong(reverseHex(hexStr.substring(22, 30)), 16));
        if (StrUtil.equals(qrCode.getType(), "3")) {
            qrCode.setTimes(Integer.parseInt(reverseHex(hexStr.substring(30, 38)), 16));
        } else if (StrUtil.equals(qrCode.getType(), "5")) {
            qrCode.setEffectiveTime(Integer.parseInt(reverseHex(hexStr.substring(32, 38)), 16));
            qrCode.setTimes(Integer.parseInt(reverseHex(hexStr.substring(30, 32)), 16));
        } else {
            qrCode.setEffectiveTime(Integer.parseInt(reverseHex(hexStr.substring(30, 38)), 16));
        }
        Integer roomSize = Integer.valueOf(hexStr.substring(5, 6));
        List<Room> roomList = new ArrayList<>(roomSize);
        Integer buildingLen = Integer.valueOf(rule.substring(0, 1));
        buildingLen=buildingLen==0?2:buildingLen;
        Integer uintLen = Integer.valueOf(rule.substring(1, 2));
        uintLen=uintLen==0?2:uintLen;
        Integer floorAndRoomLen = Integer.valueOf(rule.substring(2, 3));
        floorAndRoomLen=floorAndRoomLen==0?4:floorAndRoomLen;
        Integer roomLen = floorAndRoomLen - floorLen;
        roomLen=roomLen<=0?2:roomLen;


        for (int i = 0; i < roomSize; i++) {
            int baseIndex = 38 + i * 8;
            Room room = new Room();

            room.buildingNo = parchLeftZero(String.valueOf(Integer.parseInt(hexStr.substring(baseIndex, baseIndex + 2), 16)), buildingLen);
            room.unitNo = parchLeftZero(String.valueOf(Integer.parseInt(hexStr.substring(baseIndex + 2, baseIndex + 4), 16)), uintLen);
            room.floorNo = parchLeftZero(String.valueOf(Integer.parseInt(hexStr.substring(baseIndex + 4, baseIndex + 6), 16)), floorLen);
            room.roomNo = parchLeftZero(String.valueOf(Integer.parseInt(hexStr.substring(baseIndex + 6, baseIndex + 8), 16)), roomLen);
            roomList.add(room);
        }
        qrCode.setRoomNos(roomList);
        qrCode.setBusinessId(Long.parseLong(reverseHex(hexStr.substring(38 + roomSize * 8, 38 + (roomSize + 1) * 8)), 16));
        System.out.println(JSONUtil.toJsonStr(qrCode));
        return qrCode;
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
    private static String reverseHex(String hex) {
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

    @Data
    public static class QrCode {
        //二维码类型，1、业主二维码，2、临时分享二维码（时间段），3、临时分享二维码（N次）,5、临时分享二维码（时段+次数）
        private String type;
        private String communityCode;
        //用户码，8位字符长度
        private String userNo;
        private List<Room> roomNos;
        private Long businessId;
        //10位
        private Long startTime;
        private Integer effectiveTime;
        private Integer times;
    }

    @Data
    public static class Room {
        private String buildingNo;
        private String unitNo;
        private String floorNo;
        private String roomNo;

        public String toString(){
            return buildingNo+unitNo+floorNo+roomNo;
        }
    }
}
