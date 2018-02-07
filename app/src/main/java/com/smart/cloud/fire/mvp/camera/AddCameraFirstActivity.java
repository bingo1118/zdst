package com.smart.cloud.fire.mvp.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.cloud.fire.global.ConstantValues;

import fire.cloud.smart.com.smartcloudfire.R;

public class AddCameraFirstActivity extends Activity {
    private Context mContext;
    private Button next_add_camera_first_btn;
    private ImageView add_camera_one_image;
    private TextView text_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera_first);
        mContext = this;
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        text_pass=(TextView)findViewById(R.id.text_pass);
        text_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_device = new Intent(mContext,AddCameraFourthActivity.class);
                startActivity(add_device);
            }
        });
        next_add_camera_first_btn = (Button) findViewById(R.id.add_camera_action_one);
        next_add_camera_first_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, AddCameraSecondActivity.class);
                startActivity(i);
                finish();//@@8.11
            }
        });
    }

}
