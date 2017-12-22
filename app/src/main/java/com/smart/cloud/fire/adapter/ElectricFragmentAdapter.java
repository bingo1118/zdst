package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Electric;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ElectricFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Electric> listNormalSmoke;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(Electric)v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , Electric data);
    }

    public ElectricFragmentAdapter(Context mContext, List<Electric> listNormalSmoke, ShopInfoFragmentPresenter mShopInfoFragmentPresenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.listNormalSmoke = listNormalSmoke;
        this.mContext = mContext;
        this.mShopInfoFragmentPresenter = mShopInfoFragmentPresenter;
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
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.electric_adapter, parent, false);
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
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).repeaterRela.setVisibility(View.VISIBLE);
            final Electric normalSmoke = listNormalSmoke.get(position);
            ((ItemViewHolder) holder).address.setText(normalSmoke.getAddress());
            ((ItemViewHolder) holder).groupTv.setText(normalSmoke.getName());
            ((ItemViewHolder) holder).repeaterNameTv.setText(normalSmoke.getPlaceType());
            ((ItemViewHolder) holder).repeaterMacTv.setText(normalSmoke.getAreaName());
            final int state = normalSmoke.getNetState();

            final int privilege = MyApp.app.getPrivilege();
            final int eleState = normalSmoke.getEleState();
            //if(privilege==3){//@@8.28权限3有切换电源功能
                switch (eleState){
                    case 1:
                        ((ItemViewHolder) holder).power_button.setVisibility(View.VISIBLE);
                        ((ItemViewHolder) holder).power_button.setImageResource(R.drawable.sblb_qddy);
                        ((ItemViewHolder) holder).bhq_state_rela.setVisibility(View.VISIBLE);//@@10.9
                        ((ItemViewHolder) holder).bhq_state.setBackgroundResource(R.drawable.hezha);//@@10.9
                        break;
                    case 2:
                        ((ItemViewHolder) holder).power_button.setVisibility(View.VISIBLE);
                        ((ItemViewHolder) holder).power_button.setImageResource(R.drawable.sblb_yqd);
                        ((ItemViewHolder) holder).bhq_state_rela.setVisibility(View.VISIBLE);//@@10.9
                        ((ItemViewHolder) holder).bhq_state.setBackgroundResource(R.drawable.fenzha);//@@10.9
                        break;
                    default:
                        ((ItemViewHolder) holder).power_button.setVisibility(View.GONE);
                        ((ItemViewHolder) holder).bhq_state_rela.setVisibility(View.INVISIBLE);//@@10.9
                        break;
                }
