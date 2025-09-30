package com.dino.permissions;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import com.dino.permissions.Permission;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

final class PermissionUtils {
    PermissionUtils() {
    }

    static boolean isAndroid12() {
        return Build.VERSION.SDK_INT >= 31;
    }

    static boolean isAndroid11() {
        return Build.VERSION.SDK_INT >= 30;
    }

    static boolean isAndroid10() {
        return Build.VERSION.SDK_INT >= 29;
    }

    static boolean isAndroid9() {
        return Build.VERSION.SDK_INT >= 28;
    }

    static boolean isAndroid8() {
        return Build.VERSION.SDK_INT >= 26;
    }

    static boolean isAndroid7() {
        return Build.VERSION.SDK_INT >= 24;
    }

    static boolean isAndroid6() {
        return Build.VERSION.SDK_INT >= 23;
    }

    static List<String> getManifestPermissions(Context context) {
        try {
            return asArrayList(context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean isGrantedStoragePermission(Context context) {
        if (isAndroid11()) {
            return Environment.isExternalStorageManager();
        }
        return isGrantedPermissions(context, asArrayList(Permission.Group.STORAGE));
    }

    static boolean isGrantedInstallPermission(Context context) {
        if (isAndroid8()) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    static boolean isGrantedWindowPermission(Context context) {
        if (isAndroid6()) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }

    static boolean isGrantedNotifyPermission(Context context) {
        if (isAndroid7()) {
            return ((NotificationManager) context.getSystemService(NotificationManager.class)).areNotificationsEnabled();
        }
        if (isAndroid6()) {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                return ((Integer) appOpsManager.getClass().getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke(appOpsManager, Integer.valueOf(((Integer) appOpsManager.getClass().getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)).intValue()), Integer.valueOf(context.getApplicationInfo().uid), context.getPackageName())).intValue() == 0;
            } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | RuntimeException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    static boolean isGrantedSettingPermission(Context context) {
        if (isAndroid6()) {
            return Settings.System.canWrite(context);
        }
        return true;
    }

    static boolean containsSpecialPermission(List<String> list) {
        if (list != null && !list.isEmpty()) {
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                if (isSpecialPermission(it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean isSpecialPermission(String str) {
        return Permission.MANAGE_EXTERNAL_STORAGE.equals(str) || Permission.REQUEST_INSTALL_PACKAGES.equals(str) || Permission.SYSTEM_ALERT_WINDOW.equals(str) || Permission.NOTIFICATION_SERVICE.equals(str) || Permission.WRITE_SETTINGS.equals(str);
    }

    static boolean isGrantedPermissions(Context context, List<String> list) {
        if (!isAndroid6()) {
            return true;
        }
        if (list == null || list.isEmpty()) {
            return false;
        }
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (!isGrantedPermission(context, it.next())) {
                return false;
            }
        }
        return true;
    }

    static List<String> getDeniedPermissions(Context context, List<String> list) {
        ArrayList arrayList = new ArrayList(list.size());
        if (!isAndroid6()) {
            return arrayList;
        }
        for (String str : list) {
            if (!isGrantedPermission(context, str)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    static boolean isGrantedPermission(Context context, String str) {
        if (!isAndroid6()) {
            return true;
        }
        if (Permission.MANAGE_EXTERNAL_STORAGE.equals(str)) {
            return isGrantedStoragePermission(context);
        }
        if (Permission.REQUEST_INSTALL_PACKAGES.equals(str)) {
            return isGrantedInstallPermission(context);
        }
        if (Permission.SYSTEM_ALERT_WINDOW.equals(str)) {
            return isGrantedWindowPermission(context);
        }
        if (Permission.NOTIFICATION_SERVICE.equals(str)) {
            return isGrantedNotifyPermission(context);
        }
        if (Permission.WRITE_SETTINGS.equals(str)) {
            return isGrantedSettingPermission(context);
        }
        if (!isAndroid12()) {
            if (Permission.BLUETOOTH_SCAN.equals(str)) {
                return context.checkSelfPermission(Permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            }
            if (Permission.BLUETOOTH_CONNECT.equals(str) || Permission.BLUETOOTH_ADVERTISE.equals(str)) {
                return true;
            }
        }
        if (!isAndroid10()) {
            if (Permission.ACCESS_BACKGROUND_LOCATION.equals(str)) {
                return context.checkSelfPermission(Permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            }
            if (Permission.ACTIVITY_RECOGNITION.equals(str)) {
                return context.checkSelfPermission(Permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED;
            }
            if (Permission.ACCESS_MEDIA_LOCATION.equals(str)) {
                return true;
            }
        }
        if (!isAndroid9() && Permission.ACCEPT_HANDOVER.equals(str)) {
            return true;
        }
        if (!isAndroid8()) {
            if (Permission.ANSWER_PHONE_CALLS.equals(str)) {
                return true;
            }
            if (Permission.READ_PHONE_NUMBERS.equals(str)) {
                return context.checkSelfPermission(Permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
            }
        }
        return context.checkSelfPermission(str) == PackageManager.PERMISSION_GRANTED;
    }

    static int getPermissionStatus(Context context, String str) {
        return isGrantedPermission(context, str) ? 0 : -1;
    }

    static boolean isPermissionPermanentDenied(Activity activity, List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (isPermissionPermanentDenied(activity, it.next())) {
                return true;
            }
        }
        return false;
    }

    static boolean isPermissionPermanentDenied(Activity activity, String str) {
        if (!isAndroid6() || isSpecialPermission(str)) {
            return false;
        }
        if (!isAndroid12()) {
            if (Permission.BLUETOOTH_SCAN.equals(str)) {
                return (isGrantedPermission(activity, Permission.ACCESS_COARSE_LOCATION) || activity.shouldShowRequestPermissionRationale(Permission.ACCESS_COARSE_LOCATION)) ? false : true;
            }
            if (Permission.BLUETOOTH_CONNECT.equals(str) || Permission.BLUETOOTH_ADVERTISE.equals(str)) {
                return true;
            }
        }
        if (isAndroid10() && Permission.ACCESS_BACKGROUND_LOCATION.equals(str) && !isGrantedPermission(activity, Permission.ACCESS_BACKGROUND_LOCATION) && !isGrantedPermission(activity, Permission.ACCESS_FINE_LOCATION)) {
            return !activity.shouldShowRequestPermissionRationale(Permission.ACCESS_FINE_LOCATION);
        }
        if (!isAndroid10()) {
            if (Permission.ACCESS_BACKGROUND_LOCATION.equals(str)) {
                return (isGrantedPermission(activity, Permission.ACCESS_FINE_LOCATION) || activity.shouldShowRequestPermissionRationale(Permission.ACCESS_FINE_LOCATION)) ? false : true;
            }
            if (Permission.ACTIVITY_RECOGNITION.equals(str)) {
                return (isGrantedPermission(activity, Permission.BODY_SENSORS) || activity.shouldShowRequestPermissionRationale(Permission.BODY_SENSORS)) ? false : true;
            }
            if (Permission.ACCESS_MEDIA_LOCATION.equals(str)) {
                return false;
            }
        }
        if (!isAndroid9() && Permission.ACCEPT_HANDOVER.equals(str)) {
            return false;
        }
        if (!isAndroid8()) {
            if (Permission.ANSWER_PHONE_CALLS.equals(str)) {
                return true;
            }
            if (Permission.READ_PHONE_NUMBERS.equals(str)) {
                return (isGrantedPermission(activity, Permission.READ_PHONE_STATE) || activity.shouldShowRequestPermissionRationale(Permission.READ_PHONE_STATE)) ? false : true;
            }
        }
        return (isGrantedPermission(activity, str) || activity.shouldShowRequestPermissionRationale(str)) ? false : true;
    }

    static List<String> getDeniedPermissions(String[] strArr, int[] iArr) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < iArr.length; i++) {
            if (iArr[i] == -1) {
                arrayList.add(strArr[i]);
            }
        }
        return arrayList;
    }

    static List<String> getGrantedPermissions(String[] strArr, int[] iArr) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < iArr.length; i++) {
            if (iArr[i] == 0) {
                arrayList.add(strArr[i]);
            }
        }
        return arrayList;
    }

    static <T> ArrayList<T> asArrayList(T... tArr) {
        if (tArr == null || tArr.length == 0) {
            return null;
        }
        ArrayList<T> arrayList = new ArrayList<>(tArr.length);
        for (T t : tArr) {
            arrayList.add(t);
        }
        return arrayList;
    }

    @SafeVarargs
    static <T> ArrayList<T> asArrayLists(T[]... tArr) {
        ArrayList<T> arrayList = new ArrayList<>();
        if (tArr != null && tArr.length != 0) {
            for (T[] tArr2 : tArr) {
                arrayList.addAll(asArrayList(tArr2));
            }
        }
        return arrayList;
    }

    static int getRandomRequestCode() {
        return new Random().nextInt((int) Math.pow(2.0d, 8.0d));
    }

    static Activity findActivity(Context context) {
        while (!(context instanceof Activity)) {
            if (!(context instanceof ContextWrapper) || (context = ((ContextWrapper) context).getBaseContext()) == null) {
                return null;
            }
        }
        return (Activity) context;
    }

    static int findApkPathCookie(Context context) {
        AssetManager assets = context.getAssets();
        String str = context.getApplicationInfo().sourceDir;
        try {
            return ((Integer) assets.getClass().getDeclaredMethod("addOverlayPath", String.class).invoke(assets, str)).intValue();
        } catch (Exception e) {
            try {
                e.printStackTrace();
                String[] strArr = (String[]) assets.getClass().getDeclaredMethod("getApkPaths", new Class[0]).invoke(assets, new Object[0]);
                if (strArr == null) {
                    return 0;
                }
                for (int i = 0; i < strArr.length; i++) {
                    if (strArr[i].equals(str)) {
                        return i + 1;
                    }
                }
                return 0;
            } catch (Exception e2) {
                e2.printStackTrace();
                return 0;
            }
        }
    }

    static boolean isScopedStorage(Context context) {
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
            if (bundle == null || !bundle.containsKey("ScopedStorage")) {
                return false;
            }
            return Boolean.parseBoolean(String.valueOf(bundle.get("ScopedStorage")));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
