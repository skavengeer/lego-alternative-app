package com.dino.smart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dino.smart.R;
import com.dino.smart.model.DevInfo;
import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    private Context context;
    private List<DevInfo> list;

    private LayoutInflater mInflater;

    @Override
    public long getItemId(int i) {
        return i;
    }

    public DeviceAdapter(Context context, List<DevInfo> list) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int i) {
        return this.list.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mInflater.inflate(R.layout.item_device,(ViewGroup) null);
            //inflate(R.layout.item_device, (ViewGroup) null);
        }
        ((TextView) view.findViewById(R.id.devname)).setText(this.list.get(i).getDevName());
        return view;
    }
}
