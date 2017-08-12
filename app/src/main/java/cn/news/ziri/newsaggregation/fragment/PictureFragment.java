package cn.news.ziri.newsaggregation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import cn.news.ziri.newsaggregation.adapter.PictureViewAdapter;
import cn.news.ziri.newsaggregation.bean.ThreeDataBean;
import cn.news.ziri.newsaggregation.commons.Urls;
import cn.news.ziri.newsaggregation.utils.ImageJsonUtils;
import cn.news.ziri.newsaggregation.utils.OkHttpUtils;

/**
 * Created by admin on 2016/8/12.
 */
public class PictureFragment extends Fragment  implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private FloatingActionButton fa_firstlist;
    private LinearLayoutManager mLayoutManger;

    private ArrayList<ThreeDataBean> mData;
    private PictureViewAdapter mThreeAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newslistfragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        fa_firstlist = (FloatingActionButton) view.findViewById(R.id.fa_firstlist);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark, R.color.colorAccent,
                R.color.colorAccent);
        mSwipeRefreshWidget.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManger = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManger);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mThreeAdapter = new PictureViewAdapter(getActivity().getApplicationContext());
        mRecyclerView.setAdapter(mThreeAdapter);

        fa_firstlist.setOnClickListener(this);
        mRecyclerView.addOnScrollListener(OnScrollListener);
        onRefresh();
    }

    public RecyclerView.OnScrollListener OnScrollListener =new RecyclerView.OnScrollListener(){
        private int lastVisibleItem;
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManger.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mThreeAdapter.getItemCount() ) {
                //加载更多
                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "一次只加载20条，查看更多内容请刷新哦", Snackbar.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fa_firstlist:
                mRecyclerView.scrollToPosition(0);
                break;
        }
    }

    public void showImage(List<ThreeDataBean> list) {
        if(mData==null){
            mData = new ArrayList<>();
        }
        mData.addAll(list);
        mThreeAdapter.setData(mData);
    }

    public void showFailure(Exception e, String s) {
        View view = getActivity() == null ? mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        Snackbar.make(view, "数据加载失败!", Snackbar.LENGTH_SHORT).show();
    }

    public void showProgress() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    public void hideProgress() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        System.out.println("刷新==============");
        if(mData!=null){
            mData.clear();
        }
        LoadImageList();
    }

    private void LoadImageList()
    {
        String url = Urls.IMAGES_URL;
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                System.out.print("ward_du"+response);
                List<ThreeDataBean> iamgeBeanList = ImageJsonUtils.readJsonThreeDataBeans(response);
                prasedata(iamgeBeanList);
            }

            @Override
            public void onFailure(Exception e) {
//                listener.onFailure(e,"load image list failure");
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }

    private void prasedata( List<ThreeDataBean> list){
        if(mData==null){
            mData = new ArrayList<>();
        }
        mData.addAll(list);
        mThreeAdapter.setData(mData);
    }
}
