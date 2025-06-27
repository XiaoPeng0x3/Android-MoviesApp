package com.example.moveshare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.moveshare.MyApplication;
import com.example.moveshare.R;
import com.example.moveshare.bean.SimpleSubjectBean;
import com.example.moveshare.ui.MovieActivity;
import com.example.moveshare.utils.CelebrityUtil;
import com.example.moveshare.utils.DataUtil;
import com.example.moveshare.utils.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ItemCollectLayoutAdapter extends BaseAdapter {

    private List<SimpleSubjectBean> objects = new ArrayList<SimpleSubjectBean>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemCollectLayoutAdapter(Context context, List<SimpleSubjectBean> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public SimpleSubjectBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_collect_layout, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((SimpleSubjectBean) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(final SimpleSubjectBean sub, ViewHolder holder) {
        //TODO implement

        //if (!isComingFilm) {
        holder.llItemSimpleSubjectRating.setVisibility(View.VISIBLE);
        float rate = (float) sub.getRating().getAverage();
        holder.rbItemSimpleSubjectRating.setRating(rate / 2);
        holder.tvItemSimpleSubjectRating.setText(String.format("%s", rate));
        holder.tvItemSimpleSubjectCount.setText(context.getString(R.string.collect));
        holder.tvItemSimpleSubjectCount.append(String.format("%d", sub.getCollect_count()));
        holder.tvItemSimpleSubjectCount.append(context.getString(R.string.count));
        //}
        String title = sub.getTitle();
        String original_title = sub.getOriginal_title();
        holder.tvItemSimpleSubjectTitle.setText(title);
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra("subject_id", sub.getId());
        intent.putExtra("image_url", sub.getImages().getLarge());
        holder.tvItemSimpleSubjectTitle.setTag(intent);
        holder.tvItemSimpleSubjectTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = (Intent) v.getTag();
                context.startActivity(intent);
            }
        });
        holder.ivItemSimpleSubjectImage.setTag(intent);
        holder.ivItemSimpleSubjectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = (Intent) v.getTag();
                context.startActivity(intent);
            }
        });

        holder.btnDel.setBackgroundColor(Color.TRANSPARENT);
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //通过对话框完善删除操作
                DialogInterface.OnClickListener okListenner = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除操作
                        String mId = sub.getId();
                        DataUtil.delCollectMovie(context,mId);

                        for (int i=0;i<objects.size();i++){
                            SimpleSubjectBean subjectBean_tmp = objects.get(i);
                            if (mId.equals(subjectBean_tmp.getId())){
                                objects.remove(i);
                                break;
                            }
                        }

                        //通知列表数据改变了
                        notifyDataSetChanged();
                        Toast.makeText(context,"删除成功",Toast.LENGTH_LONG).show();
                    }
                };
                //建立对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("您确定要删除吗？");
                builder.setPositiveButton("确定",okListenner);
                builder.setNegativeButton("取消",null);
                builder.show();//对话框弹出

            }
        });


//        if (original_title.equals(title)) {
//            holder.tvItemSimpleSubjectOriginalTitle.setVisibility(View.GONE);
//        } else {
//            holder.tvItemSimpleSubjectOriginalTitle.setText(original_title);
//            holder.tvItemSimpleSubjectOriginalTitle.setVisibility(View.VISIBLE);
//        }
        holder.tvItemSimpleSubjectGenres.setText(StringUtil.getListString(sub.getGenres(), ','));
        holder.tvItemSimpleSubjectDirector.setText(StringUtil.getSpannableString(
                context.getString(R.string.directors), Color.GRAY));
        holder.tvItemSimpleSubjectDirector.append(CelebrityUtil.list2String(sub.getDirectors(), '/'));
        holder.tvItemSimpleSubjectCast.setText(StringUtil.getSpannableString(
                context.getString(R.string.casts), Color.GRAY));
        holder.tvItemSimpleSubjectCast.append(CelebrityUtil.list2String(sub.getCasts(), '/'));
        imageLoader.displayImage(sub.getImages().getLarge(),
                holder.ivItemSimpleSubjectImage, options, imageLoadingListener);
    }

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected DisplayImageOptions options = MyApplication.getLoaderOptions();
    private ImageLoadingListener imageLoadingListener =
            new AnimateFirstDisplayListener();

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }


    protected class ViewHolder {
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
        private ImageButton btnDel;

        public ViewHolder(View view) {
            ivItemSimpleSubjectImage = (ImageView) view.findViewById(R.id.iv_item_simple_subject_image);
            llItemSimpleSubjectRating = (LinearLayout) view.findViewById(R.id.ll_item_simple_subject_rating);
            rbItemSimpleSubjectRating = (RatingBar) view.findViewById(R.id.rb_item_simple_subject_rating);
            tvItemSimpleSubjectRating = (TextView) view.findViewById(R.id.tv_item_simple_subject_rating);
            tvItemSimpleSubjectCount = (TextView) view.findViewById(R.id.tv_item_simple_subject_count);
            tvItemSimpleSubjectTitle = (TextView) view.findViewById(R.id.tv_item_simple_subject_title);
            tvItemSimpleSubjectOriginalTitle = (TextView) view.findViewById(R.id.tv_item_simple_subject_original_title);
            tvItemSimpleSubjectGenres = (TextView) view.findViewById(R.id.tv_item_simple_subject_genres);
            tvItemSimpleSubjectDirector = (TextView) view.findViewById(R.id.tv_item_simple_subject_director);
            tvItemSimpleSubjectCast = (TextView) view.findViewById(R.id.tv_item_simple_subject_cast);
            btnDel = (ImageButton) view.findViewById(R.id.btnDel);
        }
    }
}
