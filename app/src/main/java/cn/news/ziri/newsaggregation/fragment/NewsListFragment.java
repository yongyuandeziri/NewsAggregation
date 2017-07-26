package cn.news.ziri.newsaggregation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.news.ziri.newsaggregation.R;
import cn.news.ziri.newsaggregation.activity.NewsDetailActivity;
import cn.news.ziri.newsaggregation.adapter.RecyclerViewAdapter;
import cn.news.ziri.newsaggregation.bean.DataBean;
import cn.news.ziri.newsaggregation.commons.Urls;
import cn.news.ziri.newsaggregation.utils.NewsJsonUtils;
import cn.news.ziri.newsaggregation.utils.OkHttpUtils;

/**
 * Created by ward on 2017/7/24.
 */

public class NewsListFragment extends android.support.v4.app.Fragment implements  SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private String newsfrom="";
    private LinearLayoutManager mLayoutManger;
    private int pageIndex=0;
    private RecyclerViewAdapter mAdapter;
    private ArrayList<DataBean> mData;
    private FloatingActionButton fa_firstlist;

    public static NewsListFragment newInstance(String newsfrom) {
        Bundle bundle = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        bundle.putString("newsfrom", newsfrom);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsfrom = getArguments().getString("newsfrom");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newslistfragment, null);
        initView(view);
        onRefresh();
        return view;
    }

    private void initView(View view) {
        fa_firstlist = (FloatingActionButton) view.findViewById(R.id.fa_firstlist);
        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark, R.color.colorAccent,
                R.color.colorAccent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManger = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManger);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecyclerViewAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

        fa_firstlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTop(0);
            }
        });
    }
    //点击滚动到列表的最顶端
    private void gotoTop(int position) {
        mRecyclerView.scrollToPosition(position);
    }

    //列表下拉刷新监听事件
    public RecyclerView.OnScrollListener mOnScrollListener=new RecyclerView.OnScrollListener(){
        private int mLastVisibleItemPosition;//最后一个角标位置
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mLastVisibleItemPosition = mLayoutManger.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //正在滚动
            if(newState==RecyclerView.SCROLL_STATE_IDLE){

                loadData(newsfrom,pageIndex+Urls.PAZE_SIZE);
            }
        }
    };
    //刷新
    @Override
    public void onRefresh() {
        // System.out.println("onRefresh=================");
        pageIndex=0;
        if(null!=mData){
            mData.clear();
        }
        loadData(newsfrom,pageIndex);
    }

    //FirstAdapter点击，跳转到新闻详情界面
    private RecyclerViewAdapter.OnItemClickListener mOnItemClickListener=new RecyclerViewAdapter.OnItemClickListener(){


        @Override
        public void onItemClick(View view, int position) {
            DataBean data = mAdapter.getItem(position);
            System.out.println("点击的数据======" + data.getTitle());
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra("news", data);

            View intoView = view.findViewById(R.id.ivNews);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            intoView, getString(R.string.transition_news_img));
            ActivityCompat.startActivity(getActivity(),intent,options.toBundle());
        }
    };

    private String getUrl(String newsfrom, int page) {
        StringBuilder sb=new StringBuilder();
        switch (newsfrom){
            case "网易":
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case "凤凰":
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case "无线苏州":
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case "新浪微博":
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            case "GITHUB":
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case "CSDN":
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case "DIY社区":
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case "优顾理财":
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(page).append(Urls.END_URL);
        return sb.toString();
    }


    /**
     * 获取ID
     * @param type
     * @return
     */
    private String getID(String type) {
        String id;
        switch (type) {
            case "网易":
                id = Urls.TOP_ID;
                break;
            case "凤凰":
                id = Urls.NBA_ID;
                break;
            case "无线苏州":
                id = Urls.CAR_ID;
                break;
            case "新浪微博":
                id = Urls.JOKE_ID;
                break;
            case "GITHUB":
                id = Urls.TOP_ID;
                break;
            case "CSDN":
                id = Urls.NBA_ID;
                break;
            case "DIY社区":
                id = Urls.CAR_ID;
                break;
            case "优顾理财":
                id = Urls.JOKE_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }




    public void showProgress() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    public void hideProgress() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    public void addData(List<DataBean> mlist) {
        if(null==mData){
            mData = new ArrayList<>();
        }
        mData.addAll(mlist);
        if(pageIndex==0){
            mAdapter.setData(mData);
        }else{
            //没有加载更多的数据时候，，隐藏加载更多的布局
            if(mlist.size()==0||mlist==null){
                mAdapter.isShowFooter(false);
            }
            mAdapter.notifyDataSetChanged();
        }
        pageIndex += Urls.PAZE_SIZE;
    }

    //当没有网络或者加载失败的时候，隐藏进度，自动弹出提示框
    public void showLoadFail() {
        if(pageIndex==0){
            mAdapter.isShowFooter(false);
            mAdapter.notifyDataSetChanged();
        }
        View view = getActivity() == null ? mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        if(view!=null) Snackbar.make(view,"数据加载失败",Snackbar.LENGTH_SHORT).show();
    }

    public void loadData(final Object newsfrom, int page) {
        String  url= getUrl((String)newsfrom,page);
        System.out.println("url========="+url);
        if(page==0){
            this.showProgress();
        }

        OkHttpUtils.ResultCallback<String> loadNewsCallback=new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<DataBean> dataBeans = NewsJsonUtils.readJsonDataBeans(response, getID((String)newsfrom));
                hideProgress();
                addData(dataBeans);
            }

            @Override
            public void onFailure(Exception e) {
                hideProgress();
                showLoadFail();
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }



}
