package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.core.entity.TreeNode;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.*;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-27
 * @Copyright:
 */
public class AurineQRCodeUnit {


    public String convert(List<String> houseCode) {
        StringBuffer dataString = new StringBuffer("");
        if (houseCode == null || houseCode.size() == 0) {
            return "00000000";
        }
        List<TreeNode> data = new ArrayList<>();
        for (String code : houseCode) {
            if (StringUtils.isNotBlank(code)) {
                String unitString = StringUtils.substring(code, 0, 4);
                String buildString = StringUtils.substring(code, 4);
                int x = 0;
                for (TreeNode treeNode : data) {
                    if (unitString.equals(treeNode.getId())) {
                        x++;
                        if (ObjectUtil.isNotEmpty(treeNode.getChildren())) {
                            List<TreeNode> thisChildren = treeNode.getChildren();
                            int y = 0;
                            for (TreeNode thisChild : thisChildren) {
                                if (buildString.equals(thisChild.getId())) {
                                    y++;
                                }
                            }
                            if (y == 0) {
                                if (StringUtils.isNotBlank(buildString)) {
                                    TreeNode newTreeNode = new TreeNode();
                                    newTreeNode.setParentId(unitString);
                                    newTreeNode.setId(buildString);
                                    thisChildren.add(newTreeNode);
                                }

                            }
                        } else {
                            if (StringUtils.isNotBlank(buildString)) {
                                List<TreeNode> children = new ArrayList<>();
                                TreeNode newTreeNode = new TreeNode();
                                newTreeNode.setParentId(unitString);
                                newTreeNode.setId(buildString);
                                children.add(newTreeNode);
                                treeNode.setChildren(children);
                            }

                        }

                    }
                }
                if (x == 0) {
                    TreeNode treeNode = new TreeNode();
                    treeNode.setParentId("");
                    treeNode.setId(unitString);
                    if (StringUtils.isNotBlank(buildString)) {
                        List<TreeNode> children = new ArrayList<>();
                        TreeNode newTreeNode = new TreeNode();
                        newTreeNode.setParentId(unitString);
                        newTreeNode.setId(buildString);
                        children.add(newTreeNode);
                        treeNode.setChildren(children);
                    }
                    data.add(treeNode);
                }
            }


        }
        int x = 0;
        for (TreeNode node : data) {
            if (x > 0) {
                dataString.append("&").append(node.getId());
                List<TreeNode> childNodes = node.getChildren();
                int y = 0;
                for (TreeNode childNode : childNodes) {
                    if (y > 0) {
                        dataString.append(",").append(childNode.getId());
                    } else {
                        dataString.append(childNode.getId());
                    }
                    y++;
                }

            } else {
                dataString.append(node.getId());
                List<TreeNode> childNodes = node.getChildren();
                int y = 0;
                for (TreeNode childNode : childNodes) {
                    if (y > 0) {
                        dataString.append(",").append(childNode.getId());
                    } else {
                        dataString.append(childNode.getId());
                    }
                    y++;
                }
            }
            x++;
        }
        return dataString.toString();
    }

    public String decrypt(String val) throws Exception {
        System.out.println("=======" + val);
        String content = val.split(":")[1];
        String type = val.split(":")[0];

        String deString = new SecurityCore().decrypt(content);
        return deString;
    }

