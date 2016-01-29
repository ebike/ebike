package com.jcsoft.emsystem.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.adapter.PhotoSingleSelectionAdapter;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.bean.ImageBucket;
import com.jcsoft.emsystem.bean.ImageItem;
import com.jcsoft.emsystem.event.FinishActivityEvent;
import com.jcsoft.emsystem.event.SelectPhotoEvent;
import com.jcsoft.emsystem.view.TopBarView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.greenrobot.event.EventBus;

public class PhotoSingleSelectionActivity extends BaseActivity {
    @ViewInject(R.id.top_bar_view)
    TopBarView topBarView;
    @ViewInject(R.id.grid_view)
    GridView gridView;
    //相册对象
    private ImageBucket imageBucket;

    private PhotoSingleSelectionAdapter adapter;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_photo_single_selection);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {
        imageBucket = (ImageBucket) getIntent().getSerializableExtra("photoAlbum");
    }

    @Override
    public void init() {
        topBarView.setCenterTextView(imageBucket.bucketName);
        adapter = new PhotoSingleSelectionAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setList(imageBucket.imageList);
    }

    @Override
    public void setListener() {
        //返回
        topBarView.setLeftCallback(new TopBarView.TopBarLeftCallback() {
            @Override
            public void setLeftOnClickListener() {
                PhotoSingleSelectionActivity.this.finish();
            }
        });
        //显示选中照片
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem imageItem = (ImageItem) parent.getItemAtPosition(position);
                EventBus.getDefault().post(new SelectPhotoEvent(imageItem));
                EventBus.getDefault().post(new FinishActivityEvent(true));
                PhotoSingleSelectionActivity.this.finish();
            }
        });
    }

    @Override
    public void setData() {

    }

    public void onEvent(FinishActivityEvent event) {
        if (event != null && event.isFinish()) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
