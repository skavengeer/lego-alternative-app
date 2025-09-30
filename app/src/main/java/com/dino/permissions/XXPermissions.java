package com.dino.permissions;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import com.dino.permissions.IPermissionInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class XXPermissions {
    public static final int REQUEST_CODE = 1025;
    private static Boolean sDebugMode;
    private static IPermissionInterceptor sInterceptor;
    private final Context mContext;
    private IPermissionInterceptor mInterceptor;
    private List<String> mPermissions;

    public static XXPermissions with(Context context) {
        return new XXPermissions(context);
    }

    public static XXPermissions with(Fragment fragment) {
        return with(fragment.getActivity());
    }

    public static XXPermissions with(androidx.fragment.app.Fragment fragment) {
        return with(fragment.getActivity());
    }

    public static void setDebugMode(boolean z) {
        sDebugMode = Boolean.valueOf(z);
    }

    private static boolean isDebugMode(Context context) {
        if (sDebugMode == null) {
            sDebugMode = Boolean.valueOf((context.getApplicationInfo().flags & 2) != 0);
        }
        return sDebugMode.booleanValue();
    }

    public static void setInterceptor(IPermissionInterceptor iPermissionInterceptor) {
        sInterceptor = iPermissionInterceptor;
    }

    public static IPermissionInterceptor getInterceptor() {
        if (sInterceptor == null) {
            sInterceptor = new IPermissionInterceptor() {
                @Override
                public void deniedPermissions(Activity activity, OnPermissionCallback onPermissionCallback, List<String> list, boolean z) {
                    CC.$default$deniedPermissions(this, activity, onPermissionCallback, list, z);
                }

                @Override
                public void grantedPermissions(Activity activity, OnPermissionCallback onPermissionCallback, List<String> list, boolean z) {
                    CC.$default$grantedPermissions(this, activity, onPermissionCallback, list, z);
                }

                @Override
                public void requestPermissions(Activity activity, OnPermissionCallback onPermissionCallback, List<String> list) {
                    PermissionFragment.beginRequest(activity, new ArrayList(list), this, onPermissionCallback);
                }
            };
        }
        return sInterceptor;
    }

    private XXPermissions(Context context) {
        this.mContext = context;
    }

    public XXPermissions interceptor(IPermissionInterceptor iPermissionInterceptor) {
        this.mInterceptor = iPermissionInterceptor;
        return this;
    }

    public XXPermissions permission(String... strArr) {
        return permission(PermissionUtils.asArrayList(strArr));
    }

    public XXPermissions permission(String[]... strArr) {
        return permission(PermissionUtils.asArrayLists(strArr));
    }

    public XXPermissions permission(List<String> list) {
        if (list != null && !list.isEmpty()) {
            if (this.mPermissions == null) {
                this.mPermissions = new ArrayList(list);
                return this;
            }
            for (String str : list) {
                if (!this.mPermissions.contains(str)) {
                    this.mPermissions.add(str);
                }
            }
        }
        return this;
    }

    public void request(OnPermissionCallback onPermissionCallback) {
        if (this.mContext == null) {
            return;
        }
        if (this.mInterceptor == null) {
            this.mInterceptor = getInterceptor();
        }
        ArrayList arrayList = new ArrayList(this.mPermissions);
        boolean zIsDebugMode = isDebugMode(this.mContext);
        Activity activityFindActivity = PermissionUtils.findActivity(this.mContext);
        if (PermissionChecker.checkActivityStatus(activityFindActivity, zIsDebugMode) && PermissionChecker.checkPermissionArgument(arrayList, zIsDebugMode)) {
            if (zIsDebugMode) {
                try {
                    PermissionChecker.checkStoragePermission(this.mContext, arrayList);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                PermissionChecker.checkLocationPermission(this.mContext, arrayList);
                PermissionChecker.checkTargetSdkVersion(this.mContext, arrayList);
            }
            PermissionChecker.optimizeDeprecatedPermission(arrayList);
            if (zIsDebugMode) {
                PermissionChecker.checkPermissionManifest(this.mContext, arrayList);
            }
            if (!PermissionUtils.isGrantedPermissions(this.mContext, arrayList)) {
                this.mInterceptor.requestPermissions(activityFindActivity, onPermissionCallback, arrayList);
            } else if (onPermissionCallback != null) {
                this.mInterceptor.grantedPermissions(activityFindActivity, onPermissionCallback, arrayList, true);
            }
        }
    }

    public static boolean isGranted(Context context, String... strArr) {
        return isGranted(context, PermissionUtils.asArrayList(strArr));
    }

    public static boolean isGranted(Context context, String[]... strArr) {
        return isGranted(context, PermissionUtils.asArrayLists(strArr));
    }

    public static boolean isGranted(Context context, List<String> list) {
        return PermissionUtils.isGrantedPermissions(context, list);
    }

    public static List<String> getDenied(Context context, String... strArr) {
        return getDenied(context, PermissionUtils.asArrayList(strArr));
    }

    public static List<String> getDenied(Context context, String[]... strArr) {
        return getDenied(context, PermissionUtils.asArrayLists(strArr));
    }

    public static List<String> getDenied(Context context, List<String> list) {
        return PermissionUtils.getDeniedPermissions(context, list);
    }

    public static boolean isSpecial(String str) {
        return PermissionUtils.isSpecialPermission(str);
    }

    public static boolean isPermanentDenied(Activity activity, String... strArr) {
        return isPermanentDenied(activity, PermissionUtils.asArrayList(strArr));
    }

    public static boolean isPermanentDenied(Activity activity, String[]... strArr) {
        return isPermanentDenied(activity, PermissionUtils.asArrayLists(strArr));
    }

    public static boolean isPermanentDenied(Activity activity, List<String> list) {
        return PermissionUtils.isPermissionPermanentDenied(activity, list);
    }

    public static void startPermissionActivity(Context context) {
        startPermissionActivity(context, (List<String>) null);
    }

    public static void startPermissionActivity(Context context, String... strArr) {
        startPermissionActivity(context, PermissionUtils.asArrayList(strArr));
    }

    public static void startPermissionActivity(Context context, String[]... strArr) {
        startPermissionActivity(context, PermissionUtils.asArrayLists(strArr));
    }

    public static void startPermissionActivity(Context context, List<String> list) {
        Activity activityFindActivity = PermissionUtils.findActivity(context);
        if (activityFindActivity != null) {
            startPermissionActivity(activityFindActivity, list);
            return;
        }
        Intent smartPermissionIntent = PermissionSettingPage.getSmartPermissionIntent(context, list);
        if (!(context instanceof Activity)) {
            smartPermissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(smartPermissionIntent);
    }

    public static void startPermissionActivity(Activity activity) {
        startPermissionActivity(activity, (List<String>) null);
    }

    public static void startPermissionActivity(Activity activity, String... strArr) {
        startPermissionActivity(activity, (List<String>) PermissionUtils.asArrayList(strArr));
    }

    public static void startPermissionActivity(Activity activity, String[]... strArr) {
        startPermissionActivity(activity, (List<String>) PermissionUtils.asArrayLists(strArr));
    }

    public static void startPermissionActivity(Activity activity, List<String> list) {
        startPermissionActivity(activity, list, 1025);
    }

    public static void startPermissionActivity(Activity activity, List<String> list, int i) {
        activity.startActivityForResult(PermissionSettingPage.getSmartPermissionIntent(activity, list), i);
    }

    public static void startPermissionActivity(Fragment fragment) {
        startPermissionActivity(fragment, (List<String>) null);
    }

    public static void startPermissionActivity(Fragment fragment, String... strArr) {
        startPermissionActivity(fragment, PermissionUtils.asArrayList(strArr));
    }

    public static void startPermissionActivity(Fragment fragment, String[]... strArr) {
        startPermissionActivity(fragment, PermissionUtils.asArrayLists(strArr));
    }

    public static void startPermissionActivity(Fragment fragment, List<String> list) {
        startPermissionActivity(fragment, list, 1025);
    }

    public static void startPermissionActivity(Fragment fragment, List<String> list, int i) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            return;
        }
        fragment.startActivityForResult(PermissionSettingPage.getSmartPermissionIntent(activity, list), i);
    }

    public static void startPermissionActivity(androidx.fragment.app.Fragment fragment) {
        startPermissionActivity(fragment, (List<String>) null);
    }

    public static void startPermissionActivity(androidx.fragment.app.Fragment fragment, String... strArr) {
        startPermissionActivity(fragment, PermissionUtils.asArrayList(strArr));
    }

    public static void startPermissionActivity(androidx.fragment.app.Fragment fragment, String[]... strArr) {
        startPermissionActivity(fragment, PermissionUtils.asArrayLists(strArr));
    }

    public static void startPermissionActivity(androidx.fragment.app.Fragment fragment, List<String> list) {
        startPermissionActivity(fragment, list, 1025);
    }

    public static void startPermissionActivity(androidx.fragment.app.Fragment fragment, List<String> list, int i) {
        FragmentActivity activity = fragment.getActivity();
        if (activity == null) {
            return;
        }
        fragment.startActivityForResult(PermissionSettingPage.getSmartPermissionIntent(activity, list), i);
    }
}
