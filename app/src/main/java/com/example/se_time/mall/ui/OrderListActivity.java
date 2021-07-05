package com.example.se_time.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.se_time.mall.R;
import com.example.se_time.mall.adapter.ConfirmOrderProductAdapter;
import com.example.se_time.mall.adapter.ListOrderAdapter;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.listener.OnItemClickListener;
import com.example.se_time.mall.pojo.CartItem;
import com.example.se_time.mall.pojo.Order;
import com.example.se_time.mall.pojo.OrderItem;
import com.example.se_time.mall.pojo.PageBean;
import com.example.se_time.mall.pojo.ResponseCode;
import com.example.se_time.mall.pojo.SverResponse;
import com.example.se_time.mall.pojo.User;
import com.example.se_time.mall.utils.JSONUtils;
import com.example.se_time.mall.utils.SpaceItemDecoration;
import com.example.se_time.mall.utils.ToastUitl;
import com.example.se_time.mall.utils.Utils;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class OrderListActivity extends AppCompatActivity {

    private ListOrderAdapter listOrderAdapter;
    private List<Order> mData;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private int pageSize=10;
    private int pageNum=1;
    private int orderType=0;
    private MaterialRefreshLayout refreshLayout;
    private SverResponse<PageBean<Order>> result;
    private String cancelOrderNo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        initView();
        bindRefreshListener();
        loadData(pageNum,pageSize,orderType,true);
    }

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param type
     * @param flag      true 清空原来数据  false 不清空
     */
    private void loadData(int pageNo, int pageSize, int type, final boolean flag) {
        OkHttpUtils.get()
                .url(Constant.API.ORDER_LIST_URL)
                .addParams("pageSize",pageSize+"")
                .addParams("pageNo",pageNo+"")
                .addParams("status",type+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println("orderList:"+response);
                        Type type=new TypeToken<SverResponse<PageBean<Order>>>(){}.getType();
                        result=JSONUtils.fromJson(response,type);
                        if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                        {
                            if(flag)
                                mData.clear();
                            mData.addAll(result.getData().getData());
                            listOrderAdapter.notifyDataSetChanged();
                        }
                        else {
                            ToastUitl.showToast(OrderListActivity.this,result.getMsg());
                            //Toast.makeText(OrderListActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        refreshLayout.finishRefreshLoadMore();
    }

    private void bindRefreshListener()
    {
        refreshLayout.setLoadMore(true);

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新
                refreshLayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //super.onRefreshLoadMore(materialRefreshLayout);
                //上拉加载更多

                if(result!=null&&result.getStatus()==ResponseCode.SUCCESS.getCode())
                {
                    PageBean pageBean=result.getData();
                    if(pageBean.getPageNum()!=pageBean.getNextPage())
                    {
                        loadData(pageBean.getNextPage(),pageBean.getPageSize(),orderType,false);
                    }
                    else {
                        System.out.println("2");
                        refreshLayout.finishRefreshLoadMore();
                        Toast.makeText(OrderListActivity.this,"已经到底了",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initView() {

        refreshLayout=(MaterialRefreshLayout)findViewById(R.id.refresh_layout);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("我的订单");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView=findViewById(R.id.order_rv);
        mData=new ArrayList<>();
        listOrderAdapter=new ListOrderAdapter(OrderListActivity.this,mData);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(OrderListActivity.this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(Utils.dp2px(this,10),Utils.dp2px(this,10)));
        recyclerView.setAdapter(listOrderAdapter);
        listOrderAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                //跳转到订单详情
                String id=mData.get(pos).getOrderNo();
                Intent intent=new Intent(OrderListActivity.this,OrderDetailActivity.class);
                intent.putExtra("id",id);

               startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int pos) {
                cancelOrderNo=mData.get(pos).getOrderNo();
                showDialog();
            }
        });
    }
    private void showDialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(OrderListActivity.this);

        dialog.setMessage("确定取消订单吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //取消订单
                cancelOrder();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void cancelOrder() {
        OkHttpUtils.get()
                .url(Constant.API.ORDER_CANCEL_URL)
                .addParams("orderNo",cancelOrderNo)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Type type = new TypeToken<SverResponse>(){}.getType();
                        SverResponse result = JSONUtils.fromJson(response,type);
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode()){
                            Toast.makeText(OrderListActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                            loadData(1,10,orderType,true);
                        }else{
                            Toast.makeText(OrderListActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
