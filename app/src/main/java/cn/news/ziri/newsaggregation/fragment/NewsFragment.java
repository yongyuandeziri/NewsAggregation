package cn.news.ziri.newsaggregation.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.news.ziri.newsaggregation.R;
import cn.news.ziri.newsaggregation.sqlite.NewsSourceSQLiteOpenHelper;
import cn.news.ziri.newsaggregation.utils.Logziri;
import cn.news.ziri.newsaggregation.utils.NoScrollViewPager;

/**
 * Created by du on 2017/7/19.
 */

public class NewsFragment extends BaseFragment {
    private TabLayout mTablayout;
    private NoScrollViewPager viewpager;
    private Cursor newssource;
    public static List<String> names = new ArrayList<>();
    public static List<String> uris = new ArrayList<>();
    private  MyPagerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newsfragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        NewsSourceSQLiteOpenHelper newsourcedb=new NewsSourceSQLiteOpenHelper(getActivity(),"newssource.db",null,1);
        SQLiteDatabase Newssource=newsourcedb.getWritableDatabase();
        Cursor ns=Newssource.rawQuery("select * from newssource where isselected=1",null);

        mTablayout = (TabLayout) view.findViewById(R.id.tab_layout);
//        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);//挤在一起显示
        viewpager = (NoScrollViewPager) view.findViewById(R.id.viewpager);
        names.clear();
        uris.clear();

        while(ns.moveToNext()){
            String name=ns.getString(ns.getColumnIndex("name"));
            String uri=ns.getString(ns.getColumnIndex("uri"));
            names.add(name);
            uris.add(uri);
            mTablayout.addTab(mTablayout.newTab().setText(name));
            Logziri.d("name:"+name+"uri:"+uri);
        }
        setupViewPager(viewpager,names,uris);
        mTablayout.setupWithViewPager(viewpager);
        ns.close();
        Newssource.close();

    }

    private void setupViewPager(ViewPager viewpager,List<String> names,List<String> uris) {
        adapter=new MyPagerAdapter(getChildFragmentManager());
        for(int i=0;i<uris.size();i++){
            if(uris.get(i).equals("")){
                adapter.addFragment(NewsListFragment.newInstance(names.get(i)),names.get(i));
            }
            else{
                HttpFragment temp =new HttpFragment();
                Bundle bundle=new Bundle();
                bundle.putString("name",names.get(i));
                bundle.putString("uri",uris.get(i));
                temp.setArguments(bundle);
                adapter.addFragment(temp,names.get(i));
            }
        }
        viewpager.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode) {
        BaseFragment baseFragment = (BaseFragment) adapter.mFragment.get(viewpager.getCurrentItem());
        baseFragment.onKeyDown(keyCode);
        Logziri.d(getClass()+"getcurrentItem():"+viewpager.getCurrentItem());
        return true;
    }


    public  static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragment=new ArrayList<Fragment>();
        private final List<String> mFragmentTitle=new ArrayList<String>();

        public void addFragment(Fragment  fragment,String title){
            mFragment.add(fragment);
            mFragmentTitle.add(title);
        }
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitle.get(position);
        }
    }




}
