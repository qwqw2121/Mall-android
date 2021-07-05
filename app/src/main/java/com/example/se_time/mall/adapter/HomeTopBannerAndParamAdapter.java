package com.example.se_time.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.se_time.mall.R;
import com.example.se_time.mall.listener.OnItemClickListener;
import com.example.se_time.mall.pojo.Param;

import java.util.List;

public class HomeTopBannerAndParamAdapter extends DelegateAdapter.Adapter<HomeTopBannerAndParamAdapter.BannerAndParamViewHoler> {

    public static final int TYPE_HEADER=0;
    public static final int TYPE_NORMAL=1;


    private View mHeadView;
    private List<Param> mData;
    private Context context;
    private LayoutHelper layoutHelper;
    private OnItemClickListener onItemClickListener;

    /**
     *
     * @param mData
     * @param context
     * @param layoutHelper
     */
    public HomeTopBannerAndParamAdapter(List<Param> mData, Context context, LayoutHelper layoutHelper) {
        this.mData = mData;
        this.context = context;
        this.layoutHelper = layoutHelper;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setmHeadView(View mHeadView) {
        this.mHeadView = mHeadView;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {

        return this.layoutHelper;
    }

    /**
     * 根据位置选择视图类型,这里一共两个视图 0为banner
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

       if(mHeadView==null)
           return TYPE_NORMAL;
       if(position==0)
           return TYPE_HEADER;
       return TYPE_NORMAL;
    }

    //返回装数据的view layout
    @Override
    public HomeTopBannerAndParamAdapter.BannerAndParamViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        if(mHeadView!=null&&viewType==TYPE_HEADER)
            return new BannerAndParamViewHoler(mHeadView);

        View view=LayoutInflater.from(context).inflate(R.layout.fragment_home_params_list_item,null,false);
        return new BannerAndParamViewHoler(view);
    }

    @Override
    public void onBindViewHolder(final HomeTopBannerAndParamAdapter.BannerAndParamViewHoler holder, final int position) {

        if(getItemViewType(position)==TYPE_HEADER)
        {
            return ;
        }

        final int pos=getRealPosition(holder);
        holder.tv.setText(mData.get(pos).getName());
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null)
                {
                    onItemClickListener.onItemClick(holder.tv,position-1);
                    onItemClickListener.onItemLongClick(holder.tv,position-1);
                }
            }
        });
    }

    private int getRealPosition(BannerAndParamViewHoler holder) {

        int pos=holder.getLayoutPosition();
        return mHeadView==null?pos:pos-1;
    }

    @Override
    public int getItemCount() {
        return mHeadView==null?mData.size():mData.size()+1;
    }

    public static  class BannerAndParamViewHoler extends RecyclerView.ViewHolder {
        public TextView tv;
        public BannerAndParamViewHoler(View itemView) {
            super(itemView);
            tv=(TextView)itemView.findViewById(R.id.item_tv);
        }
    }
}
