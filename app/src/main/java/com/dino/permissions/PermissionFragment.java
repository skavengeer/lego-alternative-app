package com.dino.permissions;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.ArraySet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PermissionFragment extends Fragment implements Runnable {

    private static final String REQUEST_CODE = "request_code";
    private static final ArraySet<Integer> REQUEST_CODE_ARRAY = new ArraySet<>();
    private static final String REQUEST_PERMISSIONS = "request_permissions";
    private OnPermissionCallback mCallBack;
    private boolean mDangerousRequest;
    private IPermissionInterceptor mInterceptor;
    private boolean mRequestFlag;
    private int mScreenOrientation;
    private boolean mSpecialRequest;

    public static void beginRequest(Activity activity, ArrayList<String> arrayList, IPermissionInterceptor iPermissionInterceptor, OnPermissionCallback onPermissionCallback) {
        int randomRequestCode;
        PermissionFragment permissionFragment = new PermissionFragment();
        Bundle bundle = new Bundle();
        do {
            randomRequestCode = PermissionUtils.getRandomRequestCode();
        } while (REQUEST_CODE_ARRAY.contains(Integer.valueOf(randomRequestCode)));
        REQUEST_CODE_ARRAY.add(Integer.valueOf(randomRequestCode));
        bundle.putInt(REQUEST_CODE, randomRequestCode);
        bundle.putStringArrayList(REQUEST_PERMISSIONS, arrayList);
        permissionFragment.setArguments(bundle);
        permissionFragment.setRetainInstance(true);
        permissionFragment.setRequestFlag(true);
        permissionFragment.setCallBack(onPermissionCallback);
        permissionFragment.setInterceptor(iPermissionInterceptor);
        permissionFragment.attachActivity(activity);
    }

    public void attachActivity(Activity activity) {
        activity.getFragmentManager().beginTransaction().add(this, toString()).commitAllowingStateLoss();
    }

    public void detachActivity(Activity activity) {
        activity.getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }

    public void setCallBack(OnPermissionCallback onPermissionCallback) {
        this.mCallBack = onPermissionCallback;
    }

    public void setRequestFlag(boolean z) {
        this.mRequestFlag = z;
    }

    public void setInterceptor(IPermissionInterceptor iPermissionInterceptor) {
        this.mInterceptor = iPermissionInterceptor;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        int requestedOrientation = activity.getRequestedOrientation();
        this.mScreenOrientation = requestedOrientation;
        if (requestedOrientation != -1) {
            return;
        }
        int i = activity.getResources().getConfiguration().orientation;
        try {
            if (i == 2) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else if (i != 1) {
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Activity activity = getActivity();
        if (activity == null || this.mScreenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mCallBack = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!this.mRequestFlag) {
            detachActivity(getActivity());
        } else {
            if (this.mSpecialRequest) {
                return;
            }
            this.mSpecialRequest = true;
            requestSpecialPermission();
        }
    }

    public void requestSpecialPermission() {
        Bundle arguments = getArguments();
        Activity activity = getActivity();
        if (arguments == null || activity == null) {
            return;
        }
        ArrayList<String> stringArrayList = arguments.getStringArrayList(REQUEST_PERMISSIONS);
        boolean z = false;
        if (PermissionUtils.containsSpecialPermission(stringArrayList)) {
            if (stringArrayList.contains(Permission.MANAGE_EXTERNAL_STORAGE) && !PermissionUtils.isGrantedStoragePermission(activity) && PermissionUtils.isAndroid11()) {
                startActivityForResult(PermissionSettingPage.getStoragePermissionIntent(activity), getArguments().getInt(REQUEST_CODE));
                z = true;
            }
            if (stringArrayList.contains(Permission.REQUEST_INSTALL_PACKAGES) && !PermissionUtils.isGrantedInstallPermission(activity)) {
                startActivityForResult(PermissionSettingPage.getInstallPermissionIntent(activity), getArguments().getInt(REQUEST_CODE));
                z = true;
            }
            if (stringArrayList.contains(Permission.SYSTEM_ALERT_WINDOW) && !PermissionUtils.isGrantedWindowPermission(activity)) {
                startActivityForResult(PermissionSettingPage.getWindowPermissionIntent(activity), getArguments().getInt(REQUEST_CODE));
                z = true;
            }
            if (stringArrayList.contains(Permission.NOTIFICATION_SERVICE) && !PermissionUtils.isGrantedNotifyPermission(activity)) {
                startActivityForResult(PermissionSettingPage.getNotifyPermissionIntent(activity), getArguments().getInt(REQUEST_CODE));
                z = true;
            }
            if (stringArrayList.contains(Permission.WRITE_SETTINGS) && !PermissionUtils.isGrantedSettingPermission(activity)) {
                startActivityForResult(PermissionSettingPage.getSettingPermissionIntent(activity), getArguments().getInt(REQUEST_CODE));
                z = true;
            }
        }
        if (z) {
            return;
        }
        requestDangerousPermission();
    }

    public void requestDangerousPermission() {
        ArrayList arrayList;
        final Activity activity = getActivity();
        Bundle arguments = getArguments();
        if (activity == null || arguments == null) {
            return;
        }
        final int i = arguments.getInt(REQUEST_CODE);
        final ArrayList<String> stringArrayList = arguments.getStringArrayList(REQUEST_PERMISSIONS);
        if (stringArrayList == null || stringArrayList.size() == 0) {
            return;
        }
        if (PermissionUtils.isAndroid10() && stringArrayList.contains(Permission.ACCESS_BACKGROUND_LOCATION)) {
            arrayList = new ArrayList();
            if (stringArrayList.contains(Permission.ACCESS_COARSE_LOCATION)) {
                arrayList.add(Permission.ACCESS_COARSE_LOCATION);
            }
            if (stringArrayList.contains(Permission.ACCESS_FINE_LOCATION)) {
                arrayList.add(Permission.ACCESS_FINE_LOCATION);
            }
        } else {
            arrayList = null;
        }
        if (!PermissionUtils.isAndroid10() || arrayList == null || arrayList.isEmpty()) {
            requestPermissions((String[]) stringArrayList.toArray(new String[stringArrayList.size() - 1]), getArguments().getInt(REQUEST_CODE));
        } else {
            beginRequest(activity, arrayList, null, new OnPermissionCallback() { // from class: com.hjq.permissions.PermissionFragment.1
                @Override
                public void onGranted(List<String> list, boolean z) {
                    if (z && PermissionFragment.this.isAdded()) {
                        PermissionFragment.beginRequest(activity, PermissionUtils.asArrayList(Permission.ACCESS_BACKGROUND_LOCATION), null, new OnPermissionCallback() { // from class: com.hjq.permissions.PermissionFragment.1.1
                            @Override
                            public void onGranted(List<String> list2, boolean z2) {
                                if (z2 && PermissionFragment.this.isAdded()) {
                                    int[] iArr = new int[stringArrayList.size()];
                                    Arrays.fill(iArr, 0);
                                    PermissionFragment.this.onRequestPermissionsResult(i, (String[]) stringArrayList.toArray(new String[0]), iArr);
                                }
                            }

                            @Override
                            public void onDenied(List<String> list2, boolean z2) {
                                if (PermissionFragment.this.isAdded()) {
                                    int[] iArr = new int[stringArrayList.size()];
                                    for (int i2 = 0; i2 < stringArrayList.size(); i2++) {
                                        iArr[i2] = Permission.ACCESS_BACKGROUND_LOCATION.equals(stringArrayList.get(i2)) ? -1 : 0;
                                    }
                                    PermissionFragment.this.onRequestPermissionsResult(i, (String[]) stringArrayList.toArray(new String[0]), iArr);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onDenied(List<String> list, boolean z) {
                    if (PermissionFragment.this.isAdded()) {
                        int[] iArr = new int[stringArrayList.size()];
                        Arrays.fill(iArr, -1);
                        PermissionFragment.this.onRequestPermissionsResult(i, (String[]) stringArrayList.toArray(new String[0]), iArr);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Bundle arguments = getArguments();
        Activity activity = getActivity();
        if (activity == null || arguments == null || this.mCallBack == null || i != arguments.getInt(REQUEST_CODE)) {
            return;
        }
        OnPermissionCallback onPermissionCallback = this.mCallBack;
        this.mCallBack = null;
        IPermissionInterceptor iPermissionInterceptor = this.mInterceptor;
        this.mInterceptor = null;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (PermissionUtils.isSpecialPermission(str)) {
                iArr[i2] = PermissionUtils.getPermissionStatus(activity, str);
            } else if (!PermissionUtils.isAndroid12() && (Permission.BLUETOOTH_SCAN.equals(str) || Permission.BLUETOOTH_CONNECT.equals(str) || Permission.BLUETOOTH_ADVERTISE.equals(str))) {
                iArr[i2] = PermissionUtils.getPermissionStatus(activity, str);
            } else if (!PermissionUtils.isAndroid10() && (Permission.ACCESS_BACKGROUND_LOCATION.equals(str) || Permission.ACTIVITY_RECOGNITION.equals(str) || Permission.ACCESS_MEDIA_LOCATION.equals(str))) {
                iArr[i2] = PermissionUtils.getPermissionStatus(activity, str);
            } else if (!PermissionUtils.isAndroid9() && Permission.ACCEPT_HANDOVER.equals(str)) {
                iArr[i2] = PermissionUtils.getPermissionStatus(activity, str);
            } else if (!PermissionUtils.isAndroid8() && (Permission.ANSWER_PHONE_CALLS.equals(str) || Permission.READ_PHONE_NUMBERS.equals(str))) {
                iArr[i2] = PermissionUtils.getPermissionStatus(activity, str);
            }
        }
        REQUEST_CODE_ARRAY.remove(Integer.valueOf(i));
        detachActivity(activity);
        List<String> grantedPermissions = PermissionUtils.getGrantedPermissions(strArr, iArr);
        if (grantedPermissions.size() == strArr.length) {
            if (iPermissionInterceptor != null) {
                iPermissionInterceptor.grantedPermissions(activity, onPermissionCallback, grantedPermissions, true);
                return;
            } else {
                onPermissionCallback.onGranted(grantedPermissions, true);
                return;
            }
        }
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(strArr, iArr);
        if (iPermissionInterceptor != null) {
            iPermissionInterceptor.deniedPermissions(activity, onPermissionCallback, deniedPermissions, PermissionUtils.isPermissionPermanentDenied(activity, deniedPermissions));
        } else {
            onPermissionCallback.onDenied(deniedPermissions, PermissionUtils.isPermissionPermanentDenied(activity, deniedPermissions));
        }
        if (grantedPermissions.isEmpty()) {
            return;
        }
        if (iPermissionInterceptor != null) {
            iPermissionInterceptor.grantedPermissions(activity, onPermissionCallback, grantedPermissions, false);
        } else {
            onPermissionCallback.onGranted(grantedPermissions, false);
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        Activity activity = getActivity();
        Bundle arguments = getArguments();
        if (activity == null || arguments == null || i != arguments.getInt(REQUEST_CODE) || this.mDangerousRequest) {
            return;
        }
        this.mDangerousRequest = true;
        activity.getWindow().getDecorView().postDelayed(this, 300L);
    }

    @Override
    public void run() {
        if (isAdded()) {
            requestDangerousPermission();
        }
    }
}
