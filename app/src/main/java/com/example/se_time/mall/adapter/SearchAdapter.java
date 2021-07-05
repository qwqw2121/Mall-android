package com.example.se_time.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.example.se_time.mall.R;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.listener.OnItemClickListener;
import com.example.se_time.mall.pojo.Product;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<Product> data;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public SearchAdapter(List<Product> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.activity_search_list_item,null,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.SearchViewHolder holder, int position) {
        Product product=data.get(position);

        holder.name.setText(product.getName());
        holder.price.setText("价格：￥"+product.getPrice());

        //stock 暂改为 statusDesc

        holder.stock.setText("状态："+product.getStatusDesc());
//        if(product.getStatus()==3)
//        {
//            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
//        }
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

    public class SearchViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout titleContainer;
        private RelativeLayout contentContainer;
        private TextView btn_more;
        private TextView name;
        private ImageView icon_url;
        private TextView stock;
        private TextView price;
        public SearchViewHolder(View itemView) {
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
