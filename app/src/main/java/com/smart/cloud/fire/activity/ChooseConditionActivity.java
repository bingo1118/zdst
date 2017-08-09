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

public class ChooseConditionActivity extends Activity {

    Context mContext;
    @Bind(R.id.title_tv)
    TextView title_text;
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
    int fromOrto=0;
    String result_id="";
    String type="";
    String dev_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_condition);
        ButterKnife.bind(this);
        mContext=this;
        type=getIntent().getStringExtra("type");

        initView();
    }

    private void initView() {
        switch (type){
            case "1":
                title_text.setText("出勤");
                break;
            case "2":
                title_text.setText("报警");
                break;
            case "3":
                title_text.setText("设备轨迹");
                area_rela.setVisibility(View.GONE);
                dev_id=getIntent().getStringExtra("dev_id");
                break;
            case "4":
                title_text.setText("设备轨迹");
                area_rela.setVisibility(View.GONE);
                dev_id=getIntent().getStringExtra("dev_id");
                break;
        }
        area_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseConditionActivity.this,ChooseAreaActivity.class);
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
                switch (type){
                    case "1":
                        if(result_id.length()==0){
                            Toast.makeText(mContext,"请选择部门区域",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent1=new Intent(ChooseConditionActivity.this,AttendanceActivity.class);
                        intent1.putExtra("deptid",result_id);
                        intent1.putExtra("begintime",from_text.getText());
                        intent1.putExtra("endtime",to_text.getText());
                        startActivity(intent1);
                        break;
                    case "2":
                        if(result_id.length()==0){
                            Toast.makeText(mContext,"请选择部门区域",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent2=new Intent(ChooseConditionActivity.this,InspectionAlarmActivity.class);
                        intent2.putExtra("deptid",result_id);
                        intent2.putExtra("begintime",from_text.getText());
                        intent2.putExtra("endtime",to_text.getText());
                        startActivity(intent2);
                        break;
                    case "3":
                        Intent intent3=new Intent(ChooseConditionActivity.this,DevTraceActivity.class);
                        intent3.putExtra("begintime",from_text.getText());
                        intent3.putExtra("endtime",to_text.getText());
                        intent3.putExtra("dev_id",dev_id);
                        intent3.putExtra("type",type);
                        startActivity(intent3);
                        break;
                    case "4":
                        Intent intent4=new Intent(ChooseConditionActivity.this,DevTraceActivity.class);
                        intent4.putExtra("begintime",from_text.getText());
                        intent4.putExtra("endtime",to_text.getText());
                        intent4.putExtra("dev_id",dev_id);
                        intent4.putExtra("type",type);
                        startActivity(intent4);
                        break;
                }

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
        if(data!=null){
            String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
            area_text.setText(result);
            result_id=data.getExtras().getString("result_id");
        }
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
        }else if(fromOrto==4){//本周
            Date resultDate = c.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(resultDate);
            // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
            int dayWeek = c.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
            if (1 == dayWeek) {
                c.add(Calendar.DAY_OF_MONTH, -1);
            }
            // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            c.setFirstDayOfWeek(Calendar.MONDAY);
            // 获得当前日期是一个星期的第几天
            int day = c.get(Calendar.DAY_OF_WEEK);
            // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            c.add(Calendar.DATE, c.getFirstDayOfWeek() - day);
            String imptimeBegin = sdf.format(c.getTime());
            // System.out.println("所在周星期一的日期：" + imptimeBegin);
            c.add(Calendar.DATE, 6);
            String imptimeEnd = sdf.format(c.getTime());
            from_text.setText(imptimeBegin);
            to_text.setText(imptimeEnd);
        }else if(fromOrto==5){//本月
//            c.add(Calendar.MONTH, -1);
            Date resultDate = c.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            c.set(Calendar.DATE, c.getActualMinimum(Calendar.DATE));
            from_text.setText(sdf.format(c.getTime()));
            // 设置日期为本月最大日期
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
            to_text.setText(sdf.format(c.getTime()));
        }else{
            from_text.setText(getDate);
            to_text.setText(getDate);
        }

    }
}
