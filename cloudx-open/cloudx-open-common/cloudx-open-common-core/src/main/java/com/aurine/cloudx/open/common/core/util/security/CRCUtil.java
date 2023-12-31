package com.aurine.cloudx.open.common.core.util.security;

public class CRCUtil {

    /**
     * CRC-8
     *
     * <table width="400px" border="1" cellpadding="0" cellspacing="0">
     *     <tr>
     *         <th>多项式</th>
     *         <th>初始值</th>
     *         <th>异或值</th>
     *         <th>Bit反转</th>
     *     </tr>
     *     <tr>
     *         <td align="center">0x07</td>
     *         <td align="center">0x00</td>
     *         <td align="center">0x00</td>
     *         <td align="center">MSB First</td>
     *     </tr>
     * </table>
     *
     * @param source
     * @param offset
     * @param length
     * @return
     */
    public static int CRC8(byte[] source, int offset, int length) {
        int wCRCin = 0x00;
        int wCPoly = 0x07;
        for (int i = offset, cnt = offset + length; i < cnt; i++) {
            for (int j = 0; j < 8; j++) {
                boolean bit = ((source[i] >> (7 - j) & 1) == 1);
                boolean c07 = ((wCRCin >> 7 & 1) == 1);
                wCRCin <<= 1;
                if (c07 ^ bit) {
                    wCRCin ^= wCPoly;
                }
            }
        }
        wCRCin &= 0xFF;
        return wCRCin ^= 0x00;
    }

    /**
     * CRC-8/DARC
     *
     * <table width="400px" border="1" cellpadding="0" cellspacing="0">
     *     <tr>
     *         <th>多项式</th>
     *         <th>初始值</th>
     *         <th>异或值</th>
     *         <th>Bit反转</th>
     *     </tr>
     *     <tr>
     *         <td align="center">0x39</td>
     *         <td align="center">0x00</td>
     *         <td align="center">0x00</td>
     *         <td align="center">LSB First</td>
     *     </tr>
     * </table>
     *
     * @param source
     * @param offset
     * @param length
     * @return
     */
    public static int CRC8_DARC(byte[] source, int offset, int length) {
        int wCRCin = 0x00;
        // Integer.reverse(0x39) >>> 24
        int wCPoly = 0x9C;
        for (int i = offset, cnt = offset + length; i < cnt; i++) {
            wCRCin ^= ((long) source[i] & 0xFF);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x01) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x00;
    }

    /**
     * CRC-8/ITU
     *
     * <table width="400px" border="1" cellpadding="0" cellspacing="0">
     *     <tr>
     *         <th>多项式</th>
     *         <th>初始值</th>
     *         <th>异或值</th>
     *         <th>Bit反转</th>
     *     </tr>
     *     <tr>
     *         <td align="center">0x07</td>
     *         <td align="center">0x00</td>
     *         <td align="center">0x55</td>
     *         <td align="center">MSB First</td>
     *     </tr>
     * </table>
     *
     * @param source
     * @param offset
     * @param length
     * @return
     */
    public static int CRC8_ITU(byte[] source, int offset, int length) {
        int wCRCin = 0x00;
        int wCPoly = 0x07;
        for (int i = offset, cnt = offset + length; i < cnt; i++) {
            for (int j = 0; j < 8; j++) {
                boolean bit = ((source[i] >> (7 - j) & 1) == 1);
                boolean c07 = ((wCRCin >> 7 & 1) == 1);
                wCRCin <<= 1;
                if (c07 ^ bit) {
                    wCRCin ^= wCPoly;
                }
            }
        }
        wCRCin &= 0xFF;
        return wCRCin ^= 0x55;
    }

    /**
     * CRC-8/MAXIM
     *
     * <table width="400px" border="1" cellpadding="0" cellspacing="0">
     *     <tr>
     *         <th>多项式</th>
     *         <th>初始值</th>
     *         <th>异或值</th>
     *         <th>Bit反转</th>
     *     </tr>
     *     <tr>
     *         <td align="center">0x31</td>
     *         <td align="center">0x00</td>
     *         <td align="center">0x00</td>
     *         <td align="center">LSB First</td>
     *     </tr>
     * </table>
     *
     * @param source
     * @param offset
     * @param length
     * @return
     */
    public static int CRC8_MAXIM(byte[] source, int offset, int length) {
        int wCRCin = 0x00;
        // Integer.reverse(0x31) >>> 24
        int wCPoly = 0x8C;
        for (int i = offset, cnt = offset + length; i < cnt; i++) {
            wCRCin ^= ((long) source[i] & 0xFF);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x01) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x00;
    }

    /**
     * CRC-8/ROHC
     *
     * <table width="400px" border="1" cellpadding="0" cellspacing="0">
     *     <tr>
     *         <th>多项式</th>
     *         <th>初始值</th>
     *         <th>异或值</th>
     *         <th>Bit反转</th>
     *     </tr>
     *     <tr>
     *         <td align="center">0x07</td>
     *         <td align="center">0xFF</td>
     *         <td align="center">0x00</td>
     *         <td align="center">LSB First</td>
     *     </tr>
     * </table>
     *
     * @param source
     * @param offset
     * @param length
     * @return
     */
    public static int CRC8_ROHC(byte[] source, int offset, int length) {
        int wCRCin = 0xFF;
        // Integer.reverse(0x07) >>> 24
        int wCPoly = 0xE0;
        for (int i = offset, cnt = offset + length; i < cnt; i++) {
            wCRCin ^= ((long) source[i] & 0xFF);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x01) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return wCRCin ^= 0x00;
    }

    //16进制字符串转byte[]
    private static byte[] hexToByteArry(String str){
        byte[] bytes = new byte[str.length()/2];
        for(int i=0,item=str.length()/2;i<item;i++){
            Integer a = Integer.parseInt(str.substring(i*2,2*i+2),16);
            bytes[i] = (byte)a.intValue();
        }
        return bytes;
    }
//    public static void main(String[] args) {
//
//        String str = "015115DBBAAA1C0000009A4937600A32000001010101E7030000";
//        byte[] bytes = hexToByteArry(str);
//        System.out.println(CRC8(bytes,0,bytes.length));
//    }

}
