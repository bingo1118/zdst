package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.activity.Inspection.GPSMapActivity;
import com.smart.cloud.fire.activity.THDevice.OneTHDevInfoActivity;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.mvp.LineChart.LineChartActivity;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security.AirInfoActivity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security.NewAirInfoActivity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ShopSmokeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Smoke> listNormalSmoke;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;

    public ShopSmokeAdapter(Context mContext, List<Smoke> listNormalSmoke,ShopInfoFragmentPresenter mShopInfoFragmentPresenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.listNormalSmoke = listNormalSmoke;
        this.mContext = mContext;
        this.mShopInfoFragmentPresenter = mShopInfoFragmentPresenter;
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
            final View view = mInflater.inflate(R.layout.shop_info_adapter, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            view.setOnClickListener(this);
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
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);//@@5.13
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).repeaterRela.setVisibility(View.VISIBLE);
            final Smoke normalSmoke = listNormalSmoke.get(position);
            int devType = normalSmoke.getDeviceType();
            int netStates = normalSmoke.getNetState();
            ((ItemViewHolder) holder).categoryGroupLin.setOnClickListener(null
            );
            switch (devType){
                case 57://@@
                case 55://@@嘉德烟感
                case 61://@@嘉德南京烟感
                case 58://@@嘉德移动烟感
                case 56://@@NBIot烟感
                case 41://@@NB烟感
                case 21://@@12.01 Lora烟感
                case 1://烟感。。
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.yg_yg_lx);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.yg_yg_zx);
                    }
                    break;
                case 22:
                case 23:
                case 16://@@9.29
                case 2://燃气。。
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.rq_ygtubiao_sxj_lx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.rq_ygtubiao_sxj);
                    }
                    break;
                case 53://NB电气
                case 52://@@Lara电气设备
                case 5://电气。。
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.dq_ygtubiao_slx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.dq_ygtubiao_sxj);
                    }
                    break;
                case 7://声光。。
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.sg_ygtubiao_sxj_lx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.sg_ygtubiao_sxj);
                    }
                    break;
                case 8://手动。。
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.sb_ygtubiao_sxj_lx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.sb_ygtubiao_sxj);
                    }
                    break;
                case 9://三江设备@@5.11。。
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.sjsb_ygtubiao_lx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.sjsb_ygtubiao);
                    }
                    break;
                case 125:
                case 70:
                case 42:
                case 10://水压设备@@5.11。。
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.shuiya_ygtubiao_sxj_lx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.shuiya_ygtubiao_sxj);
                    }
                    ((ItemViewHolder) holder).categoryGroupLin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, LineChartActivity.class);
                            intent.putExtra("electricMac",normalSmoke.getMac());
                            intent.putExtra("isWater","1");//@@是否为水压
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 124:
                case 69:
                case 19://水位设备@@2018.01.02
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.shuiwei_ygtubiao_sxj_lx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.shuiwei_ygtubiao_sxj);
                    }
                    ((ItemViewHolder) holder).categoryGroupLin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, LineChartActivity.class);
                            intent.putExtra("electricMac",normalSmoke.getMac());
                            intent.putExtra("isWater","2");//@@是否为水压
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 25://温湿度传感器
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.sy_ygtubiao_sxj_lx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.sy_ygtubiao_sxj);
                    }
                    ((ItemViewHolder) holder).categoryGroupLin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, OneTHDevInfoActivity.class);
                            intent.putExtra("Mac",normalSmoke.getMac());
                            intent.putExtra("Position",normalSmoke.getName());
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 11://红外设备@@5.15。。
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.hw_ygtubiao_sxj_lx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.hw_ygtubiao_sxj);
                    }
                    break;
                case 12://门磁设备@@5.15。。
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.mc_ygtubiao_sxj_lx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.mc_ygtubiao_sxj);
                    }
                    break;
                case 15://水禁设备@@8.3。。
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.sj_ygtubiao_sxj_lx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.sj_ygtubiao_sxj);
                    }
                    break;
                case 14://GPS
                    if (netStates == 0) {//设备不在线。。
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.gps_ygtubiao_sxj_lx);
                    } else {//设备在线。。
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.gps_ygtubiao_sxj);
                    }
                    ((ItemViewHolder) holder).repeaterRela.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).groupTvAddress.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).categoryGroupLin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Contact mContact = new Contact();
                            mContact.contactType = 0;
                            mContact.contactId = normalSmoke.getCamera().getCameraId();
                            mContact.contactPassword = normalSmoke.getCamera().getCameraPwd();
                            mContact.contactName = normalSmoke.getCamera().getCameraName();
                            mContact.apModeState = 1;

                            Intent intent=new Intent(mContext, GPSMapActivity.class);
                            intent.putExtra("mac",normalSmoke.getMac());
                            intent.putExtra("cameraId",normalSmoke.getCamera().getCameraId());
                            intent.putExtra("cameraPsw",normalSmoke.getCamera().getCameraPwd());
                            intent.putExtra("contact", mContact);
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 13://环境探测器@@5.15。。
                    if (netStates == 0) {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_lx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.hjtcq_ygtubiao_sxj_lx);
                    } else {
                        ((ItemViewHolder) holder).categoryGroupLin.setBackgroundResource(R.drawable.alarm_rela_zx_bg);
                        ((ItemViewHolder) holder).groupImage.setImageResource(R.drawable.hjtcq_ygtubiao_sxj);
                    }
                    if (normalSmoke.getEnviInfo()!=null){
                        ((ItemViewHolder) holder).envi_tv.setVisibility(View.VISIBLE);
                        switch (normalSmoke.getEnviInfo().getPriority()){
                            case "1":
                                ((ItemViewHolder) holder).envi_tv.setText("环境质量：优");
                                ((ItemViewHolder) holder).envi_tv.setTextColor(Color.parseColor("#2dac5a"));
                                break;
                            case "2":
                                ((ItemViewHolder) holder).envi_tv.setText("环境质量：良");
                                ((ItemViewHolder) holder).envi_tv.setTextColor(Color.parseColor("#4278d0"));
                                break;
                            case "3":
                                ((ItemViewHolder) holder).envi_tv.setText("环境质量：中");
                                ((ItemViewHolder) holder).envi_tv.setTextColor(Color.parseColor("#e7963f"));
                                break;
                            case "4":
                                ((ItemViewHolder) holder).envi_tv.setText("环境质量：差");
                                ((ItemViewHolder) holder).envi_tv.setTextColor(Color.parseColor("#dd5c3e"));
                                break;
                            default:
                                ((ItemViewHolder) holder).envi_tv.setText("环境质量：--");
                                ((ItemViewHolder) holder).envi_tv.setTextColor(Color.parseColor("#2dac5a"));
                        }

                    }//@@7.28
                    //@@5.18
                    ((ItemViewHolder) holder).categoryGroupLin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(normalSmoke.getNetState()==0){
                                Toast.makeText(mContext,"设备不在线",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent intent = new Intent(mContext, NewAirInfoActivity.class);
                            intent.putExtra("Mac",normalSmoke.getMac());
                            intent.putExtra("Position",normalSmoke.getName());
                            mContext.startActivity(intent);
                        }
                    });
                    break;
            }

            ((ItemViewHolder) holder).groupTvAddress.setText(normalSmoke.getAddress());
            ((ItemViewHolder) holder).groupTv.setText(normalSmoke.getName());
            ((ItemViewHolder) holder).macTv.setText("mac:"+normalSmoke.getMac());//@@
            ((ItemViewHolder) holder).repeaterNameTv.setText(normalSmoke.getPlaceType());
            ((ItemViewHolder) holder).repeaterMacTv.setText(normalSmoke.getAreaName());
            ((ItemViewHolder) holder).groupPrincipal1.setText(normalSmoke.getPrincipal1());
            ((ItemViewHolder) holder).groupPhone1.setText(normalSmoke.getPrincipal1Phone());
            ((ItemViewHolder) holder).groupPrincipal2.setText(normalSmoke.getPrincipal2());
            ((ItemViewHolder) holder).groupPhone2.setText(normalSmoke.getPrincipal2Phone());
            ((ItemViewHolder) holder).repeaterTv2.setText(normalSmoke.getRepeater());
            ((ItemViewHolder) holder).groupPhone1.setOnClickListener(new View.OnClickListener() {//拨打电话提示框。。
                @Override
                public void onClick(View v) {
                    String phoneOne = normalSmoke.getPrincipal1Phone();
                    mShopInfoFragmentPresenter.telPhoneAction(mContext,phoneOne);
                }
            });
            ((ItemViewHolder) holder).groupPhone2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneTwo = normalSmoke.getPrincipal2Phone();
                    mShopInfoFragmentPresenter.telPhoneAction(mContext,phoneTwo);
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
        return listNormalSmoke.size();
    }



    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.group_image)
        ImageView groupImage;
        @Bind(R.id.group_tv)
        TextView groupTv;
        @Bind(R.id.mac_tv)//@@
        TextView macTv;
        @Bind(R.id.repeater_tv2)
        TextView repeaterTv2;
        @Bind(R.id.repeater_rela)
        RelativeLayout repeaterRela;
        @Bind(R.id.group_tv_address)
        TextView groupTvAddress;
        @Bind(R.id.repeater_name_tv)
        TextView repeaterNameTv;
        @Bind(R.id.repeater_mac_tv)
        TextView repeaterMacTv;
        @Bind(R.id.group_principal1)
        TextView groupPrincipal1;
        @Bind(R.id.group_phone1)
        TextView groupPhone1;
        @Bind(R.id.group_principal2)
        TextView groupPrincipal2;
        @Bind(R.id.group_phone2)
        TextView groupPhone2;
        @Bind(R.id.category_group_lin)
        LinearLayout categoryGroupLin;
        @Bind(R.id.envi_tv)
        TextView envi_tv;//@@7.28
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
    public void addItem(List<Smoke> smokeList) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        smokeList.addAll(listNormalSmoke);
        listNormalSmoke.removeAll(listNormalSmoke);
        listNormalSmoke.addAll(smokeList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<Smoke> smokeList) {
        listNormalSmoke.addAll(smokeList);
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

    //@@5.13
    private OnItemClickListener mOnItemClickListener = null;
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
