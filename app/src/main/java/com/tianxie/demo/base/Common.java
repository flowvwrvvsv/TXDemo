package com.tianxie.demo.base;

import android.os.Environment;

import java.io.File;

public class Common
{
    public static final String RETURN_CODE = "0";
    public static final String SUFFIX_DUMP = ".txt";

    public static final boolean DEBUG = true;

    // Path—31307845605426b7a7b1706d7c866b10c
    public static final String FOLDER_FLAG = File.separator;
    public static final int FOLDER_FLAG_LENGTH = FOLDER_FLAG.length();
    public static final String UPDATE_FILE = "WHAPP_UPDATE";
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
    public static final int SDCARD_PATH_LENGTH = Common.SDCARD_PATH.length();
    public static final String SYSTEM_WHROOT_PATH = "TXDEMO_ROOT" + FOLDER_FLAG;
    public static final String FILE_ROOT = SDCARD_PATH + FOLDER_FLAG + SYSTEM_WHROOT_PATH;
    public static final String FILE_ROOT_CRASH = SDCARD_PATH + FOLDER_FLAG + SYSTEM_WHROOT_PATH + "crash" + FOLDER_FLAG;
    public static final String FILE_ROOT_FACE = SDCARD_PATH + FOLDER_FLAG + SYSTEM_WHROOT_PATH + "face" + FOLDER_FLAG;
    public static final String FILE_ROOT_FACE_TEMP = FILE_ROOT_FACE + "temp"+FOLDER_FLAG;
    public static final String FILE_ROOT_WEIXIN = SDCARD_PATH + FOLDER_FLAG + SYSTEM_WHROOT_PATH + "weixin" + FOLDER_FLAG;
    public static final String REMOTE_SDCARD_PATH = ":SDCARD:";
    public static final int FILE_ROOT_LENGTH = FILE_ROOT.length();

    public static final String ERROR_PATH = "Error/";
    public static final String HEAP_DUMP_PATH = "HeapDump/";
    public static final String SUFFIX_HEAP_DUMP = ".hprof";
    public static final String CRASH_DUMP_PATH = "CrashDump/";

    public static final String SP_USER_KEY = "user_key";

    public static final String PIC_URL = "http://211.149.182.113:8085/";
    public static final String BASE_URL = "http://211.149.182.113:8090/";

    public static final String BASE_BAIDU_KEY = "BASE_BAIDU_KEY";
    public static final String BASE_BAIDU_URL = "https://aip.baidubce.com/";

    public static final String BAIDU_API_KEY = "B2XgSP5bu9GgknimUR6o2BQa";
    public static final String BAIDU_SECRET_KEY = "9nEpWvAd8xYZ2k92sdIDkWML8LbiLKsX";

}
