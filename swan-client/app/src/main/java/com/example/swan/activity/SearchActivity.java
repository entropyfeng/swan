package com.example.swan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SearchView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.swan.MainActivity;
import com.example.swan.R;
import com.example.swan.adapter.InputTipsAdapter;
import com.example.swan.util.AccountHelper;
import com.example.swan.util.ToastUtil;
import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.example.swan.util.CommonUtil.IsEmptyOrNullString;

public class SearchActivity extends AppCompatActivity {

    private QMUITopBar topBar;
    private SearchView searchView;
    private List<Tip> currentTipList;
    private InputTipsAdapter inputTipsAdapter;
    private ListView inputListView;
    private TextView clearHistoryView;
    private String mCurrentCityName;
    private LatLonPoint latLonPoint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent it = getIntent();
        mCurrentCityName = it.getStringExtra("mCurrentCityName");
        latLonPoint = new LatLonPoint(it.getDoubleExtra("startPointLat", 25.063734), it.getDoubleExtra("startPointLon", 110.300496));
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        init();
    }


    private void init() {

        inputListView = findViewById(R.id.search_list);
        inputListView.setOnItemClickListener(itemClickListener);

        initTopBar();
        initSearchView();
    }

    private void initTopBar() {
        topBar = findViewById(R.id.search_top_bar);

        topBar.addLeftImageButton(R.drawable.ic_icon_back, 0).setOnClickListener(v -> finish());
        QMUIButton searchButton = new QMUIButton(this);
        topBar.addRightTextButton("搜索", searchButton.getId());

    }

    private void initSearchView() {
        searchView = findViewById(R.id.search_search_view);
        clearHistoryView = findViewById(R.id.search_list_clear);
        //修改SearchView的样式
        TextView searchText = (TextView) searchView.findViewById(searchView
                .getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null));
        searchText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        searchText.setTextColor(Color.parseColor("#365146"));
        searchText.setHintTextColor(Color.parseColor("#9e9e9e"));

        searchView.setOnQueryTextListener(queryTextListener);
        //设置SearchView默认为展开显示
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setSubmitButtonEnabled(false);
        initHistory();

    }

    private void initHistory() {
        LinkedList<String> stringList = AccountHelper.getHistorySearch(this);
        LinkedList<Tip> tempTipList = new LinkedList<Tip>();
        stringList.forEach(s -> {
            Tip tip = null;
            try {
                tip = JSON.parseObject(s, Tip.class);
            } catch (Exception e) {
                //如果反序列化失败

                tip = new Tip();
                tip.setName(s);
                tip.setTypeCode("o(╯□╰)o");
            }
            tempTipList.add(tip);
        });

        inputTipsAdapter = new InputTipsAdapter(
                getApplicationContext(),
                tempTipList);
        inputListView.setAdapter(inputTipsAdapter);
        inputTipsAdapter.notifyDataSetChanged();
        inputListView.setOnItemClickListener(itemClickListener);
        clearHistoryView.setOnClickListener(v -> {
            inputListView.setAdapter(null);
            AccountHelper.clearHistorySearch(this);
        });
    }


    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        /**
         * 按下确认键触发，本例为键盘回车或搜索键
         *
         * @param query
         * @return
         */
        @Override
        public boolean onQueryTextSubmit(String query) {
            Intent intent = new Intent();
            intent.putExtra("KeyWord", query);
            setResult(MainActivity.RESULT_CODE_KEYWORDS, intent);
            AccountHelper.writeHistorySearch(SearchActivity.this, query);
            finish();
            return false;
        }

        /**
         * 输入字符变化时触发
         *
         * @param newText
         * @return
         */
        @Override
        public boolean onQueryTextChange(String newText) {

            Log.i("query触发了", newText);
            if (!IsEmptyOrNullString(newText)) {
                InputtipsQuery inputQuery = new InputtipsQuery(newText, mCurrentCityName);
                inputQuery.setLocation(latLonPoint);
                Inputtips inputTips = new Inputtips(SearchActivity.this.getApplicationContext(), inputQuery);
                inputTips.setInputtipsListener(inputTipsListener);
                inputTips.requestInputtipsAsyn();
            } else {
                if (inputTipsAdapter != null && currentTipList != null) {
                    currentTipList.clear();
                    inputTipsAdapter.notifyDataSetChanged();
                }
            }
            return false;
        }
    };

    Inputtips.InputtipsListener inputTipsListener = new Inputtips.InputtipsListener() {
        @Override
        public void onGetInputtips(List<Tip> tipList, int rCode) {
            if (rCode == 1000) {// 正确返回
                currentTipList = tipList;
                List<String> listString = new ArrayList<String>();
                for (int i = 0; i < tipList.size(); i++) {
                    listString.add(tipList.get(i).getName());
                }
                inputTipsAdapter = new InputTipsAdapter(
                        getApplicationContext(),
                        currentTipList);
                inputListView.setAdapter(inputTipsAdapter);
                inputTipsAdapter.notifyDataSetChanged();
            } else {
                ToastUtil.showerror(SearchActivity.this, rCode);
            }
        }
    };
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (currentTipList != null) {
                Tip tip = (Tip) adapterView.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("ExtraTip", tip);
                AccountHelper.writeHistorySearch(SearchActivity.this, JSON.toJSONString(tip));

                setResult(MainActivity.RESULT_CODE_INPUT_TIPS, intent);
                finish();
            } else {

                //在初始化历史纪录时
                Tip tip = (Tip) adapterView.getItemAtPosition(position);
                if (!tip.getTypeCode().equals("o(╯□╰)o")) {
                    Intent intent = new Intent();
                    intent.putExtra("ExtraTip", tip);
                    AccountHelper.writeHistorySearch(SearchActivity.this, tip.getName());
                    setResult(MainActivity.RESULT_CODE_INPUT_TIPS, intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("KeyWord", tip.getName());
                    setResult(MainActivity.RESULT_CODE_KEYWORDS, intent);
                    AccountHelper.writeHistorySearch(SearchActivity.this, tip.getName());
                    finish();
                }

            }

        }
    };
}
