package com.smart.cloud.fire.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smart.cloud.fire.adapter.SafetyStudyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class SafetySecondListActivity extends Activity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.title_tv)
    TextView title_tv;

    Context mContext;
    List<String> list=null;
    SafetyStudyAdapter safetyStudyAdapter;
    private LinearLayoutManager linearLayoutManager;
    String firstDir;
    String firstDirName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_second_list);

        ButterKnife.bind(this);
        mContext=this;
        list=new ArrayList<String>();
        firstDir=getIntent().getStringExtra("firstDir");
        firstDirName=getIntent().getStringExtra("firstDirName");
        title_tv.setText(firstDirName);
        list.add("宣传教育");
        list.add("应急预案");
        list.add("安全演练");
        linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        safetyStudyAdapter=new SafetyStudyAdapter(mContext,list);
        safetyStudyAdapter.setOnItemClickListener(new SafetyStudyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int data) {
                Intent intent=new Intent(mContext,SafetyThirdListActivity.class);
                intent.putExtra("firstDir",firstDir);
                intent.putExtra("secondDir",data+1+"");
                intent.putExtra("secondDirName",list.get(data));
                intent.putExtra("StudyType",1);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(safetyStudyAdapter);
    }
}
