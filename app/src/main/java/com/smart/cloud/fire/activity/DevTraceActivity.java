package com.smart.cloud.fire.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.MyOverlayManager;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.GPSInfoBean;
import com.smart.cloud.fire.global.TraceItem;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class DevTraceActivity extends Activity {

    Context mContext;
    @Bind(R.id.bmapView)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    List<TraceItem> traceItems;
    List<GPSInfoBean> traceGPSItems;

    String dev_id;
    String begintime;
    String endtime;

    String type=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_trace);
        mContext=this;

        ButterKnife.bind(this);
        mBaiduMap = mMapView.getMap();// 获得MapView

        dev_id=getIntent().getStringExtra("dev_id");
        begintime=getIntent().getStringExtra("begintime");
        endtime=getIntent().getStringExtra("endtime");
        type=getIntent().getStringExtra("type");

        if(type.equals("4")){
            initGPS();
        }else{
            init();
        }
    }

    private void initGPS() {
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url= ConstantValues.SERVER_IP_NEW+"getOneGPSTrace?mac="+dev_id+"&begintime="+begintime+"&endtime="+endtime;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("errorCode")=="0"){
                                JSONArray trace=jsonObject.getJSONArray("trace");
                                for(int i=0;i<trace.length();i++){
                                    if(traceGPSItems==null){
                                        traceGPSItems=new ArrayList<>();
                                    }
                                    JSONObject temp=trace.getJSONObject(i);
                                    String devMac=temp.getString("devMac");
                                    String speed=temp.getString("speed");
                                    String lon=temp.getString("lon");
                                    String lat=temp.getString("lat");
                                    String dataTime=temp.getString("dataTime");
                                    traceGPSItems.add(new GPSInfoBean(devMac,lon,lat,speed,dataTime));
                                }
                                if(traceGPSItems!=null){
                                    getGPSDataSuccess(traceGPSItems);
                                }else{
                                    Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
                                }
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

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
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url=inspc_ip+":8091/CloudPatrolStd/getPath?companycode=&readercode="+dev_id+"&begintime="+begintime+"&endtime="+endtime+"&callback=json";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.replace("json(","").replace(")","");
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject temp = (JSONObject) jsonArray.get(i);
                                String lng=temp.getString("lng");
                                String lat=temp.getString("lat");
                                String time=temp.getString("time");
                                if(traceItems==null){
                                    traceItems=new ArrayList<>();
                                }
                                traceItems.add(new TraceItem(time,lng,lat));
                            }
                            if(traceItems!=null){
                                getDataSuccess(traceItems);
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

    private MyOverlayManager mMyOverlayManager;
    private void getDataSuccess(List<TraceItem> traceItems) {
        mBaiduMap.clear();

        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < traceItems.size(); i++) {
            // LatLng latLng=new LatLng(arg0, arg1);
            LatLng latLng = new LatLng(Double.parseDouble(traceItems.get(i)
                    .getLat()), Double.parseDouble(traceItems.get(i)
                    .getLng()));
            points.add(latLng);
        }

        OverlayOptions ooPolyline = new PolylineOptions()
                .width(1)
                .color(getResources().getColor(R.color.blue))
                .points(points);
        mBaiduMap.addOverlay(ooPolyline);

        if (points.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : points) {
                    builder.include(latLng);
            }
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(builder.build());
            mBaiduMap.setMapStatus(mapStatusUpdate);
        }
        if(points.size()==1){
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20.0f));
        }
    }




    private class ViewHolder
    {
        Button path_btn;
        TextView dev_id_text;
        TextView dev_depart_text;
        TextView dev_time_text;
        TextView dev_location_text;
    }

    @Override
    public void onResume() {
        mMapView.setVisibility(View.VISIBLE);
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.setVisibility(View.INVISIBLE);
        mMapView.onPause();
        mBaiduMap.clear();
        super.onPause();
    }

    private void getGPSDataSuccess(List<GPSInfoBean> traceGPSItems) {
        mBaiduMap.clear();

        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < traceGPSItems.size(); i++) {
            LatLng latLng = new LatLng(Double.parseDouble(traceGPSItems.get(i)
                    .getLat()), Double.parseDouble(traceGPSItems.get(i)
                    .getLon()));
            points.add(latLng);
        }

        OverlayOptions ooPolyline = new PolylineOptions()
                .width(1)
                .color(getResources().getColor(R.color.blue))
                .points(points);
        mBaiduMap.addOverlay(ooPolyline);

        if (points.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : points) {
                builder.include(latLng);
            }
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(builder.build());
            mBaiduMap.setMapStatus(mapStatusUpdate);
        }
        if(points.size()==1){
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20.0f));
        }
    }
}