    /**
     * @param userName  业主姓名，20个字节（10个汉字），可空
     * @param userRoom  业主房号，必填，最多支持18字节（室内主机设备编号去掉分机号），格式：[小区ID-]业主房号
     *                  例：
     *                  01010101;
     *                  1-01010101;
     *                  1-01010102,0203&01021030,0205;
     *                  12-01020102&02030101;
     * @param beginTime 开始时间戳（10位，秒，必填），只针对有指定次数访客二维码生效
     * @param endTime   结束时间戳（10位，秒，必填），业主访客都生效
     * @param times     有效次数（刷码次数，可空，默认1次），只对访客有效
     * @param userType  用户类型，1：业主； 2：访客，必填
     * @return 返回二维码字符串
     */
    public String getQrcode(String userName, String userRoom, Integer beginTime, Integer endTime, Integer times, Integer userType) {

        ElectronicKeyData data = new ElectronicKeyData();
        data.setBeginTime(beginTime.toString());
        data.setDevMac("");
        data.setDevType("020303");
        if (userType != null && userType == 1) {
            data.setQcodeType("1");
        } else {
            if (times == null) {
                data.setQcodeType("3");
                data.setUserid("");
            } else {
                data.setQcodeType("4");
                data.setUserid(times.toString());
            }
        }

        data.setSyncTime("0");
        if (userName == null || "".equals(userName)) {
            data.setUserName("");
        } else {
            data.setUserName(userName);
        }

        data.setUserRoom(userRoom);
        data.setValidTime(String.valueOf((endTime - beginTime) / 60));
        String qrcode = "2:" + getElectronicKeyData(data);
        System.out.println(getMD5(qrcode));
        String verify = getMD5(qrcode).substring(5, 9);
        return qrcode + ":" + verify;
    }


    public String getQrcode(String personName, String userType, String communityCode, List<String> roomNos, Long startTime, Integer effectiveTime, Integer times) {

        String userRoom = "S" + communityCode + "-";
        if (CollUtil.isNotEmpty(roomNos)) {
            userRoom += this.convert(roomNos);
        } else {
            userRoom += "00000000";
        }


        ElectronicKeyData data = new ElectronicKeyData();
        data.setBeginTime(String.valueOf(startTime / 1000));
        data.setDevMac("");
        data.setDevType("020303");
        if (StringUtils.equals(userType, PersonTypeEnum.PROPRIETOR.code)) {
            data.setQcodeType("1");
        } else {
            if (times == null) {
                data.setQcodeType("3");
                data.setUserid("");
            } else {
                data.setQcodeType("4");
                data.setUserid(times.toString());
            }
        }

        data.setSyncTime("0");
        if (personName == null || "".equals(personName)) {
            data.setUserName("");
        } else {
            data.setUserName(personName);
        }

        data.setUserRoom(userRoom);
//        data.setValidTime(String.valueOf((endTime - beginTime) / 60));
        data.setValidTime(String.valueOf(effectiveTime));
        String qrcode = "2:" + getElectronicKeyData(data);
        System.out.println(getMD5(qrcode));
        String verify = getMD5(qrcode).substring(5, 9);
        return qrcode + ":" + verify;
    }

    /**
     * @param userName   业主姓名，20个字节（10个汉字），可空
     * @param userRoom   业主房号，必填，最多支持18字节（室内主机设备编号去掉分机号），格式：[小区ID-]业主房号
     *                   例：
     *                   01010101;
     *                   1-01010101;
     *                   1-01010102,0203&01021030,0205;
     *                   12-01020102&02030101;
     * @param beginTime  开始时间戳（10位，秒，必填），只针对有指定次数访客二维码生效
     * @param activeTime 有效时间，分钟
     * @param times      有效次数（刷码次数，可空，默认1次），只对访客有效
     * @param userType   用户类型，1：业主； 2：访客，必填
     * @return 返回二维码字符串
     */
    public String getQrcodeByActiveTime(String userName, String userRoom, Integer beginTime, Integer activeTime, Integer times, Integer userType) {

        ElectronicKeyData data = new ElectronicKeyData();
        data.setBeginTime(beginTime.toString());
        data.setDevMac("");
        data.setDevType("020303");
        if (userType != null && userType == 1) {
            data.setQcodeType("1");
        } else {
            if (times == null) {
                data.setQcodeType("3");
                data.setUserid("");
            } else {
                data.setQcodeType("4");
                data.setUserid(times.toString());
            }
        }

        data.setSyncTime("0");
        if (userName == null || "".equals(userName)) {
            data.setUserName("");
        } else {
            data.setUserName(userName);
        }

        data.setUserRoom(userRoom);
        data.setValidTime(String.valueOf(activeTime));
        String qrcode = "2:" + getElectronicKeyData(data);
        System.out.println(getMD5(qrcode));
        String verify = getMD5(qrcode).substring(5, 9);
        return qrcode + ":" + verify;
    }

//	public static void main(String[] args) {
//
//		//String tzStr = QrcodeUtil.getInstance().getTimeZoneValue();
//		//System.out.println(tzStr);
//
//		try {
//			long now = new Date().getTime() / 1000;
//			// 生成以当前时间，10次限制的访客二维码
////			System.out.println(QrcodeUtil.getInstance().getQrcode("张三","S1000000329-01010101", (int)now, (int)(now + 7200), 5, 2));
////			System.out.println(QrcodeUtil.getInstance().getQrcode("张三","S1000000329-01010101", (int)now, (int)(now + 60), 10, 2));
////			System.out.println(QrcodeUtil.getInstance().getQrcode("张三","S1000000329-01010101", (int)now, (int)(now + 7200), 10, 2));
//			System.out.println(QrcodeUtil.getInstance().getQrcode("张三","S1000000329-01010000&02010000", (int)now, (int)(now + 7200), null, 1));
//
//			// 生成业主过期二维码
////			System.out.println(QrcodeUtil.getInstance().getQrcode("李四","S1000000329-01010101", (int)(now - 72000), (int)(now - 70000), null, 1));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

