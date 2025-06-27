package com.example.moveshare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.moveshare.MyApplication;
import com.example.moveshare.R;
import com.example.moveshare.bean.SimpleSubjectBean;
import com.example.moveshare.listener.MyOnItemClickListener;
import com.example.moveshare.utils.CelebrityUtil;
import com.example.moveshare.utils.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ItemNewMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SimpleSubjectBean> movieList = null;
    private Context context;
    private LayoutInflater layoutInflater;


    public int upDown=0;//下拉标志，定义当前下拉状态，默认不是下拉
    public int totalSize=0;

    FootViewHolder fvh;

    //创建点击回调接口
    private MyOnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(MyOnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ItemNewMoviesAdapter(List<SimpleSubjectBean> movieList, Context context) {
        this.context = context;
        this.movieList = movieList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    //加载更多数据
    public void loadData(List<SimpleSubjectBean> moreList){

        if (this.upDown==0){
            moreList.addAll(this.movieList);//包含之前的数据
            this.movieList = moreList;
        }else {
            this.movieList.addAll(moreList);//加入新数据
        }

//        this.ss.addAll(ss);//加入新数据
        this.notifyDataSetChanged();//通知数据更新
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==0) {
            View v = layoutInflater.inflate(R.layout.item_newmovies_layout, parent, false);
            return new ItemViewHolder(v);
        }else {
            View v = layoutInflater.inflate(R.layout.foot_load_tips, parent, false);
            fvh = new FootViewHolder(v);
            return fvh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position)==0) {
            ((ItemViewHolder) holder).update();

            //设置点击回调
            if (mOnItemClickListener !=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick( holder.itemView,position);
                    }
                });
            }


        }else {
            ((FootViewHolder) holder).update();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position<movieList.size()){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size()+1;
    }

    public void fail(){
        fvh.fail();
    }

    class FootViewHolder extends RecyclerView.ViewHolder{
        private ProgressBar pbViewLoadTip;
        private TextView tvViewLoadTip;
        public FootViewHolder(View itemView) {
            super(itemView);
            pbViewLoadTip = (ProgressBar) itemView.findViewById(R.id.pb_view_load_tip);
            tvViewLoadTip = (TextView) itemView.findViewById(R.id.tv_view_load_tip);

        }
        public void update(){
            if(movieList.size()>totalSize){
                pbViewLoadTip.setVisibility(View.VISIBLE);
                tvViewLoadTip.setText("加载更多..");
            }else if (movieList.size()==totalSize){
                pbViewLoadTip.setVisibility(View.GONE);
                tvViewLoadTip.setText("到底了");
            }else{
                pbViewLoadTip.setVisibility(View.GONE);
                tvViewLoadTip.setText("");
            }
            totalSize+=movieList.size();//记录已经显示了几条数据
        }
        public void fail(){
            pbViewLoadTip.setVisibility(View.GONE);
            tvViewLoadTip.setText("加载失败");
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemSimpleSubjectImage;
        private LinearLayout llItemSimpleSubjectRating;
        private RatingBar rbItemSimpleSubjectRating;
        private TextView tvItemSimpleSubjectRating;
        private TextView tvItemSimpleSubjectCount;
        private TextView tvItemSimpleSubjectTitle;
        private TextView tvItemSimpleSubjectOriginalTitle;
        private TextView tvItemSimpleSubjectGenres;
        private TextView tvItemSimpleSubjectDirector;
        private TextView tvItemSimpleSubjectCast;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivItemSimpleSubjectImage = (ImageView) itemView.findViewById(R.id.iv_item_simple_subject_image);
            llItemSimpleSubjectRating = (LinearLayout) itemView.findViewById(R.id.ll_item_simple_subject_rating);
            rbItemSimpleSubjectRating = (RatingBar) itemView.findViewById(R.id.rb_item_simple_subject_rating);
            tvItemSimpleSubjectRating = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_rating);
            tvItemSimpleSubjectCount = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_count);
            tvItemSimpleSubjectTitle = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_title);
            tvItemSimpleSubjectOriginalTitle = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_original_title);
            tvItemSimpleSubjectGenres = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_genres);
            tvItemSimpleSubjectDirector = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_director);
            tvItemSimpleSubjectCast = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_cast);
        }

        public void update() {
            int position = this.getLayoutPosition();
            SimpleSubjectBean subject = movieList.get(position);
            tvItemSimpleSubjectTitle.setText(subject.getTitle());

            float rate = (float) subject.getRating().getAverage();
            rbItemSimpleSubjectRating.setRating(rate / 2);
            tvItemSimpleSubjectRating.setText(String.format("%s", rate));
            tvItemSimpleSubjectCount.setText(context.getString(R.string.collect));
            tvItemSimpleSubjectCount.append(String.format("%d", subject.getCollect_count()));
            tvItemSimpleSubjectCount.append(context.getString(R.string.count));

            tvItemSimpleSubjectOriginalTitle.setText(subject.getOriginal_title());


            tvItemSimpleSubjectGenres.setText(StringUtil.getListString(subject.getGenres(), ','));
            tvItemSimpleSubjectDirector.setText(StringUtil.getSpannableString(
                    context.getString(R.string.directors), Color.GRAY));
            tvItemSimpleSubjectDirector.append(CelebrityUtil.list2String(subject.getDirectors(), '/'));
            tvItemSimpleSubjectCast.setText(StringUtil.getSpannableString(
                    context.getString(R.string.casts), Color.GRAY));
            tvItemSimpleSubjectCast.append(CelebrityUtil.list2String(subject.getCasts(), '/'));

            String image_url = subject.getImages().getLarge();
            ImageLoader.getInstance().displayImage(image_url,ivItemSimpleSubjectImage, MyApplication.getLoaderOptions());

        }

        public SpannableString getSpannableString(String str, int color) {
            SpannableString span = new SpannableString(str);
            span.setSpan(new ForegroundColorSpan(
                    color), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        }
    }
}
