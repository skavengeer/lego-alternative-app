package com.dino.smart;

import android.app.KeyguardManager;
import android.app.Service;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.dino.smart.ble.BleHandler;
import com.dino.smart.ble.BluetoothServiceHandler;
import com.dino.smart.handler.HexHandler;
import java.util.ArrayList;
import java.util.List;


public class MyService extends Service {

    Context _context;
    IntentFilter bleIntentFilter;
    BroadcastReceiver bleReceiver;
    int blecons = 3;
    List<BleHandler> blelist = new ArrayList();
    String sendData = null;
    String readSendData = null;
    public boolean isSendStop = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
        }
    };
    String oldContr = "1";
    BaseActivity.StateChangeReceiver msgReceiver ;
    Intent bleIntent;
    IntentFilter intentFilter1;
    final String LOG_TAG = "myLogs";

    public void activityStop() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("LOG_TAG", "Service onCreate");
        this._context = this;
        makeBleHander();
        this.bleReceiver = new IntentReceiver();
        IntentFilter intentFilter = new IntentFilter();
        this.bleIntentFilter = intentFilter;
        intentFilter.addAction("com.dino.smart.bleHandler");
        registerReceiver(this.bleReceiver,this.bleIntentFilter, Context.RECEIVER_EXPORTED);



        this.intentFilter1 = new IntentFilter();
        this.intentFilter1.addAction("com.dino.smart.blemsg");
        registerReceiver(this.msgReceiver,this.intentFilter1, Context.RECEIVER_EXPORTED);
        makeBleSendMsg(1);
        startRunableAdvertising();
    }

    public void makeBleSendMsg(int i) {
        Intent bleIntent = new Intent("com.dino.smart.blemsg");
        bleIntent.putExtra("type", i);
        sendBroadcast(bleIntent);
    }

    public void makeBleHander() {
        if (isbleOpen()) {
            Log.e("LOG_TAG", "makeBleHander  1112");
            if (this.blelist.size() > 0) {
                Log.e("LOG_TAG", "makeBleHander 1113");
                stopAdvertising();
                this.blelist.clear();
            }
            if (this.blelist.size() < this.blecons) {
                Log.e("LOG_TAG", "makeBleHander 1114");
                for (int i = 0; i < this.blecons; i++) {
                    this.blelist.add(new BleHandler(this));
                }
            }
        }
    }

    public void startRunableAdvertising() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    boolean z = false;
                    if (MyService.this.sendData != null && (MyService.this.readSendData == null || !MyService.this.readSendData.equals(MyService.this.sendData))) {
                        z = true;
                    }
                    if (z) {
                        synchronized (this) {
                            MyService.this.stopAdvertising();
                            MyService.this.readSendData = MyService.this.sendData;
                            MyService.this.startAdvertising(HexHandler.toBytes(MyService.this.readSendData));
                            Log.i("LOG_TAG", "MS startAdver" + MyService.this.sendData + "");
                        }
                    }
                    try {
                        Thread.sleep(20L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void activityStart() {
        this.isSendStop = false;
    }

    public void searchClear() {
        this.readSendData = null;
        this.sendData = null;
    }

    public boolean isbleOpen() {
        return ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled();
    }

    public void stopsearch() {
        stopAdvertising();
    }

    public void searchBle(String str) {
        searchClear();
        sendDev(str);
        Log.i("LOG_TAG", "MS searchBle");
    }

    public void sendDev(String str) {
        if (BluetoothServiceHandler.getInstance(this._context).isbleOpen())
            this.sendData = str;
        else
            Toast.makeText(this._context, getResources().getString(R.string.please_turn_on_bluetooth), Toast.LENGTH_SHORT).show();
    }

    public synchronized void startAdvertising(byte[] bArr) {
        for (int i = 0; i < this.blelist.size(); i++) {
            BleHandler bleHandler = this.blelist.get(i);
            if (bleHandler != null) {
                Log.e("LOG_TAG", "MS blelist() " + i);
                bleHandler.startAdvertising(bArr);
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void stopAdvertising() {
        for (int i = 0; i < this.blelist.size(); i++) {
            BleHandler bleHandler = this.blelist.get(i);
            if (bleHandler != null) {
                Log.e("LOG_TAG", "MS stopAdvertising" + i);
                bleHandler.stopAdvertising();
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d("LOG_TAG", "MS Service onStartCommand");
        Log.e("LOG_TAG", "MS onStartCommand");
        return super.onStartCommand(intent, i, i2);
    }

    @Override
    public void onDestroy() {
        Log.e("LOG_TAG", "MS Service onDestory");
        activityStop();
        Tools.saveBackKill(this, 1);
        stopAdvertising();
        super.onDestroy();
        BroadcastReceiver broadcastReceiver = this.bleReceiver;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private class IntentReceiver extends BroadcastReceiver {
        private IntentReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("type", -1);
            String stringExtra = intent.getStringExtra("blemsg");
            if (intExtra == 1) {
                return;
            }
            if (intExtra == 2) {
                MyService.this.activityStart();
                return;
            }
            if (intExtra == 3) {
                MyService.this.sendDev(stringExtra);
                return;
            }
            if (intExtra == 4) {
                MyService.this.searchBle(stringExtra);
            } else if (intExtra == 5) {
                MyService.this.stopsearch();
            } else if (intExtra == 6) {
                MyService.this.makeBleHander();
            }
        }
    }
}
