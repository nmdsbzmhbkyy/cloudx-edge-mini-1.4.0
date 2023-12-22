package com.aurine.cloudx.estate.util;

import cn.hutool.core.collection.CollUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

/**
 * Http连接工具
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-23
 * @Copyright:
 */
@Component
public class HttpUtil {
    @Resource(name = "proxyRestTemplate")
    private RestTemplate restTemplate;

    public static String postFile(String url, MultipartFile file, String fileParamName, Map<String, String> headerParams, Map<String, String> otherParams) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            String fileName = file.getOriginalFilename();
            HttpPost httpPost = new HttpPost(url);
            //添加header
            if (CollUtil.isNotEmpty(headerParams)) {
                for (Map.Entry<String, String> e : headerParams.entrySet()) {
                    httpPost.addHeader(e.getKey(), e.getValue());
                }
            }


            EntityBuilder bu = EntityBuilder.create();
            URL u = new URL("https://www.nationalgeographic.com/content/dam/travel/2017-digital/destination-hubs/01_switzerland.ngsversion.1538530101718.adapt.1900.1.jpg");
            bu.setFile(new File(u.toURI()));

            HttpEntity entity = bu.build();

            httpPost.setEntity(entity);

            System.out.println(file.getInputStream());

            HttpResponse response = httpClient.execute(httpPost);// 执行提交
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public ResponseEntity<String> postFile(String url, MultipartFile file){

        //添加对接参数


        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", DongdongConfig.CONTENT_TYPE);
//        headers.set("User-Agent:", DongdongConfig.USER_AGENT);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        headers.add("Authorization:", "Bearer dcbd6d55-4202-41cc-ad21-940cd2593a95");
        headers.setBearerAuth("9a696208-dcea-4438-b712-38205d31e8a1");

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            // 这里对文件进行临时存储，直接用MultipartFile的
            File localDir = new File("tmpImage/");
            localDir.mkdirs();
            File localFile = new File("tmpImage/" + file.getOriginalFilename());
            Files.copy(inputStream, localFile.toPath());
            List<Object> fileList = new ArrayList<>();
            fileList.add(localFile);
            ResourceLoader resourceLoader = new FileSystemResourceLoader();
            paramMap.add("file", resourceLoader.getResource(localFile.getPath()));
            org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(paramMap, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
            if (localFile.exists()) {
                localFile.delete();
            }
            return responseEntity;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;


    }


//
//    public static void main(String[] args) {
//
//
//        String url = "https://tse1-mm.cn.bing.net/th/id/OIP.R-AnTO3Alut4z5bMkYvJjwHaEK?pid=Api&rs=1";
//        MultipartFile file = MultipartFileUtil.createFile(url, UUID.randomUUID().toString().replaceAll("-", ""));
//
//        Map<String, String> headMap = new HashMap<>();
//
//        System.out.println(HttpUtil.postFile("https://icloud.aurine.cn/admin/sys-file/upload", file, "", null, null));
//    }
}
