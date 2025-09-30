package com.dino.smart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dino.smart.R;
import com.dino.smart.model.SmartInfo;
import java.util.List;


public class SmartAdapter extends BaseAdapter {


    private Context mcontext;
    private List<SmartInfo> list;
    private LayoutInflater mInflater;


    @Override
    public long getItemId(int i) {
        return i;
    }

    public SmartAdapter(Context context, List<SmartInfo> list) {
        this.list = list;
        this.mcontext = context;
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
            view = this.mInflater.inflate(R.layout.item_smart,(ViewGroup) null);
            //inflate(R.layout.item_smart, (ViewGroup) null);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.appimg);
        TextView textView = (TextView) view.findViewById(R.id.appname);
        SmartInfo smartInfo = this.list.get(i);
        if (i == 0) {
            textView.setText(this.mcontext.getResources().getString(R.string.add));
            imageView.setImageResource(R.mipmap.smartadd);
        } else {
            textView.setText(smartInfo.getSmartName());
            imageView.setImageResource(R.mipmap.smart04);
        }
        return view;
    }
}
