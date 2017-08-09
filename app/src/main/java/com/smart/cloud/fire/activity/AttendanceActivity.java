package com.smart.cloud.fire.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.smart.cloud.fire.adapter.AttendanceAdapter;
import com.smart.cloud.fire.adapter.SafetyStudyAdapter;
import com.smart.cloud.fire.global.Attendance;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;

public class AttendanceActivity extends Activity {

    String deptid;
    String begintime;
    String endtime;
    int days=0;

    List<Attendance> list=null;
    AttendanceAdapter attendanceAdapter;
    private LinearLayoutManager linearLayoutManager;

    Context mContext;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.safety_progressbar)
    ProgressBar safety_progressbar;
    @Bind(R.id.from_time_text)
    TextView from_time_text;
    @Bind(R.id.to_time_text)
    TextView to_time_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        mContext=this;
        ButterKnife.bind(this);
        deptid=getIntent().getStringExtra("deptid");
        begintime=getIntent().getStringExtra("begintime");
        endtime=getIntent().getStringExtra("endtime");
        try {
            days=getDateSpace(begintime,endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        from_time_text.setText(begintime);
        to_time_text.setText(endtime);
        String inspc_ip= SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_ip");
        String inspc_userid=SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_userid");
        if(inspc_ip==null||inspc_ip.length()==0||inspc_userid==null||inspc_userid.length()==0){
            Toast.makeText(mContext,"请前往设置用户信息",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url=inspc_ip+":8091/CloudPatrolStd/attendance?callback=json&deptid="+deptid+"&begintime="+begintime+"&endtime="+endtime+"&userid="+inspc_userid;
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
                                Attendance attendance=new Attendance(jsonObject.getString("lable"),jsonObject.getString("value"));
                                list.add(attendance);
                            }
                            if(list!=null){
                                linearLayoutManager=new LinearLayoutManager(mContext);
                                linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                attendanceAdapter=new AttendanceAdapter(mContext,list,days);
                                recyclerView.setAdapter(attendanceAdapter);
                            }else{
                                Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
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

    //获取两天区间
    public int getDateSpace(String date1, String date2)
            throws ParseException {

        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calst = Calendar.getInstance();;
        Calendar caled = Calendar.getInstance();

        calst.setTime(sdf.parse(date1));
        caled.setTime(sdf.parse(date2));

        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int days = ((int)(caled.getTime().getTime()/1000)-(int)(calst.getTime().getTime()/1000))/3600/24;

        return days+1;
    }
}
