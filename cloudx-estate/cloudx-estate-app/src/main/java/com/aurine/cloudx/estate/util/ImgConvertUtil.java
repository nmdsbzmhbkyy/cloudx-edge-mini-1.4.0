package com.aurine.cloudx.estate.util;

import com.alibaba.nacos.client.identify.Base64;
import com.pig4cloud.pigx.common.minio.service.MinioTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
/**
 * <p>
 *
 * </p>
 *
 * @ClassName: ImgConvertUtil
 * @author: 王良俊 <>
 * @date: 2020年12月02日 下午04:08:57
 * @Copyright:
 */
@Slf4j
@Component
public class ImgConvertUtil {

    @Resource
    private MinioTemplate minioTemplate;
    @Value("${server.img-uri}")
    private String imgUri;


    private final String BUCKET_NAME = "saasv4-device";


    /**
     * 将base64图片转存到Minio
     *
     * @param imgBase64
     * @return
     * @throws IOException
     * @Auther: 王伟
     */
    public String base64ToMinio(String imgBase64) throws IOException {
        if (imgBase64 == null || imgBase64 == "") {
            return null;
        }
//        log.info("开始转存图片：{}", picBase64);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//        byte[] faceImage = arrayOutputStream.toByteArray();

        BASE64Decoder decoder = new BASE64Decoder();//base64转为二进制
        byte[] date_blob = decoder.decodeBuffer(imgBase64.substring(imgBase64.indexOf(",")+1));
        for (int i = 0; i < date_blob.length; ++i) {
            if (date_blob[i] < 0) {
                date_blob[i] += 256;
            }
        }

//        URLConnection connection = new URL(picBase64).openConnection();
//        int size = connection.getContentLength();
//        InputStream intStream = connection.getInputStream();
        InputStream intStream = new ByteArrayInputStream(date_blob);

//        new URL(url).

        String extName = this.getBase64FileExtension(imgBase64);

        String extValue = "";

        String fileNameDest = "event/" + uuid + "." + extName;
        String fileName = String.format("/admin/sys-file/%s/%s", BUCKET_NAME, fileNameDest);


        try {
            minioTemplate.putObject(BUCKET_NAME, fileNameDest, intStream, date_blob.length, MediaType.IMAGE_JPEG_VALUE);
            return fileName;
        } catch (ProtocolException e) {
            log.error("图片转存本地失败");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            intStream.close();
        }
        return "";
    }


    /**
     * 将图片地址，转换为base64
     * @param url
     * @return
     * @throws IOException
     */
    public String urlToBase64(String url) throws IOException{
        if (url == null || url == "") {
            return null;
        }
        URLConnection connection = new URL(imgUri + url).openConnection();
        int size = connection.getContentLength();
        InputStream in;
        try {
            in = connection.getInputStream();
        } catch (IOException e) {
            return null;
        }

        byte[] data = null;
        // 读取图片字节数组
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "data:image/png;base64," + new String(Base64.encodeBase64(data));
}


    /**
     * 获取base64扩展名
     *
     * @param imgBase64
     * @return
     */
    private String getBase64FileExtension(String imgBase64) {
//        data:image/png;base64,
        String ext = imgBase64.substring(imgBase64.indexOf("/") + 1, imgBase64.indexOf(";"));
        return ext;

    }


}
