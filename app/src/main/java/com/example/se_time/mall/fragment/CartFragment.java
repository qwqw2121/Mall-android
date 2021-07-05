package com.example.se_time.mall.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.se_time.mall.R;
import com.example.se_time.mall.TestActivity;
import com.example.se_time.mall.adapter.CartAdapter;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.listener.OnItemClickListener;
import com.example.se_time.mall.pojo.Cart;
import com.example.se_time.mall.pojo.CartItem;
import com.example.se_time.mall.pojo.ResponseCode;
import com.example.se_time.mall.pojo.SverResponse;
import com.example.se_time.mall.ui.ConfirmOrderActivity;
import com.example.se_time.mall.ui.DetailActivity;
import com.example.se_time.mall.ui.LoginActivity;
import com.example.se_time.mall.ui.OrderListActivity;
import com.example.se_time.mall.utils.JSONUtils;
import com.example.se_time.mall.utils.ToastUitl;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private MaterialRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<CartItem> mData;
    private CartAdapter cartAdapter;

    private TextView totalPrice;
    private TextView btn_buy;
    private boolean isEdit=false;
    private CheckBox checkBox;

    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_cart, container, false);
        initView(view);
        bindRefreshListener();
        return view;
    }

    private void initView(View view) {

        refreshLayout=(MaterialRefreshLayout)view.findViewById(R.id.refresh_layout);
        recyclerView=(RecyclerView)view.findViewById(R.id.cart_rv);
        totalPrice=(TextView)view.findViewById(R.id.total);
        btn_buy=(TextView)view.findViewById(R.id.btn_buy);
        checkBox=(CheckBox)view.findViewById(R.id.btn_checked);

        mData=new ArrayList<>();
        cartAdapter=new CartAdapter(getActivity(),mData);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cartAdapter);


        cartAdapter.setOnCartOptListener(new CartAdapter.OnCartOptListener() {
            @Override
            public void updateProductInfo(int productId, int count, int checked) {
                updateProduct(productId,count,checked);
            }

            @Override
            public void deleteProductFromCart(int productId) {
                deleteProductById(productId);
            }
        });

        view.findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEdit)
                {
                    isEdit=false;
                    for(CartItem item:mData)
                    {
                        item.setEdit(true);
                    }
                }
                else {
                    isEdit=true;
                    for(CartItem item:mData)
                    {
                        item.setEdit(false);
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //购买

                System.out.println("buy");
                if(!totalPrice.getText().toString().equals("合计：￥0"))
                {
                    Intent intent=new Intent(getActivity(),ConfirmOrderActivity.class);
                    startActivity(intent);
                }
                else
                {
                    ToastUitl.showToast(getActivity(),"请先选择商品");
                    //Toast.makeText(getActivity(),"请先选择商品",Toast.LENGTH_SHORT).show();
                }

            }
        });
        cartAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                //跳转商品详情页
                String id=mData.get(pos).getProductId()+"";
                Intent intent=new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int pos) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localBroadcastManager=LocalBroadcastManager.getInstance(getActivity());
        intentFilter=new IntentFilter();
        intentFilter.addAction(Constant.ACTION.LOAD_CART_ACTION);
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadData();
            }
        };
        localBroadcastManager.registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
            loadData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void loadData()
    {
        OkHttpUtils.get()
                .url(Constant.API.CART_LIST_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println("cart:"+response);
                        Type type=new TypeToken<SverResponse<Cart>>(){}.getType();
                        SverResponse<Cart> result=JSONUtils.fromJson(response,type);
                        if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                        {
                            if(result.getData().getLists()!=null)
                            {
                                mData.clear();
                                mData.addAll(result.getData().getLists());
                                cartAdapter.notifyDataSetChanged();
                            }
                            totalPrice.setText("合计：￥"+result.getData().getTotalPrice());
                        }
                        else {
                            Intent intent=new Intent(getActivity(),LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
        refreshLayout.finishRefresh();
    }
    private void deleteProductById(int prouctId)
    {
        OkHttpUtils.get()
                .url(Constant.API.CART_DELETE_URL)
                .addParams("productId",prouctId+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {


                        Type type=new TypeToken<SverResponse<Cart>>(){}.getType();
                        SverResponse<Cart> result=JSONUtils.fromJson(response,type);
                        if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                        {
                            mData.clear();
                            mData.addAll(result.getData().getLists());
                            cartAdapter.notifyDataSetChanged();
                        }
                        totalPrice.setText("合计：￥"+result.getData().getTotalPrice());
                    }
                });
    }

    private void updateProduct(int productId,int count,int checked)
    {
        OkHttpUtils.get()
                .url(Constant.API.CART_UPDATE_URL)
                .addParams("productId",productId+"")
                .addParams("count",count+"")
                .addParams("checked",checked+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse<Cart>>(){}.getType();
                        SverResponse<Cart> result = JSONUtils.fromJson(response,type);
                        System.out.println("debug:"+response+result.getStatus());
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode()){
                            loadData();
                           //if(result.getData().getLists()!=null){
                             //  mData.clear();
                             //   mData.addAll(result.getData().getLists());
                              //  cartAdapter.notifyDataSetChanged();
                        //    }
                         //   totalPrice.setText("合计：￥"+result.getData().getTotalPrice());
                        }
                        else {
                            System.out.println("fail to update");
                        }

                    }
                });
    }
    private void bindRefreshListener()
    {
        refreshLayout.setLoadMore(true);

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新
                // loadParams();
                loadData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                refreshLayout.finishRefreshLoadMore();

            }
        });
    }
}
