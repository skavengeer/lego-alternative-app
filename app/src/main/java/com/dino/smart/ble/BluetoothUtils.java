package com.dino.smart.ble;

import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Build;
import androidx.core.content.ContextCompat;

public class BluetoothUtils {
    public static final int OpenBluetooth_Request_Code = 10086;

    public static String bytesToHexString(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String str) {
        byte[] bArr = new byte[str.length() / 2];
        byte[] bytes = str.getBytes();
        for (int i = 0; i < bytes.length / 2; i++) {
            int i2 = i * 2;
            byte b = bytes[i2 + 1];
            bArr[i] = (byte) (((byte) (Byte.decode("0x" + new String(new byte[]{bytes[i2]})).byteValue() << 4)) ^ Byte.decode("0x" + new String(new byte[]{b})).byteValue());
        }
        return bArr;
    }

    public static boolean hasPermissions(Context context, String[] strArr) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        for (String str : strArr) {
            if (ContextCompat.checkSelfPermission(context, str) != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean refreshDeviceCache(BluetoothGatt bluetoothGatt) {
        if (bluetoothGatt != null) {
            try {
                return ((Boolean) bluetoothGatt.getClass().getMethod("refresh", new Class[0]).invoke(bluetoothGatt, new Object[0])).booleanValue();
            } catch (Exception unused) {
            }
        }
        return false;
    }
}
