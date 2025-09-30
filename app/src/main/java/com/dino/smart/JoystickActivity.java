package com.dino.smart;

import static android.view.View.GONE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.dino.smart.action.OnPopClickListener;
import com.dino.smart.ble.BluetoothServiceHandler;

import com.dino.smart.databinding.ActivityJoystickBinding;
//import com.dino.smart.view.JoystickForView;
//import com.dino.smart.view.JoystickThrView;
import com.dino.smart.view.XCSlideView;

public class JoystickActivity extends BaseActivity {

    private ActivityJoystickBinding binding;
    boolean isdes;
    String lightStr;
    private XCSlideView mSlideViewLeft;
    PopDeviceView popDeviceView;
    String sValue;
    int time;
    boolean outActivity = false;
    int left1 = 0;
    int left2 = 0;
    int left = 0;
    int right = 0;
    int lockType = 0;
    long sTime = 0;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            JoystickActivity.this.onmove();
            return false;
        }
    });
    boolean isSearch = false;
    final String LOG_TAG = "myLogs";
    final String LOG_TAG77 = "myLogs";
    private Handler mHandler = new Handler() { // from class: com.tyb.smart.JoystickTwoActivity.16
        @Override
        public void handleMessage(Message message) {
            if (JoystickActivity.this.time <= 0) {
                JoystickActivity.this.isSearch = false;
                binding.searchBleBtn.setText("Search");
                BluetoothServiceHandler.getInstance(JoystickActivity.this._context).stopScanBle();
                JoystickActivity.this.popDeviceView.refList();
                return;
            }
            binding.searchBleBtn.setText("Searching..." + JoystickActivity.this.time);
            JoystickActivity.this.popDeviceView.refList();
            binding.searchBleBtn.setText("");
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityJoystickBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.lightStr = "00000000";
//****************************************************

     //    View viewInflate = LayoutInflater.from(this._context).inflate(R.layout.layout_slideview, (ViewGroup) null);

     //   XCSlideView xCSlideViewCreate = XCSlideView.create(this, XCSlideView.Positon.LEFT);

    //    this.mSlideViewLeft = xCSlideViewCreate;
    //    xCSlideViewCreate.setMenuView(this, viewInflate);
    //    this.mSlideViewLeft.setMenuWidth(Tools.dip2px(this, 250.0f));

//*********************

/*
        binding.vlight.setOnClickListener(view2 -> {
            if (JoystickActivity.this.lightStr.equals("00000000")) {
                JoystickActivity.this.lightStr = "00011111";
            } else {
                JoystickActivity.this.lightStr = "00000000";
            }
            JoystickActivity.this.refLight();
        });
*/
        binding.backbtn.setOnClickListener(view3 -> JoystickActivity.this.finish());

        binding.setbtn.setOnClickListener(view4 -> {
            JoystickActivity.this.startActivity(new Intent(JoystickActivity.this._context, CModelSetListActivity.class));
        });

        binding.blebtn.setOnClickListener(view6 -> {

            stateChangeReceiver = new StateChangeReceiver();
            if (JoystickActivity.this.devInfo.getDevId() != null) {
                JoystickActivity.this.devInfo.setDevId(null);
                JoystickActivity.this.stopSearchBle();
                JoystickActivity.this.changeIcon();
                binding.change.setVisibility(View.VISIBLE);
            } else {
                JoystickActivity.this.searchBle();
                binding.loadview.setVisibility(View.VISIBLE);
                binding.change.setVisibility(GONE);
                binding.pBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        });

        binding.loadview.setOnClickListener(view7 -> {
            binding.loadview.setVisibility(GONE);
            JoystickActivity.this.stopSearchBle();
        });

        this.bleIcon = binding.blebtn;
        this.isdes = false;



        binding.rightjoy.initMoveRes();
        if (this.devInfo.getIndex() == 0 || this.devInfo.getIndex() == 1) {
            binding.leftjoyba.initMoveLeft();
            binding.rightjoy.initMoveRight();
        }

        binding.leftjoyba.setOnJoystickMoveListener((i, i2, i3) -> {
            int i4 = i + 180;
            Log.i("LOG_TAG", "JA left "+ i2 + ":" + (i4 + 180));
            if (i2 <= 20) {
                JoystickActivity.this.left = 0;
            } else if (i4 >= 135 && i4 < 225) {
                JoystickActivity.this.left = 1;
            } else if (i4 >= 225 && i4 < 315) {
                JoystickActivity.this.left = -2;
            } else if (i4 >= 45 && i4 < 135) {
                JoystickActivity.this.left = 2;
            } else {
                JoystickActivity.this.left = -1;
            }
            Log.i("LOG_TAG", "JA " + JoystickActivity.this.left);
        }, 100L);


        binding.rightjoy.setOnJoystickMoveListener((i, i2, i3) -> {
            int i4 = i + 180;
            Log.i("LOG_TAG1", "JA right " + i2 + ":" + (i4 + 180));
            if (i2 > 20) {
                if (JoystickActivity.this.devInfo.getIndex() == 0 || JoystickActivity.this.devInfo.getIndex() == 1) {
                    if (i4 >= 135 && i4 < 225) {
                        JoystickActivity.this.right = 2;
                    } else if (i4 >= 225 && i4 < 315) {
                        JoystickActivity.this.right = -1;
                    } else if (i4 >= 45 && i4 < 135) {
                        JoystickActivity.this.right = 1;
                    } else {
                        JoystickActivity.this.right = -2;
                    }
                } else if (i4 >= 135 && i4 < 225) {
                    JoystickActivity.this.right = 1;
                } else if (i4 >= 225 && i4 < 315) {
                    JoystickActivity.this.right = -2;
                } else if (i4 >= 45 && i4 < 135) {
                    JoystickActivity.this.right = 2;
                } else {
                    JoystickActivity.this.right = -1;
                }
            } else {
                JoystickActivity.this.right = 0;
            }
            Log.i("LOG_TAG", "JA " + JoystickActivity.this.right);
        }, 100L);

        this.popDeviceView = new PopDeviceView(this._context);

        binding.searchBleBtn.setOnClickListener(view8 -> JoystickActivity.this.searchDev());

        new Thread(() -> {
            while (!JoystickActivity.this.isdes) {
                JoystickActivity.this.onmove();
                try {
                    Thread.sleep(5L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        this.msgReceiver = new MsgReceiver();
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction("com.dino.smart.blemsg");
        this.speed = 2;
    }

    /*
    public void refLight() {
        if (this.lightStr.equals("00000000")) {
            binding.vlight.setImageResource(R.mipmap.lightc);
        } else {
            binding.vlight.setImageResource(R.mipmap.lighto);
        }
    }

     */

    public void bindBle() {
        stopmove();
        changeIcon();
        binding.loadview.setVisibility(GONE);
        Tools.showAlert3(this._context, getResources().getString(R.string.device_connected_successfully));
        sendBindMsg();
        Log.e("LOG_TAG77","R.string.device_connected_successfully");

    }

    public void changeIcon() {
        if (this.devInfo.getDevId() != null) {
            binding.blebtn.setImageResource(R.mipmap.ble02);
            binding.change.setVisibility(View.VISIBLE);
            binding.searchBleBtn.setText("");
        } else {
            binding.blebtn.setImageResource(R.mipmap.ble01);
        }
    }

    public void onmove() {
        if (this.devInfo == null) {
            return;
        }
        joymove();
    }

    public void joymove() {
        String strControl_B2;

        if (this.outActivity || this.devInfo == null || this.devInfo.getDevId() == null) {
            return;
        }
        int i = this.left;
        String strControl_D2 = "00000000,00,00,00,00";
        if (i == 1) {
            strControl_B2 = this.devInfo.control_A1();
        } else if (i == -1) {
            strControl_B2 = this.devInfo.control_A2();
        } else if (i == 2) {
            strControl_B2 = this.devInfo.control_B1();
        } else {
            strControl_B2 = i == -2 ? this.devInfo.control_B2() : "00000000,00,00,00,00";
        }
        int i2 = this.right;
        if (i2 == 1) {
            strControl_D2 = this.devInfo.control_C1();
        } else if (i2 == -1) {
            strControl_D2 = this.devInfo.control_C2();
        } else if (i2 == 2) {
            strControl_D2 = this.devInfo.control_D1();
        } else if (i2 == -2) {
            strControl_D2 = this.devInfo.control_D2();
        }
        String strControlMake = controlMake(strControl_B2, strControl_D2);
        String strSubstring = strControlMake.substring(0, 8);
        String strSubstring2 = strControlMake.substring(8, 16);
        if (strSubstring.equals("00000000")) {
            strSubstring = this.lightStr;
        }
        sendValue98(strSubstring, strSubstring2);
    }

    public String controlMake(String str, String str2) {
        String[] strArrSplit = str.split(",");
        String[] strArrSplit2 = str2.split(",");
        String str3 = "";
        for (int i = 0; i < strArrSplit.length; i++) {
            String str4 = strArrSplit[i];
            String str5 = strArrSplit2[i];
            if (i == 0) {
                if (str4.equals("00000000")) {
                    str4 = "00000000";
                }
                str3 = !str5.equals("00000000") ? str5 : str4;
            } else if (str4.equals(str5)) {
                str3 = str3 + str5;
            } else if (!str4.equals(str5)) {
                if (!str4.equals("00") && !str5.equals("00")) {
                    str3 = str3 + str5;
                } else if (str5.equals("00")) {
                    str3 = str3 + str4;
                } else {
                    str3 = str3 + str5;
                }
            }
        }
        return str3;
    }

    public void searchDev() {
        if (this.isSearch) {
            return;
        }
        this.isSearch = true;
        this.time = 10;
        searchBle();
        BluetoothServiceHandler.getInstance(this._context).startScan();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (JoystickActivity.this.time > 0) {
                    JoystickActivity joystickActivity = JoystickActivity.this;
                    joystickActivity.time--;
                    JoystickActivity.this.mHandler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.outActivity = false;
        changeIcon();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.outActivity = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.isdes = true;
    }

    private class MsgReceiver extends BroadcastReceiver {
        private MsgReceiver() {        }
        @Override
        public void onReceive(Context context, Intent intent) {
            JoystickActivity.this.stopSearchBle();
            if (intent.getIntExtra("blecon", -1) == 1) {
                JoystickActivity.this.bindBle();
            } else {
                binding.loadview.setVisibility(GONE);
                Tools.showAlert3(JoystickActivity.this._context, JoystickActivity.this.getResources().getString(R.string.device_connected_error));
            }
        }
    }
}
