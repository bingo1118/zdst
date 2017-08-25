package com.smart.cloud.fire.activity.Inspection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.global.GPSInfoBean;
import com.smart.cloud.fire.ui.ApMonitorActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class GPSMapActivity extends Activity {

    Context mContext;
    @Bind(R.id.bmapView)
    MapView mMapView;
    @Bind(R.id.id_marker_info)
    RelativeLayout mMarkerInfoLy;
    @Bind(R.id.path_btn)
    Button path_btn;
    @Bind(R.id.video_btn)
    Button video_btn;
    private BaiduMap mBaiduMap;
    private GPSInfoBean gpsInfoBean=null;

    private String mac;
    private String cameraId;
    private String cameraPsw;
    private Contact contact;
    private int connectType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsmap);
        mContext=this;

        mac=getIntent().getStringExtra("mac");
        cameraId=getIntent().getStringExtra("cameraId");
        cameraPsw=getIntent().getStringExtra("cameraPsw");
        contact=(Contact) getIntent().getSerializableExtra("contact");

        ButterKnife.bind(this);
        mBaiduMap = mMapView.getMap();// 获得MapView

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }


    private void init() {
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url= ConstantValues.SERVER_IP_NEW+"getOneGPSInfo?mac="+mac;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("errorCode")=="0"){
                                JSONObject info=jsonObject.getJSONObject("info");
                                String devMac=info.getString("devMac");
                                String speed=info.getString("speed");
                                String lon=info.getString("lon");
                                String lat=info.getString("lat");
                                String dataTime=info.getString("dataTime");
                                gpsInfoBean=new GPSInfoBean(devMac,lon,lat,speed,dataTime);
                            }
                            if(gpsInfoBean!=null){
                                getDataSuccess(gpsInfoBean);
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

    private void getDataSuccess(GPSInfoBean info) {
        mBaiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        // 位置
        latLng = new LatLng(Double.parseDouble(info.getLat()), Double.parseDouble(info.getLon()));
        // 图标
        View viewA = LayoutInflater.from(mContext).inflate(
                    R.layout.image_mark, null);
        BitmapDescriptor mIconMaker= BitmapDescriptorFactory
                    .fromView(viewA);
        overlayOptions = new MarkerOptions().position(latLng)
                    .icon(mIconMaker).zIndex(5);
        marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        marker.setExtraInfo(bundle);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(u);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18.0f));

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener()
        {
            @Override
            public boolean onMapPoiClick(MapPoi arg0)
            {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0)
            {
                mMarkerInfoLy.setVisibility(View.GONE);
                mBaiduMap.hideInfoWindow();
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(final Marker marker)
            {
                //获得marker中的数据
                GPSInfoBean info = (GPSInfoBean) marker.getExtraInfo().get("info");

                //设置详细信息布局为可见
                mMarkerInfoLy.setVisibility(View.VISIBLE);
                //根据商家信息为详细信息布局设置信息
                popupInfo(mMarkerInfoLy, info);
                return true;
            }
        });


    }

    protected void popupInfo(RelativeLayout mMarkerLy, final GPSInfoBean info)
    {
        ViewHolder viewHolder = null;
        if (mMarkerLy.getTag() == null)
        {
            viewHolder = new ViewHolder();
            viewHolder.dev_id_text = (TextView) mMarkerLy
                    .findViewById(R.id.dev_id_text);
            viewHolder.dev_depart_text = (TextView) mMarkerLy
                    .findViewById(R.id.dev_depart_text);
            viewHolder.dev_time_text = (TextView) mMarkerLy
                    .findViewById(R.id.dev_time_text);
            viewHolder.dev_location_text = (TextView) mMarkerLy
                    .findViewById(R.id.dev_location_text);
            viewHolder.path_btn = (Button) mMarkerLy
                    .findViewById(R.id.path_btn);
            viewHolder.video_btn = (Button) mMarkerLy
                    .findViewById(R.id.video_btn);

            mMarkerLy.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) mMarkerLy.getTag();
        viewHolder.dev_id_text.setText("设备ID："+info.getDevMac());
        viewHolder.dev_depart_text.setVisibility(View.GONE);
        viewHolder.dev_time_text.setText("时间："+info.getDataTime());
        viewHolder.dev_location_text.setText("纬度:"+info.getLat()+"    经度:"+info.getLon());
        viewHolder.path_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GPSMapActivity.this,ChooseConditionActivity.class);
                intent.putExtra("dev_id",info.getDevMac());
                intent.putExtra("type","4");
                startActivity(intent);
            }
        });
        viewHolder.video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contact.getContactId()==null||contact.getContactId()==""){
                    Toast.makeText(mContext,"未关联摄像机",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(GPSMapActivity.this,ApMonitorActivity.class);
                    intent.putExtra("contact", contact);
                    intent.putExtra("connectType", ConstantValues.ConnectType.P2PCONNECT);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }


    private class ViewHolder
    {
        Button path_btn;
        Button video_btn;
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
        super.onPause();
    }
}
