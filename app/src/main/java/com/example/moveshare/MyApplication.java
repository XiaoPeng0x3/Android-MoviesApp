package com.example.moveshare;

import android.app.Application;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {
    private static DisplayImageOptions mLoaderOPtions;
    private static RequestQueue mQueue;

    private static DisplayImageOptions mFlatOptions;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化ImageLoader
        initImageLoader(getApplicationContext());
        mQueue = Volley.newRequestQueue(getApplicationContext(), new OkHttpStack());
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.
                Builder(context).
                memoryCacheExtraOptions(480, 480).
                denyCacheImageMultipleSizesInMemory().
                threadPriority(Thread.NORM_PRIORITY - 2).
                diskCacheFileNameGenerator(new Md5FileNameGenerator()).
                tasksProcessingOrder(QueueProcessingType.FIFO).
                build();

        ImageLoader.getInstance().init(config);
        mLoaderOPtions = new DisplayImageOptions.Builder().
                showImageOnLoading(R.mipmap.no_image).
                showImageOnFail(R.mipmap.no_image).
                showImageForEmptyUri(R.mipmap.no_image).
                imageScaleType(ImageScaleType.EXACTLY_STRETCHED).
                cacheInMemory(true).
                cacheOnDisk(true).
                considerExifParams(true).
                build();

        mFlatOptions = new DisplayImageOptions.Builder().
                showImageOnLoading(R.mipmap.no_image).
                showImageOnFail(R.mipmap.no_image).
                showImageForEmptyUri(R.mipmap.no_image).
                imageScaleType(ImageScaleType.EXACTLY_STRETCHED).
                cacheInMemory(true).
                cacheOnDisk(true).
                considerExifParams(true).
                build();
    }

    public static void addRequest(Request request, Object tag) {
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
    }

    public static DisplayImageOptions getmFlatOptions() {return mFlatOptions;};

    public static DisplayImageOptions getLoaderOptions() {return mLoaderOPtions;}

    public static RequestQueue getHttpQueue() {return mQueue;};

    public static void removeRequest(Object tag) {mQueue.cancelAll(tag);}


}
