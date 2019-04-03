//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.aip.face.stat;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import com.baidu.aip.face.stat.NetUtil.RequestAdapter;
import com.baidu.idl.facesdk.FaceInfo;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ast {
    private static final String AS_FILE_NAME = "ast";
    private static final long UPADTE_DEFUALT_DELAY_TIME = 15000L;
    private static final long SAVE_INTERVAL = 15000L;
    private static Ast instance;
    private Context context;
    private boolean isInit;
    private File asFile;
    private Properties properties;
    private Dev dev;
    private String faceHitKey = "";
    private static final String FACE_HIT_KEY_LASSTTIME = "FACE_HIT_KEY_LASSTTIME";
    private static final String UPLOAD_LASSTTIME = "UPLOAD_LASSTTIME";
    private long lastSavetime;
    private SparseArray<Integer> faceIds = new SparseArray();
    private String scene;
    ExecutorService es = Executors.newSingleThreadExecutor();
    Future future = null;

    private Ast() {
    }

    public static Ast getInstance() {
        if (instance == null) {
            Class var0 = Ast.class;
            synchronized(Ast.class) {
                instance = new Ast();
            }
        }

        return instance;
    }

    public boolean init(Context context, String sdkVersion, String scene) {
        if (this.isInit) {
            return true;
        } else {
            if (context != null) {
                this.context = context.getApplicationContext();
                this.dev = new Dev();
                this.dev.init(context);
                this.dev.setSdkVersion(sdkVersion);
                this.scene = scene;
                this.initFile();
            }

            return true;
        }
    }

    private String generateFaceHitKey(String indicator) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH");
        sb.append(simpleDateFormat.format(new Date())).append("_");
        sb.append(indicator);
        return sb.toString();
    }

    private boolean initFile() {
        this.asFile = new File(this.context.getFilesDir(), "ast");
        this.properties = new Properties();
        return !FileUtil.createFile(this.asFile) ? false : FileUtil.loadPropertiesFile(this.asFile, this.properties);
    }

    public void faceHit(String indicator, int daleyUpload, FaceInfo[] faceInfos) {
        int hitCount = 0;
        if (faceInfos == null) {
            this.faceIds.clear();
        } else {
            FaceInfo[] var5 = faceInfos;
            int var6 = faceInfos.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                FaceInfo faceInfo = var5[var7];
                Integer faceId = (Integer)this.faceIds.get(faceInfo.face_id);
                if (faceId == null) {
                    this.faceIds.put(faceInfo.face_id, faceInfo.face_id);
                    ++hitCount;
                }
            }

            if (hitCount != 0) {
                this.faceHit(indicator, (long)daleyUpload, hitCount);
            }
        }
    }

    public void faceHit() {
        this.faceHit("liveness", 15000L, 1);
    }

    public void faceHit(String indicator) {
        this.faceHit(indicator, 15000L, 1);
    }

    public void faceHit(String indicator, int count) {
        this.faceHit(indicator, 15000L, count);
    }

    public void faceHit(String indicator, long daleyUpload, int count) {
        String faceHitKey = this.generateFaceHitKey(indicator);
        String value = this.properties.getProperty(faceHitKey);
        if (TextUtils.isEmpty(value)) {
            this.properties.setProperty(faceHitKey, String.valueOf(count));
            this.properties.setProperty("FACE_HIT_KEY_LASSTTIME", String.valueOf(System.currentTimeMillis()));
        } else {
            int val = Integer.parseInt(value);
            this.properties.setProperty(faceHitKey, String.valueOf(val + count));
        }

        String lasttimeStr = this.properties.getProperty("FACE_HIT_KEY_LASSTTIME");
        long lastSaveTime = 0L;

        try {
            lastSaveTime = Long.parseLong(lasttimeStr);
        } catch (Exception var15) {
            var15.printStackTrace();
        }

        if (System.currentTimeMillis() - lastSaveTime > 15000L) {
            long starttime = System.currentTimeMillis();
            FileUtil.savePropertiesFile(this.asFile, this.properties);
            this.properties.setProperty("FACE_HIT_KEY_LASSTTIME", String.valueOf(System.currentTimeMillis()));
        }

        String lastUploadTimeStr = this.properties.getProperty("UPLOAD_LASSTTIME");
        long lastUploadtime = 0L;

        try {
            lastUploadtime = Long.parseLong(lastUploadTimeStr);
        } catch (Exception var14) {
            var14.printStackTrace();
        }

        if (this.dev.getFirstRun() || System.currentTimeMillis() - lastUploadtime >= daleyUpload) {
            this.sendData();
        }
    }

    public void immediatelyUpload() {
        this.sendData();
    }

    private void clear() {
        this.properties.clear();
    }

    private void sendData() {
        if (this.future == null || this.future.isDone()) {
            this.future = this.es.submit(new Runnable() {
                public void run() {
                    Ast.this.netRequest();
                }
            });
        }
    }

    private void netRequest() {
        if (this.properties.size() != 0) {
            NetUtil.uploadData(new RequestAdapter<Object>() {
                public String getURL() {
                    return "http://brain.baidu.com/record/api";
                }

                public String getRequestString() {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("mh", "faceSdkStatistic");
                        Properties asCopy = (Properties)Ast.this.properties.clone();
                        Iterator iter = asCopy.entrySet().iterator();
                        JSONArray dt = new JSONArray();

                        while(iter.hasNext()) {
                            Entry entry = (Entry)iter.next();
                            String key = (String)entry.getKey();
                            String value = (String)entry.getValue();
                            if (!key.equalsIgnoreCase("FACE_HIT_KEY_LASSTTIME") && !key.equalsIgnoreCase("UPLOAD_LASSTTIME")) {
                                JSONObject data = new JSONObject();
                                data.put("type", "facesdk");
                                data.put("scene", Ast.this.scene);
                                data.put("appid", Ast.this.dev.getPackagename());
                                data.put("device", Ast.this.dev.getBrand());
                                data.put("imei", Ast.this.dev.getUniqueID());
                                data.put("os", "Android");
                                data.put("system", Ast.this.dev.getSysVersion());
                                data.put("version", Ast.this.dev.getSdkVersion());
                                if (key.contains("liveness")) {
                                    data.put("isliving", "true");
                                } else {
                                    data.put("isliving", "false");
                                }

                                data.put("finish", "1");
                                String[] keySplit = key.split("_");
                                if (keySplit.length > 4) {
                                    data.put("year", keySplit[0]);
                                    data.put("month", keySplit[1]);
                                    data.put("day", keySplit[2]);
                                    data.put("hour", keySplit[3]);
                                }

                                data.put("num", value);
                                dt.put(data);
                            }
                        }

                        json.put("dt", dt);
                        return json.toString();
                    } catch (JSONException var10) {
                        var10.printStackTrace();
                        return "";
                    }
                }

                public void parseResponse(InputStream in) throws IOException, JSONException {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];

                    try {
                        int len1;
                        while((len1 = in.read(buffer)) > 0) {
                            out.write(buffer, 0, len1);
                        }

                        out.flush();
                        JSONObject json = new JSONObject(new String(out.toByteArray(), "UTF-8"));
                        int code = json.optInt("code");
                        if (code == 0) {
                            Ast.this.properties.clear();
                            Ast.this.dev.setFirstRun(false);
                            FileUtil.savePropertiesFile(Ast.this.asFile, Ast.this.properties);
                            Ast.this.properties.setProperty("UPLOAD_LASSTTIME", String.valueOf(System.currentTimeMillis()));
                        }
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException var12) {
                                var12.printStackTrace();
                            }
                        }

                    }

                }
            });
        }
    }
}
