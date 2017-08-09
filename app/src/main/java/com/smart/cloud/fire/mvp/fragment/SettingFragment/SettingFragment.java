package com.smart.cloud.fire.mvp.fragment.SettingFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.activity.InspectionActivity;
import com.smart.cloud.fire.activity.SafetyStudyItemsActivity;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.camera.AddCameraFirstActivity;
import com.smart.cloud.fire.ui.AboutActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class SettingFragment extends MvpFragment<SettingFragmentPresenter> implements SettingFragmentView {
    @Bind(R.id.setting_user_id)
    TextView settingUserId;
    @Bind(R.id.setting_user_code)
    TextView settingUserCode;
    @Bind(R.id.setting_help_rela)
    RelativeLayout settingHelpRela;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.setting_camera_relative)
    RelativeLayout settingCameraRelative;
    @Bind(R.id.line_state)
    TextView lineState;
    @Bind(R.id.safety_study_rela)
    RelativeLayout safety_study_rela;//@@7.24 安全培训
    @Bind(R.id.safety_rule_info)
    RelativeLayout safety_rule_info;//@@7.26 安全制度
    @Bind(R.id.safety_xj_rela)
    RelativeLayout safety_xj_rela;//@@7.26
    @Bind(R.id.safety_push_switch)
    Switch safety_push_switch;//@@8.7
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container,
                false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        init();
    }

    private void init() {
        String userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS_NUMBER);
        String username = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        settingUserId.setText(userID);
        settingUserCode.setText(username);
        String state = MyApp.app.getPushState();
        if(state!=null&&state.length()>0){
            if(state.equals("Online")){
                lineState.setText("在线");
            }
            if(state.equals("Offline")){
                lineState.setText("离线");
            }
        }
        int privilege = MyApp.app.getPrivilege();
        if (privilege == 3) {
            settingHelpRela.setVisibility(View.VISIBLE);//显示添加摄像机。。
            settingCameraRelative.setVisibility(View.VISIBLE);//显示绑定摄像机。。
        }
        String ifSecurityPush = SharedPreferencesManager.getInstance().getData(mContext,
                "setting",
                "ifSecurityPush");
        if(ifSecurityPush=="2"){
            safety_push_switch.setChecked(false);
        }else{
            safety_push_switch.setChecked(true);
        }
        safety_push_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferencesManager.getInstance().putData(mContext,
                            "setting",
                            "ifSecurityPush","1");
                    Toast.makeText(mContext,"已开启接收安防设备报警",Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferencesManager.getInstance().putData(mContext,
                            "setting",
                            "ifSecurityPush","2");
                    Toast.makeText(mContext,"已关闭接收安防设备报警",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick({R.id.app_update, R.id.setting_help_about, R.id.setting_help_rela, R.id.setting_help_exit
                , R.id.setting_camera_relative,R.id.safety_study_rela,R.id.safety_rule_info,R.id.safety_xj_rela})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_update:
                mvpPresenter.checkUpdate(mContext);
                break;
            case R.id.setting_help_about:
                Intent intent = new Intent(mContext, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_help_rela:
                Intent intent2 = new Intent(mContext, AddCameraFirstActivity.class);
                startActivity(intent2);
                break;
            case R.id.setting_help_exit:
                Intent in = new Intent();
                in.setAction("APP_EXIT");
                mContext.sendBroadcast(in);
                break;
            case R.id.setting_camera_relative:
                mvpPresenter.bindDialog(mContext);
                break;
            case R.id.safety_study_rela:
                Intent intent3 = new Intent(mContext, SafetyStudyItemsActivity.class);
                intent3.putExtra("StudyType",1);
                startActivity(intent3);
                break;
            case R.id.safety_rule_info:
                Intent intent4 = new Intent(mContext, SafetyStudyItemsActivity.class);
                intent4.putExtra("StudyType",2);
                startActivity(intent4);
                break;
            case R.id.safety_xj_rela:
                Intent intent5 = new Intent(mContext, InspectionActivity.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
    }

    @Override
    protected SettingFragmentPresenter createPresenter() {
        SettingFragmentPresenter mMapFragmentPresenter = new SettingFragmentPresenter(SettingFragment.this);
        return mMapFragmentPresenter;
    }

    @Override
    public String getFragmentName() {
        return "settingFragment";
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void bindResult(String msg) {
        T.showShort(mContext, msg);
    }
}
