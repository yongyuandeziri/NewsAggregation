package cn.news.ziri.newsaggregation.fragment;

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
import cn.news.ziri.newsaggregation.fragment.NewsListFragment;

/**
 * Created by du on 2017/7/19.
 */

public class NewsFragment extends Fragment {
    private TabLayout mTablayout;
    private ViewPager viewpager;
    public static List<String> titles = new ArrayList<>();//方便之后动态添加标签，让用户自定义数据源

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newsfragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mTablayout = (TabLayout) view.findViewById(R.id.tab_layout);
//        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);//挤在一起显示
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);

        titles.add("网易");
        titles.add("凤凰");
        titles.add("无线苏州");
        titles.add("新浪微博");
        titles.add("GITHUB");
        titles.add("CSDN");
        titles.add("DIY社区");
        titles.add("优顾理财");

        setupViewPager(viewpager,titles);

        for(int i=0;i<titles.size();i++){
            mTablayout.addTab(mTablayout.newTab().setText(titles.get(i)));
        }
        mTablayout.setupWithViewPager(viewpager);

    }

    private void setupViewPager(ViewPager viewpager,List<String> titles) {
        MyPagerAdapter adapter=new MyPagerAdapter(getChildFragmentManager());
        for(int i=0;i<titles.size();i++){
            adapter.addFragment(NewsListFragment.newInstance(i),titles.get(i));
        }
        viewpager.setAdapter(adapter);
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
