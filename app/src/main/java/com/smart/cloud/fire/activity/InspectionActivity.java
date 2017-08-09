package com.smart.cloud.fire.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.Department;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class InspectionActivity extends Activity {

    @Bind(R.id.chart)
    PieChartView chart;
    @Bind(R.id.spinner)
    Spinner spinner;
    Context mContext;

    private PieChartData data;
    private boolean hasLabels = false;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;

    int qualifiedCount;//合格
    int unqualifiedCount;//漏检
    int waitCount;//待检


    List<Department> childrenList = null;
    List<String> childrenNameList=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        ButterKnife.bind(this);
        mContext=this;

//        initSpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        childrenList=null;
        childrenNameList=null;
        initSpinner();
    }

    private void initSpinner() {
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String inspc_ip= SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_ip");
        String inspc_userid=SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_userid");
        if(inspc_ip==null||inspc_ip.length()==0||inspc_userid==null||inspc_userid.length()==0){
            Toast.makeText(mContext,"请前往设置用户信息",Toast.LENGTH_SHORT).show();
            return;
        }
        String url=inspc_ip+":8091/CloudPatrolStd/getDept?callback=json&userid="+inspc_userid+"&username=admin";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.replace("json(","").replace(")","");
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject temp = (JSONObject) jsonArray.get(i);
                                String departName = temp.getString("DeptName");
                                String departId = temp.getString("DeptID");
                                String departParentId = temp.getString("ParentDeptID");
//                                if(departParentId!="0"){
                                    if(childrenList==null){
                                        childrenList=new ArrayList<>();
                                    }
                                    if(childrenNameList==null){
                                        childrenNameList=new ArrayList<>();
                                    }
                                    childrenList.add(new Department(departName,departId,departParentId));
                                    childrenNameList.add(departName);
//                                }
                            }
                            if(childrenList!=null&&childrenNameList!=null){
                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_dropdown_item, childrenNameList);
                                //绑定 Adapter到控件
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        initChart(childrenList.get(position).getDepartId());
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
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

    @OnClick({R.id.cq_img,R.id.dt_img,R.id.bj_img,R.id.ljtx_rela,R.id.sz_img})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.cq_img:
                Intent intent_cq=new Intent(mContext,ChooseConditionActivity.class);
                intent_cq.putExtra("type","1");
                startActivity(intent_cq);
                break;
            case R.id.bj_img:
                Intent intent_bj=new Intent(mContext,ChooseConditionActivity.class);
                intent_bj.putExtra("type","2");
                startActivity(intent_bj);
                break;
            case R.id.dt_img:
                Intent intent_dt=new Intent(mContext,MapPathActivity.class);
                startActivity(intent_dt);
                break;
            case R.id.ljtx_rela:
                Intent intent_lj=new Intent(mContext,LostHintActivity.class);
                startActivity(intent_lj);
                break;
            case R.id.sz_img:
                Intent intent_sz=new Intent(mContext,InspectionSettingActivity.class);
                startActivity(intent_sz);
                break;
        }
    }


    private void initChart(String deptid) {
        String inspc_ip= SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_ip");
        String inspc_userid=SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_userid");
        if(inspc_ip==null||inspc_ip.length()==0||inspc_userid==null||inspc_userid.length()==0){
            Toast.makeText(mContext,"请前往设置用户信息",Toast.LENGTH_SHORT).show();
            return;
        }
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate= dateformat.format(date);
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url=inspc_ip+":8091/CloudPatrolStd/chartTotalRate?deptid="+deptid+"&lineid=1&begintime="+strDate+"&endtime="+strDate+"&userid="+inspc_userid+"&callback=json";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.replace("json(","").replace(")","");
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            qualifiedCount=jsonObject.getInt("qualifiedCount");
                            unqualifiedCount=jsonObject.getInt("unqualifiedCount");
                            waitCount=jsonObject.getInt("waitCount");
                            List<SliceValue> values = new ArrayList<SliceValue>();
                            SliceValue sliceValue1 = new SliceValue(qualifiedCount, Color.parseColor("#008000"));
                            values.add(sliceValue1);
                            SliceValue sliceValue2 = new SliceValue(unqualifiedCount, Color.parseColor("#DC143C"));
                            values.add(sliceValue2);
                            SliceValue sliceValue3 = new SliceValue(waitCount, Color.parseColor("#0000FF"));
                            values.add(sliceValue3);

                            data = new PieChartData(values);
                            data.setHasLabels(hasLabels);
                            data.setHasLabelsOnlyForSelected(hasLabelForSelected);
                            data.setHasLabelsOutside(hasLabelsOutside);
                            data.setHasCenterCircle(hasCenterCircle);

                            if (isExploded) {
                                data.setSlicesSpacing(24);
                            }
                            chart.setPieChartData(data);
                            chart.setOnValueTouchListener(new ValueTouchListener());

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
    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            switch(arcIndex){
                case 0:
                    Toast.makeText(InspectionActivity.this, "合格数量: " + (int)value.getValue(), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(InspectionActivity.this, "漏检数量: " + (int)value.getValue(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(InspectionActivity.this, "待检数量: " + (int)value.getValue(), Toast.LENGTH_SHORT).show();
                    break;
            }

        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}
