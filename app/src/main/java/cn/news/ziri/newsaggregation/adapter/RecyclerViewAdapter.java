package cn.news.ziri.newsaggregation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.news.ziri.newsaggregation.R;
import cn.news.ziri.newsaggregation.bean.DataBean;
import cn.news.ziri.newsaggregation.utils.ImageLoaderUtils;


/**
 * Created by ward on 2017/7/25.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<String> mTitles=null;
    private static final int TYPE_ITEM=0;
    private static final int TYPE_FOOTER=1;
    private OnItemClickListener mOnItemClickListener;
    private List<DataBean> mData;
    private boolean mShowFooter=true;
    private Context mContext;

    public RecyclerViewAdapter(Context context) {
        this.mContext=context;
    }
    /**设置数据**/
    public void setData(List<DataBean> data){
        this.mData=data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(!mShowFooter){
            return TYPE_ITEM;
        }
        if(position+1==getItemCount()){//到了底部
            return TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }
    }
    public DataBean getItem(int position){
        return mData.get(position);
    }
    //根据传递过来的值 控制是否显示加载更多布局
    public void isShowFooter(boolean b){
        this.mShowFooter=b;
    }
    public boolean isShowFooter(){
        return this.mShowFooter;
    }

    /**
     * item显示类型
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM){
            View news = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent,false);
            ItemViewHolder vh=new ItemViewHolder(news);
            return vh;
        }else {
            View footer = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, null);
            footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            return  new FooterViewHolder(footer);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            DataBean dataBean = mData.get(position);
            ((ItemViewHolder) holder).mTitle.setText(dataBean.getTitle());
            ((ItemViewHolder) holder).mDesc.setText(dataBean.getDigest());
            ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).mNewsImg, dataBean.getImgsrc());
        }
    }

    @Override
    public int getItemCount() {
        int begin=mShowFooter?1:0;
        if(mData==null){
            return begin;
        }else {
            return mData.size()+begin;
        }
    }

    public class ItemViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTitle;
        public TextView mDesc;
        public ImageView mNewsImg;

        public ItemViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.tvTitle);
            mDesc = (TextView) v.findViewById(R.id.tvDesc);
            mNewsImg = (ImageView) v.findViewById(R.id.ivNews);
            v.setOnClickListener(this);//给item注册点击监听 必须写
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(v,this.getPosition());
            }
        }
    }
    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }


    //给RecyclerView注册点击回调接口
    public interface  OnItemClickListener {
        void onItemClick(View view,int position);
    }
    //给外部类调用,实例化后设置引用
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }
}
