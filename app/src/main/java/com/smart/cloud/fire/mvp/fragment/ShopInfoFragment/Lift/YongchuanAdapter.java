package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift;

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

import com.smart.cloud.fire.activity.GasDevice.OneGasInfoActivity;
import com.smart.cloud.fire.activity.Inspection.GPSMapActivity;
import com.smart.cloud.fire.activity.THDevice.OneTHDevInfoActivity;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.mvp.LineChart.LineChartActivity;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Devices.YCDevicesActivity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.TransmissionDevice;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.Yongchuan;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security.NewAirInfoActivity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2019/10/29.
 */
public class YongchuanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Yongchuan> listNormalSmoke;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;

    public YongchuanAdapter(Context mContext, List<Yongchuan> listNormalSmoke, ShopInfoFragmentPresenter mShopInfoFragmentPresenter) {
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
            final View view = mInflater.inflate(R.layout.yc_item, parent, false);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);//@@5.13
        if (holder instanceof ItemViewHolder) {
            final Yongchuan normalSmoke = listNormalSmoke.get(position);
            ((ItemViewHolder) holder).categoryGroupLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, YCDevicesActivity.class);
                    i.putExtra("mac", normalSmoke.getId() + "");
                    i.putExtra("name", normalSmoke.getInfotransferName() + "");
                    mContext.startActivity(i);
                }
            });

            ((ItemViewHolder) holder).tv_name.setText(normalSmoke.getInfotransferName());
            ((ItemViewHolder) holder).tv_mac.setText("ID:" + normalSmoke.getInfotransferNum());
            ((ItemViewHolder) holder).tv_address.setText("地址:"+normalSmoke.getInfotransferAddress());
            ((ItemViewHolder) holder).tv_state.setText("运行状态:"+(normalSmoke.getInfotransferRunningState().equals("1")?"正常":"测试"));
            ((ItemViewHolder) holder).tv_more.setOnClickListener(new View.OnClickListener() {
                boolean isShow=false;
                @Override
                public void onClick(View v) {
                    if(!isShow){
                        ((ItemViewHolder) holder).more_line.setVisibility(View.VISIBLE);
                        isShow=true;
                    }else{
                        ((ItemViewHolder) holder).more_line.setVisibility(View.GONE);
                        isShow=false;
                    }
                }
            });
            ((ItemViewHolder) holder).tv_fire_state.setText("火警状态:"+(normalSmoke.getInfotransferFireAlarm().equals("1")?"火警":"无火警"));
            ((ItemViewHolder) holder).tv_trouble_state.setText("故障状态:"+(normalSmoke.getInfotransferTroubleAlarm().equals("1")?"故障":"正常"));
            ((ItemViewHolder) holder).tv_mainpower_state.setText("主电状态:"+(normalSmoke.getInfotransferMainPowerTrouble().equals("1")?"故障":"正常"));
            ((ItemViewHolder) holder).tv_secondpower_state.setText("备电状态:"+(normalSmoke.getInfotransferStandbyPowerTrouble().equals("1")?"故障":"正常"));
            ((ItemViewHolder) holder).tv_channel_state.setText("信道状态:"+(normalSmoke.getInfotransferChannelTrouble().equals("1")?"故障":"正常"));
            ((ItemViewHolder) holder).tv_line_state.setText("线路状态:"+(normalSmoke.getInfotransferLineTrouble().equals("1")?"故障":"正常"));

            ((ItemViewHolder) holder).groupPrincipal1.setText(normalSmoke.getInfotransferManagerName());
            ((ItemViewHolder) holder).groupPhone1.setText(normalSmoke.getInfotransferManagerPhone());
            ((ItemViewHolder) holder).groupPhone1.setOnClickListener(new View.OnClickListener() {//拨打电话提示框。。
                @Override
                public void onClick(View v) {
                    String phoneOne = normalSmoke.getInfotransferManagerPhone();
                    mShopInfoFragmentPresenter.telPhoneAction(mContext, phoneOne);
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
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_mac)
        TextView tv_mac;
        @Bind(R.id.tv_state)
        TextView tv_state;
        @Bind(R.id.tv_address)
        TextView tv_address;
        @Bind(R.id.tv_more)
        TextView tv_more;
        @Bind(R.id.more_line)
        LinearLayout more_line;
        @Bind(R.id.tv_fire_state)
        TextView tv_fire_state;
        @Bind(R.id.tv_trouble_state)
        TextView tv_trouble_state;
        @Bind(R.id.tv_mainpower_state)
        TextView tv_mainpower_state;
        @Bind(R.id.tv_secondpower_state)
        TextView tv_secondpower_state;
        @Bind(R.id.tv_channel_state)
        TextView tv_channel_state;
        @Bind(R.id.tv_line_state)
        TextView tv_line_state;
        @Bind(R.id.group_principal1)
        TextView groupPrincipal1;
        @Bind(R.id.group_phone1)
        TextView groupPhone1;
        @Bind(R.id.category_group_lin)
        LinearLayout categoryGroupLin;

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
    public void addItem(List<Yongchuan> smokeList) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        smokeList.addAll(listNormalSmoke);
        listNormalSmoke.removeAll(listNormalSmoke);
        listNormalSmoke.addAll(smokeList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<Yongchuan> smokeList) {
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
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}

