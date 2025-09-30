package com.dino.smart;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import com.google.gson.Gson;
import com.dino.smart.model.SmartInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tools {
    public static final String BasePath = Environment.getExternalStorageDirectory().getPath() + "/smartlamp/";
    public static final String filepath = "smartlamp";
    public static final int pageSize = 20;
    public static final int timeout = 5;

    public static float LowPassFilter0(float f, float f2, float f3) {
        return (f * f2) + ((1.0f - f2) * f3);
    }

    public static float changelength(int i, int i2) {
        float f = i2 / 1000.0f;
        if (i == 2) {
            f *= 0.621f;
        }
        return ((int) (f * 100.0f)) / 100.0f;
    }

    public static String timeFormat(Date date, String str) {
        return new SimpleDateFormat(str).format(date);
    }

    public static long timeForLong(String str) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
        }
        return date.getTime();
    }

    public static long zeroTimeForLong(Long l) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(l.longValue());
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(simpleDateFormat.format(date) + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static void showAlert3(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    public static Bitmap small(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getwindowwidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getwindowheight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static void saveDevId(Context context, String str) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartc", 0).edit();
        if (str == null) {
            editorEdit.remove("DevId");
        } else {
            editorEdit.putString("DevId", str);
        }
        editorEdit.commit();
    }

    public static String getDevId(Context context) {
        String string = context.getSharedPreferences("smartc", 0).getString("DevId", null);
        if (string != null) {
            return string;
        }
        Random random = new Random();
        int iNextInt = random.nextInt(254) + 1;
        int iNextInt2 = random.nextInt(254) + 1;
        int iNextInt3 = random.nextInt(254) + 1;
        String hexString = Integer.toHexString(iNextInt);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        String hexString2 = Integer.toHexString(iNextInt2);
        if (hexString2.length() == 1) {
            hexString2 = "0" + hexString2;
        }
        String hexString3 = Integer.toHexString(iNextInt3);
        if (hexString3.length() == 1) {
            hexString3 = "0" + hexString3;
        }
        String str = hexString + " " + hexString2 + " " + hexString3;
        saveDevId(context, str);
        return str;
    }

    public static void saveleftchange99(Context context, boolean z) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartc", 0).edit();
        editorEdit.putBoolean("leftchange99", z);
        editorEdit.commit();
    }

    public static boolean getleftchange99(Context context) {
        return context.getSharedPreferences("smartc", 0).getBoolean("leftchange99", false);
    }

    public static void saverightchange99(Context context, boolean z) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartc", 0).edit();
        editorEdit.putBoolean("rightchange99", z);
        editorEdit.commit();
    }

    public static boolean getrightchange99(Context context) {
        return context.getSharedPreferences("smartc", 0).getBoolean("rightchange99", false);
    }

    public static void saveDev98L1Turn(Context context, String str) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartc", 0).edit();
        editorEdit.putString("Dev98L1Turn", str);
        editorEdit.commit();
    }

    public static String getDev98L1Turn(Context context) {
        return context.getSharedPreferences("smartc", 0).getString("Dev98L1Turn", "10");
    }

    public static void saveDev98L2Turn(Context context, String str) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartc", 0).edit();
        editorEdit.putString("Dev98L2Turn", str);
        editorEdit.commit();
    }

    public static String getDev98L2Turn(Context context) {
        return context.getSharedPreferences("smartc", 0).getString("Dev98L2Turn", "10");
    }

    public static void saveDev98RTurn(Context context, String str) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartc", 0).edit();
        editorEdit.putString("Dev98RTurn", str);
        editorEdit.commit();
    }

    public static String getDev98RTurn(Context context) {
        return context.getSharedPreferences("smartc", 0).getString("Dev98RTurn", "10");
    }

    public static String getDevControl(String paramString, Context paramContext, int paramInt) {
        String str2 = paramContext.getSharedPreferences("smartc", 0).getString(paramString, null);
        String str1 = str2;
        if (str2 == null) {
            if (paramInt == 2) {
                if (paramString.equals("go"))
                    return "01,00,00,00,00000000";
                if (paramString.equals("back"))
                    return "10,00,00,00,00000000";
                if (paramString.equals("left"))
                    return "00,00,01,00,00000000";
                if (paramString.equals("right"))
                    return "00,00,10,00,00000000";
                if (paramString.equals("codeA2"))
                    return "01,00,00,00,00000000";
                if (paramString.equals("codeB2"))
                    return "00,01,00,00,00000000";
                if (paramString.equals("codeC2"))
                    return "00,00,01,00,00000000";
                if (paramString.equals("codeD2"))
                    return "00,00,00,01,00000000";
                if (paramString.equals("codeA1"))
                    return "10,00,00,00,00000000";
                if (paramString.equals("codeB1"))
                    return "00,10,00,00,00000000";
                if (paramString.equals("codeC1"))
                    return "00,00,10,00,00000000";
                if (paramString.equals("codeD1"))
                    return "00,00,00,10,00000000";
            } else if (paramInt == 0) {
                if (paramString.equals("go"))
                    return "01,00,00,00,00000000";
                if (paramString.equals("back"))
                    return "10,00,00,00,00000000";
                if (paramString.equals("left"))
                    return "00,00,00,01,00000000";
                if (paramString.equals("right"))
                    return "00,00,00,10,00000000";
                if (paramString.equals("codeA2"))
                    return "01,00,00,00,00000000";
                if (paramString.equals("codeB2"))
                    return "00,01,00,00,00000000";
                if (paramString.equals("codeC2"))
                    return "00,00,00,01,00000000";
                if (paramString.equals("codeD2"))
                    return "00,00,01,00,00000000";
                if (paramString.equals("codeA1"))
                    return "10,00,00,00,00000000";
                if (paramString.equals("codeB1"))
                    return "00,10,00,00,00000000";
                if (paramString.equals("codeC1"))
                    return "00,00,00,10,00000000";
                if (paramString.equals("codeD1"))
                    return "00,00,10,00,00000000";
            } else if (paramInt == 1) {
                if (paramString.equals("go"))
                    return "00,01,00,00,00000000";
                if (paramString.equals("back"))
                    return "00,10,00,00,00000000";
                if (paramString.equals("left"))
                    return "00,00,01,00,00000000";
                if (paramString.equals("right"))
                    return "00,00,10,00,00000000";
                if (paramString.equals("codeA2"))
                    return "00,01,00,00,00000000";
                if (paramString.equals("codeB2"))
                    return "01,00,00,00,00000000";
                if (paramString.equals("codeC2"))
                    return "00,00,01,00,00000000";
                if (paramString.equals("codeD2"))
                    return "00,00,00,01,00000000";
                if (paramString.equals("codeA1"))
                    return "00,10,00,00,00000000";
                if (paramString.equals("codeB1"))
                    return "10,00,00,00,00000000";
                if (paramString.equals("codeC1"))
                    return "00,00,10,00,00000000";
                if (paramString.equals("codeD1"))
                    return "00,00,00,10,00000000";
            }
            str1 = "00,00,00,00,00000000";
        }
        return str1;
    }

    public static void saveDevControl(String str, String str2, Context context) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartc", 0).edit();
        editorEdit.putString(str, str2);
        editorEdit.commit();
    }

    public static boolean getPyChange(Context context) {
        return context.getSharedPreferences("smartc", 0).getBoolean("PyChange", false);
    }

    public static void savePyChange(boolean z, Context context) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartc", 0).edit();
        editorEdit.putBoolean("PyChange", z);
        editorEdit.commit();
    }

    public static void saveBackKill(Context context, int i) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartc", 0).edit();
        editorEdit.putInt("BackKill", i);
        editorEdit.commit();
    }

    public static int getBackKill(Context context) {
        return context.getSharedPreferences("smartc", 0).getInt("BackKill", 0);
    }

    public static void saveleftpath99(Context context, int i) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartc", 0).edit();
        editorEdit.putInt("leftpath99", i);
        editorEdit.commit();
    }

    public static int getleftpath99(Context context) {
        return context.getSharedPreferences("smartc", 0).getInt("leftpath99", 10);
    }

    public static void saverightpath99(Context context, int i) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartc", 0).edit();
        editorEdit.putInt("rightpath99", i);
        editorEdit.commit();
    }

    public static int getrightpath99(Context context) {
        return context.getSharedPreferences("smartc", 0).getInt("rightpath99", 10);
    }

    public static void saveSmart(Context context, SmartInfo smartInfo, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("smartc", 0);
        String string = sharedPreferences.getString("snart" + str, null);
        JSONArray jSONArray = new JSONArray();
        Gson gson = new Gson();
        if (string != null) {
            try {
                jSONArray = new JSONArray(string);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                if (((SmartInfo) gson.fromJson(jSONArray.getJSONObject(i).toString(), SmartInfo.class)).getSid().equals(smartInfo.getSid())) {
                    jSONArray.remove(i);
                    break;
                }
                continue;
            } catch (JSONException e2) {
                e2.printStackTrace();
            }

        }
        String json = gson.toJson(smartInfo);
        JSONArray jSONArray2 = new JSONArray();
        try {
            jSONArray2.put(new JSONObject(json));
        } catch (JSONException e3) {
            e3.printStackTrace();
        }
        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
            try {
                jSONArray2.put(jSONArray.getJSONObject(i2));
            } catch (JSONException e4) {
                e4.printStackTrace();
            }
        }
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        editorEdit.putString("snart" + str, jSONArray2.toString());
        editorEdit.commit();
    }

    public static void removeSmart(Context context, SmartInfo smartInfo, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("smartc", 0);
        String string = sharedPreferences.getString("snart" + str, null);
        JSONArray jSONArray = new JSONArray();
        Gson gson = new Gson();
        if (string != null) {
            try {
                jSONArray = new JSONArray(string);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                if (((SmartInfo) gson.fromJson(jSONArray.getJSONObject(i).toString(), SmartInfo.class)).getSid().equals(smartInfo.getSid())) {
                    jSONArray.remove(i);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            continue;
        }
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        editorEdit.putString("snart" + str, jSONArray.toString());
        editorEdit.commit();
    }

    public static List<SmartInfo> getSmart(Context context, String str) {
        String string = context.getSharedPreferences("smartc", 0).getString("snart" + str, null);
        ArrayList arrayList = new ArrayList();
        if (string != null) {
            try {
                Gson gson = new Gson();
                JSONArray jSONArray = new JSONArray(string);
                for (int i = 0; i < jSONArray.length(); i++) {
                    arrayList.add((SmartInfo) gson.fromJson(jSONArray.getJSONObject(i).toString(), SmartInfo.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public static String getIdentity() {
        return UUID.randomUUID().toString();
    }

    public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap, int i) {
        try {
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            float f = i;
            canvas.drawRoundRect(rectF, f, f, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), rect, paint);
            return bitmapCreateBitmap;
        } catch (Exception unused) {
            return bitmap;
        }
    }

    public static String BinaryToHex(String str) {
        return str.equals("") ? "0" : Long.toHexString(Long.parseLong(str, 2));
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static Bitmap decodeUriAsBitmap(Uri uri, Context context) {
        try {
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> urlToMap(String str) {
        HashMap map = new HashMap();
        for (String str2 : str.split("&")) {
            String[] strArrSplit = str2.split("=");
            map.put(strArrSplit[0], strArrSplit[1]);
        }
        return map;
    }

    public static int getDpi(Activity activity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class.forName("android.view.Display").getMethod("getRealMetrics", DisplayMetrics.class).invoke(defaultDisplay, displayMetrics);
            return displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int[] getScreenWH(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return new int[]{windowManager.getDefaultDisplay().getWidth(), windowManager.getDefaultDisplay().getHeight()};
    }

    public static int getVrtualBtnHeight(Context context) {
        int ints = 0;
        try {
             ints = getDpi((Activity) context) - getScreenWH(context)[1];
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ints;
    }

    public static String logbyte(byte[] bArr) {
        String str = "";
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b);
            str = hexString.length() == 1 ? str + "0" + hexString + "," : str + hexString.replace("ffffff", "") + ",";
        }
        return str;
    }

    public static int BCD_CO(int i) {
        return ((i / 10) * 16) + (i % 10);
    }

    public static int BCD_TO_TEN(int i) {
        return ((i / 16) * 10) + (i % 16);
    }

    public static void saveUpTime(Context context, String str) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("smartam", 0).edit();
        editorEdit.putLong(str, System.currentTimeMillis());
        editorEdit.commit();
    }

    public static String getUpTime(Context context, String str) {
        long j = context.getSharedPreferences("smartam", 0).getLong(str, 0L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        if (j == 0) {
            return simpleDateFormat.format(new Date());
        }
        return simpleDateFormat.format(new Date(j));
    }

    public static float changeHeight(int i, int i2) {

        return i == 1 ? Math.round(((float) i2 * 0.0328f) * 10.0f) / 10.0f : (float) i2;
    }

    public static float changeWidth(int i, int i2) {
        int iRound;
        float f = i2;
        if (i == 2) {
            iRound = Math.round(f * 2.2046225f * 10.0f);
        } else {
            if (i != 3) {
                return f;
            }
            iRound = Math.round(f * 0.157473f * 10.0f);
        }
        return iRound / 10.0f;
    }

    private boolean ExistSDCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String creatFile(String str) {
        String str2 = BasePath + str;
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        return str2;
    }

    public static void writeToFile(String str, String str2) throws IOException {
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(file.length());
            randomAccessFile.write(str2.getBytes());
            randomAccessFile.close();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static List<String> getFileList(String str) {
        File[] fileArrListFiles;
        File file = new File(str);
        ArrayList arrayList = new ArrayList();
        if (!file.isDirectory() || (fileArrListFiles = file.listFiles()) == null) {
            return arrayList;
        }
        for (File file2 : fileArrListFiles) {
            String absolutePath = file2.getAbsolutePath();
            if (absolutePath.indexOf(".txt") != -1) {
                arrayList.add(absolutePath);
            }
        }
        return arrayList;
    }

    public static String readFile(String str) throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(str)), "UTF-8"));
            String str2 = "";
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    return str2;
                }
                str2 = str2 + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean objIsNullStr(Object obj) {
        return obj == null || obj.toString().isEmpty() || obj.toString().equals("");
    }
}
