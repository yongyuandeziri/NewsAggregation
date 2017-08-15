package cn.news.ziri.newsaggregation.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
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
import cn.news.ziri.newsaggregation.utils.Logziri;
import cn.news.ziri.newsaggregation.utils.OkHttpUtils;

import static android.content.Context.TELEPHONY_SERVICE;

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

    private String url = Urls.IMAGES_URL;
    private OkHttpUtils.ResultCallback<String> loadNewsCallback=null;
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

        mLayoutManger = new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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
        loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
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
        int returnvalue=isNetworkConnected(getActivity());
        View view = getActivity() == null ? mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        if(returnvalue==-1){ //network not available
            Logziri.d(getClass()+"network not avilable");
            if(view!=null) Snackbar.make(view,"数据加载失败,请检查网络连接",Snackbar.LENGTH_SHORT).show();
        }else if(returnvalue==1){ /*wifi*/
            Logziri.d(getClass()+"network is wifi");
            OkHttpUtils.get(url, loadNewsCallback);
        } else if(returnvalue==2){/*mobile*/
            Logziri.d(getClass()+"network is moblie");
            showNormalDialog();
        }

    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    private void prasedata( List<ThreeDataBean> list){
        if(mData==null){
            mData = new ArrayList<>();
        }
        mData.addAll(list);
        mThreeAdapter.setData(mData);
        if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE ||
                !mRecyclerView.isComputingLayout()) {
            mThreeAdapter.notifyDataSetChanged();
        }
    }


    // 网络状态

    /**
     *
     * @param context
     * @return
     *   -1  network not availible
     *   1   wifi
     *   2   mobile
     */
    public int isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            TelephonyManager mTelephony = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                System.out.println("ward_du 1111111111");
                int netType = mNetworkInfo.getType();
                int netSubtype = mNetworkInfo.getSubtype();
                if (netType == ConnectivityManager.TYPE_WIFI) {  //WIFI
                    if(mNetworkInfo.isAvailable()==true) return 1;
                    else return -1;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) { //MOBILE
                    System.out.println("ward_du 22222222222");
                    if(mNetworkInfo.isAvailable()) return 2;
                    else return -1;
                } else {
                    System.out.println("ward_du 3333333333");
                    return -1;
                }
            }
        }
        return -1;
    }



    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("目前在使用移动网络");
        normalDialog.setMessage("下载图片可能耗费较多流量，是否继续?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        OkHttpUtils.get(url, loadNewsCallback);
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
}
