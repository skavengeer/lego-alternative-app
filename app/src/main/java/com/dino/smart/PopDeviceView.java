package com.dino.smart;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.dino.smart.action.OnPopClickListener;
import com.dino.smart.adapter.DeviceAdapter;
import com.dino.smart.model.DevInfo;
import java.util.ArrayList;
import java.util.List;

public class PopDeviceView extends PopupWindow {

    private View conentView;
    private Context contexts;
    DeviceAdapter deviceAdapter;
    List<DevInfo> dlist = new ArrayList();
    GridView gridView;
    public OnPopClickListener onPopClickListener;
    TextView searchBleBtn;

    public void refList() {
    }

    public PopDeviceView(Context context) {
        this.contexts = context;

        //***********************************
        View viewInflate = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pop_device_view,(ViewGroup) null);

        this.conentView = viewInflate;
        setContentView(viewInflate);
        setHeight(-1);
        setWidth(-1);
        setFocusable(true);
        setOutsideTouchable(true);
        update();
        setBackgroundDrawable(new ColorDrawable(0));
        this.conentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopDeviceView.this.dismiss();
            }
        });
        this.gridView = (GridView) this.conentView.findViewById(R.id.gridView);
        DeviceAdapter deviceAdapter = new DeviceAdapter(context, this.dlist);
        this.deviceAdapter = deviceAdapter;
        this.gridView.setAdapter((ListAdapter) deviceAdapter);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                PopDeviceView.this.onPopClickListener.onClick(i);
                PopDeviceView.this.dismiss();
            }
        });
    }

    public void showPopupWindow(View view, OnPopClickListener onPopClickListener) {
        this.onPopClickListener = onPopClickListener;
        if (!isShowing()) {
            view.getLocationInWindow(new int[2]);
            showAtLocation(view, 0, 0, 0);
        } else {
            dismiss();
        }
    }
}
