package com.dino.smart.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.dino.smart.BaseActivity;
import com.dino.smart.Tools;
import com.dino.smart.handler.HexHandler;

public class BleHandler {

    AdvertiseData advertiseData;
    AdvertiseSettings advertiseSettings;
    BluetoothAdapter bluetoothAdapter;
    BluetoothManager bluetoothManager;
    BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    Context mContext;
    public int aaa = 1;
    BaseActivity baseActivity;

    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings advertiseSettings) {
            super.onStartSuccess(advertiseSettings);
            Log.i("xxx1=", "BT=OK" + BleHandler.this.aaa);
        }

        @Override
        public void onStartFailure(int i) {
            super.onStartFailure(i);
            Log.e("xxx1=", "BT errorCode:" + i);
        }
    };

    public boolean initAdvertising(Context context) {
        return true;
    }

    public BleHandler(Context context) {
        this.mContext = context;
        if (isSupportBLE()) {
            this.bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        }
        BluetoothAdapter adapter = this.bluetoothManager.getAdapter();
        this.bluetoothAdapter = adapter;
        this.mBluetoothLeAdvertiser = adapter.getBluetoothLeAdvertiser();
        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();
        builder.setAdvertiseMode(2);
        builder.setConnectable(true);
        builder.setTxPowerLevel(3);
        builder.setTimeout(0);
        this.advertiseSettings = builder.build();
    }

    private boolean isSupportBLE() {
        return this.mContext.getApplicationContext().getPackageManager().hasSystemFeature("android.hardware.bluetooth_le");
    }

    public void stopAdvertising() {
        if (this.mBluetoothLeAdvertiser == null) {
            return;
        }
        Log.i("xxx1=", "BT stop==== OK" + this.aaa);
        mContext.getApplicationContext();
        if (ActivityCompat.checkSelfPermission( mContext, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
           // baseActivity.requestPermissions();
            return;
        }

        this.mBluetoothLeAdvertiser.stopAdvertising(this.mAdvertiseCallback);
    }

    public void startAdvertising(byte[] bArr) {
        if (this.mBluetoothLeAdvertiser == null) {
            return;
        }
        String devId = Tools.getDevId(this.mContext);
        System.out.println("metod 1：" + devId);
        String[] strArrSplit = devId.split(" ");
        byte[] bytes = HexHandler.toBytes(strArrSplit[0] + " " + strArrSplit[1]);
        int iBytes2Dec = HexHandler.Bytes2Dec(new byte[]{0, 0, bytes[1], bytes[0]});
        AdvertiseData.Builder builder = new AdvertiseData.Builder();
        builder.setIncludeDeviceName(false);
        builder.addManufacturerData(iBytes2Dec, bArr);
        AdvertiseData advertiseDataBuild = builder.build();
        this.advertiseData = advertiseDataBuild;
        this.mBluetoothLeAdvertiser.startAdvertising(this.advertiseSettings, advertiseDataBuild, this.mAdvertiseCallback);
    }

    public void stopsend() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BleHandler.this.stopAdvertising();
            }
        }).start();
    }
}
