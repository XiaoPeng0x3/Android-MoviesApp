package com.example.moveshare.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moveshare.MyApplication;
import com.example.moveshare.R;
import com.example.moveshare.bean.CelebrityEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ItemSimpleCastLayoutAdapter extends BaseAdapter {

    private List<CelebrityEntity> objects = new ArrayList<CelebrityEntity>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemSimpleCastLayoutAdapter(Context context, List<CelebrityEntity> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public CelebrityEntity getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_simple_cast_layout, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((CelebrityEntity)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(CelebrityEntity data, ViewHolder holder) {
        //TODO implement
        holder.tvItemSimpleCastText.setText(data.getName());


        if (data.getAvatars() == null) return;
        imageLoader.displayImage(data.getAvatars().getLarge(), holder.ivItemSimpleCastImage, options);
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
        private ImageView ivItemSimpleCastImage;
    private TextView tvItemSimpleCastText;

        public ViewHolder(View view) {
            ivItemSimpleCastImage = (ImageView) view.findViewById(R.id.iv_item_simple_cast_image);
            tvItemSimpleCastText = (TextView) view.findViewById(R.id.tv_item_simple_cast_text);
        }
    }
}
