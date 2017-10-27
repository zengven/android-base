package com.github.crash;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import com.github.utils.AppUtil;
import com.github.utils.DateUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * author: zengven
 * date: 2017/7/20 16:37
 * desc: UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    //crash log 保存路径
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "Logcat" + File.separator + "crash_log";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFEIX = ".log";

    private static CrashHandler sCrashHandler = new CrashHandler();
    private Context mContext;
    //系统默认的crash处理类
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sCrashHandler;
    }

    /**
     * 初始化crash handler
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this); //由本类接管crash
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //处理异常信息
        handleException(e);

        //如果系统提供了默认异常处理就交给系统进行处理，否则自己进行处理。
        if (mDefaultUncaughtExceptionHandler != null) {
            mDefaultUncaughtExceptionHandler.uncaughtException(t, e);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //收集设备参数信息
        try {
            writeToSDcard(ex);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //保存日志文件
        return true;
    }

    //将异常写入文件
    private void writeToSDcard(Throwable ex) throws IOException, PackageManager.NameNotFoundException {
        //如果没有SD卡，直接返回
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        File filedir = new File(PATH);
        if (!filedir.exists()) {
            filedir.mkdirs();
        }

        String time = DateUtil.timeFormat(DateUtil.TIME_PATTERN);

        File crashFile = new File(PATH + File.separator + FILE_NAME + time + FILE_NAME_SUFEIX);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(crashFile)));
        pw.println(time);
        //当前版本号
        pw.println("App Version:" + AppUtil.getChannelName(mContext));
        //当前系统
        pw.println("OS version:" + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT);
        //制造商
        pw.println("Vendor:" + Build.MANUFACTURER);
        //手机型号
        pw.println("Model:" + Build.MODEL);
        //CPU架构
        pw.println("CPU ABI:" + Build.CPU_ABI);

        ex.printStackTrace(pw);

        pw.close();
    }
}
