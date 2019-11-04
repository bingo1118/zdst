package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Devices;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.adapter.ShopCameraAdapter;
import com.smart.cloud.fire.adapter.YCDevicesAdapter;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.TransmissionDevice;
import com.smart.cloud.fire.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class YCDevicesActivity extends MvpActivity<YCDevicesPresenter> implements YCDevicesView {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_fresh_layout)
    SwipeRefreshLayout swipeFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.title_text)
    TextView title_text;
    private YCDevicesPresenter mPresenter;
    private YCDevicesAdapter mAdapter;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;
    private String yc_mac;
    private String yc_name;
    private String page="1";
    private List<TransmissionDevice> list;
    private int loadMoreCount;
    private int lastVisibleItem;
    private boolean research = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ycdevices);
        mContext=this;
        ButterKnife.bind(this);
        yc_mac=getIntent().getStringExtra("mac");
        yc_name=getIntent().getStringExtra("name");
        list = new ArrayList<>();
        refreshListView();
        mPresenter.getAllTransmissionDevice(yc_mac,list,page,false);
        title_text.setText(yc_name);
        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = "1";
                list.clear();
                mPresenter.getAllTransmissionDevice(yc_mac,list,page,false);
                swipeFreshLayout.setRefreshing(false);
            }
        });
    }

    private void refreshListView() {
        //设置刷新时动画的颜色，可以设置4个
        swipeFreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeFreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //下拉刷新。。
        swipeFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = "1";
                list.clear();
                mPresenter.getAllTransmissionDevice(yc_mac,list,page,false);
                swipeFreshLayout.setRefreshing(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (research) {
                    if(mAdapter!=null){
                        mAdapter.changeMoreStatus(YCDevicesAdapter.NO_DATA);
                    }
                    return;
                }
                if(mAdapter==null){
                    return;
                }
                int count = mAdapter.getItemCount();
//                int itemCount = lastVisibleItem+2;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == count) {
                    if(loadMoreCount>=20){
                        page = Integer.parseInt(page) + 1 + "";
                        mPresenter.getAllTransmissionDevice(yc_mac,list,page,false);
                    }else{
                        T.showShort(mContext,"已经没有更多数据了");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected YCDevicesPresenter createPresenter() {
        if(mPresenter==null){
            mPresenter=new YCDevicesPresenter(this);
        }
        return mPresenter;
    }



    @Override
    public void getDataSuccess(List<?> smokeList,boolean search) {
        research = search;
        if(smokeList.size()==0){
            T.showShort(mContext,"无数据");
        }
        loadMoreCount = smokeList.size();
        list.clear();
        list.addAll((List<TransmissionDevice>)smokeList);
        mAdapter = new YCDevicesAdapter(mContext, list, mPresenter);
        recyclerView.setAdapter(mAdapter);
        swipeFreshLayout.setRefreshing(false);
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext, msg);
        swipeFreshLayout.setRefreshing(false);
        if(mAdapter!=null){
            mAdapter.changeMoreStatus(mAdapter.NO_DATA);
        }
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingMore(List<?> smokeList) {
        loadMoreCount = smokeList.size();
        list.addAll((List<TransmissionDevice>)smokeList);
        mAdapter.changeMoreStatus(mAdapter.LOADING_MORE);
    }
}
