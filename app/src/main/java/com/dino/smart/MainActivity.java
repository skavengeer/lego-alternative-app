package com.dino.smart;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.dino.permissions.OnPermissionCallback;
import com.dino.permissions.Permission;
import com.dino.permissions.XXPermissions;
import com.dino.smart.ble.BluetoothServiceHandler;
import com.dino.smart.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    PopDeviceView popDeviceView;
    public static final int REQUEST_CODE = 101;
    int time;
    boolean isSearch = false;
    boolean pom=false;
    int ppom=0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {    }

     };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestPermissions();
        this.devInfo.setDevType("76");
        this.devInfo.setIndex(0);  //0,1,2
        Tools.saveBackKill(this, 0);
        startService(new Intent(this, MyService.class));

        binding.backbtn.setOnClickListener(view3 -> MainActivity.this.finish());

        binding.start.setOnClickListener(view1 ->  {
                    initBle();
                    if (pom) {
                        if (ppom > 0) {
                        Intent intent = new Intent(MainActivity.this, JoystickActivity.class);
                        if (MainActivity.this.devInfo != null) {
                            intent.putExtra("devInfo", MainActivity.this.devInfo);
                        }
                        MainActivity.this.startActivity(intent);
                    }
                }
            });

            binding.exit.setOnClickListener(view4 -> finish());

            popDeviceView = new PopDeviceView(this);

            if (isLocServiceEnable(this)) {
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.alert))
                    .setMessage(getString(R.string.open_location_service))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 MainActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                             }
            }).setNegativeButton(getString(R.string.no), (DialogInterface.OnClickListener) null).show();


    }

    public void searchDev() {
        if (this.isSearch) {
            return;
        }
        this.isSearch = true;
        this.time = 10;
        searchBle();
        BluetoothServiceHandler.getInstance(this).startScan();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MainActivity.this.time > 0) {
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.time--;
                    MainActivity.this.mHandler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public boolean isLocServiceEnable(Context context) {
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps");
    }

    /*
    public void changeIndex() {
        binding.indexTxt.setVisibility(View.VISIBLE);
        if (this.devInfo.getIndex() == 0) {
            binding.indexTxt.setText("A");
            binding.devimg.setImageResource(R.mipmap.cariocn0);
        } else if (this.devInfo.getIndex() == 1) {
            binding.indexTxt.setText("B");
            binding.devimg.setImageResource(R.mipmap.cariocn1);
        } else {
            binding.indexTxt.setText("C");
            binding.devimg.setImageResource(R.mipmap.cariocn);
        }
    }

     */

    public void initBle() {
        BluetoothServiceHandler.getInstance(this).initBle();
        try {
            if (!BluetoothServiceHandler.getInstance(this).isbleOpen()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, REQUEST_CODE);
            }else {
                Toast.makeText(this,"Bluetooth is ON",Toast.LENGTH_SHORT).show();
                pom = true;
                ppom++;
            }
        }catch (SecurityException e){}

    }

    public void requestPermissions() {
        XXPermissions.with(this).permission(Permission.BLUETOOTH_SCAN, Permission.BLUETOOTH_ADVERTISE, Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION).request(new OnPermissionCallback() {
            @Override
            public void onDenied(List<String> list, boolean z) {
                if (z) {
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.open_permission), Toast.LENGTH_SHORT).show();
                    XXPermissions.startPermissionActivity(MainActivity.this, list);
                } else {
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.authorization_failed), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onGranted(List<String> list, boolean z) {
                if (!z) {
                    XXPermissions.startPermissionActivity(MainActivity.this, list);
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.open_permission), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.authorization_succeeded), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this,"Permission is OK",Toast.LENGTH_SHORT).show();
                pom = true; ppom++;
                return;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Turn ON bluetooth and RESTART or Cancel aplication")
                        .setTitle("Please allow")
                        .setCancelable(false)
                        .setNegativeButton("Cencel", ((dialog,which)->{
                            finish();
                            dialog.dismiss();
                        }))
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (ppom >= 1) finish();
                                startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                                ppom++;
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        }
    }
}
