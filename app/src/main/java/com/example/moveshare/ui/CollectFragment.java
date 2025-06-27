package com.example.moveshare.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.moveshare.MyApplication;
import com.example.moveshare.R;
import com.example.moveshare.adapter.ItemCollectLayoutAdapter;
import com.example.moveshare.bean.SimpleSubjectBean;
import com.example.moveshare.utils.Constant;
import com.example.moveshare.utils.DataUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectFragment extends Fragment {

    private ListView lv1;
    private ItemCollectLayoutAdapter itemCollectLayoutAdapter;

    private List<SimpleSubjectBean> mSimData = new ArrayList<>();

    public CollectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv1 = view.findViewById(R.id.mylistView);
        loadCollectMovies_Net();//加载收藏电影列表
    }

    //加载收藏电影列表
    private void loadCollectMovies_Net(){
        //电影列表API接口
        String mRequestUrl = Constant.API + Constant.IN_THEATERS + "?start=0&count=20";
        //创建volley请求对象
        JsonObjectRequest request = new JsonObjectRequest(mRequestUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int mTotalItem = response.getInt("total");
                            int mCountItem = response.getInt("count");
                            //获取电影列表字符串
                            String moviesString = response.getString("subjects");
                            Gson gson = new Gson();
                            //使用Gson框架转换电影列表
                            List<SimpleSubjectBean> movieList_net = gson.fromJson(moviesString,  new TypeToken<List<SimpleSubjectBean>>() {}.getType());

                            //封装消息，传递给主线程
                            Message message = Message.obtain();
                            message.obj = movieList_net;
                            message.what = 100;//标识线程
                            handler.sendMessage(message);//发送消息给主线程

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MovieListFragment",error.toString());
                        Toast.makeText(CollectFragment.this.getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        MyApplication.addRequest(request, "MovieListFragment");
    }
    //建立一个Handler对象，用于主线程和子线程之间进行通信
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            //msg.what 用于判断从那个线程传递过来的消息
            if(message.what==100){
                List<SimpleSubjectBean> movieAllList = (List<SimpleSubjectBean> )message.obj;
                //获取收藏的电影
                mSimData = DataUtil.getAllCollectMovies(CollectFragment.this.getActivity(),movieAllList);

                itemCollectLayoutAdapter = new ItemCollectLayoutAdapter(CollectFragment.this.getActivity(), mSimData);
                lv1.setAdapter(itemCollectLayoutAdapter);
            }
        }
    };


}
