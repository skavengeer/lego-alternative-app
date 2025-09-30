package com.dino.smart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.dino.smart.ble.BluetoothServiceHandler;
import com.dino.smart.model.DevInfo;
import com.dino.permissions.OnPermissionCallback;
import com.dino.permissions.Permission;
import com.dino.permissions.XXPermissions;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseActivity extends AppCompatActivity {

    public Context _context;
    ImageView bleIcon;
    DevInfo devInfo;
    String devid;

    IntentFilter intentFilter;
    BroadcastReceiver msgReceiver;
    public Intent bleIntent = new Intent("com.dino.smart.bleHandler");

    private Dialog mLoadDialog;
    int speed;
    String strvalue;
    public String imagePath = "";
    private AtomicInteger progressDialogCount = new AtomicInteger(0);
    int apwd = 0;
    String c98Str = "";
    long c98Time = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Toast.makeText(BaseActivity.this._context, BaseActivity.this.getResources().getString(R.string.please_turn_on_bluetooth), Toast.LENGTH_SHORT).show();
        }
    };
    int same = 0;
    StateChangeReceiver stateChangeReceiver;
    IntentFilter stateChangeFilter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        this._context = this;
        this.devInfo = BluetoothServiceHandler.getInstance(this).devInfo;
        this.devid = Tools.getDevId(this);
        System.out.println("Metod 1：" + this.devid);
        this.devid = this.devid.split(" ")[2];
        inidMsg();
        this.stateChangeFilter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        stateChangeReceiver = new StateChangeReceiver();

     }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Tools.getBackKill(this) == 1) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, MyService.class));
            } else {
                startService(new Intent(this, MyService.class));
            }
            Tools.saveBackKill(this, 0);
        }
        BroadcastReceiver msgbroadcastReceiver = this.msgReceiver;
        if (msgbroadcastReceiver != null) {
             registerReceiver(msgbroadcastReceiver, this.intentFilter, Context.RECEIVER_EXPORTED);
        }
        stateChangeReceiver = new StateChangeReceiver();
        if (stateChangeReceiver != null) {
            registerReceiver(stateChangeReceiver, this.stateChangeFilter);
        }
        makeBleSendMsg(2, null);
        inidMsg();
        Log.i("aa=", "onStart" + getClass().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Tools.getBackKill(this) == 1) {
            startService(new Intent(this, MyService.class));
            Tools.saveBackKill(this, 0);
        }
        makeBleSendMsg(2, null);
        stopmove();
        Log.i("aa=", "onResume" + getClass().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("aa=", "onPause" + getClass().toString());
        makeBleSendMsg(1, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BroadcastReceiver msgbroadcastReceiver = this.msgReceiver;
        if (msgbroadcastReceiver != null) {
            unregisterReceiver(msgbroadcastReceiver);
        }
        Log.i("aa=", "onStop" + getClass().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("aa=", "onDestroy" + getClass().toString());
    }

    public void makeBleSendMsg(int i, String str) {
        this.bleIntent.putExtra("type", i);
        this.bleIntent.putExtra("blemsg", str);
        sendBroadcast(this.bleIntent);
    }

    public void inidMsg() {
        DevInfo devInfo = this.devInfo;
        devInfo.setBindgo(Tools.getDevControl("go", this, devInfo.getIndex()));
        DevInfo devInfo2 = this.devInfo;
        devInfo2.setBindback(Tools.getDevControl("back", this, devInfo2.getIndex()));
        DevInfo devInfo3 = this.devInfo;
        devInfo3.setBindleft(Tools.getDevControl("left", this, devInfo3.getIndex()));
        DevInfo devInfo4 = this.devInfo;
        devInfo4.setBindright(Tools.getDevControl("right", this, devInfo4.getIndex()));
        DevInfo devInfo5 = this.devInfo;
        devInfo5.setBindmid(Tools.getDevControl("mid", this, devInfo5.getIndex()));
        DevInfo devInfo6 = this.devInfo;
        devInfo6.setCodeA1(Tools.getDevControl("codeA1", this, devInfo6.getIndex()));
        DevInfo devInfo7 = this.devInfo;
        devInfo7.setCodeA2(Tools.getDevControl("codeA2", this, devInfo7.getIndex()));
        DevInfo devInfo8 = this.devInfo;
        devInfo8.setCodeB1(Tools.getDevControl("codeB1", this, devInfo8.getIndex()));
        DevInfo devInfo9 = this.devInfo;
        devInfo9.setCodeB2(Tools.getDevControl("codeB2", this, devInfo9.getIndex()));
        DevInfo devInfo10 = this.devInfo;
        devInfo10.setCodeC1(Tools.getDevControl("codeC1", this, devInfo10.getIndex()));
        DevInfo devInfo11 = this.devInfo;
        devInfo11.setCodeC2(Tools.getDevControl("codeC2", this, devInfo11.getIndex()));
        DevInfo devInfo12 = this.devInfo;
        devInfo12.setCodeD1(Tools.getDevControl("codeD1", this, devInfo12.getIndex()));
        DevInfo devInfo13 = this.devInfo;
        devInfo13.setCodeD2(Tools.getDevControl("codeD2", this, devInfo13.getIndex()));
    }

    public void searchBle() {
        if (BluetoothServiceHandler.getInstance(this._context).initScan()) {
            BluetoothServiceHandler.getInstance(this._context).isScanA = true;
            sendSearch();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaseActivity.this.beginScan();
                }
            }, 1000L);
            return;
        }
        Toast.makeText(this._context, getResources().getString(R.string.please_turn_on_bluetooth), Toast.LENGTH_SHORT).show();
    }

    public void beginScan() {
        BluetoothServiceHandler.getInstance(this._context).startScan();
    }

    public void stopSearchBle() {
        BluetoothServiceHandler.getInstance(this._context).stopScanBle();
        makeBleSendMsg(5, null);
    }


    public void sendSearch() {
        makeBleSendMsg(2, null);
        makeBleSendMsg(4, this.devid + " 00 00 00 " + BluetoothServiceHandler.getInstance(this).devApwd + " 00 00 00 00 00 E1 " + this.devInfo.getDevType());
    }

    public void sendDev(String str) {
        if (BluetoothServiceHandler.getInstance(this._context).isbleOpen()) {
            makeBleSendMsg(3, str);
        } else {
            this.handler.sendEmptyMessage(0);
        }
    }

    public void sendBindMsg() {
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
                BaseActivity.this.stopmove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stopmove() {
        DevInfo devInfo = this.devInfo;
        if (devInfo == null || devInfo.getDevType() == null) {
            return;
        }
        if (this.devInfo.getDevType().equals("99")) {
            this.strvalue = "0000";
            sendContrMsg();
        } else {
            sendValue98("000000");
            sendValue98("000000");
        }
    }

    public void control_forward() {
        if (!this.devInfo.isChangeL()) {
            this.strvalue = "1010";
        } else {
            this.strvalue = "0101";
        }
        sendContrMsg();
    }

    public void control_backward() {
        if (!this.devInfo.isChangeL()) {
            this.strvalue = "0101";
        } else {
            this.strvalue = "1010";
        }
        sendContrMsg();
    }

    public void control_left() {
        if (this.devInfo.isChangeL()) {
            if (this.devInfo.isChangeR()) {
                this.strvalue = "0001";
            } else {
                this.strvalue = "0100";
            }
        } else if (this.devInfo.isChangeR()) {
            this.strvalue = "0010";
        } else {
            this.strvalue = "1000";
        }
        sendContrMsg();
    }

    public void control_right() {
        if (this.devInfo.isChangeL()) {
            if (this.devInfo.isChangeR()) {
                this.strvalue = "0100";
            } else {
                this.strvalue = "0001";
            }
        } else if (this.devInfo.isChangeR()) {
            this.strvalue = "1000";
        } else {
            this.strvalue = "0010";
        }
        sendContrMsg();
    }

    public void control_turn_right() {
        if (this.devInfo.isChangeR()) {
            this.strvalue = "1001";
        } else {
            this.strvalue = "0110";
        }
        sendContrMsg();
    }

    public void control_turn_left() {
        if (this.devInfo.isChangeR()) {
            this.strvalue = "0110";
        } else {
            this.strvalue = "1001";
        }
        sendContrMsg();
    }

    public void control_back_right() {
        if (this.devInfo.isChangeL()) {
            if (this.devInfo.isChangeR()) {
                this.strvalue = "1000";
            } else {
                this.strvalue = "0010";
            }
        } else if (this.devInfo.isChangeR()) {
            this.strvalue = "0100";
        } else {
            this.strvalue = "0001";
        }
        sendContrMsg();
    }

    public void control_back_left() {
        if (this.devInfo.isChangeL()) {
            if (this.devInfo.isChangeR()) {
                this.strvalue = "0010";
            } else {
                this.strvalue = "1000";
            }
        } else if (this.devInfo.isChangeR()) {
            this.strvalue = "0001";
        } else {
            this.strvalue = "0100";
        }
        sendContrMsg();
    }

    public void sendContrMsg() {
        DevInfo devInfo = BluetoothServiceHandler.getInstance(this).devInfo;
        this.devInfo = devInfo;
        if (devInfo == null || devInfo.getDevId() == null) {
            return;
        }
        sendValue("00" + this.strvalue);
    }

    public void sendValue(String str) {
        int i = this.speed;
        String str2 = "11";
        if (i == 1) {
            str2 = "01";
        } else if (i == 2) {
            str2 = "10";
        }
        String str3 = str2 + str;
        this.devInfo = BluetoothServiceHandler.getInstance(this).devInfo;
        this.apwd = BluetoothServiceHandler.getInstance(this._context).apwd;
        DevInfo devInfo = this.devInfo;
        if (devInfo == null || devInfo.getDevId() == null) {
            return;
        }
        String strMakePwdVal = makePwdVal(Integer.parseInt(str3, 2));
        String strMakePwdVal2 = makePwdVal(makecrc(str3));
        String hexString = Integer.toHexString(this.apwd);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        sendDev(this.devid + " " + this.devInfo.getDevId() + " 00 00 " + hexString + " " + strMakePwdVal + " 00 00 " + strMakePwdVal + " " + strMakePwdVal2 + " C3 " + this.devInfo.getDevType());
    }

    public void sendValue98(String str) {
        if (str == null || str.equals("")) {
            return;
        }
        this.devInfo = BluetoothServiceHandler.getInstance(this).devInfo;
        this.apwd = BluetoothServiceHandler.getInstance(this._context).apwd;
        DevInfo devInfo = this.devInfo;
        if (devInfo == null || devInfo.getDevId() == null) {
            return;
        }
        sendValue98("00000000", "00" + str);
    }

    public void sendpValue98(String str) {
        if (str == null || str.equals("")) {
            return;
        }
        this.devInfo = BluetoothServiceHandler.getInstance(this).devInfo;
        this.apwd = BluetoothServiceHandler.getInstance(this._context).apwd;
        DevInfo devInfo = this.devInfo;
        if (devInfo == null || devInfo.getDevId() == null) {
            return;
        }
        sendValue98("00000000", "00" + makeCheckValue(str));
    }

    public String makeCheckValue(String str) {
        String str2;
        String str3;
        String str4;
        if (this.c98Str.equals(str)) {
            return str;
        }
        if (this.c98Str == "") {
            this.c98Str = str;
            this.c98Time = System.currentTimeMillis();
            return str;
        }
        if (str.length() != 6) {
            return "000000";
        }
        boolean z = false;
        String strSubstring = str.substring(0, 2);
        String strSubstring2 = str.substring(2, 4);
        String strSubstring3 = str.substring(4, 6);
        String strSubstring4 = this.c98Str.substring(0, 2);
        String strSubstring5 = this.c98Str.substring(2, 4);
        String strSubstring6 = this.c98Str.substring(4, 6);
        String strMakeNewCode = makeNewCode(strSubstring, strSubstring4);
        String strMakeNewCode2 = makeNewCode(strSubstring2, strSubstring5);
        String strMakeNewCode3 = makeNewCode(strSubstring3, strSubstring6);
        boolean z2 = true;
        if (!strMakeNewCode.equals("00") && !strSubstring4.equals(strMakeNewCode)) {
            z = true;
        }
        if (!strMakeNewCode2.equals("00") && !strSubstring5.equals(strMakeNewCode2) && !z) {
            str3 = strMakeNewCode + strMakeNewCode2;
        } else {
            if (strMakeNewCode2.equals("00")) {
                str2 = strMakeNewCode + strMakeNewCode2;
            } else {
                str2 = strMakeNewCode + strSubstring5;
            }
            z2 = z;
            str3 = str2;
        }
        if (!strMakeNewCode3.equals("00") && !strSubstring6.equals(strMakeNewCode3) && !z2) {
            str4 = str3 + strMakeNewCode3;
        } else if (strMakeNewCode2.equals("00")) {
            str4 = str3 + strMakeNewCode3;
        } else {
            str4 = str3 + strSubstring6;
        }
        if (System.currentTimeMillis() - this.c98Time < 30 || str4.equals(this.c98Str)) {
            return str;
        }
        this.c98Time = System.currentTimeMillis();
        this.c98Str = str4;
        Log.e("xdxda==","checkwalue:" + str4);
        return this.c98Str;
    }

    public String makeNewCode(String str, String str2) {
        return (str.equals("00") || str2.equals(str) || str2.equals("00")) ? str : "00";
    }

    public void sendValue98(String str, String str2) {
        this.devInfo = BluetoothServiceHandler.getInstance(this).devInfo;
        this.apwd = BluetoothServiceHandler.getInstance(this._context).apwd;
        DevInfo devInfo = this.devInfo;
        if (devInfo == null || devInfo.getDevId() == null) {
            return;
        }
        String strMakePwdVal = makePwdVal(Integer.parseInt(str, 2));
        String strMakePwdVal2 = makePwdVal(Integer.parseInt(str2, 2));
        String strMakePwdVal3 = makePwdVal(makecrc(str) + makecrc(str2));
        String hexString = Integer.toHexString(this.apwd);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        sendDev(this.devid + " " + this.devInfo.getDevId() + " 00 00 " + hexString + " " + strMakePwdVal + " 00 00 " + strMakePwdVal2 + " " + strMakePwdVal3 + " C3 " + this.devInfo.getDevType());
    }

    public int makecrc(String str) {
        int i = 0;
        int i2 = 0;
        while (i < str.length()) {
            int i3 = i + 1;
            if (str.substring(i, i3).equals("1")) {
                i2++;
            }
            i = i3;
        }
        return i2 < this.apwd ? i2 + 256 : i2;
    }

    public String makePwdVal(int i) {
        if (i < this.apwd) {
            i += 256;
        }
        String hexString = Integer.toHexString(i - this.apwd);
        if (hexString.length() != 1) {
            return hexString;
        }
        return "0" + hexString;
    }

    public class StateChangeReceiver extends BroadcastReceiver {

        public StateChangeReceiver() {        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == "android.bluetooth.adapter.action.STATE_CHANGED") {
                switch (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 0)) {
                    case 10:
                        Log.e("xx", "onReceive---------STATE_OFF");
                        break;
                    case 11:
                        Log.e("xx", "onReceive---------STATE_TURNING_ON");
                        break;
                    case 12:
                        Log.e("xx", "onReceive---------STATE_ON");
                        BaseActivity.this.makeBleSendMsg(6, null);
                        break;
                    case 13:
                        Log.e("xx", "onReceive---------STATE_TURNING_OFF");
                        break;
                }
            }
        }
    }

}
