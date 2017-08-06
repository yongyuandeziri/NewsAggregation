package cn.news.ziri.newsaggregation.fragment;

import android.net.http.SslError;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cn.news.ziri.newsaggregation.R;
import cn.news.ziri.newsaggregation.activity.MainActivity;
import cn.news.ziri.newsaggregation.utils.Logziri;

/**
 * Created by ward on 2017/7/29.
 */

public class GitHubFragment extends BaseFragment{
    private  WebView browser;
    private ProgressBar pg;
    private String name;
    private String uri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        name=getArguments().getString("name");
        uri=getArguments().getString("uri");
        View view = inflater.inflate(R.layout.githubfragment, null);
        InitView(view);
        return view;
    }

    private void InitView(View view){
        //WebView
        pg=(ProgressBar)view.findViewById(R.id.progressBar1);
        browser=(WebView)view.findViewById(R.id.Toweb);
        browser.loadUrl(uri);
        //设置可自由缩放网页
        browser.getSettings().setSupportZoom(true);
        browser.getSettings().setBuiltInZoomControls(true);
        browser.getSettings().setJavaScriptEnabled(true);

//        browser.setInitialScale(25);
//        browser.getSettings().setUseWideViewPort(true);
//        browser.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        browser.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    pg.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else
                {
                    pg.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg.setProgress(newProgress);//设置进度值
                }
            }
        });

        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
        browser.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();  // 接受信任所有网站的证书
                    // handler.cancel();   // 默认操作 不处理
                    // handler.handleMessage(null);  // 可做其他处理
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode) {
        // Check if the key event was the Back button and if there's history
        Logziri.d(getClass()+"onkeydown");
        if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {
            browser.goBack();
            return true;
        }
        return false;
    }

}
