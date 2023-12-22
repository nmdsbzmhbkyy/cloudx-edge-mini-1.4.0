package com.aurine.cloudx.open.common.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-04-07 09:13
 */
public class NewDateUtil {
    public static String getNewDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }
}
