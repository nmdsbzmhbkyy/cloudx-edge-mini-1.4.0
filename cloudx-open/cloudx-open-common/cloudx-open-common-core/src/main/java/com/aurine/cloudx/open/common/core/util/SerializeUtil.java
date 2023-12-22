package com.aurine.cloudx.open.common.core.util;

import java.io.*;
import java.util.Base64;

/**
 * 序列化工具
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-05-20
 * @Copyright:
 */
public class SerializeUtil {

    /**
     * 序列化对象
     *
     * @param obj 对象
     * @return 序列化后的字节数组
     * @throws IOException
     */
    public static String serialize(Object obj) throws IOException {
        if (null == obj) {
            return null;
        }

        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        ) {

            out.writeObject(obj);
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        }

    }

    /**
     * 反序列化
     *
     * @param bytes 对象序列化后的字节数组
     * @return 反序列化后的对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(String str) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(str);

        if (null == bytes) {
            return null;
        }

        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream in = new ObjectInputStream(byteArrayInputStream);
        ) {

            return in.readObject();
        }
    }

//
//
//    public static String serialize(Object obj) throws IOException, IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream;
//        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//        objectOutputStream.writeObject(obj);
//        String string = byteArrayOutputStream.toString("UTF-8");
//        objectOutputStream.close();
//        byteArrayOutputStream.close();
//
//        String s = Base64.getEncoder().encodeToString(SerializationUtils.serialize(list));
//        System.out.println(s);
//        // 使用Base64解码
//        System.out.println(SerializationUtils.deserialize(Base64.getDecoder().decode(s)));
//        return string;
//    }
//
//    public static Object serializeToObject(String str) throws IOException, ClassNotFoundException {
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("UTF-8"));
//        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//        Object object = objectInputStream.readObject();
//        objectInputStream.close();
//        byteArrayInputStream.close();
//        return object;
//    }
}
