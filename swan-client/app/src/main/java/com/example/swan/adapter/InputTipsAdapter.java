package com.example.swan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.example.swan.R;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.List;

/**
 * 输入提示adapter，展示item名称和地址
 */
public class InputTipsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Tip> mListTips;

    public InputTipsAdapter(Context context, List<Tip> tipList) {
        mContext = context;
        mListTips = tipList;
    }

    @Override
    public int getCount() {
        if (mListTips != null) {
            return mListTips.size();
        }
        return 0;
    }


    @Override
    public Object getItem(int i) {
        if (mListTips != null) {
            return mListTips.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_input_tips, null);
            holder.name = view.findViewById(R.id.input_tips_name);
            holder.address = view.findViewById(R.id.input_tips_address);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        if (mListTips == null) {
            return view;
        }

        holder.name.setText(mListTips.get(i).getName());
        String address = mListTips.get(i).getAddress();
        if (address == null || address.equals("")) {
            holder.address.setVisibility(View.GONE);
        } else {
            holder.address.setVisibility(View.VISIBLE);
            holder.address.setText(address);
        }

        return view;
    }

    class Holder {
        TextView name;
        TextView address;
    }
}
