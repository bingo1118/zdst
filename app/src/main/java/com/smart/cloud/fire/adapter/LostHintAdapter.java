package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smart.cloud.fire.global.Attendance;
import com.smart.cloud.fire.global.LostHint;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2017/8/2.
 */
public class LostHintAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<LostHint> listItems;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int data);
    }

    public LostHintAdapter(Context mContext, List<LostHint> list) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.listItems = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.losthint_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LostHint item = listItems.get(position);
        ((ItemViewHolder) holder).time_text.setText("时间:"+item.getLostdate());
        ((ItemViewHolder) holder).deptname_text.setText("区域："+item.getDeptname());
        ((ItemViewHolder) holder).lostrate_text.setText("漏检数："+item.getLostrate());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.time_text)
        TextView time_text;
        @Bind(R.id.deptname_text)
        TextView deptname_text;
        @Bind(R.id.lostrate_text)
        TextView lostrate_text;
        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
