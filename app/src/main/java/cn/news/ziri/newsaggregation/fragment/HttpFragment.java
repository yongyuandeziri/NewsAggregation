package cn.news.ziri.newsaggregation.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
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

public class HttpFragment extends BaseFragment  implements  SwipeRefreshLayout.OnRefreshListener{
    private  WebView browser;
    private String name;
    private String uri;
    View view;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        name=getArguments().getString("name");
        uri=getArguments().getString("uri");
        view = inflater.inflate(R.layout.githubfragment, null);
        InitView(view);
        LoadData(view);
        return view;
    }

    private void LoadData(View view){
        if(isNetworkConnected(getActivity())==true){
            browser.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            browser.loadUrl(uri);
            showProgress();
        }
        else{
            Logziri.d(getClass()+"LoadData else");
            browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            hideProgress();
            View viewcurrent = getActivity() == null ? view.getRootView() : getActivity().findViewById(R.id.drawer_layout);
            Snackbar.make(viewcurrent, "数据加载失败,请检查网络连接", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void InitView(View view){
        /**下拉刷新**/
        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.http_swipe_refresh_widget);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark, R.color.colorAccent,
                R.color.colorAccent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        //WebView
        browser=(WebView)view.findViewById(R.id.Toweb);
        browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置可自由缩放网页
        browser.getSettings().setSupportZoom(true);
        browser.getSettings().setBuiltInZoomControls(true);//设置支持缩放
        browser.getSettings().setJavaScriptEnabled(true); //设置WebView支持JavaScript
        browser.getSettings().setDefaultTextEncodingName("UTF-8");
        browser.setInitialScale(25);
        browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        browser.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);//设置渲染的优先级
        browser.getSettings().setDomStorageEnabled(true);// 开启 DOM storage API 功能
        browser.getSettings().setDatabaseEnabled(true);//开启 database storage API 功能
        String cacheDirPath = getActivity().getFilesDir().getAbsolutePath() + "/webViewCache"+name;
        Logziri.d(getClass()+" cacheDirPath"+cacheDirPath);
        //设置数据库缓存路径
        browser.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        browser.getSettings().setAppCachePath(cacheDirPath);
        browser.getSettings().setAppCacheEnabled(true);//开启 Application Caches 功能
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getSettings().setAllowFileAccess(true);//设置可以访问文件

        browser.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if(newProgress==100){
//                    pg.setVisibility(View.GONE);//加载完网页进度条消失
//                }
//                else
//                {
//                    pg.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
//                    pg.setProgress(newProgress);//设置进度值
//                }
//            }
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
                    hideProgress();
                    // handler.cancel();   // 默认操作 不处理
                    // handler.handleMessage(null);  // 可做其他处理
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideProgress();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                // super.onPageFinished(view, url);
                hideProgress();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
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

    @Override
    public void onRefresh() {
        LoadData(view);
    }

    public void showProgress() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    public void hideProgress() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    // 网络状态
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        browser.removeAllViews();
        browser.destroy();
    }
}
