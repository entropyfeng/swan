package com.example.swan.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.swan.R;
import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.widget.QMUITopBar;

public class SearchActivity extends AppCompatActivity {

    private QMUITopBar topBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        initTopBar();
    }

    private void initTopBar(){
        topBar = findViewById(R.id.search_top_bar);
        topBar.addLeftBackImageButton();

        QMUIButton backButton=new QMUIButton(topBar.getContext());
        topBar.addLeftImageButton(R.drawable.ic_icon_back,backButton.getId());


        ImageView imageView=new ImageView(this);
        imageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_icon_search));

        topBar.addLeftView(imageView,imageView.getId());
        EditText editText=new EditText(topBar.getContext());
        topBar.addLeftView(editText,editText.getId());



        QMUIButton searchButton = new QMUIButton(this);
        topBar.addRightTextButton("搜索", searchButton.getId());

    }
}
