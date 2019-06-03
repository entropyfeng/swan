package com.example.swan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.swan.R;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

public class MainTitleView extends LinearLayout {
    private QMUIRadiusImageView imageView;
    private TextView textView;
    private static final String DEFAULT_LABEL_CONTENT="查找地点、公交、地铁";
    public MainTitleView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.widget_main_title_view,this);
        imageView=findViewById(R.id.main_title_view_image);
        textView=findViewById(R.id.main_title_view_text_view);
        textView.setText(DEFAULT_LABEL_CONTENT);
        textView.setTextSize(20);
        textView.setCursorVisible(false);
        imageView.setCircle(true);
    }
    public void setImageListener(OnClickListener onClickListener){
        imageView.setOnClickListener(onClickListener);
    }
    public void setTextViewListener(OnClickListener onClickListener){
        textView.setOnClickListener(onClickListener);
    }
    public TextView getTextView(){
        return textView;
    }
}
