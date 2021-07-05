package com.example.se_time.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.se_time.mall.R;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.pojo.CartItem;

import java.util.List;

public class ConfirmOrderProductAdapter extends RecyclerView.Adapter<ConfirmOrderProductAdapter.ConfirmOrderViewHolder> {

    private Context context;

    private List<CartItem> mData;
    private boolean showAll = false;        //显示所有产品


    public ConfirmOrderProductAdapter(Context context, List<CartItem> mData) {
        this.context = context;
        this.mData = mData;

    }

    public void setmData(List<CartItem> mData) {
        this.mData = mData;
    }
    @Override
    public ConfirmOrderProductAdapter.ConfirmOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_confirm_order_item, null, false);

        return new ConfirmOrderViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ConfirmOrderProductAdapter.ConfirmOrderViewHolder holder, int position) {
        System.out.println("!!!!confirmAdapter onbindViewHolder:");
        CartItem item = mData.get(position);

        if(showAll)
        {
            holder.product_name.setText(item.getName());
            holder.price.setText(item.getPrice() + "");
            holder.num.setText("X" + item.getQuantity() + "");
            Glide.with(context).load(Constant.API.BASE_URL + item.getIconUrl()).into(holder.icon_url);
        }
        else {
            if (item.getChecked() == 1 ) {
                holder.product_name.setText(item.getName());
                holder.price.setText(item.getPrice() + "");
                holder.num.setText("X" + item.getQuantity() + "");
                Glide.with(context).load(Constant.API.BASE_URL + item.getIconUrl()).into(holder.icon_url);
            }
        }
    }

    @Override
    public int getItemCount() {
//        for (int i = 0; i < mData.size(); i++) {
//            System.out.println("data:" + mData.get(i).getName()+" "+mData.get(i).getQuantity()+" "+mData.get(i).getPrice());
//        }
        if(!isShowAll())
       {
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getChecked() == 0) {
                    mData.remove(i);
                }
            }
        }
        return mData.size();
    }

    public boolean isShowAll() {
        return showAll;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    public static class ConfirmOrderViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView icon_url;
        private TextView price;
        private TextView product_name;
        private TextView num;

        public ConfirmOrderViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            icon_url = (ImageView) itemView.findViewById(R.id.icon_url);
            price = (TextView) itemView.findViewById(R.id.price);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            num = (TextView) itemView.findViewById(R.id.num);

        }
    }
}
