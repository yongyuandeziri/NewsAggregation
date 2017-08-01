package cn.news.ziri.newsaggregation.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import cn.news.ziri.newsaggregation.R;
import cn.news.ziri.newsaggregation.bean.DataBean;
import cn.news.ziri.newsaggregation.bean.DataDetilBean;
import cn.news.ziri.newsaggregation.commons.Urls;
import cn.news.ziri.newsaggregation.utils.ImageLoaderUtils;
import cn.news.ziri.newsaggregation.utils.NewsJsonUtils;
import cn.news.ziri.newsaggregation.utils.OkHttpUtils;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by ward on 17-7-26.
 */

public class NewsDetailActivity extends AppCompatActivity {

    private ImageView ivImage;
    private CollapsingToolbarLayout collapsing_toolbar;
    private ProgressBar progress;
    private HtmlTextView htNewsContent;
    private DataBean mData;//详情数据
    private FloatingActionButton sharebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsdetailactivity);

        sharebutton = (FloatingActionButton)findViewById(R.id.news_share);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        ivImage = (ImageView) findViewById(R.id.ivImage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        progress = (ProgressBar) findViewById(R.id.progress);
        htNewsContent = (HtmlTextView) findViewById(R.id.htNewsContent);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setSupportActionBar(toolbar);
        // 给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //通过 NavigationDrawer 打开关闭 抽屉---返回
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();//返回上一级
            }
        });

        mData = (DataBean) getIntent().getSerializableExtra("news");
        collapsing_toolbar.setTitle(mData.getTitle());//设置折叠状态栏的标题，有缩放的效果
        String url="http://www.201744pic.pw/uploadfile/2017/0709/20/19.jpg";
        ImageLoaderUtils.display(getApplicationContext(), (ImageView) findViewById(R.id.ivImage), mData.getImgsrc());//通过url加载图片
//        ImageLoaderUtils.display(getApplicationContext(), (ImageView) findViewById(R.id.ivImage), url);
        loaddata(mData);
    }

    private void loaddata(final DataBean mData)
    {
        //加载数据
        showProgress();
        String detailUrl = getDetailUrl(mData.getDocid());
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                DataDetilBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response, mData.getDocid());
                if(newsDetailBean!=null){
                    htNewsContent.setHtmlFromString(newsDetailBean.getBody(), new HtmlTextView.LocalImageGetter());
                }
                hideprogress();
            }

            @Override
            public void onFailure(Exception e) {
                hideprogress();
//                mfirstDetilView.showFailure(e);
            }
        };
        OkHttpUtils.get(detailUrl, loadNewsCallback);
    }
    private String getDetailUrl(String docId) {
        StringBuffer sb = new StringBuffer(Urls.NEW_DETAIL);
        sb.append(docId).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }

    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideprogress() {
        progress.setVisibility(View.GONE);
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("NewsAggration");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(mData.getImgsrc());
        // 启动分享GUI
        oks.show(this);
    }
}
