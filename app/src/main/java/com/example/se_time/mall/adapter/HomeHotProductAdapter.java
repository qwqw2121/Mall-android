package com.example.se_time.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.example.se_time.mall.R;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.listener.OnItemClickListener;
import com.example.se_time.mall.pojo.Product;

import java.util.List;

public class HomeHotProductAdapter extends DelegateAdapter.Adapter<HomeHotProductAdapter.HotProductViewholder>{

    private List<Product> data;
    private Context context;
    private LayoutHelper layoutHelper;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HomeHotProductAdapter(List<Product> data, Context context, LayoutHelper layoutHelper) {
        this.data = data;
        this.context = context;
        this.layoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.layoutHelper;
    }

    @Override
    public HomeHotProductAdapter.HotProductViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.fragment_home_hot_list_item,null,false);
        return new HotProductViewholder(view);
    }

    @Override
    public void onBindViewHolder(HomeHotProductAdapter.HotProductViewholder holder, int position) {

        if(position==0)
        {
            holder.titleContainer.setVisibility(View.VISIBLE);
        }
        else {
            holder.titleContainer.setVisibility(View.GONE);
        }
        Product product=data.get(position);

        holder.name.setText(product.getName());
        holder.price.setText("价格：￥"+product.getPrice());
        holder.stock.setText("库存："+product.getStock());
        holder.contentContainer.setTag(position);
       // System.out.println(Constant.API.BASE_URL2+product.getIconUrl());
        Glide.with(context).load(Constant.API.BASE_URL+product.getIconUrl()).into(holder.icon_url);

        holder.contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null)
                {
                    onItemClickListener.onItemClick(v,(int)v.getTag());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class HotProductViewholder extends RecyclerView.ViewHolder{

        private RelativeLayout titleContainer;
        private RelativeLayout contentContainer;
        private TextView btn_more;
        private TextView name;
        private ImageView icon_url;
        private TextView stock;
        private TextView price;

        public HotProductViewholder(View itemView) {
            super(itemView);
            titleContainer=(RelativeLayout)itemView.findViewById(R.id.title_containnr);
            contentContainer=(RelativeLayout)itemView.findViewById(R.id.content);
            btn_more=(TextView)itemView.findViewById(R.id.btn_more);
            name=(TextView)itemView.findViewById(R.id.name);
            stock=(TextView)itemView.findViewById(R.id.stock);
            price=(TextView)itemView.findViewById(R.id.price);
            icon_url=(ImageView)itemView.findViewById(R.id.icon_url);
        }
    }
}
