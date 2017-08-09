package com.smart.cloud.fire.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ChooseDevActivity extends Activity {

    Context mContext;
    @Bind(R.id.qurey_btn)
    Button qurey_btn;
    @Bind(R.id.radio_group)
    RadioGroup radio_group;
    @Bind(R.id.today_radio)
    RadioButton today_radio;
    @Bind(R.id.week_radio)
    RadioButton week_radio;
    @Bind(R.id.from_rela)
    RelativeLayout from_rela;
    @Bind(R.id.from_text)
    TextView from_text;
    @Bind(R.id.to_text)
    TextView to_text;
    @Bind(R.id.to_rela)
    RelativeLayout to_rela;
    @Bind(R.id.month_radio)
    RadioButton month_radio;
    @Bind(R.id.area_rela)
    RelativeLayout area_rela;
    @Bind(R.id.area_text)
    TextView area_text;

    int radio_choose=0;
    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_DATAPICK = 0;
    private int mYear;
    private int mMonth;
    private int mDay;

    String getDate;
    String toDate;
    int fromOrto=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dev);
        ButterKnife.bind(this);
        mContext=this;

        initView();
    }

    private void initView() {
        area_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseDevActivity.this,ChooseAreaActivity.class);
                startActivityForResult(intent,1);
            }
        });
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==today_radio.getId()){
                    radio_choose=1;
                    fromOrto=3;
                }
                if(checkedId==week_radio.getId()){
                    radio_choose=2;
                    fromOrto=4;
                }
                if(checkedId==month_radio.getId()){
                    radio_choose=3;
                    fromOrto=5;
                }
                setDateTime();
            }
        });
        qurey_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseDevActivity.this,MapPathActivity.class);
                intent.putExtra("readercode",area_text.getText());
                intent.putExtra("begintime",from_text.getText());
                intent.putExtra("endtime",to_text.getText());
                startActivity(intent);
                Toast.makeText(mContext,radio_choose+"",Toast.LENGTH_SHORT).show();
            }
        });
        from_rela.setOnClickListener(new DateButtonOnClickListener());
        to_rela.setOnClickListener(new DateButtonOnClickListener());
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        setDateTime();
    }
    private void setDateTime() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay(c);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
        area_text.setText(result);
    }

    private class DateButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Message msg = new Message();
            if (from_rela.equals((RelativeLayout) v)) {
                msg.what = SHOW_DATAPICK;
                fromOrto=1;
            }
            if (to_rela.equals((RelativeLayout) v)) {
                msg.what = SHOW_DATAPICK;
                fromOrto=2;
            }
            saleHandler.sendMessage(msg);
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }
    /**

     * 处理日期控件的Handler

     */

    Handler saleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
            }
        }
    };
    /**
     * 日期控件的事件
     */

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(null);
        }
    };
    /**
     * 更新日期
     */
    private void updateDisplay(Calendar c) {
        getDate=new StringBuilder().append(mYear).append(
                (mMonth + 1) < 10 ? "-0" + (mMonth + 1) : "-"+(mMonth + 1)).append(
                (mDay < 10) ? "-0" + mDay : "-"+mDay).toString();
        if(fromOrto==1){
            from_text.setText(getDate);
        }else if(fromOrto==2){
            to_text.setText(getDate);
        }else if(fromOrto==4){
            c.add(Calendar.DATE, -6);
            Date resultDate = c.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            from_text.setText(sdf.format(resultDate));
            to_text.setText(getDate);
        }else if(fromOrto==5){
            c.add(Calendar.MONTH, -1);
            Date resultDate = c.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            from_text.setText(sdf.format(resultDate));
            to_text.setText(getDate);
        }else{
            from_text.setText(getDate);
            to_text.setText(getDate);
        }

    }
}

