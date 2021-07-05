package com.example.se_time.mall.adapter;

import android.content.Context;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.se_time.mall.R;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.listener.OnItemClickListener;
import com.example.se_time.mall.pojo.CartItem;

import java.util.List;

public class CartAdapter
        extends RecyclerView.Adapter<CartAdapter.CartViewHolder>
            implements View.OnClickListener{
    private Context context;
    private List<CartItem> mData;
    private OnItemClickListener onItemClickListener;
    private OnCartOptListener onCartOptListener;
//    private TextChangeListener textChangeListener;

    public CartAdapter(Context context, List<CartItem> mData) {
        this.context = context;
        this.mData = mData;
    }

//    public void setTextChangeListener(TextChangeListener textChangeListener) {
//        this.textChangeListener = textChangeListener;
//    }

    public void setOnCartOptListener(OnCartOptListener onCartOptListener) {
        this.onCartOptListener = onCartOptListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.fragment_cart_list_item,null,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.CartViewHolder holder, final int position) {
        final CartItem cartItem=mData.get(position);
        holder.name.setText(cartItem.getName());
        holder.price.setText(cartItem.getPrice()+"");
        holder.edit_num.setText(cartItem.getQuantity()+"");
        Glide.with(context).load(Constant.API.BASE_URL+cartItem.getIconUrl()).into(holder.icon_url);

        System.out.println("cartItem icon_url:"+Constant.API.BASE_URL+cartItem.getIconUrl());
       if(cartItem.getChecked()==1)
       {
            holder.checkBox.setChecked(true);
       }
       else {
           holder.checkBox.setChecked(false);
       }

        holder.edit_num.setEnabled(false);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                System.out.println("change");
                if(onCartOptListener!=null)
                {
                    if(isChecked)
                    {
                        onCartOptListener.updateProductInfo(cartItem.getProductId(),cartItem.getQuantity(),1);
                    }
                    else{
                        onCartOptListener.updateProductInfo(cartItem.getProductId(),cartItem.getQuantity(),0);
                    }
                }
            }
        });


        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
        holder.btn_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartItem.getQuantity()-1>=1)
                {
                    if(onCartOptListener!=null)
                    {
                        onCartOptListener.updateProductInfo(cartItem.getProductId(),cartItem.getQuantity()-1,cartItem.getChecked());
                    }
                }
            }
        });
        holder.btn_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartItem.getQuantity()+1<=cartItem.getStock())
                {
                    if(onCartOptListener!=null)
                    {
                        onCartOptListener.updateProductInfo(cartItem.getProductId(),cartItem.getQuantity()+1,cartItem.getChecked());
                    }
                }
            }
        });

        if(cartItem.isEdit())
        {
            holder.delete.setVisibility(View.VISIBLE);
        }
        else {
            holder.delete.setVisibility(View.GONE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCartOptListener!=null)
                {
                    onCartOptListener.deleteProductFromCart(cartItem.getProductId());
                }
            }
        });
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

    public static class CartViewHolder extends RecyclerView.ViewHolder{

        private View itemView;
        private ImageView icon_url;
        private TextView name;
        private TextView price;
        private TextView btn_jian;
        private TextView btn_jia;
        private EditText edit_num;
        private TextView delete;
        private CheckBox checkBox;

        public CartViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            checkBox=(CheckBox)itemView.findViewById(R.id.btn_checked);
            name=(TextView)itemView.findViewById(R.id.name);
            price=(TextView)itemView.findViewById(R.id.price);
            btn_jia=(TextView)itemView.findViewById(R.id.btn_jia);
            btn_jian=(TextView)itemView.findViewById(R.id.btn_jian);
            delete=(TextView)itemView.findViewById(R.id.delete);
            icon_url=(ImageView)itemView.findViewById(R.id.icon_url);
            edit_num=(EditText)itemView.findViewById(R.id.edit_num);
        }
    }


    public interface OnCartOptListener
    {
        public void updateProductInfo(int productId,int count,int checked);
        public  void deleteProductFromCart(int productId);
    }
//    public interface TextChangeListener
//    {
//        public void afterTextChanged(int productId,CharSequence text,int checked);
//    }
}
