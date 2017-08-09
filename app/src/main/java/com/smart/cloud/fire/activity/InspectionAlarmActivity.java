package com.smart.cloud.fire.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.global.Department;
import com.smart.cloud.fire.global.InspectionAlarmInfo;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.cloud.smart.com.smartcloudfire.R;

public class InspectionAlarmActivity extends Activity {

    ExpandableListView mainlistview = null;
    List<String> parent = null;
    Map<String, List<InspectionAlarmInfo>> map = null;
    Context mContext;
    List<InspectionAlarmInfo> list=null;
    String deptid;
    String begintime;
    String endtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_alarm);

        mContext=this;
        deptid=getIntent().getStringExtra("deptid");
        begintime=getIntent().getStringExtra("begintime");
        endtime=getIntent().getStringExtra("endtime");
        mainlistview = (ExpandableListView) this
                .findViewById(R.id.main_expandablelistview);

        initData();
    }
    // 初始化数据
    public void initData() {

        String inspc_ip= SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_ip");
        String inspc_userid=SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_userid");
        if(inspc_ip==null||inspc_ip.length()==0||inspc_userid==null||inspc_userid.length()==0){
            Toast.makeText(mContext,"请前往设置用户信息",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url=inspc_ip+":8091/CloudPatrolStd/alarmData?callback=json&deptid="+deptid+"&time1="+begintime+"&time2="+endtime+"&userid="+inspc_userid;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.replace("json(","").replace(")","");
                        if(list!=null){
                            list=new ArrayList<InspectionAlarmInfo>();
                        }
                        if(parent==null){
                            parent=new ArrayList<>();
                        }
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject temp = (JSONObject) jsonArray.get(i);
                                String deptid = temp.getString("deptid");
                                String linename = temp.getString("linename");
                                parent.add(linename);
                                String alarmnum = temp.getString("alarmnum");
                                JSONArray jsonArraytemp=temp.getJSONArray("alarmlist");
                                List<InspectionAlarmInfo> list1 = null;
                                for(int j = 0; j < jsonArraytemp.length(); j++){
                                    JSONObject temp1 = (JSONObject) jsonArraytemp.get(j);
                                    String alarmtype = temp1.getString("alarmtype");
                                    String deviceno = temp1.getString("deviceno");
                                    String alarmtime = temp1.getString("alarmtime");
                                    if(list1==null){
                                        list1 = new ArrayList<InspectionAlarmInfo>();
                                    }
                                    list1.add(new InspectionAlarmInfo(alarmtype,deviceno,alarmtime));
                                }
                                if(list1!=null){
                                    if(map==null){
                                        map=new HashMap<String, List<InspectionAlarmInfo>>();
                                    }
                                    map.put(linename, list1);
                                }
                            }
                            if(parent.size()==0){
                                Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
                            }
                            mainlistview.setAdapter(new MyAdapter());
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

    class MyAdapter extends BaseExpandableListAdapter {

        //得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key = parent.get(groupPosition);
            return (map.get(key).get(childPosition));
        }

        //得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        //设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            String key = InspectionAlarmActivity.this.parent.get(groupPosition);
            final  InspectionAlarmInfo info = map.get(key).get(childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) InspectionAlarmActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.alarm_layout_children, null);
            }
            TextView time_text = (TextView) convertView
                    .findViewById(R.id.time_text);
            time_text.setText(info.getAlarmtime());
            TextView dev_id_text = (TextView) convertView
                    .findViewById(R.id.dev_id_text);
            dev_id_text.setText("设备号："+info.getDeviceno());
            TextView alarm_type_text = (TextView) convertView
                    .findViewById(R.id.alarm_type_text);
            alarm_type_text.setText("报警类型:"+changeType(info.getAlarmtype()));
            return convertView;
        }

        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            String key = parent.get(groupPosition);
            int size=map.get(key).size();
            return size;
        }
        //获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return parent.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return parent.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        //设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) InspectionAlarmActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_parent, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.parent_textview);
            tv.setText(InspectionAlarmActivity.this.parent.get(groupPosition));
            return tv;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    private String changeType(String alarmtype) {
        String temp="";
        switch (alarmtype){
            case "alarm1":
                temp="手动报警";
                break;
            case "alarm2":
                temp="低电报警";
                break;
            case "alarm3":
                temp="漏检报警";
                break;
            case "alarm4":
                temp="倾斜报警";
                break;
            case "alarm5":
                temp="静止报警";
                break;
            case "alarm6":
                temp="摔碰报警";
                break;
        }
        return temp;
    }
}

