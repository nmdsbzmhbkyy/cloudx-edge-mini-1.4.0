package com.aurine.cloudx.open.common.core.util;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.common.minio.service.MinioTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

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
    @Resource
    private HttpUtil httpUtil;

    @Value("${server.base-uri}")
    private String imgUri;

    private final String BUCKET_NAME = "saasv4-device";

    /**
     * <p>
     * 把图片从第三方服务器下载到冠林云文件服务器并返回新的url
     * </p>
     *
     * @param thirdpartyImgUrl 要进行转储的第三方图片url地址
     */
    public String convertToLocalUrl(String thirdpartyImgUrl) {

        if (StringUtils.isEmpty(thirdpartyImgUrl)) {
            return thirdpartyImgUrl;
        }
//        if (thirdpartyImgUrl.indexOf("http://139.9.139.196:7000/aid/") >= 0) {
//            thirdpartyImgUrl = thirdpartyImgUrl.replace("http://139.9.139.196:7000/aid/", "https://139.9.139.196:7003/aid/");
//        }

//        return thirdpartyImgUrl;
        /**
         * 由于中台返回请求为异步处理，设备返回图片可能并未上传完成，导致图片地址无法获取数据问题
         * 故暂停图片转存服务，使用原图片地址
         */
//        if (StrUtil.isBlank(thirdpartyImgUrl)) {
//            return "";
//        }
//
//
        /**
         * 判断文件类型，并转存到minio
         * @Auther:王伟
         * @since 2021-01-07 18:07
         */
        String url = thirdpartyImgUrl;

        //检查是否为图片
        String extName = this.getFileExtension(url);

        try {
            url = saveToMinio(thirdpartyImgUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return url;

    }


    /**
     * 将URL地址图片转存到Minio
     *
     * @param url
     * @return
     * @throws IOException
     * @Auther: 王伟
     */
    private String saveToMinio(String url) throws IOException {
        log.info("开始转存图片：{}", url);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//        byte[] faceImage = arrayOutputStream.toByteArray();


        URLConnection connection = new URL(url).openConnection();
        int size = connection.getContentLength();
        InputStream intStream = connection.getInputStream();

//        new URL(url).

        String extName = this.getFileExtension2(url);

        String extValue = "";

        String fileNameDest = "event/" + uuid + "." + extName;
        String fileName = String.format("/admin/sys-file/%s/%s", BUCKET_NAME, fileNameDest);

        System.out.println(size);

        try {
            minioTemplate.putObject(BUCKET_NAME, fileNameDest, intStream, size, MediaType.IMAGE_JPEG_VALUE);
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
     * 获取扩展名
     * 去除？后参数
     *
     * @param path
     * @return
     */
    private String getFileExtension2(String path) {

        String extension = "";
        String[] pathContents = path.split("[\\\\/]");
        if (pathContents != null) {
            int pathContentsLength = pathContents.length;

            String lastPart = pathContents[pathContentsLength - 1];
            String[] lastPartContents = lastPart.split("\\.");
            if (lastPartContents != null && lastPartContents.length > 1) {
                int lastPartContentLength = lastPartContents.length;

                String name = "";
                for (int i = 0; i < lastPartContentLength; i++) {

                    if (i < (lastPartContents.length - 1)) {
                        name += lastPartContents[i];
                        if (i < (lastPartContentLength - 2)) {
                            name += ".";
                        }
                    }
                }
                extension = lastPartContents[lastPartContentLength - 1];

            }
        }
        int i = extension.indexOf("?");
        String sub = extension;
        if (i != -1){
             sub = StrUtil.sub(extension, 0, i);
        }

        return sub;
    }

    /**
     * 获取扩展名
     *
     * @param path
     * @return
     */
    private String getFileExtension(String path) {

        String extension = "";
        String[] pathContents = path.split("[\\\\/]");
        if (pathContents != null) {
            int pathContentsLength = pathContents.length;

            String lastPart = pathContents[pathContentsLength - 1];
            String[] lastPartContents = lastPart.split("\\.");
            if (lastPartContents != null && lastPartContents.length > 1) {
                int lastPartContentLength = lastPartContents.length;

                String name = "";
                for (int i = 0; i < lastPartContentLength; i++) {

                    if (i < (lastPartContents.length - 1)) {
                        name += lastPartContents[i];
                        if (i < (lastPartContentLength - 2)) {
                            name += ".";
                        }
                    }
                }
                extension = lastPartContents[lastPartContentLength - 1];

            }
        }
        return extension;
    }

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
        byte[] date_blob = decoder.decodeBuffer(imgBase64.substring(imgBase64.indexOf(",") + 1));
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
            System.err.println("intStream = " + intStream);
            System.err.println("fileNameDest = " + fileNameDest);
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
