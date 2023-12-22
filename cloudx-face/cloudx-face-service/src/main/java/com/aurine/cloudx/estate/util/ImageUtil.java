package com.aurine.cloudx.estate.util;

import com.aurine.cloudx.estate.config.ImageConfig;
import com.aurine.cloudx.estate.config.WaterMarkConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-01
 * @Copyright:
 */
@Component
public class ImageUtil {
    @Resource
    private ImageConfig imageConfig;


    /**
     * 给multipartFile加上文字水印
     *
     * @param multipartFile 需要上传的文件
     * @param text          需要添加的水印文字
     * @throws IOException
     */
    public MultipartFile addWorkMarkToMultipartFile(MultipartFile multipartFile, String text, String templateName) throws IOException {
        // 获取图片文件名
        String originFileName = multipartFile.getOriginalFilename();
        // 获取原图片后缀
        int lastSplit = originFileName.lastIndexOf(".");
        String suffix = originFileName.substring(lastSplit + 1);
        // 获取图片原始信息
        String dOriginFileName = multipartFile.getOriginalFilename();
        String dContentType = multipartFile.getContentType();
        // 是图片且不是gif才加水印
        if (!suffix.equalsIgnoreCase("gif")) {
            // 获取水印图片
            InputStream inputImg = multipartFile.getInputStream();
            Image img = ImageIO.read(inputImg);
            // 加图片水印
            int imgWidth = img.getWidth(null);
            int imgHeight = img.getHeight(null);

            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
//            //设置字体
//            Font font = new Font("宋体", Font.PLAIN, 45);
            //调用画文字水印的方法
            markWord(bufImg, img, text, imgWidth, imgHeight, templateName);

            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bufImg, suffix, imOut);
            InputStream is = new ByteArrayInputStream(bs.toByteArray());
            // 加水印后的文件上传
            multipartFile = new MockMultipartFile(dOriginFileName, dOriginFileName, dContentType, is);
        }
        //返回加了水印的上传对象
        return multipartFile;
    }


    public String addWorkMarkToBase64(String base64Str, String suffix, String text, String templateName) {

        BASE64Encoder base64en = new BASE64Encoder();
        BASE64Decoder base64de = new BASE64Decoder();

        BASE64Decoder decoder = new BASE64Decoder();//base64转为二进制
        BASE64Decoder encode = new BASE64Decoder();//base64转为二进制

        InputStream is = null;
        OutputStream os = null;
        byte[] b;
        try {
            //针对上送的图片是Base64场景
//            byte[] date_blob = base64de.decodeBuffer(base64Str);
            byte[] date_blob = decoder.decodeBuffer(base64Str.substring(base64Str.indexOf(",") + 1));
            for (int i = 0; i < date_blob.length; ++i) {
                if (date_blob[i] < 0) {
                    date_blob[i] += 256;
                }
            }

            is = new java.io.ByteArrayInputStream(date_blob);
//            BufferedImage srcImg = ImageIO.read(is);

            Image img = ImageIO.read(is);
            // 加图片水印
            int imgWidth = img.getWidth(null);
            int imgHeight = img.getHeight(null);

            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
//            //设置字体
//            Font font = new Font("宋体", Font.PLAIN, 45);
            //调用画文字水印的方法
            markWord(bufImg, img, text, imgWidth, imgHeight, templateName);

            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bufImg, suffix, imOut);

            return base64en.encode(bs.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return base64Str;
    }


    //加文字水印
    private void markWord(BufferedImage bufImg, Image img, String text, int width, int hight, String templateName) {

        //获取模板
        WaterMarkConfig waterMarkConfig = imageConfig.getByTemplateName(templateName);

        if (StringUtils.isEmpty(text)) {
            text = waterMarkConfig.getMark();
        }
        //取到画笔
        Graphics2D g = bufImg.createGraphics();
        //画底片
        g.drawImage(img, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
        g.setColor(new Color(waterMarkConfig.getRgb()[0], waterMarkConfig.getRgb()[1], waterMarkConfig.getRgb()[2]));

        //设置旋转
        Float degree = waterMarkConfig.getDegree();
        g.rotate(Math.toRadians(degree), width / 2, hight / 2);//设置水印旋转,旋转中心点为图片中心

        //透明度
        float alpha = waterMarkConfig.getAlpha();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));//设置水印文字透明度

        int fontSize = 0;
        if (waterMarkConfig.getType().equalsIgnoreCase("relative")) {
            //字体为相对大小
            fontSize = (width / waterMarkConfig.getSize()[0]) * waterMarkConfig.getSize()[1];
            g.setFont(new Font(waterMarkConfig.getFamily(), Font.BOLD, fontSize));
            g.drawString(text, ((width / 2) - (fontSize * text.length()) / 2), hight / 2);  //位置
        } else {
            //字体为绝对大小
            fontSize = waterMarkConfig.getSize()[0];
            g.setFont(new Font(waterMarkConfig.getFamily(), Font.BOLD, fontSize));
            g.drawString(text, ((width / 2) - (fontSize * text.length()) / 2), hight / 2);  //位置
        }

        g.dispose();
    }


    /**
     * 给multipartFile加上图片水印
     *
     * @param multipartFile 需要上传的文件
     * @param markImg       本地水印绝对路径
     * @throws IOException
     */
    public MultipartFile addPicMarkToMutipartFile(MultipartFile multipartFile,
                                                  String markImg) throws IOException {
        // 获取图片文件名
        String originFileName = multipartFile.getOriginalFilename();
        // 获取原图片后缀
        int lastSplit = originFileName.lastIndexOf(".");
        String suffix = originFileName.substring(lastSplit + 1);
        // 获取图片原始信息
        String dOriginFileName = multipartFile.getOriginalFilename();
        String dContentType = multipartFile.getContentType();
        // 是图片且不是gif才加水印
        if (!suffix.equalsIgnoreCase("gif")) {
            // 获取水印图片
            InputStream inputImg = multipartFile.getInputStream();
            Image img = ImageIO.read(inputImg);
            File file = new File(markImg);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            Image mark = ImageIO.read(bis);
            // 加图片水印
            int imgWidth = img.getWidth(null);
            int imgHeight = img.getHeight(null);

            int markWidth = mark.getWidth(null);
            int markHeight = mark.getHeight(null);

            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight,
                    BufferedImage.TYPE_INT_RGB);
            //水印的相对位置  ps：这里是右下角  水印宽为底片的四分之一
            markPic(bufImg, img, mark, imgWidth / 4, (imgWidth * markHeight) / (4 * markWidth),
                    imgWidth - imgWidth / 4, imgHeight - (imgWidth * markHeight) / (4 * markWidth));
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bufImg, suffix, imOut);
            InputStream is = new ByteArrayInputStream(bs.toByteArray());

            // 加水印后的文件上传
            multipartFile = new MockMultipartFile(dOriginFileName, dOriginFileName, dContentType,
                    is);
        }
        //返回加了水印的上传对象
        return multipartFile;
    }

    //加图片水印
    private void markPic(BufferedImage bufImg, Image img, Image markImg, int width, int height, int x, int y) {
        //取到画笔
        Graphics2D g = bufImg.createGraphics();
        //画底片
        g.drawImage(img, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
        //画水印位置
        g.drawImage(markImg, x, y, width, height, null);
        g.dispose();
    }


}
