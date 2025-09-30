package com.dino.permissions;

import android.app.Activity;
import java.util.List;

public interface IPermissionInterceptor {
    void deniedPermissions(Activity activity, OnPermissionCallback onPermissionCallback, List<String> list, boolean z);

    void grantedPermissions(Activity activity, OnPermissionCallback onPermissionCallback, List<String> list, boolean z);

    void requestPermissions(Activity activity, OnPermissionCallback onPermissionCallback, List<String> list);

    public final class CC {
        public static void $default$grantedPermissions(IPermissionInterceptor _this, Activity activity, OnPermissionCallback onPermissionCallback, List list, boolean z) {
            if (onPermissionCallback == null) {
                return;
            }
            onPermissionCallback.onGranted(list, z);
        }

        public static void $default$deniedPermissions(IPermissionInterceptor _this, Activity activity, OnPermissionCallback onPermissionCallback, List list, boolean z) {
            if (onPermissionCallback == null) {
                return;
            }
            onPermissionCallback.onDenied(list, z);
        }
    }
}
