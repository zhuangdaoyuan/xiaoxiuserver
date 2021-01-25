package com.xiuxiu.confinement_nurse.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;

import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_NEED_LOGIN;

@Route(path = RouterHelper.KEY_NEWSACTIVITY,extras = KEY_NEED_LOGIN)
public class NewsActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, NewsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void loadData() {
    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }
}
