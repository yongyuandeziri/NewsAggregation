package cn.news.ziri.newsaggregation.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

import cn.news.ziri.newsaggregation.R;
import cn.news.ziri.newsaggregation.adapter.CloudTagAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CloudTagFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CloudTagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CloudTagFragment extends Fragment  implements TagCloudView.OnTagClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TagCloudView tcvTags;//标签云对象
    Button SelectButoon;//选择标签
    List<String> list = new ArrayList<>();//标签云数据的集合
    List<String> listClick = new ArrayList<>();//点击过的标签云的数据的集合
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CloudTagFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CloudTagFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CloudTagFragment newInstance(String param1, String param2) {
        CloudTagFragment fragment = new CloudTagFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_cloud_tag, container, false);
        //给集合添加数据
        for (int i = 0; i < 20; i++) {
            list.add("这是标签" + i);
        }

        tcvTags = (TagCloudView) view.findViewById(R.id.tcv_tags);
        SelectButoon=(Button) view.findViewById(R.id.selectbutton);
        SelectButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存数据并且传送数据到“新闻”页面

            }
        });
        //设置标签云的点击事件
        tcvTags.setOnTagClickListener(this);
        //给标签云设置适配器
        CloudTagAdapter adapter = new CloudTagAdapter(list);
        tcvTags.setAdapter(adapter);
        return view;
    }

    /**
     * 点击标签是回调的方法
     */
    @Override
    public void onItemClick(ViewGroup parent, View view, int position) {
        view.setSelected(!view.isSelected());//设置标签的选择状态
        if (view.isSelected()) {
            //加入集合
            listClick.add(list.get(position));
        } else {
            //移除集合
            listClick.remove(list.get(position));
        }
        Snackbar.make(view, "点击过的标签"+ listClick.toString(), Snackbar.LENGTH_SHORT).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
