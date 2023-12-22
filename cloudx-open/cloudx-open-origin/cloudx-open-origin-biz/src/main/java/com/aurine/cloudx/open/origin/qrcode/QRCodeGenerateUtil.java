package com.aurine.cloudx.open.origin.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 原文：<a>https://www.cnblogs.com/oneBreeze1855/p/8996442.html</a>
 * </p>
 * 二维码生成工具
 *
 * @author 王良俊
 */

public class QRCodeGenerateUtil {

    private volatile static QRCodeGenerateUtil instance = null;

    public static QRCodeGenerateUtil getInstance() {
        if (instance == null) {
            synchronized (QRCodeGenerateUtil.class) {
                if (instance == null) {
                    instance = new QRCodeGenerateUtil();
                }
            }
        }
        return instance;
    }

    private QRCodeGenerateUtil() {
    }

    /**
     * <p>
     * 生成可下载的zip文件
     * </p>
     *
     * @param contentMap key时二维码的内容 value是二维码图片名
     * @param height     二维码图片高度
     * @param width      二维码图片宽度
     */
    public void genQRCodeZip(HttpServletResponse response, Map<String, String> contentMap, Integer width, Integer height, MultipartFile logoFile, int rgb) {
        File zipDir = new File("QrCodeImages/zip");
        File imgDir = new File("QrCodeImages/img");
        checkDir(zipDir);
        checkDir(imgDir);

        String uuidName = UUID.randomUUID().toString().replaceAll("-", "");
        File imagesDir = new File(imgDir.getPath() + "/" + uuidName);
        checkDir(imagesDir);
        File zipFile = new File(zipDir.getPath() + "/" + uuidName + ".zip");
        contentMap.forEach((pointId, pointName) -> {
            try {
                if (logoFile != null && logoFile.getSize() != 0) {
                    this.genQRCode(logoFile, new File(imagesDir.getPath() + "/" + pointName + ".png"), pointId, width, height, rgb);
                } else {
                    this.genQRCode(new File(imagesDir.getPath() + "/" + pointName + ".png"), pointId, width, height, rgb);
                }
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                throw new RuntimeException("二维码生成失败");
            }
        });
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            putZipFiles(imagesDir.listFiles(File::isFile), zos);
            Files.copy(zipFile.toPath(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 这里删除临时存放的图片和zip文件
            if (imagesDir.exists()) {
                deleteDir(imagesDir);
            }
            if (zipFile.exists()) {
                zipFile.delete();
            }
        }
    }

    /**
     * <p>
     * 生成二维码图片
     * </p>
     *
     * @param qrcCodeImg 二维码图片文件对象
     * @param content    二维码要存储的内容
     * @param width      二维码宽度
     * @param height     二维码高度
     */
    public void genQRCode(File qrcCodeImg, String content, int width, int height, int rgb) throws WriterException, IOException {
        String info = qrcCodeImg.getName().split("\\.")[0];
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 配置纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        // 根据高度和宽度生成像素矩阵
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(rgb, Color.white.getRGB());
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
        // 这里获取二维码图片的graphics2D对象
        Graphics2D graphics2D = bufferedImage.createGraphics();
        // 二维码右下角的坐标
        int[] bottomRightOnBit = bitMatrix.getBottomRightOnBit();

        // 底部内边距
        int marginToBottom = height - bottomRightOnBit[1];


        double rows = (int) Math.sqrt((info.length() * marginToBottom) / width);
        int fontSize;
        while (true) {
            // 可以正常显示的字符数
            int validChar = (int) (width * rows * (int) rows / marginToBottom);
            if (validChar >= info.length()) {
                fontSize = (int) (marginToBottom * 0.9 / rows);
                break;
            }
            rows += 0.1;
        }

        if (rgb != Color.black.getRGB()) {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        Font font = new Font("宋体", Font.PLAIN, fontSize);
        graphics2D.setFont(font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics(font);

        // 这里设置字体颜色为黑色
        graphics2D.setColor(Color.BLACK);

        // 每行能放下的字符数
        int chars = width / fontSize;
        // 总行数
        int totalRows = (int) Math.ceil(Math.ceil(info.length() * 1.0 / chars));

        if (totalRows == 1) {
            // 不需要换行的情况
            int offsetY = (marginToBottom + fontSize) / 2;
            int textWidth = fontMetrics.stringWidth(info);
            graphics2D.drawString(info, (width - textWidth) / 2, bottomRightOnBit[1] + offsetY);
        } else {
            // 需要换行的情况
            int offsetY = (marginToBottom - fontSize * totalRows) / 2;
            for (int row = 1; row <= totalRows; row++) {
                int endIndex;
                if (row == totalRows) {
                    endIndex = info.length();
                } else {
                    endIndex = chars * row;
                }
                String substring = info.substring(chars * (row - 1), endIndex);
                int currentTextWidth = fontMetrics.stringWidth(substring);
                graphics2D.drawString(substring, (width - currentTextWidth) / 2, offsetY + bottomRightOnBit[1] + fontSize * row);
            }
        }

        graphics2D.dispose();
        ImageIO.write(bufferedImage, "PNG", qrcCodeImg);
    }

    /**
     * <p>
     * 生成二维码图片(带logo的)
     * </p>
     *
     * @param qrcCodeImg 二维码图片文件对象
     * @param content    二维码要存储的内容
     * @param width      二维码宽度
     * @param height     二维码高度
     */
    public void genQRCode(MultipartFile logoFile, File qrcCodeImg, String content, int width, int height, int rgb) throws WriterException, IOException {
        String info = qrcCodeImg.getName().split("\\.")[0];
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 配置纠错等级 这里因为有图片遮挡所以就设置的高点
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        // 根据高度和宽度生成像素矩阵
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(rgb, Color.white.getRGB());
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
        // 这里获取二维码图片的graphics2D对象
        Graphics2D graphics2D = bufferedImage.createGraphics();
        // 二维码右下角的坐标
        int[] bottomRightOnBit = bitMatrix.getBottomRightOnBit();

        // 底部内边距
        int marginToBottom = height - bottomRightOnBit[1];

        // int totalChar = (int)(width * row * row / height);
        // 这里获取到能正常显示的字符数
        // double validChar = (totalChar / row) * (int) row;

        double rows = (int) Math.sqrt((info.length() * marginToBottom) / width);
        int fontSize;

        while (true) {
            // 可以正常显示的字符数
            int validChar = (int) (width * rows * (int) rows / marginToBottom);
            if (validChar >= info.length()) {
                fontSize = (int) (marginToBottom * 0.9 / rows);
                break;
            }
            rows += 0.1;
        }

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        

        Font font = new Font("宋体", Font.PLAIN, fontSize);
        graphics2D.setFont(font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics(font);

        // 这里设置字体颜色为黑色
        graphics2D.setColor(Color.BLACK);

        // 每行能放下的字符数
        int chars = width / fontSize;
        // 总行数
        int totalRows = (int) Math.ceil(info.length() * 1.0 / chars);

        if (totalRows == 1) {
            int offsetY = (marginToBottom + fontSize) / 2;
            // 不需要换行的情况
            // 这里获取文本的宽度
            int textWidth = fontMetrics.stringWidth(info);
            graphics2D.drawString(info, (width - textWidth) / 2, bottomRightOnBit[1] + offsetY);
        } else {
            int offsetY = (marginToBottom - fontSize * totalRows) / 2;
            // 需要换行的情况
            for (int row = 1; row <= totalRows; row++) {
                int endIndex;
                if (row == totalRows) {
                    endIndex = info.length();
                } else {
                    endIndex = chars * row;
                }
                String substring = info.substring(chars * (row - 1), endIndex);
                int currentTextWidth = fontMetrics.stringWidth(substring);
                graphics2D.drawString(substring, (width - currentTextWidth) / 2, offsetY + bottomRightOnBit[1] + fontSize * row);
            }
        }

        // 这里算出二维码的高度
        int[] topLeftOnBit = bitMatrix.getTopLeftOnBit();
        int qrCodeHeight = bottomRightOnBit[1] - topLeftOnBit[1];

        BufferedImage logo = ImageIO.read(logoFile.getInputStream());
        int logoWidth = (int) Math.sqrt(qrCodeHeight * qrCodeHeight * 0.1);
        int logoHeight = logoWidth;
        int logoX = (width - logoWidth) / 2;
        int logoY = logoX;

        graphics2D.drawImage(logo.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH), logoX, logoY, null);
        // 这里设置logo边框宽度为图片宽度的6%
        int borderWidth = (int) (logoWidth * 0.06);
        BasicStroke stroke = new BasicStroke(borderWidth);
        graphics2D.setStroke(stroke);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawRoundRect(logoX, logoY, logoWidth, logoHeight, 10, 10);
        graphics2D.dispose();
        ImageIO.write(bufferedImage, "PNG", qrcCodeImg);

    }

    /**
     * 将待压缩的文件 加入压缩文件中
     */
    public void putZipFiles(File[] imageFiles, ZipOutputStream zos) throws IOException {
        for (File file : imageFiles) {
            if (file != null && file.exists()) {
                //加入压缩文件中
                zos.putNextEntry(new ZipEntry(file.getName()));
                Files.copy(file.toPath(), zos);
                zos.closeEntry();
            }
        }
        zos.flush();
        zos.close();
    }

    private static void checkDir(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 递归删除文件夹
     *
     * @param dir 文件夹
     */
    public static void deleteDir(File dir) {
        File[] files = dir.listFiles();
        if (!dir.isFile() && files != null) {
            for (File file : files) {
                deleteDir(new File(file.getAbsolutePath()));
            }
        }
        dir.delete();
    }

}