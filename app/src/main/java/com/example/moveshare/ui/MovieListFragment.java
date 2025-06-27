package com.example.moveshare.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.moveshare.MyApplication;
import com.example.moveshare.R;
import com.example.moveshare.adapter.ItemMovieListAdapter;
import com.example.moveshare.bean.Movie;
import com.example.moveshare.bean.SimpleSubjectBean;
import com.example.moveshare.listener.MyOnItemClickListener;
import com.example.moveshare.utils.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class MovieListFragment extends Fragment {

    private SwipeRefreshLayout swipeRf;//刷新框架
    private RecyclerView myRecyclerView;
    private List<SimpleSubjectBean> movieList = new ArrayList<SimpleSubjectBean>();//电影列表
    private ItemMovieListAdapter itemMovieListAdapter;//适配器

    private int start = 0;//默认从0开始获取数据
    private int count = 5;//每次显示的记录条数
    private Movie movie;//电影对象

    public MovieListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化
        swipeRf = (SwipeRefreshLayout) view.findViewById(R.id.swipeRf1);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.myRecyclerView1);
        //创建适配器
        itemMovieListAdapter= new ItemMovieListAdapter(movieList,MovieListFragment.this.getActivity());
        LinearLayoutManager llm = new LinearLayoutManager(MovieListFragment.this.getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        myRecyclerView.setLayoutManager(llm);
        myRecyclerView.setAdapter(itemMovieListAdapter);
        //设置刷新监听
        swipeRf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                itemMovieListAdapter.upDown=0;
                loadMovies_Net();//加载电影
            }
        });
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if((newState==recyclerView.SCROLL_STATE_IDLE)
                        &&(lastVisibleItem+2>itemMovieListAdapter.getItemCount())
                        &&(itemMovieListAdapter.getItemCount()-1<movie.getTotal())){
                    itemMovieListAdapter.upDown=1;
                    loadMovies_Net();//加载电影
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                lastVisibleItem = lm.findLastVisibleItemPosition();
            }
        } );


        //设置单击回调
        itemMovieListAdapter.setmOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SimpleSubjectBean subjects = movieList.get(position);
                //打开电影详细
                Intent intent = new Intent(MovieListFragment.this.getActivity(), MovieActivity.class);
                intent.putExtra("subject_id",subjects.getId());
                intent.putExtra("image_url", subjects.getImages().getLarge());
                startActivity(intent);//打开新的activity
            }
        });
        loadMovies_Net();//加载电影
    }

    //网络加载数据
    private void loadMovies_Net(){
        //电影列表API接口
        String mRequestUrl = Constant.API + Constant.IN_THEATERS + "?start="+start+"&count="+count;
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
                            //构建返回的电影列表对象
                            Movie movie_net = new Movie();
                            movie_net.setStart(start);
                            movie_net.setCount(mCountItem);
                            movie_net.setTotal(mTotalItem);
                            movie_net.setSubjects(movieList_net);
                            //封装消息，传递给主线程
                            Message message = Message.obtain();
                            message.obj = movie_net;
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
                        Toast.makeText(MovieListFragment.this.getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
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
                movie = (Movie)message.obj;
                itemMovieListAdapter.upDown=1;
                start = movie.getStart()+movie.getCount();
                itemMovieListAdapter.loadData(movie.getSubjects());
            }
        }
    };
}
