package com.jcsoft.emsystem.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.activity.MainActivity;
import com.jcsoft.emsystem.adapter.AlarmMessageAdapter;
import com.jcsoft.emsystem.bean.AlarmMessageBean;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.callback.DFinishedCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.db.XUtil;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.DRequestParamsUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.view.PullListFragmentHandler;
import com.jcsoft.emsystem.view.pullrefresh.EmptyViewForList;
import com.jcsoft.emsystem.view.pullrefresh.PullToRefreshBase;
import com.jcsoft.emsystem.view.pullrefresh.PullToRefreshListView;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 报警消息
 * Created by jimmy on 15/12/28.
 */
public class AlarmMessageFragment extends BaseListFragment {
    @ViewInject(R.id.pull_list_view)
    PullToRefreshListView pullToRefreshListView;
    @ViewInject(R.id.empty_view)
    EmptyViewForList emptyViewForList;
    private AlarmMessageAdapter adapter;
    private List<AlarmMessageBean> alarmMessageBeans;
    //标志位，标志已经初始化完成
    private boolean isPrepared;
    //加载的列表数据的handler
    private Handler mHandler;
    //第一次进来查询数据库数据，用户操作请求数据才调用接口
    private boolean loadedDB;
    //查询参数
    //1刷新；2加载更多
    private int mark;
    private int eventId;
    //初始打开的页面是该页面时，需要在初始化时加载数据
    private int initPosition;

    public AlarmMessageFragment() {
    }

    @SuppressLint("ValidFragment")
    public AlarmMessageFragment(int initPosition) {
        this.initPosition = initPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        x.view().inject(this, view);
        mHandler = new PullListFragmentHandler(this, pullToRefreshListView);
        isPrepared = true;
        init();
        if (initPosition == 1) {
            requestDatas();
        }
        setListener();
        return view;
    }

    public void init() {
        adapter = new AlarmMessageAdapter(getActivity());
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setScrollLoadEnabled(true);
        pullToRefreshListView.setDriverLine();
        pullToRefreshListView.setAdapter(adapter);
    }

    private void setListener() {
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView, boolean isAutoRefresh) {
                mHandler.sendEmptyMessage(PULL_DOWN_TO_REFRESH);
                mark = 1;
                if (alarmMessageBeans != null && alarmMessageBeans.size() > 0) {
                    eventId = alarmMessageBeans.get(0).getEventId();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mHandler.sendEmptyMessage(PULL_UP_TO_REFRESH);
                mark = 2;
                if (alarmMessageBeans != null && alarmMessageBeans.size() > 0) {
                    eventId = alarmMessageBeans.get(alarmMessageBeans.size() - 1).getEventId();
                }
            }
        });
        //点击消息
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlarmMessageBean bean = alarmMessageBeans.get(position);
                //如果是新报警消息，则修改为已消警
                if (bean.getStatus() == 1) {
                    bean.setStatus(2);
                    //执行查看报警消息接口
                    RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.viewAlarmEvent(bean.getEventId()));
                    DHttpUtils.get_String((MainActivity) getActivity(), false, params, new DCommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            //无操作
                        }
                    });
                    //更新数据库
                    try {
                        XUtil.db.update(bean.getClass(), WhereBuilder.b("eventId", "=", bean.getEventId() + ""), new KeyValue("status", "2"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //更新底部菜单报警消息角标数
                    if (AppConfig.badge != null || AppConfig.badge.isShown()) {
                        int count = Integer.valueOf(AppConfig.badge.getText().toString()) - 1;
                        if (count == 0) {
                            AppConfig.badge.hide();
                        } else {
                            AppConfig.badge.setText(count + "");
                        }
                    }
                }
                //展开内容信息
                if (!bean.isExpand()) {
                    bean.setIsExpand(true);
                } else {
                    bean.setIsExpand(false);
                }
                adapter.setList(alarmMessageBeans);
            }
        });
    }

    @Override
    public void requestDatas() {
        //如果有警报铃声，则关闭
        closePlayer();
        if (!isPrepared || !isVisible || hasLoadedOnce || !isAdded()) {
            return;
        }
        try {
            //先查询数据库中消息数据
            if (!loadedDB) {
                alarmMessageBeans = XUtil.db.selector(AlarmMessageBean.class).where("carId", "=", AppConfig.userInfoBean.getCarId()).orderBy("eventId", true).findAll();
                loadedDB = true;
                if (alarmMessageBeans != null && alarmMessageBeans.size() > 0) {
                    adapter.setList(alarmMessageBeans);
                    hasLoadedOnce = true;
                } else {
                    alarmMessageBeans = new ArrayList<AlarmMessageBean>();
                    requestDatas();
                }
            } else {
                //从接口获取数据
                RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getNewAlarmEventInfo(mark, eventId));
                DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DFinishedCallback<String>() {
                    @Override
                    public void onFinished() {
                        mHandler.sendEmptyMessage(PULL_TO_REFRESH_COMPLETE);
                    }

                    @Override
                    public void onSuccess(String result) {
                        ResponseBean<List<AlarmMessageBean>> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<List<AlarmMessageBean>>>() {
                        }.getType());
                        if (bean.getCode() == 1) {
                            if (bean.getData() != null && bean.getData().size() > 0) {
                                if (mPage == 1) {
                                    alarmMessageBeans.addAll(0, bean.getData());
                                    //刷新数据后更新底部菜单报警消息角标数
                                    if (!AppConfig.badge.isShown()) {
                                        AppConfig.badge.show();
                                    }
                                    String oldCount = AppConfig.badge.getText().toString();
                                    int count = 0;
                                    if (!CommonUtils.strIsEmpty(oldCount)) {
                                        count = Integer.valueOf(oldCount) + bean.getData().size();
                                    } else {
                                        count = bean.getData().size();
                                    }
                                    if (count == 0) {
                                        AppConfig.badge.hide();
                                    } else {
                                        AppConfig.badge.setText(count + "");
                                    }
                                } else {
                                    alarmMessageBeans.addAll(bean.getData());
                                }
                            } else {
                                mIsMore = false;
                            }
                            hasLoadedOnce = true;
                            //刷新界面
                            setViewData();
                            //将数据插入数据库
                            insertAlarmMessageBeansToDB(bean.getData());
                        } else {
                            showShortText(bean.getErrmsg());
                        }
                    }
                });
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //刷新界面
    private void setViewData() {
        if (alarmMessageBeans != null && alarmMessageBeans.size() > 0) {
            pullToRefreshListView.setVisibility(View.VISIBLE);
            emptyViewForList.setVisibility(View.GONE);
            adapter.setList(alarmMessageBeans);
        } else {
            pullToRefreshListView.setVisibility(View.GONE);
            emptyViewForList.setVisibility(View.VISIBLE);
            emptyViewForList.setTextDesc("你还没有消息哦");
        }
    }

    //将数据插入数据库
    private void insertAlarmMessageBeansToDB(List<AlarmMessageBean> alarmMessageBeans) {
        if (alarmMessageBeans != null && alarmMessageBeans.size() > 0) {
            for (AlarmMessageBean bean : alarmMessageBeans) {
                try {
                    XUtil.db.saveOrUpdate(bean);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //如果有警报铃声，则关闭
    private void closePlayer() {
        if (AppConfig.mediaPlayer != null) {
            AppConfig.mediaPlayer.pause();//暂停
            AppConfig.mediaPlayer.stop();//停止播放
            AppConfig.mediaPlayer.release();//释放资源
            AppConfig.mediaPlayer = null;
        }
    }
}
