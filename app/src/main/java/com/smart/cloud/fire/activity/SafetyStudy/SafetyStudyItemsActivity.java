package com.smart.cloud.fire.activity.SafetyStudy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.adapter.SafetyStudyAdapter;
import com.smart.cloud.fire.global.ConstantValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class SafetyStudyItemsActivity extends Activity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.safety_progressbar)
    ProgressBar safety_progressbar;
    @Bind(R.id.title_tv)
    TextView title_tv;
    Context mContext;
    List<String> list=null;
    SafetyStudyAdapter safetyStudyAdapter;
    private LinearLayoutManager linearLayoutManager;
    int studyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_study_items);
        ButterKnife.bind(this);
        mContext=this;
        studyType=getIntent().getIntExtra("StudyType",1);
        init();
    }

    private void init() {
        safety_progressbar.setVisibility(View.VISIBLE);
        String url;
        if(studyType==1){
            title_tv.setText("安全培训");
            url= ConstantValues.SERVER_IP_NEW+"getSafetyItem";
        }else{
            title_tv.setText("安全制度");
            url= ConstantValues.SERVER_IP_NEW+"getStudyRuleItems";
        }
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode=response.getInt("errorCode");
                            if(errorCode==0){
                                JSONArray jsonArray=response.getJSONArray("safetyItems");
                                list=new ArrayList<String>();
                                for(int i=0;i<jsonArray.length();i++){
                                    list.add(jsonArray.getJSONObject(i).getString("studyName"));
                                }
                                linearLayoutManager=new LinearLayoutManager(mContext);
                                linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                safetyStudyAdapter=new SafetyStudyAdapter(mContext,list);
                                safetyStudyAdapter.setOnItemClickListener(new SafetyStudyAdapter.OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int data) {
                                        if(studyType==1){
                                            Intent intent=new Intent(mContext,SafetySecondListActivity.class);
                                            intent.putExtra("firstDir",data+1+"");
                                            intent.putExtra("firstDirName",list.get(data));
                                            intent.putExtra("StudyType",1);
                                            startActivity(intent);
                                        }else{
                                            Intent intent=new Intent(mContext,SafetyThirdListActivity.class);
                                            intent.putExtra("firstDir",data+1+"");
                                            intent.putExtra("firstDirName",list.get(data));
                                            intent.putExtra("secondDir",data+1+"");
                                            intent.putExtra("secondDirName",list.get(data));
                                            intent.putExtra("StudyType",2);
                                            startActivity(intent);
                                        }

                                    }
                                });
                                recyclerView.setAdapter(safetyStudyAdapter);
                            }else{
                                Toast.makeText(mContext,response.getString("error"),Toast.LENGTH_SHORT).show();
                            }
                            safety_progressbar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            safety_progressbar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                safety_progressbar.setVisibility(View.GONE);
                Toast.makeText(mContext,"获取服务器数据失败",Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }
}
