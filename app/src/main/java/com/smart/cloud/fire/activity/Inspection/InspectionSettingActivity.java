package com.smart.cloud.fire.activity.Inspection;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.global.InspectionAlarmInfo;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspectionSettingActivity extends Activity {

    @Bind(R.id.ip_edit)
    EditText ip_edit;
    @Bind(R.id.userid_edit)
    EditText userid_edit;
    @Bind(R.id.save_btn)
    Button save_btn;

    Context mContext;
    String inspc_ip="http://101.200.238.252:8091";
    String inspc_userid="166";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_setting);
        ButterKnife.bind(this);
        mContext=this;
        init();
    }

    private void init() {
        inspc_ip= SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_ip");
        inspc_userid=SharedPreferencesManager.getInstance().getData(mContext,
                "inspction","inspc_userid");
        if(inspc_ip==""){
            ip_edit.setText("http://");
        }else{
            ip_edit.setText(inspc_ip);
        }
        userid_edit.setText(inspc_userid);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                String temp_ip=ip_edit.getText().toString();
                String temp_id=userid_edit.getText().toString();
                cheak(temp_ip,temp_id);
            }
        });
    }

    private void cheak(final String temp_ip, final String temp_id) {
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        if(!temp_ip.contains("http://")){
            Toast.makeText(mContext,"输入不正确，保存失败",Toast.LENGTH_SHORT).show();
            return;
        }
        String url=temp_ip+"/CloudPatrolStd/getDept?callback=json&userid="+temp_id+"&username=admin";
        final StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.replace("json(","").replace(")","");
                        SharedPreferencesManager.getInstance().putData(mContext,"inspction","inspc_ip",temp_ip);
                        SharedPreferencesManager.getInstance().putData(mContext,"inspction","inspc_userid",temp_id);
                        Toast.makeText(mContext,"保存成功",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,"输入不正确，保存失败",Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(stringRequest);
    }
}
