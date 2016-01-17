package com.jcsoft.emsystem.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dive on 2015/8/11.
 */
public abstract  class TAdapter<T> extends android.widget.BaseAdapter implements Serializable {

    protected List<T> mList;
    protected Context mContext;
    protected ListView mListView;
    protected ImageLoader imageLoader;

    protected ImageLoadingListener animateFirstListener;
    public TAdapter(Context mContext) {
        this.mContext = mContext;
        imageLoader = ImageLoader.getInstance();
        animateFirstListener = new AnimateFirstDisplayListener();
    }


    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
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

    @Override
    public int getCount() {
        if (mList != null)
            return mList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView,
                                 ViewGroup parent);

    public void setList(T[] list) {
        if (list == null || list.length == 0) {
            return;
        }
        List<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setList(arrayList);
    }

    public List<T> getData(){
        return mList;
    }

    public void setList(List<T> list) {
        if (list == null || list.isEmpty()) {
            notifyChangeEmpty();
            return;
        }
        this.mList = new ArrayList<T>();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void notifyChangeEmpty() {
        this.mList = new ArrayList<T>();
        notifyDataSetChanged();
    }

    public ListView getListView() {
        return mListView;
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }
}
