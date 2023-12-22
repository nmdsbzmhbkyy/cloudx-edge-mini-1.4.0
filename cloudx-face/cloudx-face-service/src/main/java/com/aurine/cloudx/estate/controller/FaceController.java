
package com.aurine.cloudx.estate.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.FaceConstant;
import com.aurine.cloudx.estate.util.DetectFaceResultCode;
import com.aurine.cloudx.estate.util.DetectResult;
import com.aurine.cloudx.estate.util.FaceDetect;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.minio.service.MinioTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/12/16
 */
@RestController
@RequestMapping("/face")
@Api(value = "face", tags = "人脸验证")
@Slf4j
public class FaceController {
    @Resource
    private MinioTemplate minioTemplate;


    @ApiOperation(value = "校验人脸并返回人脸图片数据,type: dst:识别图，src：原图，out：压缩图，也可以通过dst_src的方式，同时获取多个版本的图片地址")
    @PostMapping("/uploadAndCheck/{type}")
    public R indexPost(@RequestParam("file") MultipartFile upload, @PathVariable("type") String type) {

        String[] types = type.split(StrUtil.UNDERLINE);

        FaceDetect detector = new FaceDetect();
        byte[] jpgBuf = null;
        try {
            jpgBuf = streamToBytes(new ByteArrayInputStream(changeToJpg(upload.getInputStream())));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(DetectFaceResultCode.ERROR_JPG_READ.getCode(), DetectFaceResultCode.ERROR_JPG_READ.getMsg());
        }
        DetectResult result = detector.detectStream(jpgBuf);
        if (result.getResult() == 0) {

            try {
                Map map = redrawFaceRect(jpgBuf, result, types);
                return R.ok(map);
            } catch (IOException e) {
                e.printStackTrace();
                return R.failed(DetectFaceResultCode.ERROR_DETECT_UNKNOWN.getCode(), DetectFaceResultCode.ERROR_DETECT_UNKNOWN.getMsg());
            }
        }


        return R.failed(result.getCode().getCode(), result.getCode().getMsg());
    }

    @ApiOperation("校验人脸是否能够识别")
    @PostMapping("/checkFile")
    public R check(@RequestParam("file") MultipartFile upload) {

        FaceDetect detector = new FaceDetect();
        byte[] jpgBuf = null;
        try {
            jpgBuf = streamToBytes(new ByteArrayInputStream(changeToJpg(upload.getInputStream())));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(DetectFaceResultCode.ERROR_JPG_READ.getMsg());
        }
        DetectResult result = detector.detectStream(jpgBuf);
        if (result.getResult() != 0) {
            return R.failed(result.getCode().getMsg());
        }
        return R.ok();
    }

