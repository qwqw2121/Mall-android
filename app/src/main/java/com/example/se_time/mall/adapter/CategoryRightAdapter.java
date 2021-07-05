package com.example.se_time.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.se_time.mall.R;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.listener.OnItemClickListener;
import com.example.se_time.mall.pojo.Product;

import java.util.List;

public class CategoryRightAdapter
        extends RecyclerView.Adapter<CategoryRightAdapter.ProductViewHolder>
            implements View.OnClickListener{

    private Context context;
    private List<Product> mData;
    private OnItemClickListener onItemClickListener;


    public CategoryRightAdapter(Context context, List<Product> mData) {
        this.context = context;
        this.mData = mData;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public CategoryRightAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.fragment_category_right_list_item,null,false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryRightAdapter.ProductViewHolder holder, int position) {
        Product product=mData.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice()+"");
        Glide.with(context).load(Constant.API.BASE_URL+product.getIconUrl()).into(holder.icon_url);
        System.out.println(Constant.API.BASE_URL+product.getIconUrl());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if(onItemClickListener!=null)
        {
            onItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        private View itemView;
        private TextView name;
        private TextView price;
        private ImageView icon_url;

        public ProductViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            name=(TextView)itemView.findViewById(R.id.name);
            price=(TextView)itemView.findViewById(R.id.price);
            icon_url=(ImageView)itemView.findViewById(R.id.icon_url);
        }
    }
}
