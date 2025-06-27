package com.example.moveshare.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.example.moveshare.MyApplication;
import com.example.moveshare.R;
import com.example.moveshare.adapter.ItemSimpleCastLayoutAdapter;
import com.example.moveshare.bean.SimpleSubjectBean;
import com.example.moveshare.bean.SubjectBean;
import com.example.moveshare.utils.Constant;
import com.example.moveshare.utils.DataUtil;
import com.example.moveshare.utils.StringUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;


public class MovieActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivSubjImages;
    private LinearLayout introduceContainerSubj;
    private RatingBar rbSubjRating;
    private TextView tvSubjRating;
    private TextView tvSubjCollectCount;
    private TextView tvSubjTitle;
    private TextView tvSubjOriginalTitle;
    private TextView tvSubjGenres;
    private TextView tvSubjAke;
    private TextView tvSubjCountries;
    private TextView tvSubjSummary;
    private GridView mygridView;
    private ImageButton btnCollect;
    private ImageButton btnShare;

    private String mId;
    String imageUri;
    private SubjectBean mSubject;
    private String mContent;




    //收藏列表
    List<SimpleSubjectBean> movieCollectList = new ArrayList<SimpleSubjectBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_movie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.navtoolbar);
        toolbar.setTitle("电影详细");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用


        initView();
        mId = getIntent().getStringExtra("subject_id");
        imageUri = getIntent().getStringExtra("image_url");
        loadMovie_net();//加载电影详情
    }

    //初始化电影详情页面控件
    public void initView(){
        ivSubjImages = (ImageView) findViewById(R.id.iv_subj_images);
        introduceContainerSubj = (LinearLayout) findViewById(R.id.introduce_container_subj);
        rbSubjRating = (RatingBar) findViewById(R.id.rb_subj_rating);
        tvSubjRating = (TextView) findViewById(R.id.tv_subj_rating);
        tvSubjCollectCount = (TextView) findViewById(R.id.tv_subj_collect_count);
        tvSubjTitle = (TextView) findViewById(R.id.tv_subj_title);
        tvSubjOriginalTitle = (TextView) findViewById(R.id.tv_subj_original_title);
        tvSubjGenres = (TextView) findViewById(R.id.tv_subj_genres);
        tvSubjAke = (TextView) findViewById(R.id.tv_subj_ake);
        tvSubjCountries = (TextView) findViewById(R.id.tv_subj_countries);
        tvSubjSummary = (TextView) findViewById(R.id.tv_subj_summary);
        mygridView = (GridView) findViewById(R.id.mygridView);

        btnCollect = (ImageButton) findViewById(R.id.btnCollect);
        btnShare = (ImageButton) findViewById(R.id.btnShare);
        btnCollect.setBackgroundColor(Color.TRANSPARENT);
        btnShare.setBackgroundColor(Color.TRANSPARENT);
        btnCollect.setOnClickListener(this);
        btnShare.setOnClickListener(this);

    }


    //加载电影详情数据
    private void loadMovie_net() {

        String mRequestUrl = Constant.API + Constant.SUBJECT + mId;

        Log.d("mRequestUrl", mRequestUrl);
        StringRequest request = new StringRequest(mRequestUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        mContent = response;
                        Gson gson = new Gson();
                        mSubject = gson.fromJson(mContent,SubjectBean.class);
                        initAfterGetData();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MovieActivity.this, error.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        MyApplication.addRequest(request, "MovieActivity");
    }


    /**
     * 得到网络返回数据初始化界面
     */
    private void initAfterGetData() {
        if (mSubject == null) return;

        ImageLoader.getInstance().displayImage(imageUri,
                ivSubjImages, MyApplication.getLoaderOptions());
        if (mSubject.getRating() != null) {
            float rate = (float) (mSubject.getRating().getAverage() / 2);
            rbSubjRating.setRating(rate);
            tvSubjRating.setText(String.format("%s", rate * 2));
        }

        tvSubjCollectCount.setText(getString(R.string.collect));
        tvSubjCollectCount.append(String.format("%s", mSubject.getCollect_count()));
        tvSubjCollectCount.append(getString(R.string.count));
        tvSubjTitle.setText(String.format("%s   ", mSubject.getTitle()));
        tvSubjTitle.append(StringUtil.getSpannableString1(
                String.format("  %s  ", mSubject.getYear()),
                new ForegroundColorSpan(Color.WHITE),
                new BackgroundColorSpan(Color.parseColor("#5ea4ff")),
                new RelativeSizeSpan(0.88f)));

        if (!mSubject.getOriginal_title().equals(mSubject.getTitle())) {
            tvSubjOriginalTitle.setText(mSubject.getOriginal_title());
            tvSubjOriginalTitle.setVisibility(View.VISIBLE);
        } else {
            tvSubjOriginalTitle.setVisibility(View.GONE);
        }
        tvSubjGenres.setText(StringUtil.getListString(mSubject.getGenres(), ','));
        tvSubjAke.setText(StringUtil.getSpannableString(
                getString(R.string.ake), Color.GRAY));
        tvSubjAke.append(StringUtil.getListString(mSubject.getAka(), '/'));
        tvSubjCountries.setText(StringUtil.getSpannableString(
                getString(R.string.countries), Color.GRAY));
        tvSubjCountries.append(StringUtil.getListString(mSubject.getCountries(), '/'));

        tvSubjSummary.setText(StringUtil.getSpannableString(
                getString(R.string.summary), Color.parseColor("#5ea4ff")));
        tvSubjSummary.append(mSubject.getSummary());
        tvSubjSummary.setEllipsize(TextUtils.TruncateAt.END);
        //获得导演演员数据列表
        mygridView.setAdapter(new ItemSimpleCastLayoutAdapter(this,mSubject.getCasts()));
        //显示View并配上动画
        introduceContainerSubj.setAlpha(0f);
        introduceContainerSubj.setVisibility(View.VISIBLE);
        introduceContainerSubj.animate().alpha(1f).setDuration(800);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnCollect){
            boolean flag = DataUtil.collectMovie(MovieActivity.this,mId);
            Toast.makeText(this,"收藏成功",Toast.LENGTH_LONG).show();
        }else if (v.getId()==R.id.btnShare){
            // 先不分享
            // showShare();
            Toast.makeText(this,"分享成功",Toast.LENGTH_LONG).show();
        }
    }

    //导航，返回
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private static final int REQUEST_FILE_SELECT_CODE = 100;


    /**
     * 打开文件管理选择文件
     */
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Choose File"), REQUEST_FILE_SELECT_CODE);
        } catch (Exception ex) {
            // not install file manager.
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE_SELECT_CODE && resultCode == RESULT_OK) {
            // 获取到的系统返回的 Uri（图片）
            Uri shareFileUrl = data.getData();
            DataUtil.shareImage(MovieActivity.this,shareFileUrl);
        }
    }


}
