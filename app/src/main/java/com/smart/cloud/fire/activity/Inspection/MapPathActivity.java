package com.smart.cloud.fire.activity.Inspection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.MyOverlayManager;
import com.smart.cloud.fire.global.InspectionDev;
import com.smart.cloud.fire.global.InspectionSiteBean;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class MapPathActivity extends Activity {


    Context mContext;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.bmapView)
    MapView mMapView;
    @Bind(R.id.id_marker_info)
    RelativeLayout mMarkerInfoLy;
    @Bind(R.id.path_btn)
    Button path_btn;
    private BaiduMap mBaiduMap;
    List<InspectionSiteBean> smokeList;
    List<Marker> markerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_path);
        mContext=this;

        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mBaiduMap = mMapView.getMap();// 获得MapView

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
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
        final RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url=inspc_ip+"/CloudPatrolStd/getMapData?userid="+inspc_userid+"&callback=json";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.replace("json(","").replace(")","");
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject temp = (JSONObject) jsonArray.get(i);
                                String deptname=temp.getString("deptname");
                                String deptid=temp.getString("deptid");
                                String siteid=temp.getString("siteid");
                                String sitename=temp.getString("sitename");
                                String begintime=temp.getString("begintime");
                                String endtime=temp.getString("endtime");
                                String happentime=temp.getString("happentime");
                                String lng=temp.getString("longitude");
                                String lat=temp.getString("latitude");
                                String tourdataid=temp.getString("tourdataid");
                                if(smokeList==null){
                                    smokeList=new ArrayList<>();
                                }
                                smokeList.add(new InspectionSiteBean(deptname,deptid,siteid,sitename,lng,lat,begintime,endtime,happentime,tourdataid));
                            }
                            if(smokeList!=null){
                                getDataSuccess(smokeList);
                            }else{
                                Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                mProgressBar.setVisibility(View.GONE);
            }
        });
        mQueue.add(stringRequest);
    }

    private MyOverlayManager mMyOverlayManager;
    private void getDataSuccess(List<InspectionSiteBean> smokeList) {
        mBaiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        for (InspectionSiteBean info : smokeList)
        {
            if(markerList==null){
                markerList=new ArrayList<>();
            }
            // 位置
            latLng = new LatLng(Double.parseDouble(info.getLat()), Double.parseDouble(info.getLng()));
            // 图标
            View viewA = LayoutInflater.from(mContext).inflate(
                    R.layout.image_gps_mark, null);//@@待检
            BitmapDescriptor mIconMaker= BitmapDescriptorFactory
                    .fromView(viewA);
            View viewB = LayoutInflater.from(mContext).inflate(
                    R.layout.image_hjtcq_mark, null);//@@已检
            BitmapDescriptor mIconMaker2= BitmapDescriptorFactory
                    .fromView(viewB);
            if(info.getHappentime().equals("")){
                overlayOptions = new MarkerOptions().position(latLng)
                        .icon(mIconMaker).zIndex(5);
            }else{
                overlayOptions = new MarkerOptions().position(latLng)
                        .icon(mIconMaker2).zIndex(5);
            }
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
            markerList.add(marker);
        }

        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mBaiduMap.animateMapStatus(setLatLngBounds(markerList,mMapView));
            }
        });//@@10.16
//        if (markerList.size() > 0) {
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            for (Overlay overlay : markerList) {
//                // polyline 中的点可能太多，只按marker 缩放
//                if (overlay instanceof Marker) {
//                    builder.include(((Marker) overlay).getPosition());
//                }
//            }
//            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(builder.build());
//            mBaiduMap.setMapStatus(mapStatusUpdate);
//        }
//        if(markerList.size()==1){
//            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20.0f));
//        }

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
                InspectionSiteBean info = (InspectionSiteBean) marker.getExtraInfo().get("info");

                //设置详细信息布局为可见
                mMarkerInfoLy.setVisibility(View.VISIBLE);
                //根据商家信息为详细信息布局设置信息
                popupInfo(mMarkerInfoLy, info);
                return true;
            }
        });
        mProgressBar.setVisibility(View.GONE);

    }

    protected void popupInfo(RelativeLayout mMarkerLy, final InspectionSiteBean info)
    {
        ViewHolder viewHolder = null;
        if (mMarkerLy.getTag() == null)
        {
            viewHolder = new ViewHolder();
            viewHolder.dev_depart_text = (TextView) mMarkerLy
                    .findViewById(R.id.dev_depart_text);
            viewHolder.dev_location_text = (TextView) mMarkerLy
                    .findViewById(R.id.dev_location_text);
            viewHolder.dev_state_text = (TextView) mMarkerLy
                .findViewById(R.id.dev_state_text);
            viewHolder.dev_begintime_text = (TextView) mMarkerLy
                    .findViewById(R.id.dev_begintime_text);
            viewHolder.dev_endtime_text = (TextView) mMarkerLy
                    .findViewById(R.id.dev_endtime_text);
            viewHolder.path_btn = (Button) mMarkerLy
                    .findViewById(R.id.path_btn);

            mMarkerLy.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) mMarkerLy.getTag();
        viewHolder.dev_depart_text.setText("部门:"+info.getDeptname());
        viewHolder.dev_location_text.setText("点位:"+info.getSitename());
        if(info.getHappentime().equals("")){
            viewHolder.dev_state_text.setText("状态:未检");
        }else{
            viewHolder.dev_state_text.setText("状态:已检 检查时间:"+info.getHappentime());
        }
        viewHolder.dev_begintime_text.setText("班次开始时间:"+info.getBegintime());
        viewHolder.dev_endtime_text.setText("班次结束时间:"+info.getEndtime());
//        viewHolder.path_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MapPathActivity.this,ChooseConditionActivity.class);
//                intent.putExtra("dev_id",info.getReadercode());
//                intent.putExtra("type","3");
//                startActivity(intent);
//            }
//        });
    }


    private class ViewHolder
    {
        Button path_btn;
        TextView dev_depart_text;
        TextView dev_location_text;
        TextView dev_state_text;
        TextView dev_begintime_text;
        TextView dev_endtime_text;
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

    //@@10.16 所有marker都能合理显示在视图范围内
    public MapStatusUpdate setLatLngBounds(List<Marker> points, MapView mMapView) {
        LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
        for (Marker p : points) {
            builder2 = builder2.include(p.getPosition());
        }
        LatLngBounds latlngBounds = builder2.build();
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds, mMapView.getWidth(), mMapView.getHeight());
        return u;
    }
}
