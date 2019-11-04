package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.cloud.fire.global.ElectricValue;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Devices.YCDevicesPresenter;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Lift.Entity.TransmissionDevice;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2019/10/30.
 */
public class YCDevicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<TransmissionDevice> electricList;
    private YCDevicesPresenter electricPresenter;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private int ElectricOne=0;
    private int ElectricTwo=0;
    private int ElectricFour=0;
    private int ElectricThree = 0;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (ElectricValue.ElectricValueBean) v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ElectricValue.ElectricValueBean data);
    }

    public YCDevicesAdapter(Context mContext, List<TransmissionDevice> electricList, YCDevicesPresenter electricPresenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.electricList = electricList;
        this.mContext = mContext;
        this.electricPresenter = electricPresenter;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
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
        View view = mInflater.inflate(R.layout.ycdevices_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        TransmissionDevice normalSmoke = electricList.get(position);

        ((ItemViewHolder) holder).tv_name.setText(normalSmoke.getDeviceName());
        ((ItemViewHolder) holder).tv_mac.setText("ID:" + normalSmoke.getId());
        ((ItemViewHolder) holder).tv_address.setText("地址:"+normalSmoke.getDeviceAddress());
        if(normalSmoke.getDeviceRunningState()==null){
            ((ItemViewHolder) holder).tv_state.setVisibility(View.GONE);
        }else{
            ((ItemViewHolder) holder).tv_state.setText("运行状态:"+(normalSmoke.getDeviceRunningState().equals("1")?"正常":"测试"));
        }
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

        if(normalSmoke.getDeviceFireAlarm()==null){
            ((ItemViewHolder) holder).tv_fire_state.setText("火警状态:无数据");
        }else{
            ((ItemViewHolder) holder).tv_fire_state.setText("火警状态:"+(normalSmoke.getDeviceFireAlarm().equals("1")?"火警":"无火警"));
        }
        if(normalSmoke.getDeviceTroubleAlarm()==null){
            ((ItemViewHolder) holder).tv_trouble_state.setText("故障状态:无数据");
        }else{
            ((ItemViewHolder) holder).tv_trouble_state.setText("故障状态:"+(normalSmoke.getDeviceTroubleAlarm().equals("1")?"故障":"正常"));
        }
        if(normalSmoke.getDeviceShieldAlarm()==null){
            ((ItemViewHolder) holder).tv_mainpower_state.setText("屏蔽状态:无数据");
        }else{
            ((ItemViewHolder) holder).tv_mainpower_state.setText("屏蔽状态:"+(normalSmoke.getDeviceShieldAlarm().equals("1")?"屏蔽":"无"));
        }
        if(normalSmoke.getDeviceSuperviseAlarm()==null){
            ((ItemViewHolder) holder).tv_secondpower_state.setText("监管状态:无数据");
        }else{
            ((ItemViewHolder) holder).tv_secondpower_state.setText("监管状态:"+(normalSmoke.getDeviceSuperviseAlarm().equals("1")?"监管":"无"));
        }
        if(normalSmoke.getDeviceStartUp()==null){
            ((ItemViewHolder) holder).tv_channel_state.setText("启动状态:无数据");
        }else{
            ((ItemViewHolder) holder).tv_channel_state.setText("启动状态:"+(normalSmoke.getDeviceStartUp().equals("1")?"启动":"停止"));
        }
        if(normalSmoke.getDevicePowerTrouble()==null){
            ((ItemViewHolder) holder).tv_line_state.setText("电源状态:无数据");
        }else{
            ((ItemViewHolder) holder).tv_line_state.setText("电源状态:"+(normalSmoke.getDevicePowerTrouble().equals("1")?"故障":"正常"));
        }


        holder.itemView.setTag(normalSmoke);
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return electricList.size();
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
        @Bind(R.id.category_group_lin)
        LinearLayout categoryGroupLin;
        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //添加数据
    public void addItem(List<TransmissionDevice> electrics) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        electrics.addAll(electricList);
        electricList.removeAll(electricList);
        electricList.addAll(electrics);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<TransmissionDevice> electrics) {
        electricList.addAll(electrics);
        notifyDataSetChanged();
    }

    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }

}
