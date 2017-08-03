package cn.news.ziri.newsaggregation.adapter;

/**
 * Created by ward on 2017/8/3.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.List;

import cn.news.ziri.newsaggregation.R;

/**
 * 标签云页面数据的适配器
 */

public class CloudTagAdapter extends TagsAdapter {


    private List<String> mList;

    public CloudTagAdapter( List<String> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        TextView tv = (TextView) View.inflate(context, R.layout.item_cloudtag, null);
        tv.setText(getItem(position));
        return tv;
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return 1;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
