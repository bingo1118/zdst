package com.smart.cloud.fire.mvp.electric;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smart.cloud.fire.adapter.ElectricActivityAdapterTest;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ElectricDXDetailEntity;
import com.smart.cloud.fire.global.ElectricValue;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.LineChart.LineChartActivity;
import com.smart.cloud.fire.mvp.electric_change_history.ElectricChangeHistoryActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ElectricActivity extends MvpActivity<ElectricPresenter> implements ElectricView {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_fresh_layout)
    SwipeRefreshLayout swipeFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.power_change_history_text)
    TextView power_change_history_text;//@@8.28电源切换记录
    @Bind(R.id.timer_tv)
    TextView timer_tv;//定时任务
    private ElectricPresenter electricPresenter;
    private ElectricActivityAdapterTest electricActivityAdapter;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;
    private String electricMac;
    private String userID;
    private int privilege;

    private int devType;//@@2018.05.15设备类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric);
        mContext=this;
        electricMac = getIntent().getExtras().getString("ElectricMac");
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        devType = getIntent().getExtras().getInt("devType");
        if(devType==75||devType==77){
            timer_tv.setVisibility(View.VISIBLE);
        }else{
            timer_tv.setVisibility(View.GONE);
        }
        power_change_history_text.setVisibility(View.VISIBLE);//@@8.28
        ButterKnife.bind(this);
        refreshListView();
        electricPresenter.getOneElectricInfo(userID,privilege+"",devType+"",electricMac,false);
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
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                electricPresenter.getOneElectricInfo(userID,privilege+"",devType+"",electricMac,true);
            }
        });
    }

    @OnClick({R.id.power_change_history_text,R.id.timer_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.power_change_history_text:
                Intent intent=new Intent(mContext, ElectricChangeHistoryActivity.class);
                intent.putExtra("mac",electricMac);
                startActivity(intent);
                break;
            case R.id.timer_tv:
                Intent intent2=new Intent(mContext, AutoTimeSettingActivity.class);
                intent2.putExtra("mac",electricMac);
                intent2.putExtra("devType",devType+"");
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    protected ElectricPresenter createPresenter() {
        electricPresenter = new ElectricPresenter(this);
        return electricPresenter;
    }

    @Override
    public void getDataSuccess(List<ElectricValue.ElectricValueBean> smokeList) {
        electricActivityAdapter = new ElectricActivityAdapterTest(mContext, smokeList, electricPresenter);
        recyclerView.setAdapter(electricActivityAdapter);
        swipeFreshLayout.setRefreshing(false);
        electricActivityAdapter.setOnItemClickListener(new ElectricActivityAdapterTest.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view, ElectricValue.ElectricValueBean data){
                Intent intent = new Intent(mContext, LineChartActivity.class);
                intent.putExtra("electricMac",electricMac);
                intent.putExtra("electricType",data.getElectricType());
                intent.putExtra("electricNum",data.getId());
                intent.putExtra("devType",devType);
                startActivity(intent);
            }
        });
    }

    @Override
    public void getDataDXSuccess(ElectricDXDetailEntity entity) {

    }

    @Override
    public void getDataFail(String msg) {
        swipeFreshLayout.setRefreshing(false);
        T.showShort(mContext,msg);
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
    public void getDataDXyuzhiSuccess(ElectricDXDetailEntity model) {

    }
}
