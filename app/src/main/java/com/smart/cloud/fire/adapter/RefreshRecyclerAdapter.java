package com.smart.cloud.fire.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.global.InitBaiduNavi;
import com.smart.cloud.fire.mvp.fragment.CollectFragment.AlarmMessageModel;
import com.smart.cloud.fire.mvp.fragment.CollectFragment.CollectFragmentPresenter;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.T;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

public class RefreshRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private List<AlarmMessageModel> messageModelList;
    private Activity mContext;
    private CollectFragmentPresenter collectFragmentPresenter;
    private String userId;
    private String privilege;

    public RefreshRecyclerAdapter(Activity mContext, List<AlarmMessageModel> messageModelList
            , CollectFragmentPresenter collectFragmentPresenter, String userId, String privilege) {
        this.mInflater = LayoutInflater.from(mContext);
        this.messageModelList = messageModelList;
        this.mContext = mContext;
        this.collectFragmentPresenter = collectFragmentPresenter;
        this.userId = userId;
        this.privilege = privilege;
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            final View view = mInflater.inflate(R.layout.collect_fragment_adapter, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.recycler_load_more_layout, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final AlarmMessageModel mNormalAlarmMessage = messageModelList.get(position);
            int alarmType = mNormalAlarmMessage.getAlarmType();
            int ifDeal = mNormalAlarmMessage.getIfDealAlarm();
            ((ItemViewHolder) holder).alarmTimeTv.setText(mNormalAlarmMessage.getAlarmTime());
            ((ItemViewHolder) holder).smokeMacTv.setText(mNormalAlarmMessage.getName());
            ((ItemViewHolder) holder).repeaterAddressTv.setText(mNormalAlarmMessage.getAddress());
            ((ItemViewHolder) holder).repeaterNameTv.setText(mNormalAlarmMessage.getPlaceType());
            ((ItemViewHolder) holder).repeaterMacTv.setText(mNormalAlarmMessage.getAreaName());
            ((ItemViewHolder) holder).userSmokeMarkPrincipal.setText(mNormalAlarmMessage.getPrincipal1());
            ((ItemViewHolder) holder).userSmokeMarkPhoneTv.setText(mNormalAlarmMessage.getPrincipal1Phone());
            ((ItemViewHolder) holder).userSmokeMarkPrincipalTwo.setText(mNormalAlarmMessage.getPrincipal2());
            ((ItemViewHolder) holder).userSmokeMarkPhoneTvTwo.setText(mNormalAlarmMessage.getPrincipal2Phone());
            ((ItemViewHolder) holder).mac_tv.setText("ID:"+mNormalAlarmMessage.getMac());
            if (ifDeal == 0) {
                ((ItemViewHolder) holder).dealAlarmActionTv.setText("取消报警");
                ((ItemViewHolder) holder).dealAlarmActionTv.setVisibility(View.VISIBLE);
            } else {
                ((ItemViewHolder) holder).dealAlarmActionTv.setVisibility(View.GONE);
            }
            int devType= mNormalAlarmMessage.getDeviceType();
            ((ItemViewHolder) holder).alarmMarkImage.setVisibility(View.VISIBLE);//@@5.24区分开探测器和安防设备
            switch (devType){
                case 41:
                case 1:
                    ((ItemViewHolder) holder).smokeMac.setText("烟感:");
                    if (alarmType == 202) {
                        ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.yanwu);
                        ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                        ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    } else {
                        ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_ddy);
                        ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                        ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                    }
                    break;
                case 16://@@9.29
                case 2:
                    ((ItemViewHolder) holder).smokeMac.setText("燃气:");
                    ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.ranqi);
                    ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    break;
                case 10://@@4.28
                    int alarmFamily10 = mNormalAlarmMessage.getAlarmFamily();//@@水压值8.31
                    ((ItemViewHolder) holder).smokeMac.setText("水压:");
                    switch(alarmType){
                        case 193://低电压@@
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_ddy);
                            ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                            ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                            break;
                        case 209://低水压@@
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.dishuiya);
                            ((ItemViewHolder) holder).smokeMacTv.setText(mNormalAlarmMessage.getName()+"(水压值:"+alarmFamily10+"kpa)");
                            ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                            ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                            break;
                        case 218://高水压@@
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.gaoshuiya);
                            ((ItemViewHolder) holder).smokeMacTv.setText(mNormalAlarmMessage.getName()+"(水压值:"+alarmFamily10+"kpa)");
                            ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                            ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                            break;
                        case 217://水压升高@@
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.shuiyashenggao);
                            ((ItemViewHolder) holder).smokeMacTv.setText(mNormalAlarmMessage.getName()+"(水压值:"+alarmFamily10+"kpa)");
                            ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                            ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                            break;
                        case 210://水压降低@@
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.shuiyajiangdi);
                            ((ItemViewHolder) holder).smokeMacTv.setText(mNormalAlarmMessage.getName()+"(水压值:"+alarmFamily10+"kpa)");
                            ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                            ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                            break;
                    }
                    break;
                case 5:
                    ((ItemViewHolder) holder).smokeMac.setText("电气:");
                    int alarmFamily = mNormalAlarmMessage.getAlarmFamily();
                    switch (alarmType){
                        case 36:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_gz);
                            break;
                        case 43:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_gy);
                            break;
                        case 44:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_qy);
                            break;
                        case 45:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_gl);
                            break;
                        case 46:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_ld);
                            break;
                        case 47:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_gw);
                            break;
                        case 48:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_hz);//@@8.11
                            break;
                        case 143:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_gy);
                            ((ItemViewHolder) holder).smokeMacTv.setText(mNormalAlarmMessage.getName()+" (线路已断开)");
                            break;
                        case 144:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_qy);
                            ((ItemViewHolder) holder).smokeMacTv.setText(mNormalAlarmMessage.getName()+" (线路已断开)");
                            break;
                        case 145:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_gl);
                            ((ItemViewHolder) holder).smokeMacTv.setText(mNormalAlarmMessage.getName()+" (线路已断开)");
                            break;
                        case 146:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_ld);
                            ((ItemViewHolder) holder).smokeMacTv.setText(mNormalAlarmMessage.getName()+" (线路已断开)");
                            break;
                        case 147:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_gw);
                            ((ItemViewHolder) holder).smokeMacTv.setText(mNormalAlarmMessage.getName()+" (线路已断开)");
                            break;
                        case 148:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_hz);
                            break;
                        case 51:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_fz);
                            ((ItemViewHolder) holder).smokeMacTv.setText(mNormalAlarmMessage.getName()+" (线路已断开)");
                            break;
                        case 136:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_gz);
                            break;
                        case 52:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.duanlu1);//@@12.26
                            break;
                        default:
                            ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_huojing);
                            break;
                    }
                    ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    break;
                case 7:
                    ((ItemViewHolder) holder).smokeMac.setText("声光:");
                    ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.shengguang);
                    ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    break;
                case 8:
                    ((ItemViewHolder) holder).smokeMac.setText("手报:");
                    ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.shoubao);
                    ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    break;
                case 11://@@5.24
                    ((ItemViewHolder) holder).smokeMac.setText("红外:");
                    if (alarmType == 202||alarmType==206) {
                        ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.baojing);//@@8.10
                        ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                        ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    } else {
                        ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_ddy);
                        ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                        ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                    }
                    break;
                case 12://@@5.24
                    ((ItemViewHolder) holder).smokeMac.setText("门磁:");
                    if (alarmType == 202||alarmType==205) {
                        ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.baojing);//@@8.10
                        ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                        ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    } else {
                        ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_ddy);
                        ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                        ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                    }
                    break;
                case 15://@@5.24
                    ((ItemViewHolder) holder).smokeMac.setText("水浸:");
                    if (alarmType == 202||alarmType==221) {
                        ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.baojing);//@@8.10
                        ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                        ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    } else {
                        ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.xx_ddy);
                        ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                        ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.ddy_color_text));
                    }
                    break;
                case 13://@@5.24
                    ((ItemViewHolder) holder).smokeMac.setText("环境探测器:");
                    ((ItemViewHolder) holder).alarmMarkImage.setImageResource(R.drawable.baojing);//@@8.10
                    ((ItemViewHolder) holder).smokeMac.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    ((ItemViewHolder) holder).smokeMacTv.setTextColor(mContext.getResources().getColor(R.color.hj_color_text));
                    break;
            }
            RxView.clicks(((ItemViewHolder) holder).actionNowTv).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    Smoke smoke = new Smoke();
                    smoke.setLatitude(mNormalAlarmMessage.getLatitude());
                    smoke.setLongitude(mNormalAlarmMessage.getLongitude());
                    Reference<Activity> reference = new WeakReference(mContext);
                    new InitBaiduNavi(reference.get(), smoke);
                }
            });
            //取消报警
            RxView.clicks(((ItemViewHolder) holder).dealAlarmActionTv).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    collectFragmentPresenter.dealAlarm(userId, mNormalAlarmMessage.getMac(), privilege,messageModelList.indexOf(mNormalAlarmMessage));
                }
            });
            RxView.clicks(((ItemViewHolder) holder).userSmokeMarkPhoneTv).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    collectFragmentPresenter.telPhoneAction(mContext, mNormalAlarmMessage.getPrincipal1Phone());
                }
            });
            RxView.clicks(((ItemViewHolder) holder).userSmokeMarkPhoneTvTwo).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    collectFragmentPresenter.telPhoneAction(mContext, mNormalAlarmMessage.getPrincipal2Phone());
                }
            });
            holder.itemView.setTag(position);
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.footViewItemTv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.footViewItemTv.setText("正在加载更多数据...");
                    break;
                case NO_MORE_DATA:
                    T.showShort(mContext, "没有更多数据");
                    footViewHolder.footer.setVisibility(View.GONE);
                    break;
                case NO_DATA:
                    footViewHolder.footer.setVisibility(View.GONE);
                    break;
            }
        }

    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    //@@5.18
    public void setList(int index) {
        this.messageModelList.get(index).setIfDealAlarm(1);//@@5.19
        notifyDataSetChanged();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.alarm_time_tv)
        TextView alarmTimeTv;
        @Bind(R.id.smoke_mac)
        TextView smokeMac;
        @Bind(R.id.smoke_mac_tv)
        TextView smokeMacTv;
        @Bind(R.id.alarm_mark_image)
        ImageView alarmMarkImage;
        @Bind(R.id.repeater_address_tv)
        TextView repeaterAddressTv;
        @Bind(R.id.repeater_name_tv)
        TextView repeaterNameTv;
        @Bind(R.id.repeater_mac_tv)
        TextView repeaterMacTv;
        @Bind(R.id.user_smoke_mark_principal)
        TextView userSmokeMarkPrincipal;
        @Bind(R.id.user_smoke_mark_phone_tv)
        TextView userSmokeMarkPhoneTv;
        @Bind(R.id.user_smoke_mark_principal_two)
        TextView userSmokeMarkPrincipalTwo;
        @Bind(R.id.user_smoke_mark_phone_tv_two)
        TextView userSmokeMarkPhoneTvTwo;
        @Bind(R.id.deal_alarm_action_tv)
        TextView dealAlarmActionTv;
        @Bind(R.id.action_now_tv)
        TextView actionNowTv;
        @Bind(R.id.mac_tv)
        TextView mac_tv;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.foot_view_item_tv)
        TextView footViewItemTv;
        @Bind(R.id.footer)
        LinearLayout footer;
        public FootViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //添加数据
    public void addItem(List<AlarmMessageModel> alarmMessageModelList) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        alarmMessageModelList.addAll(messageModelList);
        messageModelList.removeAll(messageModelList);
        messageModelList.addAll(alarmMessageModelList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<AlarmMessageModel> alarmMessageModelList) {
        messageModelList.addAll(alarmMessageModelList);
        notifyDataSetChanged();
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
