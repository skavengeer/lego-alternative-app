package com.dino.smart.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.dino.smart.BaseActivity;
import com.dino.smart.Tools;
import com.dino.smart.handler.HexHandler;
import com.dino.smart.model.DevInfo;
import java.util.List;

public class BluetoothServiceHandler {

    private static BluetoothServiceHandler instance;
    public BluetoothAdapter bluetoothAdapter;
    Context mcontext;
    BluetoothLeScanner mBleScanner;
    ScanSettings mScanSettings;
    public Intent sendIntent = new Intent("com.dino.smart.blemsg");
    boolean isScan = false;
    public boolean isScanA = false;
    int scanTime = 0;
    BaseActivity baseActivity ;
    public int apwd = 0;
    public String devApwd = "";
    public DevInfo devInfo = new DevInfo();
    final String LOG_TAG = "myLogs";

    private Handler maHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            BluetoothServiceHandler.this.stopScanBle();
            BluetoothServiceHandler.this.sendIntent.putExtra("blecon", -1);
            BluetoothServiceHandler.this.mcontext.sendBroadcast(BluetoothServiceHandler.this.sendIntent);
        }
    };

    private BluetoothServiceHandler(Context context) {
        makePwd();
        this.mcontext = context;
    }

    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int i, ScanResult scanResult) {
            super.onScanResult(i, scanResult);
            String strBytesToHexFun3 = HexHandler.bytesToHexFun3(scanResult.getScanRecord().getManufacturerSpecificData(61951));
            Log.i("LOG_TAG77","BSH callbackByte:" + strBytesToHexFun3);
            if (BluetoothServiceHandler.this.devInfo.getDevId() != null) {
                return;
            }
            Log.e("LOG_TAG","BSH "+ strBytesToHexFun3);
            boolean pop = Tools.objIsNullStr(strBytesToHexFun3);
            if (!pop) {
                String[] strArrSplit = strBytesToHexFun3.split(" ");
                if (strArrSplit.length == 14 && strArrSplit[12].equals("d2")) {
                    BluetoothServiceHandler.this.devInfo.setDevId(strArrSplit[3] + " " + strArrSplit[4] + " " + strArrSplit[5]);
                    BluetoothServiceHandler.this.devInfo.setDevName(strArrSplit[3] + " " + strArrSplit[4] + " " + strArrSplit[5]);
                    if (BluetoothServiceHandler.this.devInfo.getDevType().equals(strArrSplit[13])) {
                        Log.e("LOG_TAG", "BSH Conect");
                        BluetoothServiceHandler.this.stopScanBle();
                        if (BluetoothServiceHandler.this.mcontext != null) {
                            BluetoothServiceHandler.this.sendIntent.putExtra("blecon", 1);
                            BluetoothServiceHandler.this.mcontext.sendBroadcast(BluetoothServiceHandler.this.sendIntent);

                        }
                    }
                }
            }
            Log.i("LOG_TAG", "BSH device:" + scanResult.getDevice() + "rssi:" + scanResult.getRssi() + "scanRecord:" +
                    scanResult.getScanRecord().getBytes() + "ManufacturerSpecificData:" +
                    HexHandler.bytesToHexFun3(scanResult.getScanRecord().getManufacturerSpecificData(65521)));
        }

        @Override
        public void onBatchScanResults(List<ScanResult> list) {
            super.onBatchScanResults(list);
        }

        @Override
        public void onScanFailed(int i) {
            super.onScanFailed(i);
        }
    };

    public static BluetoothServiceHandler getInstance(Context context) {
        if (instance == null) {
            instance = new BluetoothServiceHandler(context);
        }
        return instance;
    }

    public void initBle() {
        if (Build.VERSION.SDK_INT >= 31)
            this.bluetoothAdapter = ((BluetoothManager) this.mcontext.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        else
            this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isbleOpen() {   return this.bluetoothAdapter.isEnabled();    }

    public boolean initScan() {
        if (!this.bluetoothAdapter.isEnabled()) {
            return false;
        }
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(1);
        if (Build.VERSION.SDK_INT >= 23) {
            builder.setCallbackType(1);
            builder.setMatchMode(2);
        }
        if (this.bluetoothAdapter.isOffloadedScanBatchingSupported()) {
            builder.setReportDelay(0L);
        }
        this.mScanSettings = builder.build();
        return true;
    }

    public void startScan() {
        if (this.bluetoothAdapter.isEnabled() && !this.isScan) {
            this.isScan = true;
            this.scanTime = 0;
            if (this.mBleScanner == null) {
                this.mBleScanner = this.bluetoothAdapter.getBluetoothLeScanner();
            }
            this.devInfo.setDevId(null);
            new Thread(new Runnable() {
                Context mContext = mcontext.getApplicationContext();
                @Override
                public void run() {

                    if (ActivityCompat.checkSelfPermission(mContext.getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    //*************вика   mScanCallback


                    BluetoothServiceHandler.this.mBleScanner.startScan((List<ScanFilter>) null, BluetoothServiceHandler.this.mScanSettings, BluetoothServiceHandler.this.mScanCallback);

                }
            }).start();
            new Thread(new Runnable() {
                public void run() {
                    while (BluetoothServiceHandler.this.isScan) {
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        BluetoothServiceHandler.this.scanTime++;
                        if (BluetoothServiceHandler.this.scanTime >= 20) {
                            Log.i("LOG_TAG", "BSH Scan");
                            BluetoothServiceHandler.this.maHandler.sendEmptyMessage(0);
                        }
                    }
                }
            }).start();
        }
    }

    public void stopScanBle() {
        Log.i("LOG_TAG", "BSH stopscanBle");
        this.isScan = false;
        this.isScanA = false;
        BluetoothLeScanner bluetoothLeScanner = this.mBleScanner;
        if (bluetoothLeScanner == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this.mcontext.getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        bluetoothLeScanner.stopScan(this.mScanCallback);

    }

    public void makePwd() {
        this.apwd = 0;
        this.devApwd = "00 00 00";
    }


}
