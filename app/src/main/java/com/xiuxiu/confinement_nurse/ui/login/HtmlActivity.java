package com.xiuxiu.confinement_nurse.ui.login;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.core.content.ContextCompat;

import com.monster.gamma.callback.GammaCallback;
import com.monster.gamma.callback.LayoutError;
import com.monster.gamma.callback.LayoutLoading;
import com.monster.gamma.core.Gamma;
import com.monster.gamma.core.LoadService;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by huke on 2020/3/20.
 *
 * @Description:
 */

public class HtmlActivity extends BaseActivity implements GammaCallback.OnReloadListener {

    public static final String KEY_MUSIC_PLAY_LIST_URL = "url";


    private WebView webView;

    private LoadService loadService;

    public static void start(Context context, String msg) {
        Intent starter = new Intent(context, HtmlActivity.class);
        starter.putExtra(KEY_MUSIC_PLAY_LIST_URL, msg);
        context.startActivity(starter);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || TextUtils.isEmpty(getIntent().getStringExtra(KEY_MUSIC_PLAY_LIST_URL))) {
            finish();
            return;
        }
        hookWebView();
        setContentView(R.layout.activity_html);
        loadService = Gamma.getDefault().register(this, this);

        webView = findViewById(R.id.activity_html_webView);

        webView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        webView.setBackgroundResource(android.R.color.transparent);
        //解决芒果嗨盒子不显示二维码的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("enable();", null);
        } else {
            webView.loadUrl("javascript:enable();");
        }
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(false);//让webView失去焦点防止按下键  网页背景出来

        WebSettings webSettings = webView.getSettings();
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setJavaScriptEnabled(true);//支持js必须设置
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webView.setVerticalScrollBarEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET); //取消滚动条白边效果
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);//设定支持缩放
        webSettings.setAllowFileAccess(true);//暂时改为false

//        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);


        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(getIntent().getStringExtra(KEY_MUSIC_PLAY_LIST_URL));

    }


    @Override
    protected void onDestroy() {
        unregisterWebView();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 防止 WebView 内存泄漏;
     */
    private void unregisterWebView() {
        if (webView != null) {
            if (webView.getParent() != null) {
                ((ViewGroup) webView.getParent()).removeView(webView);
            }
            webView.stopLoading();
            webView.clearHistory();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            if (webView.getChildCount() > 0) {
                webView.removeAllViews();
            }
            webView.destroy();
        }
    }

    @Override
    public void onReload(View v) {
        loadService.showCallback(LayoutLoading.class);
        webView.loadUrl(getIntent().getStringExtra(KEY_MUSIC_PLAY_LIST_URL));
    }

    public void onBack(View view) {
        finish();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ViewHelper.showView(view);

//            cancelSpecialLoadingDialog();
//            cancelLoadingDialog();

            loadService.showSuccess();

            webView.requestFocus();
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            view.loadUrl("javascript:window.java_obj.getSource('<head>'+"
                    + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // 接受所有证书

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return;
            }
            // 在这里显示自定义错误页

            if (webView.getParent() != null) {
//                showNetErrorView(true, rootView);
//                cancelSpecialLoadingDialog();
                unregisterWebView();
            }

            loadService.showCallback(LayoutError.class);
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);


            if (request.isForMainFrame()) { // 或者： if(request.getUrl().toString() .equals(getUrl()))
                // 在这里显示自定义错误页

                if (webView.getParent() != null) {
//                showNetErrorView(true, rootView);
//                cancelSpecialLoadingDialog();
                    unregisterWebView();
                }

                loadService.showCallback(LayoutError.class);
            }
        }
    }

    @SuppressLint("PrivateApi")
    public static void hookWebView() {
        int sdkInt = Build.VERSION.SDK_INT;
        try {
            Class<?> factoryClass = Class.forName("android.webkit.WebViewFactory");
            Field field = factoryClass.getDeclaredField("sProviderInstance");
            field.setAccessible(true);
            Object sProviderInstance = field.get(null);
            if (sProviderInstance != null) {
                return;
            }
            Method getProviderClassMethod;
            if (sdkInt > 22) { // above 22
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass");
            } else if (sdkInt == 22) { // method name is a little different
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass");
            } else { // no security check below 22
                return;
            }
            getProviderClassMethod.setAccessible(true);
            Class<?> providerClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
            Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
            Constructor<?> providerConstructor = providerClass.getConstructor(delegateClass);
            if (providerConstructor != null) {
                providerConstructor.setAccessible(true);
                Constructor<?> declaredConstructor = delegateClass.getDeclaredConstructor();
                declaredConstructor.setAccessible(true);
                sProviderInstance = providerConstructor.newInstance(declaredConstructor.newInstance());
                field.set("sProviderInstance", sProviderInstance);
            }
        } catch (Throwable ignored) {
        }
    }
}
