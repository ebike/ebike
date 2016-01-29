package com.jcsoft.emsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.adapter.ImageBucketAdapter;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.bean.ImageBucket;
import com.jcsoft.emsystem.bean.ImageFetcher;
import com.jcsoft.emsystem.event.FinishActivityEvent;
import com.jcsoft.emsystem.view.TopBarView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 选择相册
 */
public class PhotoAlbumListActivity extends BaseActivity {
    @ViewInject(R.id.top_bar_view)
    TopBarView topBarView;
    @ViewInject(R.id.grid_view)
    GridView gridView;
    private ImageFetcher fetcher;
    //相册列表
    private List<ImageBucket> photoAlbumList = new ArrayList<ImageBucket>();
    //相册列表适配器
    private ImageBucketAdapter adapter;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_photo_album_list);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        fetcher = ImageFetcher.getInstance(getApplicationContext());
        photoAlbumList = fetcher.getImagesBucketList(false);
        adapter = new ImageBucketAdapter(this, photoAlbumList);
        gridView.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        //返回
        topBarView.setLeftCallback(new TopBarView.TopBarLeftCallback() {
            @Override
            public void setLeftOnClickListener() {
                PhotoAlbumListActivity.this.finish();
            }
        });
        //进入选中相册
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PhotoAlbumListActivity.this, PhotoSingleSelectionActivity.class);
                intent.putExtra("photoAlbum", photoAlbumList.get(position));
                startActivity(intent);
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
