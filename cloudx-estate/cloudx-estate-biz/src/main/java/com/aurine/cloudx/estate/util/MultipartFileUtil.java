package com.aurine.cloudx.estate.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.http.entity.ContentType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-05
 * @Copyright:
 */
public class MultipartFileUtil {

    /**
     * url转变为 MultipartFile对象
     * 转换失败返回null对象
     * @param url
     * @param fileName
     * @return
     * @throws Exception
     */
    public static MultipartFile createFile(String url, String fileName) {
        FileItem item = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            //设置应用程序要从网络连接读取数据
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();

                FileItemFactory factory = new DiskFileItemFactory(16, null);
                String textFieldName = "uploadfile";
                item = factory.createItem(textFieldName, ContentType.APPLICATION_OCTET_STREAM.toString(), false, fileName);
                OutputStream os = item.getOutputStream();

                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                is.close();
            }
        } catch (IOException e) {
           e.printStackTrace();
        }

        return new CommonsMultipartFile(item);
    }
}
