package com.example.se_time.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.se_time.mall.R;
import com.example.se_time.mall.listener.OnItemClickListener;

public class HomeActAdapter extends DelegateAdapter.Adapter<HomeActAdapter.ActViewHolder> {

    private Context context;
    private LayoutHelper layoutHelper;


    public HomeActAdapter(Context context, LayoutHelper layoutHelper) {
        this.context = context;
        this.layoutHelper = layoutHelper;
     }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.layoutHelper;
    }

    @Override
    public ActViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_home_act_list_item,null,false));

    }

    @Override
    public void onBindViewHolder(final ActViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }


    protected static class ActViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        public ActViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.img_act);
        }
    }
}
