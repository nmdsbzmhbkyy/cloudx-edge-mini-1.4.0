package com.aurine.cloudx.edge.sync.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import sun.misc.BASE64Encoder;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.net.ssl.SSLContext;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: wrm
 * @Date: 2022/03/02 15:07
 * @Package: com.aurine.cloudx.edge.sync.biz.util
 * @Version: 1.0
 * @Remarks:
 **/
@Slf4j
public class ImageBase64ConvertUtils {

    //第一种：常规的方法转化图片稳定，一般不会出现变色等问题，但是此方法不支持https链接
    private static byte[] read2Array(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        try (
                InputStream inStream = conn.getInputStream();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream()
        ) {
            byte[] buffer = new byte[2 * 1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            return outStream.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException("图片转化失败");
        }
    }

    //第二种：网上比较常见的是这一种，但其实与第一种差异不大，只是重画了图片
    private static byte[] url2Array2(String url) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIcon bufImg = new ImageIcon(new URL(url));
            BufferedImage biOut = new BufferedImage(bufImg.getIconWidth(), bufImg.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = (Graphics2D) biOut.getGraphics();
            graphics.drawImage(bufImg.getImage(), 0, 0, null);
            graphics.dispose();
            ImageIO.write(biOut, "jpeg", baos);
            return baos.toByteArray();
        }catch (Exception  e){
            log.error("图片转化失败："+e.getMessage(),e);
            throw new IOException(e.getMessage());
        }
    }
    private static BufferedImage read(URL url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL is NULL");
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(true);
        connection.setReadTimeout(5000);
        InputStream inStream;
        try {
            inStream = connection.getInputStream();
        } catch (IOException e) {
            throw new IIOException("Can't get input stream from URL!", e);
        }
        ImageInputStream stream = ImageIO.createImageInputStream(inStream);
        BufferedImage bi;
        try {
            bi = ImageIO.read(stream);
            if (bi == null) {
                stream.close();
            }
        } finally {
            inStream.close();
            connection.disconnect();
        }
        return bi;
    }

    //第三种：特点是支持https，当然其实http也可以用
    private static byte[] url2Array3(String url) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            HttpGet httpGet=new HttpGet(url);
            CloseableHttpClient httpClient = HttpsClient.getInstance();
            CloseableHttpResponse resp = httpClient.execute(httpGet);
            resp.getEntity().writeTo(baos);
            return baos.toByteArray();
        }catch (Exception  e){
            log.error("图片转化失败："+e.getMessage(),e);
            throw new IOException(e.getMessage());
        }
    }
    //支持https,此方法可以绕过https验证
    private static class HttpsClient {
        private static CloseableHttpClient getInstance() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            return HttpClients.custom().setSSLSocketFactory(factory).build();
        }
    }


    //暴露给外部的静态方法，不带前缀
    public static String urlToBase64(String url) throws Exception {
        byte[] bytes;
        try {
            if(url.startsWith("https")) {
                bytes = url2Array3(url);
            }else {
                bytes = read2Array(new URL(url));
            }
        } catch (Exception e) {
            log.info("默认图片转化方法异常,尝试备份方法");
            bytes=url2Array2(url);
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encodeBuffer(bytes).trim().replaceAll("\n", "").replaceAll("\r", "");
    }


    //暴露给外部的静态方法，带前缀
    public static String urlToFullBase64(String url) throws Exception {
        return "data:image/jpeg;base64," + urlToBase64(url);
    }

}