    private static AurineQRCodeUnit instance = null;

    private AurineQRCodeUnit() {
    }

    public static AurineQRCodeUnit getInstance() {
        if (instance == null) {
            instance = new AurineQRCodeUnit();
        }
        return instance;
    }

    private String getElectronicKeyData(ElectronicKeyData data) {

        String seriaNo = getSeriaNo(data.getDevMac(), data.getUserid());

        String electronicKeyDataStr = data.getDevType() + "|" +
                data.getDevMac() + "|" +
                data.getUserid() + "|" +
                data.getQcodeType() + "|" +
                data.getUserRoom() + "|" +
                data.getUserName() + "|" +
                data.getBeginTime() + ";" + getTimeZoneValue() + "|" +
                data.getValidTime() + "|" +
                seriaNo + "|" +
                data.getSyncTime();

        String checkCode = getMD5(electronicKeyDataStr).substring(5, 9);

        electronicKeyDataStr += "|" + checkCode;

        System.out.println(electronicKeyDataStr);

        return SecurityCore.encrypt(electronicKeyDataStr);
    }

    private String getSeriaNo(String devMac, String userid) {

        Random random = new Random();
        int i = random.nextInt(1000);

        String seriaNo = devMac + "|" + userid + "|" + (i - (System.currentTimeMillis() / 1000));
        return getMD5(seriaNo).substring(3, 12);

    }

    private String getTimeZoneValue() {
        int rawOffset = 0;
        synchronized (TimeZone.class) {
            TimeZone.setDefault(null);
            System.setProperty("user.timezone", "");

            TimeZone tz = TimeZone.getDefault();
            rawOffset = tz.getRawOffset();
        }
        String symbol = "+";
        if (rawOffset < 0) {
            symbol = "-";
        }
        rawOffset = Math.abs(rawOffset);
        int offsetHore = rawOffset / 3600000;
        String hour = String.format("%1$02d", offsetHore);
        String timeZone = symbol + hour;
        //System.out.println(timeZone);
        return timeZone;
    }

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

    private class ElectronicKeyData {

        private String devType;
        private String devMac;
        private String userid;
        private String qcodeType;
        private String userRoom;
        private String userName;
        private String beginTime;
        private String validTime;
        private String serialNo;
        private String syncTime;
        private String checkCode;

        public String getDevType() {
            return devType;
        }

        public void setDevType(String devType) {
            this.devType = devType;
        }

        public String getDevMac() {
            return devMac;
        }

