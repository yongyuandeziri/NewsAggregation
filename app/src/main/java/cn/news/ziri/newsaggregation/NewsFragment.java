package cn.news.ziri.newsaggregation;

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

import cn.news.ziri.newsaggregation.fragment.NewsListFragment;

/**
 * Created by du on 2017/7/19.
 */

public class NewsFragment extends Fragment {
    private TabLayout mTablayout;
    private ViewPager viewpager;

    public static final int ONE=0;
    public static final int TWO=1;
    public static final int THREE=2;
    public static final int FOUR=3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newsfragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mTablayout = (TabLayout) view.findViewById(R.id.tab_layout);
        //mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);//挤在一起显示
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewpager);
        mTablayout.addTab(mTablayout.newTab().setText("头条"));
        mTablayout.addTab(mTablayout.newTab().setText("NBA"));
        mTablayout.addTab(mTablayout.newTab().setText("汽车"));
        mTablayout.addTab(mTablayout.newTab().setText("笑话"));

        mTablayout.setupWithViewPager(viewpager);

    }

    private void setupViewPager(ViewPager viewpager) {
        MyPagerAdapter adapter=new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(NewsListFragment.newInstance(ONE),"头条");
        adapter.addFragment(NewsListFragment.newInstance(TWO),"NBA");
        adapter.addFragment(NewsListFragment.newInstance(THREE),"汽车");
        adapter.addFragment(NewsListFragment.newInstance(FOUR),"笑话");
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
