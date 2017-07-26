package cn.news.ziri.newsaggregation.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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

/**
 * Created by ward on 17-7-26.
 */

public class NewsDetailActivity extends AppCompatActivity {

    private ImageView ivImage;
    private CollapsingToolbarLayout collapsing_toolbar;
    private ProgressBar progress;
    private HtmlTextView htNewsContent;
    private DataBean mData;//详情数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsdetailactivity);

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
        collapsing_toolbar.setTitle(mData.getTitle());
        ImageLoaderUtils.display(getApplicationContext(), (ImageView) findViewById(R.id.ivImage), mData.getImgsrc());

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

}
