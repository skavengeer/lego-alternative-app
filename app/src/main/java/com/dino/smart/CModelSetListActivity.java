package com.dino.smart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dino.smart.databinding.ActivityCModelSetListBinding;

public class CModelSetListActivity extends BaseActivity {

    private ActivityCModelSetListBinding binding;

    @Override
    protected void onCreate(Bundle bundle) throws NumberFormatException {
        super.onCreate(bundle);
        binding = ActivityCModelSetListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.backbtn.setOnClickListener(view1 -> CModelSetListActivity.this.finish());
        refview();
        binding.changebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CModelSetListActivity.this, WModelSetActivity.class);
                intent.putExtra("bindType", "codeA1");
                intent.putExtra("codeStr", CModelSetListActivity.this.devInfo.getCodeA1());
                CModelSetListActivity.this.startActivity(intent);
            }
        });
        binding.changebtn2.setOnClickListener(view2 -> {
            Intent intent = new Intent(CModelSetListActivity.this,  WModelSetActivity.class);
            intent.putExtra("bindType", "codeA2");
            intent.putExtra("codeStr", CModelSetListActivity.this.devInfo.getCodeA2());
            CModelSetListActivity.this.startActivity(intent);
        });
        binding.changebtn3.setOnClickListener(view3 -> {
            Intent intent = new Intent(CModelSetListActivity.this,  WModelSetActivity.class);
            intent.putExtra("bindType", "codeB1");
            intent.putExtra("codeStr", CModelSetListActivity.this.devInfo.getCodeB1());
            CModelSetListActivity.this.startActivity(intent);
        });
        binding.changebtn4.setOnClickListener(view4 -> {
            Intent intent = new Intent(CModelSetListActivity.this, WModelSetActivity.class);
            intent.putExtra("bindType", "codeB2");
            intent.putExtra("codeStr", CModelSetListActivity.this.devInfo.getCodeB2());
            CModelSetListActivity.this.startActivity(intent);
        });
        binding.changebtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CModelSetListActivity.this,  WModelSetActivity.class);
                intent.putExtra("bindType", "codeC1");
                intent.putExtra("codeStr", CModelSetListActivity.this.devInfo.getCodeC1());
                CModelSetListActivity.this.startActivity(intent);
            }
        });
        binding.changebtn6.setOnClickListener(view5 -> {
            Intent intent = new Intent(CModelSetListActivity.this,  WModelSetActivity.class);
            intent.putExtra("bindType", "codeC2");
            intent.putExtra("codeStr", CModelSetListActivity.this.devInfo.getCodeC2());
            CModelSetListActivity.this.startActivity(intent);
        });
        binding.changebtn7.setOnClickListener(view6 -> {
            Intent intent = new Intent(CModelSetListActivity.this, WModelSetActivity.class);
            intent.putExtra("bindType", "codeD1");
            intent.putExtra("codeStr", CModelSetListActivity.this.devInfo.getCodeD1());
            CModelSetListActivity.this.startActivity(intent);
        });
        binding.changebtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CModelSetListActivity.this, WModelSetActivity.class);
                intent.putExtra("bindType", "codeD2");
                intent.putExtra("codeStr", CModelSetListActivity.this.devInfo.getCodeD2());
                CModelSetListActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() throws NumberFormatException {
        super.onStart();
        refview();
    }

    public void refview() throws NumberFormatException {
        makeBtn(100, this.devInfo.getCodeA1());
        makeBtn(200, this.devInfo.getCodeA2());
        makeBtn(300, this.devInfo.getCodeB1());
        makeBtn(400, this.devInfo.getCodeB2());
        makeBtn(500, this.devInfo.getCodeC1());
        makeBtn(600, this.devInfo.getCodeC2());
        makeBtn(700, this.devInfo.getCodeD1());
        makeBtn(800, this.devInfo.getCodeD2());
    }

    public void makeBtn(int i, String str) throws NumberFormatException {

        String[] strArrSplit = str.split(",");
        int i2 = 0;
        int i3 = 0;
        while (i3 < 5) {
            TextView textView = (TextView) binding.conview.findViewWithTag((i + i3) + "");
            textView.setVisibility(i2);
            String str2 = strArrSplit[i3];
            if (i3 == 0) {
                if (this.devInfo.getIndex() == 0) {
                    if (str2.equals("10")) {
                        textView.setText("A " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("A " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                } else if (this.devInfo.getIndex() == 1) {
                    if (str2.equals("10")) {
                        textView.setText("B " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("B " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                } else if (this.devInfo.getIndex() == 2) {
                    if (str2.equals("10")) {
                        textView.setText("A " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("A " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                }
            } else if (i3 == 1) {
                if (this.devInfo.getIndex() == 0) {
                    if (str2.equals("10")) {
                        textView.setText("B " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("B " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                } else if (this.devInfo.getIndex() == 1) {
                    if (str2.equals("10")) {
                        textView.setText("A " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("A " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                } else if (this.devInfo.getIndex() == 2) {
                    if (str2.equals("10")) {
                        textView.setText("B " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("B " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                }
            } else if (i3 == 2) {
                if (this.devInfo.getIndex() == 0) {
                    if (str2.equals("10")) {
                        textView.setText("D " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("D " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                } else if (this.devInfo.getIndex() == 1) {
                    if (str2.equals("10")) {
                        textView.setText("C " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("C " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                } else if (this.devInfo.getIndex() == 2) {
                    if (str2.equals("10")) {
                        textView.setText("C " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("C " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                }
            } else if (i3 == 3) {
                if (this.devInfo.getIndex() == 0) {
                    if (str2.equals("10")) {
                        textView.setText("C " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("C " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                } else if (this.devInfo.getIndex() == 1) {
                    if (str2.equals("10")) {
                        textView.setText("D " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("D " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                } else if (this.devInfo.getIndex() == 2) {
                    if (str2.equals("10")) {
                        textView.setText("D " + getResources().getString(R.string.left_l));
                    } else if (str2.equals("01")) {
                        textView.setText("D " + getResources().getString(R.string.rightR));
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                }
            } else if (i3 == 4) {
                int i4 = Integer.parseInt(str2.substring(7, 8));
                int i5 = Integer.parseInt(str2.substring(6, 7));
                int i6 = Integer.parseInt(str2.substring(5, 6));
                int i7 = Integer.parseInt(str2.substring(4, 5));
                int i8 = Integer.parseInt(str2.substring(3, 4));
                String str3 = i4 == 1 ? "A" : null;
                if (i5 == 1) {
                    if (str3 == null) {
                        str3 = "B";
                    } else {
                        str3 = str3 + ",B";
                    }
                }
                if (i6 == 1) {
                    if (str3 == null) {
                        str3 = "C";
                    } else {
                        str3 = str3 + ",C";
                    }
                }
                if (i7 == 1) {
                    if (str3 == null) {
                        str3 = "D";
                    } else {
                        str3 = str3 + ",D";
                    }
                }
                if (i8 == 1) {
                    if (str3 == null) {
                        str3 = "E";
                    } else {
                        str3 = str3 + ",E";
                    }
                }
                if (str3 != null) {
                    textView.setText("灯 " + str3);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
            i3++;
            i2 = 0;
        }

    }


}
