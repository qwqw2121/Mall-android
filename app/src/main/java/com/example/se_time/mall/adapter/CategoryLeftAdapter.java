package com.example.se_time.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.se_time.mall.R;
import com.example.se_time.mall.TestActivity;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.listener.OnItemClickListener;
import com.example.se_time.mall.pojo.Param;

import java.util.List;

public class CategoryLeftAdapter
        extends RecyclerView.Adapter<CategoryLeftAdapter.CategoryViewHolder>
            implements View.OnClickListener {

    private Context context;
    private List<Param> mData;
    private OnItemClickListener onItemClickListener;

    public CategoryLeftAdapter(Context context, List<Param> data) {
        this.context = context;
        this.mData = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public CategoryLeftAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.fragment_category_left_list_item,null,false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryLeftAdapter.CategoryViewHolder holder, int position) {
        Param param=mData.get(position);
        holder.name.setText(param.getName());
        holder.name.setTag(position);
        if(param.isPressed())
        {
            holder.name.setBackgroundResource(R.color.colorPressedBackground);
        }else {
            holder.name.setBackgroundResource(R.color.colorWhite);
        }
        holder.name.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        int pos=(int)v.getTag();
        for(int i=0;i<mData.size();i++)
        {
            if(pos==i)
            {
                mData.get(i).setPressed(true);
            }
            else {
                mData.get(i).setPressed(false);
            }
        }
        notifyDataSetChanged();
        if(onItemClickListener!=null)
        {
            onItemClickListener.onItemClick(v,pos);
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.categoty_tv);

        }
    }
}
