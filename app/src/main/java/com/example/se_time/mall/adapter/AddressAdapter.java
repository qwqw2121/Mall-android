package com.example.se_time.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.se_time.mall.R;
import com.example.se_time.mall.listener.OnItemClickListener;
import com.example.se_time.mall.pojo.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private Context context;

    private List<Address> mData;
    private OnItemClickListener onItemClickListener;
    private OnAddrOptListener onAddrOptListener;

    public void setOnAddrOptListener(OnAddrOptListener onAddrOptListener) {
        this.onAddrOptListener = onAddrOptListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AddressAdapter(Context context, List<Address> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public AddressAdapter.AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.activity_addres_list_item,null,false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressAdapter.AddressViewHolder holder, final int position) {
        final Address item=mData.get(position);
        holder.mobile.setText(item.getMobile());
        holder.name.setText(item.getName());
        holder.addr_detail.setText(item.getAddrDetail());

        if(item.getDefaultAddr()==1)
            holder.checkbox.setChecked(true);
        if(item.getDefaultAddr()==0)
            holder.checkbox.setChecked(false);

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("Change");
                if(onAddrOptListener!=null)
                {
                    if(isChecked)
                      onAddrOptListener.changeAddrDefault(item.getId());
                }
            }
        });

        holder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                if(onAddrOptListener!=null)
                {
                    onAddrOptListener.deleteItem(v,position);
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转修改
                if(onItemClickListener!=null)
                {
                    onItemClickListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnAddrOptListener{
        public void deleteItem(View v,int pos);
        public  void changeAddrDefault(int addrId);
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder{

        private View itemView;
        private TextView mobile;
        private TextView name;
        private TextView addr_detail;
        private TextView btn_del;
        private CheckBox checkbox;
        public AddressViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            mobile=(TextView)itemView.findViewById(R.id.mobile);
            name=(TextView)itemView.findViewById(R.id.name);
            addr_detail=(TextView)itemView.findViewById(R.id.addr_detail);
            btn_del=(TextView)itemView.findViewById(R.id.btn_del);
            checkbox=(CheckBox)itemView.findViewById(R.id.checkbox);
        }
    }
}
