package com.smart.cloud.fire.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.adapter.AttendanceAdapter;
import com.smart.cloud.fire.adapter.LostHintAdapter;
import com.smart.cloud.fire.global.Attendance;
import com.smart.cloud.fire.global.LostHint;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class LostHintActivity extends Activity {

    List<LostHint> list=null;
    LostHintAdapter lostHintAdapter;
    private LinearLayoutManager linearLayoutManager;

    Context mContext;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_hint);
        mContext=this;
        ButterKnife.bind(this);
        init();
    }
    private void init() {
        String inspc_ip= SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_ip");
        String inspc_userid=SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_userid");
        if(inspc_ip==null||inspc_ip.length()==0||inspc_userid==null||inspc_userid.length()==0){
            Toast.makeText(mContext,"请前往设置用户信息",Toast.LENGTH_SHORT).show();
            return;
        }
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String endtime=sdf.format(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        now = calendar.getTime();
        String begintime=sdf.format(now);

        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url=inspc_ip+":8091/CloudPatrolStd/lostHint?callback=json&userid="+inspc_userid+"&begintime="+begintime+"&endtime="+endtime;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.replace("json(","").replace(")","");
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                if(list==null){
                                    list=new ArrayList<>();
                                }
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                LostHint lostHint=new LostHint(jsonObject.getString("deptid"),jsonObject.getString("deptname"),
                                        jsonObject.getString("lostdate"),jsonObject.getString("lostrate"));
                                list.add(lostHint);
                            }
                            if(list!=null||list.size()!=0){
                                linearLayoutManager=new LinearLayoutManager(mContext);
                                linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                lostHintAdapter=new LostHintAdapter(mContext,list);
                                recyclerView.setAdapter(lostHintAdapter);
                            }else{
                                Toast.makeText(mContext, "无数据", Toast.LENGTH_SHORT).show();
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
}