        public void setDevMac(String devMac) {
            this.devMac = devMac;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getQcodeType() {
            return qcodeType;
        }

        public void setQcodeType(String qcodeType) {
            this.qcodeType = qcodeType;
        }

        public String getUserRoom() {
            return userRoom;
        }

        public void setUserRoom(String userRoom) {
            this.userRoom = userRoom;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getValidTime() {
            return validTime;
        }

        public void setValidTime(String validTime) {
            this.validTime = validTime;
        }

        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public String getSyncTime() {
            return syncTime;
        }

        public void setSyncTime(String syncTime) {
            this.syncTime = syncTime;
        }

        public String getCheckCode() {
            return checkCode;
        }

        public void setCheckCode(String checkCode) {
            this.checkCode = checkCode;
        }
    }

    private static class SecurityCore {
        /*
         * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
         */
        private static String sKey = "eimKGGm!B3Fn47TD";
        private static String ivParameter = "3zcI#vOkxL8uJLZ8";

        public static String webSafeBase64StringEncoding(byte[] sSrc, boolean padded) throws Exception {
            Base64.Encoder enc = Base64.getEncoder();
            String encodeString = new String(enc.encode(sSrc));// 此处使用BASE64做转码。

            //websafe base64
            encodeString = encodeString.replace("+", "-");
            encodeString = encodeString.replace("/", "_");

            //nopadding base64
            if (!padded) {
                if (encodeString.endsWith("=")) {
                    encodeString = encodeString.substring(0, encodeString.length() - 1);
                    if (encodeString.endsWith("=")) {
                        encodeString = encodeString.substring(0, encodeString.length() - 1);
                    }
                }
            }
            return encodeString;
        }

        public static byte[] webSafeBase64StringDecoding(String sSrc) throws Exception {
            //websafe base64
            sSrc = sSrc.replace("-", "+");
            sSrc = sSrc.replace("_", "/");

            Base64.Decoder dec = Base64.getDecoder();
            return dec.decode(sSrc);
        }

        public static String base64StringEncoding(byte[] sSrc, boolean padded) throws Exception {
            Base64.Encoder enc = Base64.getEncoder();
            String encodeString = new String(enc.encode(sSrc));// 此处使用BASE64做转码。
            //String encodeString=Base64.encodeBase64String(sSrc);// 此处使用BASE64做转码。

            //nopadding base64
            if (!padded) {
                if (encodeString.endsWith("=")) {
                    encodeString = encodeString.substring(0, encodeString.length() - 1);
                    if (encodeString.endsWith("=")) {
                        encodeString = encodeString.substring(0, encodeString.length() - 1);
                    }
                }
            }
            return encodeString;
        }

        public static byte[] base64StringDecoding(String sSrc) throws Exception {
            Base64.Decoder dec = Base64.getDecoder();
            return dec.decode(sSrc);
            //return Base64.decodeBase64(sSrc);
        }

        public static byte[] AES128CBCStringEncoding(String encData, String secretKey, String vector) throws Exception {

            if (secretKey == null) {
                return null;
            }
            if (secretKey.length() != 16) {
                return null;
            }
            if (vector != null && vector.length() != 16) {
                return null;
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = secretKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));

            return encrypted;
        }

        public static String AES128CBCStringDecoding(byte[] sSrc, String key, String ivs) throws Exception {
            try {
                if (key == null) {
                    return null;
                }
                if (key.length() != 16) {
                    return null;
                }
                if (ivs != null && ivs.length() != 16) {
                    return null;
                }
                byte[] raw = key.getBytes("ASCII");
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
                byte[] original = cipher.doFinal(sSrc);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception ex) {
                return null;
            }
        }


        // 加密
        public static String encrypt(String sSrc) {
            try {
                String encodeString = base64StringEncoding(AES128CBCStringEncoding(sSrc, sKey, ivParameter), false);

                return encodeString;
            } catch (Exception ex) {
                return null;
            }
        }

        // 解密
        public String decrypt(String sSrc) throws Exception {
            try {
                String decodeString = AES128CBCStringDecoding(base64StringDecoding(sSrc), sKey, ivParameter);
                return decodeString;
            } catch (Exception ex) {
                return null;
            }
        }

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
    }
}
