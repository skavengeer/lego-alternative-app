package com.dino.smart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.dino.smart.databinding.ActivityCModelSetListBinding;
import com.dino.smart.databinding.ActivityWModelSetBinding;

public class WModelSetActivity extends BaseActivity {

    private ActivityWModelSetBinding binding;

    String bindStr1;
    String bindStr2;
    String bindStr3;
    String bindStr4;
    String bindType;
    String codeStr;
    boolean istest = false;
    int l1;
    int l2;
    int l3;
    int l4;
    int l5;

    String lightStr;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityWModelSetBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.bindType = getIntent().getStringExtra("bindType");
        this.codeStr = getIntent().getStringExtra("codeStr");
        this.speed = 3;
        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WModelSetActivity.this.finish();
            }
        });
        binding.blebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WModelSetActivity.this.devInfo.getDevId() != null) {
                    WModelSetActivity.this.devInfo.setDevId(null);
                    WModelSetActivity.this.stopSearchBle();
                    WModelSetActivity.this.changeIcon();
                } else {
                    WModelSetActivity.this.searchBle();
                    binding.loadview.setVisibility(View.VISIBLE);
                }
            }
        });
        this.bleIcon = binding.blebtn;
        binding.testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WModelSetActivity.this.testAction();
            }
        });
        binding.loadview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.loadview.setVisibility(View.GONE);
                WModelSetActivity.this.stopSearchBle();
            }
        });
        makeStr(this.codeStr);
        refview();
        this.msgReceiver = new MsgReceiver();
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction("com.tyb.smart.blemsg");
        binding.drive1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WModelSetActivity.this.devInfo.getIndex() == 0) {
                    if (WModelSetActivity.this.bindStr1.equals("10")) {
                        WModelSetActivity.this.bindStr1 = "01";
                    } else if (WModelSetActivity.this.bindStr1.equals("01")) {
                        WModelSetActivity.this.bindStr1 = "00";
                    } else {
                        WModelSetActivity.this.bindStr1 = "10";
                    }
                } else if (WModelSetActivity.this.devInfo.getIndex() == 1) {
                    if (WModelSetActivity.this.bindStr2.equals("10")) {
                        WModelSetActivity.this.bindStr2 = "01";
                    } else if (WModelSetActivity.this.bindStr2.equals("01")) {
                        WModelSetActivity.this.bindStr2 = "00";
                    } else {
                        WModelSetActivity.this.bindStr2 = "10";
                    }
                } else if (WModelSetActivity.this.devInfo.getIndex() == 2) {
                    if (WModelSetActivity.this.bindStr1.equals("10")) {
                        WModelSetActivity.this.bindStr1 = "01";
                    } else if (WModelSetActivity.this.bindStr1.equals("01")) {
                        WModelSetActivity.this.bindStr1 = "00";
                    } else {
                        WModelSetActivity.this.bindStr1 = "10";
                    }
                }
                WModelSetActivity.this.refview();
            }
        });
        binding.drive2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WModelSetActivity.this.devInfo.getIndex() == 0) {
                    if (WModelSetActivity.this.bindStr2.equals("10")) {
                        WModelSetActivity.this.bindStr2 = "01";
                    } else if (WModelSetActivity.this.bindStr2.equals("01")) {
                        WModelSetActivity.this.bindStr2 = "00";
                    } else {
                        WModelSetActivity.this.bindStr2 = "10";
                    }
                } else if (WModelSetActivity.this.devInfo.getIndex() == 1) {
                    if (WModelSetActivity.this.bindStr1.equals("10")) {
                        WModelSetActivity.this.bindStr1 = "01";
                    } else if (WModelSetActivity.this.bindStr1.equals("01")) {
                        WModelSetActivity.this.bindStr1 = "00";
                    } else {
                        WModelSetActivity.this.bindStr1 = "10";
                    }
                } else if (WModelSetActivity.this.devInfo.getIndex() == 2) {
                    if (WModelSetActivity.this.bindStr2.equals("10")) {
                        WModelSetActivity.this.bindStr2 = "01";
                    } else if (WModelSetActivity.this.bindStr2.equals("01")) {
                        WModelSetActivity.this.bindStr2 = "00";
                    } else {
                        WModelSetActivity.this.bindStr2 = "10";
                    }
                }
                WModelSetActivity.this.refview();
            }
        });
        binding.drive3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WModelSetActivity.this.devInfo.getIndex() == 0) {
                    if (WModelSetActivity.this.bindStr4.equals("10")) {
                        WModelSetActivity.this.bindStr4 = "01";
                    } else if (WModelSetActivity.this.bindStr4.equals("01")) {
                        WModelSetActivity.this.bindStr4 = "00";
                    } else {
                        WModelSetActivity.this.bindStr4 = "10";
                    }
                } else if (WModelSetActivity.this.devInfo.getIndex() == 1) {
                    if (WModelSetActivity.this.bindStr3.equals("10")) {
                        WModelSetActivity.this.bindStr3 = "01";
                    } else if (WModelSetActivity.this.bindStr3.equals("01")) {
                        WModelSetActivity.this.bindStr3 = "00";
                    } else {
                        WModelSetActivity.this.bindStr3 = "10";
                    }
                } else if (WModelSetActivity.this.devInfo.getIndex() == 2) {
                    if (WModelSetActivity.this.bindStr3.equals("10")) {
                        WModelSetActivity.this.bindStr3 = "01";
                    } else if (WModelSetActivity.this.bindStr3.equals("01")) {
                        WModelSetActivity.this.bindStr3 = "00";
                    } else {
                        WModelSetActivity.this.bindStr3 = "10";
                    }
                }
                WModelSetActivity.this.refview();
            }
        });
        binding.drive4.setOnClickListener(new View.OnClickListener() { // from class: com.tyb.smart.WModelSetActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (WModelSetActivity.this.devInfo.getIndex() == 0) {
                    if (WModelSetActivity.this.bindStr3.equals("10")) {
                        WModelSetActivity.this.bindStr3 = "01";
                    } else if (WModelSetActivity.this.bindStr3.equals("01")) {
                        WModelSetActivity.this.bindStr3 = "00";
                    } else {
                        WModelSetActivity.this.bindStr3 = "10";
                    }
                } else if (WModelSetActivity.this.devInfo.getIndex() == 1) {
                    if (WModelSetActivity.this.bindStr4.equals("10")) {
                        WModelSetActivity.this.bindStr4 = "01";
                    } else if (WModelSetActivity.this.bindStr4.equals("01")) {
                        WModelSetActivity.this.bindStr4 = "00";
                    } else {
                        WModelSetActivity.this.bindStr4 = "10";
                    }
                } else if (WModelSetActivity.this.devInfo.getIndex() == 2) {
                    if (WModelSetActivity.this.bindStr4.equals("10")) {
                        WModelSetActivity.this.bindStr4 = "01";
                    } else if (WModelSetActivity.this.bindStr4.equals("01")) {
                        WModelSetActivity.this.bindStr4 = "00";
                    } else {
                        WModelSetActivity.this.bindStr4 = "10";
                    }
                }
                WModelSetActivity.this.refview();
            }
        });
        binding.light1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WModelSetActivity.this.l1 == 1) {
                    WModelSetActivity.this.l1 = 0;
                } else {
                    WModelSetActivity.this.l1 = 1;
                }
                WModelSetActivity.this.refview();
            }
        });
        binding.light2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WModelSetActivity.this.l2 == 1) {
                    WModelSetActivity.this.l2 = 0;
                } else {
                    WModelSetActivity.this.l2 = 1;
                }
                WModelSetActivity.this.refview();
            }
        });
        binding.light3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WModelSetActivity.this.l3 == 1) {
                    WModelSetActivity.this.l3 = 0;
                } else {
                    WModelSetActivity.this.l3 = 1;
                }
                WModelSetActivity.this.refview();
            }
        });
        binding.light4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WModelSetActivity.this.l4 == 1) {
                    WModelSetActivity.this.l4 = 0;
                } else {
                    WModelSetActivity.this.l4 = 1;
                }
                WModelSetActivity.this.refview();
            }
        });
        binding.light5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WModelSetActivity.this.l5 == 1) {
                    WModelSetActivity.this.l5 = 0;
                } else {
                    WModelSetActivity.this.l5 = 1;
                }
                WModelSetActivity.this.refview();
            }
        });
        binding.savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WModelSetActivity.this.lightStr = "000" + WModelSetActivity.this.l5 + WModelSetActivity.this.l4 + WModelSetActivity.this.l3 + WModelSetActivity.this.l2 + WModelSetActivity.this.l1;
                Tools.saveDevControl(WModelSetActivity.this.bindType, WModelSetActivity.this.bindStr1 + "," + WModelSetActivity.this.bindStr2 + "," + WModelSetActivity.this.bindStr3 + "," + WModelSetActivity.this.bindStr4 + "," + WModelSetActivity.this.lightStr, WModelSetActivity.this._context);
                WModelSetActivity.this.finish();
            }
        });
        binding.cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WModelSetActivity.this.finish();
            }
        });
    }

    public void testAction() {
        this.lightStr = "000" + this.l5 + this.l4 + this.l3 + this.l2 + this.l1;
        if (!this.istest) {
            this.istest = true;
            String str = this.lightStr + this.bindStr4 + this.bindStr3 + this.bindStr2 + this.bindStr1;
            sendValue98(str.substring(0, 8), str.substring(8, 16));
            binding.testBtn.setImageResource(R.mipmap.wmodeset_06);
            return;
        }
        this.istest = false;
        sendValue98("00000000", "00000000");
        binding.testBtn.setImageResource(R.mipmap.wmodeset_05);
    }

    public void makeStr(String str) {
        String[] strArrSplit = str.split(",");
        this.bindStr1 = strArrSplit[0];
        this.bindStr2 = strArrSplit[1];
        this.bindStr3 = strArrSplit[2];
        this.bindStr4 = strArrSplit[3];
        String str2 = strArrSplit[4];
        this.lightStr = str2;
        this.l1 = Integer.parseInt(str2.substring(7, 8));
        this.l2 = Integer.parseInt(this.lightStr.substring(6, 7));
        this.l3 = Integer.parseInt(this.lightStr.substring(5, 6));
        this.l4 = Integer.parseInt(this.lightStr.substring(4, 5));
        this.l5 = Integer.parseInt(this.lightStr.substring(3, 4));
    }

    public void refview() {
        if (this.devInfo.getIndex() == 0) {
            if (this.bindStr1.equals("10")) {
                binding.drive1.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr1.equals("01")) {
                binding.drive1.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive1.setImageResource(R.mipmap.wmodeset_04);
            }
            if (this.bindStr2.equals("10")) {
                binding.drive2.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr2.equals("01")) {
                binding.drive2.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive2.setImageResource(R.mipmap.wmodeset_04);
            }
            if (this.bindStr3.equals("10")) {
                binding.drive4.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr3.equals("01")) {
                binding.drive4.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive4.setImageResource(R.mipmap.wmodeset_04);
            }
            if (this.bindStr4.equals("10")) {
                binding.drive3.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr4.equals("01")) {
                binding.drive3.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive3.setImageResource(R.mipmap.wmodeset_04);
            }
        } else if (this.devInfo.getIndex() == 1) {
            if (this.bindStr1.equals("10")) {
                binding.drive2.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr1.equals("01")) {
                binding.drive2.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive2.setImageResource(R.mipmap.wmodeset_04);
            }
            if (this.bindStr2.equals("10")) {
                binding.drive1.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr2.equals("01")) {
                binding.drive1.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive1.setImageResource(R.mipmap.wmodeset_04);
            }
            if (this.bindStr3.equals("10")) {
                binding.drive3.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr3.equals("01")) {
                binding.drive3.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive3.setImageResource(R.mipmap.wmodeset_04);
            }
            if (this.bindStr4.equals("10")) {
                binding.drive4.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr4.equals("01")) {
                binding.drive4.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive4.setImageResource(R.mipmap.wmodeset_04);
            }
        } else if (this.devInfo.getIndex() == 2) {
            if (this.bindStr1.equals("10")) {
                binding.drive1.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr1.equals("01")) {
                binding.drive1.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive1.setImageResource(R.mipmap.wmodeset_04);
            }
            if (this.bindStr2.equals("10")) {
                binding.drive2.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr2.equals("01")) {
                binding.drive2.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive2.setImageResource(R.mipmap.wmodeset_04);
            }
            if (this.bindStr3.equals("10")) {
                binding.drive3.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr3.equals("01")) {
                binding.drive3.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive3.setImageResource(R.mipmap.wmodeset_04);
            }
            if (this.bindStr4.equals("10")) {
                binding.drive4.setImageResource(R.mipmap.wmodeset_02);
            } else if (this.bindStr4.equals("01")) {
                binding.drive4.setImageResource(R.mipmap.wmodeset_03);
            } else {
                binding.drive4.setImageResource(R.mipmap.wmodeset_04);
            }
        }
        if (this.l1 == 1) {
            binding.light1.setImageResource(R.mipmap.llighto);
        } else {
            binding.light1.setImageResource(R.mipmap.llightc);
        }
        if (this.l2 == 1) {
            binding.light2.setImageResource(R.mipmap.llighto);
        } else {
            binding.light2.setImageResource(R.mipmap.llightc);
        }
        if (this.l3 == 1) {
            binding.light3.setImageResource(R.mipmap.llighto);
        } else {
            binding.light3.setImageResource(R.mipmap.llightc);
        }
        if (this.l4 == 1) {
            binding.light4.setImageResource(R.mipmap.llighto);
        } else {
            binding.light4.setImageResource(R.mipmap.llightc);
        }
        if (this.l5 == 1) {
            binding.light5.setImageResource(R.mipmap.llighto);
        } else {
            binding.light5.setImageResource(R.mipmap.llightc);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.devInfo.setChangeL(Tools.getleftchange99(this._context));
        this.devInfo.setChangeR(Tools.getrightchange99(this._context));
        changeIcon();
    }

    private class MsgReceiver extends BroadcastReceiver {
        private MsgReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            WModelSetActivity.this.stopSearchBle();
            if (intent.getIntExtra("blecon", -1) == 1) {
                WModelSetActivity.this.bindBle();
            } else {
                binding.loadview.setVisibility(View.GONE);
                Tools.showAlert3(WModelSetActivity.this._context, WModelSetActivity.this.getResources().getString(R.string.device_connected_error));
            }
        }
    }

    public void bindBle() {
        stopmove();
        changeIcon();
        binding.loadview.setVisibility(View.GONE);
        Tools.showAlert3(this._context, getResources().getString(R.string.device_connected_successfully));
        sendBindMsg();
    }

    public void changeIcon() {
        if (this.devInfo.getDevId() != null) {
            binding.blebtn.setImageResource(R.mipmap.ble02);
        } else {
            binding.blebtn.setImageResource(R.mipmap.ble01);
        }
    }
}
