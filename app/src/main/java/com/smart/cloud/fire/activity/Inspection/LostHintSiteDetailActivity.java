package com.smart.cloud.fire.activity.Inspection;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.activity.LostHintSiteDetailBean;
import com.smart.cloud.fire.adapter.LostHintAdapter;
import com.smart.cloud.fire.adapter.LostHintSiteDetailAdapter;
import com.smart.cloud.fire.adapter.ShopCameraAdapter;
import com.smart.cloud.fire.adapter.ShopSmokeAdapter;
import com.smart.cloud.fire.global.LostHint;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class LostHintSiteDetailActivity extends Activity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.title_tv)
    TextView title_tv;
    private LinearLayoutManager linearLayoutManager;
    private LostHintSiteDetailAdapter lostHintSiteDetailAdapter;
    private int lastVisibleItem;
    private Context mContext;
    private List<LostHintSiteDetailBean> list;
    private int loadMoreCount;
    private String page;
    private String lineid;
    private String siteid;
    private String siteName;
    private String dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_hint_site_detail);

        ButterKnife.bind(this);
        mContext=this;
        page = "1";
        list = new ArrayList<>();
        lineid=getIntent().getStringExtra("lineid");
        siteid=getIntent().getStringExtra("site");
        siteName=getIntent().getStringExtra("siteName");
        title_tv.setText(siteName);
        dateTime=getIntent().getStringExtra("time");
        getLostHintDate(lineid,siteid,dateTime,page);

        initView();
    }

    private void initView() {
        linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(lostHintSiteDetailAdapter==null){
                    return;
                }
                int count = lostHintSiteDetailAdapter.getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == count) {
                    if(loadMoreCount>=10){
                        page = Integer.parseInt(page) + 1 + "";
                        getLostHintDate(lineid,siteid,dateTime,page);
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

    public void getDataSuccess(List<?> smokeList) {
        loadMoreCount = smokeList.size();
        list.clear();
        list.addAll((List<LostHintSiteDetailBean>)smokeList);
        lostHintSiteDetailAdapter = new LostHintSiteDetailAdapter(mContext, list);
        recyclerView.setAdapter(lostHintSiteDetailAdapter);
    }


    public void getDataFail(String msg) {
        T.showShort(mContext, msg);
        if(lostHintSiteDetailAdapter!=null){
            lostHintSiteDetailAdapter.changeMoreStatus(ShopSmokeAdapter.NO_DATA);
        }
    }


    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }


    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void onLoadingMore(List<?> smokeList) {
        if(smokeList==null){
            smokeList=new ArrayList<>();
        }
        loadMoreCount = smokeList.size();
        list.addAll((List<LostHintSiteDetailBean>)smokeList);
        lostHintSiteDetailAdapter.changeMoreStatus(ShopSmokeAdapter.LOADING_MORE);
    }


    private void getLostHintDate(String lineid, String siteid, String dateTime, final String page) {
        String inspc_ip= SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_ip");
        String inspc_userid=SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_userid");
        if(inspc_ip==null||inspc_ip.length()==0||inspc_userid==null||inspc_userid.length()==0){
            Toast.makeText(mContext,"请前往设置用户信息",Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url=inspc_ip+"/CloudPatrolStd/lostSiteDetail?callback=json&siteid="+siteid+"&lineid="+lineid+"&begintime="+dateTime+"&endtime="+dateTime+"&pageIndex="+page;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.replace("json(","").replace(")","");
                        try {
                            List<LostHintSiteDetailBean> listTemp=null;
                            JSONArray jsonArray=new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                if(listTemp==null){
                                    listTemp=new ArrayList<>();
                                }
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                LostHintSiteDetailBean lostHintSiteDetailBeanTemp=new LostHintSiteDetailBean();
                                lostHintSiteDetailBeanTemp.setBegintime(jsonObject.getString("begintime"));
                                lostHintSiteDetailBeanTemp.setEndtime(jsonObject.getString("endtime"));
                                lostHintSiteDetailBeanTemp.setGuardname(jsonObject.getString("guardname"));
                                listTemp.add(lostHintSiteDetailBeanTemp);
                            }
                            if(page.equals("1")){
                                getDataSuccess(listTemp);
                            }else{
                                if(listTemp!=null){
                                    onLoadingMore(listTemp);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
