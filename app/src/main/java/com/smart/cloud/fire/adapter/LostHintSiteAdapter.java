package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.cloud.fire.activity.Inspection.LostHintSiteDetailActivity;
import com.smart.cloud.fire.activity.LostHintSiteBean;
import com.smart.cloud.fire.global.LostHint;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2017/9/8.
 */
public class LostHintSiteAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<LostHintSiteBean> listItems;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    String lineid;
    String dateTime;

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

    public LostHintSiteAdapter(Context mContext, List<LostHintSiteBean> list,String lineid,String dateTime) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.listItems = list;
        this.lineid=lineid;
        this.dateTime=dateTime;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.losthint_site_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LostHintSiteBean item = listItems.get(position);
        ((ItemViewHolder) holder).id_text.setText("ID:"+item.getSiteid());
        ((ItemViewHolder) holder).deptname_text.setText("位置:"+item.getSitename());
        ((ItemViewHolder) holder).lostrate_text.setText("漏检数:"+item.getUnqualifiedcount());
        ((ItemViewHolder) holder).rela_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, LostHintSiteDetailActivity.class);
                intent.putExtra("lineid",lineid);
                intent.putExtra("site",item.getSiteid());
                intent.putExtra("siteName",item.getSitename());
                intent.putExtra("time",dateTime);
                mContext.startActivity(intent);
            }
        });
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.id_text)
        TextView id_text;
        @Bind(R.id.deptname_text)
        TextView deptname_text;
        @Bind(R.id.lostrate_text)
        TextView lostrate_text;
        @Bind(R.id.rela_item)
        RelativeLayout rela_item;
        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