    @ApiOperation(value = "上传图片")
    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile upload) {
        String fileName = IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(upload.getOriginalFilename());
        Map<String, String> resultMap = new HashMap<>(4);
        resultMap.put("bucketName", FaceConstant.BUCKET_NAME);
        resultMap.put("fileName", fileName);

        resultMap.put("url", String.format("/admin/sys-file/%s/%s", FaceConstant.BUCKET_NAME, fileName));


        try {
            minioTemplate.putObject(FaceConstant.BUCKET_NAME, fileName, upload.getInputStream(), upload.getSize(), upload.getContentType());
        } catch (Exception e) {
            log.error("上传失败", e);
            return R.failed(e.getLocalizedMessage());
        }
        return R.ok(resultMap);
    }


    /**
     * 图片转换为jpg格式
     *
     * @param inputStream
     */
    private byte[] changeToJpg(InputStream inputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        // create a blank, RGB, same width and height, and a white
        // background
        int height = bufferedImage.getHeight();
        int weight = bufferedImage.getWidth();
        // 奇数高度或宽度则减1像素单位
        if (height % 2 != 0) {
            height = height - 1;
        }
        if (weight % 2 != 0) {
            weight = weight - 1;
        }
        BufferedImage newBufferedImage = new BufferedImage(
                weight, height,
                BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                Color.WHITE, null);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(newBufferedImage, "jpg", output);

        return output.toByteArray();
    }


    /**
     * 根据识别结果，在jpg原图上绘制人脸框
     *
     * @param jpgBuf jpg原图二进制数据
     * @param result 识别结果
     * @return 返回绘制完成后的jpg图片url（默认指向static目录下）
     */
    private Map redrawFaceRect(byte[] jpgBuf, DetectResult result, String[] types) throws IOException {


        int x = result.getX(), y = result.getY(), w = result.getW(), h = result.getH();
        int widthPen = 2;
        int lenLine = (int) ((w > h ? h : w) / 10);

        Map map = new HashMap();


        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new ByteArrayInputStream(jpgBuf));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String uuid = IdUtil.simpleUUID();
        for (String type : types) {

            if (FaceConstant.DST.equals(type)) {


                Graphics2D g2d = (Graphics2D) bimg.getGraphics();


                g2d.setColor(Color.gray);
                g2d.setStroke(new BasicStroke(widthPen));
                g2d.drawRoundRect(x, y, w, h, 5, 5);

                widthPen = 8;
                g2d.setColor(Color.blue);
                g2d.setStroke(new BasicStroke(widthPen));

                // 左上角 横线
                g2d.drawLine(x, y, x + lenLine, y);
                // 竖线
                g2d.drawLine(x, y, x, y + lenLine);

                // 右上角  横线
                g2d.drawLine(x + w - lenLine, y, x + w, y);
                // 竖线
                g2d.drawLine(x + w, y, x + w, y + lenLine);

                // 左下角 横线
                g2d.drawLine(x, y + h, x + lenLine, y + h);
                // 竖线
                g2d.drawLine(x, y + h - lenLine, x, y + h);

                // 右下角 横线
                g2d.drawLine(x + w - lenLine, y + h, x + w, y + h);
                // 竖线
                g2d.drawLine(x + w, y + h - lenLine, x + w, y + h);
                // 存储绘制人脸框后的jpg图
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(arrayOutputStream);
                ImageIO.write(bimg, "jpg", arrayOutputStream);
//                encoder.encode(bimg);

                byte[] faceImage = arrayOutputStream.toByteArray();
                String fileNameDest = uuid + StrUtil.UNDERLINE + FaceConstant.DST + ".jpg";
                String fileName = String.format("/admin/sys-file/%s/%s", FaceConstant.BUCKET_NAME, fileNameDest);
                try {
                    minioTemplate.putObject(FaceConstant.BUCKET_NAME, fileNameDest, new ByteArrayInputStream(faceImage), faceImage.length, MediaType.IMAGE_JPEG_VALUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                map.put(FaceConstant.DST, fileName);

            }

            if (FaceConstant.OUT.equals(type)) {

                try { // 存储输出的jpg图，日期-out.jpg，向设备通知注册人脸时使用该jpg图片
                    BufferedImage buffImgTemp = null;
                    try {
                        buffImgTemp = ImageIO.read(new ByteArrayInputStream(result.getJpgBuf()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

//                    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(arrayOutputStream);
                    ImageIO.write(buffImgTemp, "jpg", arrayOutputStream);
//                    encoder.encode(buffImgTemp);
                    byte[] faceImage = arrayOutputStream.toByteArray();

                    String fileNameDest = uuid + StrUtil.UNDERLINE + FaceConstant.OUT + ".jpg";
                    String fileName = String.format("/admin/sys-file/%s/%s", FaceConstant.BUCKET_NAME, fileNameDest);
                    try {
                        minioTemplate.putObject(FaceConstant.BUCKET_NAME, fileNameDest, new ByteArrayInputStream(faceImage), faceImage.length, MediaType.IMAGE_JPEG_VALUE);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    map.put(FaceConstant.OUT, fileName);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (FaceConstant.SRC.equals(type)) {

                // 存储原图jpg 格式

                String fileNameDest = uuid + StrUtil.UNDERLINE + FaceConstant.SRC + ".jpg";
                String fileName = String.format("/admin/sys-file/%s/%s", FaceConstant.BUCKET_NAME, fileNameDest);
                try {
                    minioTemplate.putObject(FaceConstant.BUCKET_NAME, fileNameDest, new ByteArrayInputStream(jpgBuf), jpgBuf.length, MediaType.IMAGE_JPEG_VALUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                map.put(FaceConstant.SRC, fileName);


            }

        }

        return map;
    }


    private byte[] streamToBytes(InputStream input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (true) {
            try {
                if (-1 == (n = input.read(buffer))) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }


}