//            }

            switch (state){
                case 0:
//                    ((ItemViewHolder) holder).state.setText("离线");
                    if(normalSmoke.getIsFault()!=null){
                        if(normalSmoke.getIsFault().equals("1")){
                            ((ItemViewHolder) holder).state.setText("离线(故障)");
                        }else{
                            ((ItemViewHolder) holder).state.setText("离线");
                        }
                    }else{
                        ((ItemViewHolder) holder).state.setText("离线");
                    }
                    ((ItemViewHolder) holder).bhq_state.setBackgroundResource(R.drawable.fenzha);//@@12.14
                    break;
                case 1:
                    if(normalSmoke.getIsFault()!=null){
                        if(normalSmoke.getIsFault().equals("1")){
                            ((ItemViewHolder) holder).state.setText("在线(故障)");
                            ((ItemViewHolder) holder).bhq_state.setBackgroundResource(R.drawable.fenzha);//@@12.14
                        }else{
                            ((ItemViewHolder) holder).state.setText("在线");
                        }
                    }else{
                        ((ItemViewHolder) holder).state.setText("在线");
                    }
                    break;
                default:
                    break;
            }

            ((ItemViewHolder) holder).power_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(state==0){
                        Toast.makeText(mContext,"设备不在线",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(privilege!=3&&privilege!=4){
                        Toast.makeText(mContext,"您没有该权限",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(eleState!=1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("如未排除故障，合闸将造成严重事故!");
                        builder.setTitle("警告");
                        builder.setPositiveButton("我已知晓", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                changepower(2,normalSmoke);
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }else{
                        changepower(1,normalSmoke);
                    }

                }
            });
            ((ItemViewHolder) holder).installTime.setText(normalSmoke.getAddSmokeTime());
            String phoneOne = normalSmoke.getPrincipal1Phone();
            String phoneTwo = normalSmoke.getPrincipal2Phone();
            if(phoneOne!=null&&phoneOne.length()>0){
                ((ItemViewHolder) holder).relateManOne.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).groupPrincipal1.setText(normalSmoke.getPrincipal1());
                ((ItemViewHolder) holder).groupPhone1.setText(normalSmoke.getPrincipal1Phone());
            }else{
                ((ItemViewHolder) holder).relateManOne.setVisibility(View.GONE);
            }
            if(phoneTwo!=null&&phoneTwo.length()>0){
                ((ItemViewHolder) holder).relateManTwo.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).groupPrincipal2.setText(normalSmoke.getPrincipal2());
                ((ItemViewHolder) holder).groupPhone2.setText(normalSmoke.getPrincipal2Phone());
            }else{
                ((ItemViewHolder) holder).relateManTwo.setVisibility(View.GONE);
            }
            ((ItemViewHolder) holder).electricId.setText(normalSmoke.getMac());
            ((ItemViewHolder) holder).repeaterTv2.setText(normalSmoke.getRepeater());
            ((ItemViewHolder) holder).groupPhone1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneOne = normalSmoke.getPrincipal1Phone();
                    mShopInfoFragmentPresenter.telPhoneAction(mContext, phoneOne);
                }
            });
            ((ItemViewHolder) holder).groupPhone2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneTwo = normalSmoke.getPrincipal2Phone();
                    mShopInfoFragmentPresenter.telPhoneAction(mContext, phoneTwo);
                }
            });
            holder.itemView.setTag(normalSmoke);
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

    public void changepower(final int eleState, final Electric normalSmoke){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if(eleState==1){
            builder.setMessage("确认切断电源吗？");
        }else{
            builder.setMessage("隐患已解决，确定合闸？");
        }
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userID = SharedPreferencesManager.getInstance().getData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTNAME);
                RequestQueue mQueue = Volley.newRequestQueue(mContext);
                String url="";
                if(eleState==1){
                    url= ConstantValues.SERVER_IP_NEW+"fireSystem/ackControl?smokeMac="+normalSmoke.getMac()+"&eleState=2&userId="+userID;
                }else{
                    url=ConstantValues.SERVER_IP_NEW+"fireSystem/ackControl?smokeMac="+normalSmoke.getMac()+"&eleState=1&userId="+userID;
                }
                Toast.makeText(mContext,"设置中，请稍候",Toast.LENGTH_SHORT).show();
                StringRequest stringRequest = new StringRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    int errorCode=jsonObject.getInt("errorCode");
                                    if(errorCode==0){
                                        switch (eleState){
                                            case 2:
                                                normalSmoke.setEleState(1);
                                                break;
                                            case 1:
                                                normalSmoke.setEleState(2);
                                                break;
                                        }
                                        notifyDataSetChanged();
                                        Toast.makeText(mContext,jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(mContext,jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext,"设置超时",Toast.LENGTH_SHORT).show();
                    }
                });
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                mQueue.add(stringRequest);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
        @Bind(R.id.electric_id)
        TextView electricId;
        @Bind(R.id.install_time)
        TextView installTime;
        @Bind(R.id.state_tv)
        TextView stateTv;
        @Bind(R.id.state)
        TextView state;
        @Bind(R.id.repeater_tv1)
        TextView repeaterTv1;
        @Bind(R.id.repeater_tv2)
        TextView repeaterTv2;
        @Bind(R.id.repeater_rela)
        RelativeLayout repeaterRela;
        @Bind(R.id.address_tv)
        TextView addressTv;
        @Bind(R.id.address)
        TextView address;
        @Bind(R.id.repeater_name)
        TextView repeaterName;
        @Bind(R.id.repeater_name_tv)
        TextView repeaterNameTv;
        @Bind(R.id.zjq_name)
        RelativeLayout zjqName;
        @Bind(R.id.repeater_mac)
        TextView repeaterMac;
        @Bind(R.id.repeater_mac_tv)
        TextView repeaterMacTv;
        @Bind(R.id.zjq_mac)
        RelativeLayout zjqMac;
        @Bind(R.id.group_principal1)
        TextView groupPrincipal1;
        @Bind(R.id.category_group_phone)
        ImageView categoryGroupPhone;
        @Bind(R.id.group_phone1)
        TextView groupPhone1;
        @Bind(R.id.phone_lin1)
        LinearLayout phoneLin1;
        @Bind(R.id.relate_man_one)
        LinearLayout relateManOne;
        @Bind(R.id.group_principal2)
        TextView groupPrincipal2;
        @Bind(R.id.category_group_phone2)
        ImageView categoryGroupPhone2;
        @Bind(R.id.group_phone2)
        TextView groupPhone2;
        @Bind(R.id.phone_lin2)
        LinearLayout phoneLin2;
        @Bind(R.id.relate_man_two)
        LinearLayout relateManTwo;
        @Bind(R.id.category_group_lin)
        LinearLayout categoryGroupLin;
        @Bind(R.id.power_button)
        ImageView power_button;//@@切换电源按钮
        @Bind(R.id.bhq_state)
        ImageView bhq_state;//@@10.9 保护器状态
        @Bind(R.id.bhq_state_rela)
        RelativeLayout bhq_state_rela;//@@10.9 保护器状态

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
    public void addItem(List<Electric> smokeList) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        smokeList.addAll(listNormalSmoke);
        listNormalSmoke.removeAll(listNormalSmoke);
        listNormalSmoke.addAll(smokeList);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<Electric> smokeList) {
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

}
