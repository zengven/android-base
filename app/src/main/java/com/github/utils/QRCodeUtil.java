package com.github.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * author: zengven
 * date: 2017/9/25 16:59
 * desc: 二维码工具类
 */
public class QRCodeUtil {

    private static final int sQRDefaultWidth = 400; //px
    private static final int sQRDefaultHeight = 400; //px

    private QRCodeUtil() {
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param size    尺寸,不传默认400*400像素,只传一位默认为宽高,两位及以上只解析前两位,index=0位宽,index=1位高  单位:px
     * @return
     */
    public static Bitmap createQRCode(String content, int... size) {
        int width, height;
        if (size.length == 0) {
            width = sQRDefaultWidth;
            height = sQRDefaultHeight;
        } else if (size.length == 1) {
            width = size[0];
            height = size[0];
        } else {
            width = size[0];
            height = size[1];
        }
        Map<EncodeHintType, Object> hints = new HashMap<>(); //配置参数
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); //设置编码格式
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); //设置容错级别
        hints.put(EncodeHintType.MARGIN, 2); //设置边距默认为4
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (bitMatrix.get(j, i)) {
                        pixels[i * width + j] = 0xff000000; //确定像素点黑色
                    } else {
                        pixels[i * width + j] = 0xffffffff; //确定像素点白色
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
