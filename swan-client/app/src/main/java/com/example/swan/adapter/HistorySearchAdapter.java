package com.example.swan.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public class HistorySearchAdapter extends BaseAdapter {

    private Context context;
    private LinkedList<String> historyList;
   public HistorySearchAdapter(Context context, LinkedHashSet<String> historySet){

       this.context=context;
       historyList=new LinkedList<>();
       historyList.addAll(historySet);

   }
    @Override
    public int getCount() {
       if(historyList!=null){
           return historyList.size();
       }
       return 0;

    }

    @Override
    public Object getItem(int position) {
       if(historyList!=null){
           return historyList.get(position);
       }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

             view=new TextView(context);
             ((TextView) view).setText(historyList.get(position));
        return view;
    }
}
