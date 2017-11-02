package com.github.utils.photo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import com.github.utils.FileUtil;
import com.github.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * author: zengven
 * date: 2017/8/3 17:36
 * desc: 拍照相关工具类
 */
public class PhotoUtil {

    public static final int CODE_TAKE_PHOTO = 1; //相机RequestCode
    public static final int CODE_SELECT_IMAGE = 2; //相册RequestCode

    private static File sCurrentFile; //当前拍摄照片文件
    private static Uri sCurrentUri;

    /**
     * 拍照
     *
     * @param activity
     */
    public static void takePhoto(Activity activity) {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        sCurrentFile = FileUtil.createImgFile(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sCurrentUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", sCurrentFile);
            takeIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            sCurrentUri = Uri.fromFile(sCurrentFile);
        }
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, sCurrentUri);
        activity.startActivityForResult(takeIntent, CODE_TAKE_PHOTO);
    }

    /**
     * 打开系统相册
     *
     * @param activity
     */
    public static void startGallery(Activity activity) {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        albumIntent.setType("image/*");
        activity.startActivityForResult(albumIntent, CODE_SELECT_IMAGE);
    }

    /**
     * 跳转至系统相册预览页面
     *
     * @param context
     * @param path
     */
    public static void startPreview(Context context, String path) {
        Logger.e("path : " + path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(path));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.parse("file://" + path);
        }
        Logger.e(" uri: " + uri.toString());
        intent.setDataAndType(uri, "image/*");
        context.startActivity(intent);
    }

    /**
     * 刷新系统相册
     *
     * @param context
     */
    public static void refreshGallery(Context context) {
        if (sCurrentFile == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), sCurrentFile.getAbsolutePath(), sCurrentFile.getName(), null);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(sCurrentFile));
                context.sendBroadcast(intent);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    /**
     * 向图库中插入图片,不生成缩略图
     *
     * @param cr
     * @param imagePath
     * @param name
     * @param description
     * @return
     * @throws FileNotFoundException
     */
    private static String insertImage(ContentResolver cr, String imagePath,
                                      String name, String description) throws FileNotFoundException {
        // Check if file exists with a FileInputStream
        FileInputStream stream = new FileInputStream(imagePath);
        try {
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            String ret = insertImage(cr, bm, name, description);
            bm.recycle();
            return ret;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 向图库中插入图片,不生成缩略图(有些手机会生成一张灰色的缩略图)
     *
     * @param cr
     * @param source
     * @param title
     * @param description
     * @return
     */
    private static String insertImage(ContentResolver cr, Bitmap source, String title, String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */
        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }
        if (url != null) {
            stringUrl = url.toString();
        }
        return stringUrl;
    }


    /**
     * 根据uri获取path
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getLocalPathFromUri(Context context, Uri uri) {
        Logger.e("uri: " + uri.toString());
        String picPath = null;
        if (TextUtils.equals(uri.getScheme().toLowerCase(), "content")) { // MediaStore (and general)
            String[] projection = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor == null)
                return null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            picPath = cursor.getString(columnIndex);
            cursor.close();
        } else if (TextUtils.equals(uri.getScheme().toLowerCase(), "file")) { // File
            picPath = uri.getPath();
        }
        Logger.e("real path :" + picPath);
        return picPath;
    }

    /**
     * 根据uri获取bitmap
     *
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapFactory.decodeFile(uri.getPath());
        }
        return bitmap;
    }

    /**
     * 获取当前拍摄照片文件
     *
     * @return
     */
    public static File getCurrentFile() {
        return sCurrentFile;
    }

    /**
     * 获取当前拍摄照片uri
     *
     * @return
     */
    public static Uri getCurrentUri() {
        return sCurrentUri;
    }
}
