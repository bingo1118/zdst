package com.smart.cloud.fire.utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by Rain on 2018/1/29.
 */
public class VolleyHelper {
    private static RequestQueue mRequestQueue;
    private static VolleyHelper mInstance;
    private Context context;

    private VolleyHelper(Context context) {
        this.context = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyHelper(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context
                    .getApplicationContext());
        }
        return mRequestQueue;
    }
    public void stopRequestQueue() {
        if (mRequestQueue != null) {
            mRequestQueue.stop();
            mRequestQueue=null;
        }
    }

    public void getJsonResponse(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,null,listener,errorListener);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(mRequestQueue==null){
            mRequestQueue=getRequestQueue();
        }
        mRequestQueue.add(jsonObjectRequest);
    }

}
