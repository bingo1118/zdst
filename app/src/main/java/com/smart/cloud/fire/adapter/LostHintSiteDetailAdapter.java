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
import com.smart.cloud.fire.activity.LostHintSiteDetailBean;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security.NewAirInfoActivity;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2017/9/8.
 */
public class LostHintSiteDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<LostHintSiteDetailBean> listLostHintSiteDetailBean;

    public LostHintSiteDetailAdapter(Context mContext, List<LostHintSiteDetailBean> listLostHintSiteDetailBean) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.listLostHintSiteDetailBean = listLostHintSiteDetailBean;
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
            final View view = mInflater.inflate(R.layout.losthint_sitedetail_item, parent, false);
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
            final LostHintSiteDetailBean normalSmoke = listLostHintSiteDetailBean.get(position);

            ((ItemViewHolder) holder).worker_name_text.setText("巡检人员:"+normalSmoke.getGuardname());
            ((ItemViewHolder) holder).during_text.setText("巡检时间段:"+normalSmoke.getBegintime()+"--"+normalSmoke.getEndtime());
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
        return listLostHintSiteDetailBean.size();
    }



    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.worker_name_text)
        TextView worker_name_text;
        @Bind(R.id.during_text)
        TextView during_text;
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
    public void addItem(List<LostHintSiteDetailBean> smokeList) {
        smokeList.addAll(listLostHintSiteDetailBean);
        listLostHintSiteDetailBean.removeAll(listLostHintSiteDetailBean);
        listLostHintSiteDetailBean.addAll(smokeList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<LostHintSiteDetailBean> smokeList) {
        listLostHintSiteDetailBean.addAll(smokeList);
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

