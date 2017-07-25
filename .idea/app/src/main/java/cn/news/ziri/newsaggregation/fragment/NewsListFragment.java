package cn.news.ziri.newsaggregation.fragment;

import android.app.Fragment;
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

import cn.news.ziri.newsaggregation.NewsFragment;
import cn.news.ziri.newsaggregation.R;
import cn.news.ziri.newsaggregation.bean.DataBean;

/**
 * Created by ward on 2017/7/24.
 */

public class NewsListFragment extends android.support.v4.app.Fragment implements  SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private int type= NewsFragment.ONE;
    private LinearLayoutManager mLayoutManger;
    private int pageIndex=0;

    private ArrayList<DataBean> mData;
    private FloatingActionButton fa_firstlist;

    public static NewsListFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        firstPresenter = new FirstFragmentImpl(this);
        type = getArguments().getInt("type");
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

//                firstPresenter.loadData(type,pageIndex+Urls.PAZE_SIZE);
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
//        firstPresenter.loadData(type,pageIndex);
    }

    //FirstAdapter点击，跳转到新闻详情界面
//    private FirstAdapter.OnItemClickListener mOnItemClickListener=new FirstAdapter.OnItemClickListener(){
//
//
//        @Override
//        public void onItemClick(View view, int position) {
//            DataBean data = mAdapter.getItem(position);
//            System.out.println("点击的数据======" + data.getTitle());
//            Intent intent = new Intent(getActivity(), FirstDetilActivity.class);
//            intent.putExtra("news", data);
//
//            View intoView = view.findViewById(R.id.ivNews);
//            ActivityOptionsCompat options =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                            intoView, getString(R.string.transition_news_img));
//            ActivityCompat.startActivity(getActivity(),intent,options.toBundle());
//        }
//    };
}
