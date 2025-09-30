package com.dino.permissions;

import java.util.List;

public interface OnPermissionCallback {

    public final class CC {
        public static void $default$onDenied(OnPermissionCallback onPermissionCallback, List list, boolean z) {
        }
    }

    void onDenied(List<String> list, boolean z);

    void onGranted(List<String> list, boolean z);
}
