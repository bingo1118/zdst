package com.smart.cloud.fire.activity.Inspection;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.global.Department;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.cloud.smart.com.smartcloudfire.R;

public class ChooseAreaActivity extends Activity {

    ExpandableListView mainlistview = null;
    List<Department> parent = null;
    Map<String, List<Department>> map = null;
    Context mContext;

    List<Department> list=null;
    private MyAdapter adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);

        mContext=this;
        mainlistview = (ExpandableListView) this
                .findViewById(R.id.main_expandablelistview);

        initData();
//        mainlistview.setAdapter(new MyAdapter());
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
        String url=inspc_ip+"/CloudPatrolStd/getDept?callback=json&userid="+inspc_userid+"&username=admin";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.replace("json(","").replace(")","");
                        if(list!=null){
                            list=new ArrayList<Department>();
                        }
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject temp = (JSONObject) jsonArray.get(i);
                                String departName = temp.getString("DeptName");
                                String departId = temp.getString("DeptID");
                                String departParentId = temp.getString("ParentDeptID");
                                if(departParentId.equals("0")||departParentId=="0"){
                                    if(parent==null){
                                        parent=new ArrayList<Department>();
                                    }
                                    parent.add(new Department(departName,departId,departParentId));
                                    List<Department> list1 = null;
                                   for(int j = 0; j < jsonArray.length(); j++){
                                       JSONObject temp1 = (JSONObject) jsonArray.get(j);
                                       String departName1 = temp1.getString("DeptName");
                                       String departId1 = temp1.getString("DeptID");
                                       String departParentId1 = temp1.getString("ParentDeptID");
                                       if(departParentId1.equals(departId)){
                                           if(list1==null){
                                               list1 = new ArrayList<Department>();
                                           }
                                           list1.add(new Department(departName1,departId1,departParentId1));
                                       }
                                   }
                                    if(list1!=null){
                                        if(map==null){
                                            map=new HashMap<String, List<Department>>();
                                        }
                                        map.put(departName, list1);
                                    }
                                }
                            }
                            adapter=new MyAdapter();
                            mainlistview.setAdapter(adapter);
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
            String key = parent.get(groupPosition).getDepartName();
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
            String key = ChooseAreaActivity.this.parent.get(groupPosition).getDepartName();
            final Department info = map.get(key).get(childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ChooseAreaActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_children, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.second_textview);
            tv.setText(info.getDepartName());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(ChooseAreaActivity.this,info.getDepartName(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    //把返回数据存入Intent
                    intent.putExtra("result",info.getDepartName() );
                    intent.putExtra("result_id",info.getDepartId() );
                    //设置返回数据
                    setResult(RESULT_OK, intent);
                    //关闭Activity
                    finish();
                }
            });
            return tv;
        }

        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            String key = parent.get(groupPosition).getDepartName();
            if(map==null||map.get(key)==null){
                return 0;
            }else{
                int size=map.get(key).size();
                return size;
            }
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
                                 View convertView, ViewGroup parentView) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ChooseAreaActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_parent, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.parent_textview);
            ImageView iv = (ImageView) convertView
                    .findViewById(R.id.all_cheak);
            final Department info=parent.get(groupPosition);
            tv.setText(info.getDepartName());
            if(isExpanded){
                iv.setVisibility(View.VISIBLE);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ChooseAreaActivity.this,info.getDepartName(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result",info.getDepartName() );
                        intent.putExtra("result_id",info.getDepartId() );
                        //设置返回数据
                        setResult(RESULT_OK, intent);
                        //关闭Activity
                        finish();
                    }
                });
            }else{
                iv.setVisibility(View.GONE);
            }
            return convertView;
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
}
