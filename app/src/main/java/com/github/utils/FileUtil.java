package com.github.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * author: zengven
 * date: 2017/8/3 14:57
 * desc: 文件工具类
 */
public class FileUtil {


    /**
     * create image file
     *
     * @param context
     * @return
     */
    public static File createImgFile(Context context) {
        String state = Environment.getExternalStorageState();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//            File camera = new File(pic, "camera");
            if (!pic.exists()) {
                if (!pic.mkdirs()) {
                    return null;
                }
            }
            return new File(pic, File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            File cacheDir = context.getCacheDir();
            return new File(cacheDir, File.separator + "IMG_" + timeStamp + ".jpg");
        }
    }

    public static void saveFile(String content, String fileName) {
        String state = Environment.getExternalStorageState();
        if (TextUtils.equals(state, Environment.MEDIA_MOUNTED)) {
            File rootFile = Environment.getExternalStorageDirectory();
            File dirFile = new File(rootFile, "huijiayou" + File.separator + "ticket" + File.separator + "file");
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File file = new File(dirFile, fileName);
            if (file.exists()) {
                file.delete();
            }
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file);
                fileWriter.write(content);
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {

        }
    }
}
