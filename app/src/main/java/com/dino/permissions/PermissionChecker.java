package com.dino.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Build;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

final class PermissionChecker {

    PermissionChecker() {
    }

    static boolean checkActivityStatus(Activity activity, boolean z) {
        if (activity == null) {
            if (z) {
                throw new IllegalArgumentException("The instance of the context must be an activity object");
            }
            return false;
        }
        if (activity.isFinishing()) {
            if (z) {
                throw new IllegalStateException("The activity has been finishing, please manually determine the status of the activity");
            }
            return false;
        }
        if (Build.VERSION.SDK_INT < 17 || !activity.isDestroyed()) {
            return true;
        }
        if (z) {
            throw new IllegalStateException("The activity has been destroyed, please manually determine the status of the activity");
        }
        return false;
    }

    static boolean checkPermissionArgument(List<String> list, boolean z) {
        if (list == null || list.isEmpty()) {
            if (z) {
                throw new IllegalArgumentException("The requested permission cannot be empty");
            }
            return false;
        }
        if (z) {
            ArrayList arrayList = new ArrayList();
            Field[] declaredFields = Permission.class.getDeclaredFields();
            if (declaredFields.length == 0) {
                return true;
            }
            for (Field field : declaredFields) {
                if (String.class.equals(field.getType())) {
                    try {
                        arrayList.add((String) field.get(null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (String str : list) {
                if (!arrayList.contains(str)) {
                    throw new IllegalArgumentException("The " + str + " is not a dangerous permission or special permission");
                }
            }
        }
        return true;
    }

    static void checkStoragePermission(Context context, List<String> list) throws IOException {
        if (list.contains(Permission.MANAGE_EXTERNAL_STORAGE) || list.contains(Permission.READ_EXTERNAL_STORAGE) || list.contains(Permission.WRITE_EXTERNAL_STORAGE)) {
            boolean zIsScopedStorage = PermissionUtils.isScopedStorage(context);
            int iFindApkPathCookie = PermissionUtils.findApkPathCookie(context);
            if (iFindApkPathCookie == 0) {
                return;
            }
            try {
                XmlResourceParser xmlResourceParserOpenXmlResourceParser = context.getAssets().openXmlResourceParser(iFindApkPathCookie, "AndroidManifest.xml");
                while (true) {
                    if (xmlResourceParserOpenXmlResourceParser.getEventType() == 1) {
                        break;
                    }
                    if (xmlResourceParserOpenXmlResourceParser.getEventType() == 2 && "application".equals(xmlResourceParserOpenXmlResourceParser.getName())) {
                        int i = context.getApplicationInfo().targetSdkVersion;
                        boolean attributeBooleanValue = xmlResourceParserOpenXmlResourceParser.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "requestLegacyExternalStorage", false);
                        if (i >= 29 && !attributeBooleanValue && (list.contains(Permission.MANAGE_EXTERNAL_STORAGE) || !zIsScopedStorage)) {
                            throw new IllegalStateException("Please register the android:requestLegacyExternalStorage=\"true\" attribute in the manifest file");
                        }
                        if (i >= 30 && !list.contains(Permission.MANAGE_EXTERNAL_STORAGE) && !zIsScopedStorage) {
                            throw new IllegalArgumentException("Please adapt the scoped storage, or use the MANAGE_EXTERNAL_STORAGE permission");
                        }
                    } else {
                        xmlResourceParserOpenXmlResourceParser.next();
                    }
                }
                xmlResourceParserOpenXmlResourceParser.close();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    static void checkLocationPermission(Context context, List<String> list) {
        if (context.getApplicationInfo().targetSdkVersion >= 31 && list.contains(Permission.ACCESS_FINE_LOCATION) && !list.contains(Permission.ACCESS_COARSE_LOCATION)) {
            throw new IllegalArgumentException("If your app targets Android 12 or higher and requests the ACCESS_FINE_LOCATION runtime permission, you must also request the ACCESS_COARSE_LOCATION permission. You must include both permissions in a single runtime request.");
        }
        if (list.contains(Permission.ACCESS_BACKGROUND_LOCATION)) {
            if (list.contains(Permission.ACCESS_COARSE_LOCATION) && !list.contains(Permission.ACCESS_FINE_LOCATION)) {
                throw new IllegalArgumentException("The application for background location permissions must include precise location permissions");
            }
            for (String str : list) {
                if (!Permission.ACCESS_FINE_LOCATION.equals(str) && !Permission.ACCESS_COARSE_LOCATION.equals(str) && !Permission.ACCESS_BACKGROUND_LOCATION.equals(str)) {
                    throw new IllegalArgumentException("Because it includes background location permissions, do not apply for permissions unrelated to location");
                }
            }
        }
    }

    static void checkTargetSdkVersion(Context context, List<String> list) {
        int i;
        if (list.contains(Permission.BLUETOOTH_SCAN) || list.contains(Permission.BLUETOOTH_CONNECT) || list.contains(Permission.BLUETOOTH_ADVERTISE)) {
            i = 31;
        } else if (list.contains(Permission.MANAGE_EXTERNAL_STORAGE)) {
            i = 30;
        } else if (list.contains(Permission.ACCEPT_HANDOVER)) {
            i = 28;
        } else if (list.contains(Permission.ACCESS_BACKGROUND_LOCATION) || list.contains(Permission.ACTIVITY_RECOGNITION) || list.contains(Permission.ACCESS_MEDIA_LOCATION)) {
            i = 29;
        } else {
            i = (list.contains(Permission.REQUEST_INSTALL_PACKAGES) || list.contains(Permission.ANSWER_PHONE_CALLS) || list.contains(Permission.READ_PHONE_NUMBERS)) ? 26 : 23;
        }
        if (context.getApplicationInfo().targetSdkVersion >= i) {
            return;
        }
        throw new RuntimeException("The targetSdkVersion SDK must be " + i + " or more");
    }

    static void optimizeDeprecatedPermission(List<String> list) {
        if (!PermissionUtils.isAndroid12() && list.contains(Permission.BLUETOOTH_SCAN) && !list.contains(Permission.ACCESS_COARSE_LOCATION)) {
            list.add(Permission.ACCESS_COARSE_LOCATION);
        }
        if (!PermissionUtils.isAndroid12() && list.contains(Permission.BLUETOOTH_SCAN)) {
            list.add(Permission.ACCESS_COARSE_LOCATION);
        }
        if (list.contains(Permission.MANAGE_EXTERNAL_STORAGE)) {
            if (list.contains(Permission.READ_EXTERNAL_STORAGE) || list.contains(Permission.WRITE_EXTERNAL_STORAGE)) {
                throw new IllegalArgumentException("If you have applied for MANAGE_EXTERNAL_STORAGE permissions, do not apply for the READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE permissions");
            }
            if (!PermissionUtils.isAndroid11()) {
                list.add(Permission.READ_EXTERNAL_STORAGE);
                list.add(Permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        if (!PermissionUtils.isAndroid8() && list.contains(Permission.READ_PHONE_NUMBERS) && !list.contains(Permission.READ_PHONE_STATE)) {
            list.add(Permission.READ_PHONE_STATE);
        }
        if (PermissionUtils.isAndroid10() || !list.contains(Permission.ACTIVITY_RECOGNITION) || list.contains(Permission.BODY_SENSORS)) {
            return;
        }
        list.add(Permission.BODY_SENSORS);
    }

    static void checkPermissionManifest(Context context, List<String> list) {
        List<String> manifestPermissions = PermissionUtils.getManifestPermissions(context);
        if (manifestPermissions == null || manifestPermissions.isEmpty()) {
            throw new ManifestRegisterException();
        }
        int i = Build.VERSION.SDK_INT >= 24 ? context.getApplicationInfo().minSdkVersion : 23;
        for (String str : list) {
            if (i < 31) {
                if (Permission.BLUETOOTH_SCAN.equals(str)) {
                    if (!manifestPermissions.contains("android.permission.BLUETOOTH_ADMIN")) {
                        throw new ManifestRegisterException("android.permission.BLUETOOTH_ADMIN");
                    }
                    if (!manifestPermissions.contains(Permission.ACCESS_COARSE_LOCATION)) {
                        throw new ManifestRegisterException(Permission.ACCESS_COARSE_LOCATION);
                    }
                }
                if (Permission.BLUETOOTH_CONNECT.equals(str) && !manifestPermissions.contains("android.permission.BLUETOOTH")) {
                    throw new ManifestRegisterException("android.permission.BLUETOOTH");
                }
                if (Permission.BLUETOOTH_ADVERTISE.equals(str) && !manifestPermissions.contains("android.permission.BLUETOOTH_ADMIN")) {
                    throw new ManifestRegisterException("android.permission.BLUETOOTH_ADMIN");
                }
            }
            if (i < 30 && Permission.MANAGE_EXTERNAL_STORAGE.equals(str)) {
                if (!manifestPermissions.contains(Permission.READ_EXTERNAL_STORAGE)) {
                    throw new ManifestRegisterException(Permission.READ_EXTERNAL_STORAGE);
                }
                if (!manifestPermissions.contains(Permission.WRITE_EXTERNAL_STORAGE)) {
                    throw new ManifestRegisterException(Permission.WRITE_EXTERNAL_STORAGE);
                }
            }
            if (i < 29 && Permission.ACTIVITY_RECOGNITION.equals(str) && !manifestPermissions.contains(Permission.BODY_SENSORS)) {
                throw new ManifestRegisterException(Permission.BODY_SENSORS);
            }
            if (i < 26 && Permission.READ_PHONE_NUMBERS.equals(str) && !manifestPermissions.contains(Permission.READ_PHONE_STATE)) {
                throw new ManifestRegisterException(Permission.READ_PHONE_STATE);
            }
            if (!Permission.NOTIFICATION_SERVICE.equals(str) && !manifestPermissions.contains(str)) {
                throw new ManifestRegisterException(str);
            }
        }
    }
}
