package com.example.se_time.mall.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.se_time.mall.R;
import com.example.se_time.mall.adapter.CategoryLeftAdapter;
import com.example.se_time.mall.adapter.CategoryRightAdapter;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.listener.OnItemClickListener;
import com.example.se_time.mall.pojo.PageBean;
import com.example.se_time.mall.pojo.Param;
import com.example.se_time.mall.pojo.Product;
import com.example.se_time.mall.pojo.ResponseCode;
import com.example.se_time.mall.pojo.SverResponse;
import com.example.se_time.mall.ui.DetailActivity;
import com.example.se_time.mall.ui.SearchActivity;
import com.example.se_time.mall.utils.JSONUtils;
import com.example.se_time.mall.utils.SpaceItemDecoration;
import com.example.se_time.mall.utils.ToastUitl;
import com.example.se_time.mall.utils.Utils;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private RecyclerView leftRecyclerView;
    private List<Param>  leftCategoryData;
    private CategoryLeftAdapter categoryLeftAdapter;

    private RecyclerView rightRecyclerView;
    private List<Product>  rightCategoryData;
    private CategoryRightAdapter categoryrightAdapter;

    private MaterialRefreshLayout refreshLayout;
    private SverResponse<PageBean<Product>> result;
    private String typeId;
    private String name;
    private EditText search_edit;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_category, container, false);
        initView(view);
        bindRefreshListener();
        loadParams();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
        {

            loadParams();
        }
    }

    private  void initView(View view)
    {
        //初始化
        leftRecyclerView=(RecyclerView)view.findViewById(R.id.categoty_rv);


        search_edit=(EditText)view.findViewById(R.id.search_edit);
        search_edit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId==EditorInfo.IME_ACTION_SEARCH)
                {

                    String searchKey=search_edit.getText().toString();
                    if(!TextUtils.isEmpty(searchKey))
                    {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken() , 0);
                        Intent intent=new Intent(getActivity(),SearchActivity.class);
                        intent.putExtra("searchKey",searchKey);
                        startActivity(intent);
                    }
                    else {
                        ToastUitl.showToast(getActivity(),"搜索不能为空");
                        //Toast.makeText(getActivity(),"搜索不能为空",Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });


        leftCategoryData=new ArrayList<>();
        categoryLeftAdapter=new CategoryLeftAdapter(getActivity(),leftCategoryData);
        refreshLayout=(MaterialRefreshLayout)view.findViewById(R.id.refresh_layout);

       rightRecyclerView=(RecyclerView)view.findViewById(R.id.product_rv);
        rightCategoryData=new ArrayList<>();
        categoryrightAdapter=new CategoryRightAdapter(getActivity(),rightCategoryData);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        leftRecyclerView.setLayoutManager(linearLayoutManager);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        rightRecyclerView.addItemDecoration(new SpaceItemDecoration(Utils.dp2px(getActivity(),10),Utils.dp2px(getActivity(),5)));
        rightRecyclerView.setLayoutManager(gridLayoutManager);
        rightRecyclerView.setAdapter(categoryrightAdapter);
        categoryrightAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                //跳转到商品详情
                String id=rightCategoryData.get(pos).getId()+"";
                Intent intent=new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int pos) {

            }
        });


        //设置适配器
        leftRecyclerView.setAdapter(categoryLeftAdapter);
        categoryLeftAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                 typeId=leftCategoryData.get(pos).getId()+"";
                name=leftCategoryData.get(pos).getName();
                findProductByParams(typeId,1,10,name,true);
            }

            @Override
            public void onItemLongClick(View v, int pos) {

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
                refreshLayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //super.onRefreshLoadMore(materialRefreshLayout);
                //上拉加载更多
               // System.out.println("!!");
                if(result!=null&&result.getStatus()==ResponseCode.SUCCESS.getCode())
                {
                    PageBean pageBean=result.getData();
                    if(pageBean.getPageNum()!=pageBean.getNextPage())
                    {
                        findProductByParams(typeId,pageBean.getNextPage(),pageBean.getPageSize(),name,false);

                    }
                    else {
                        refreshLayout.finishRefreshLoadMore();
                        ToastUitl.showToast(getActivity(),"已经到底了");
                       // Toast.makeText(getActivity(),"已经到底了",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void findProductByParams(String productTypeId, int pageNum, int pageSize, String name,final boolean flag)
    {
        OkHttpUtils.get()
                .url(Constant.API.CATEGORY_PRODUCT_URL)
                .addParams("name","")
                .addParams("productTypeId",productTypeId)
                .addParams("partsId",0+"")
                .addParams("pageNum",pageNum+"")
                .addParams("pageSize",pageSize+"")

                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(String response, int id) {
                        final Type type= new TypeToken<SverResponse<PageBean<Product>>>(){}.getType();
                        result=JSONUtils.fromJson(response,type);
                        if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                        {
                            if(null!=result.getData())
                            {
                                if(flag) rightCategoryData.clear();
                                rightCategoryData.addAll(result.getData().getData());
                                categoryrightAdapter.notifyDataSetChanged();
                            }
                            if(!flag)
                            {
                               // refreshLayout.finishRefreshLoadMore();
                            }
                        }
                    }
                });
        refreshLayout.finishRefreshLoadMore();
    }



    private void loadParams()
    {
        OkHttpUtils.get()
                .url(Constant.API.CATEGOTYRY_PARAM_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println("error:"+e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        final Type type= new TypeToken<SverResponse<List<Param>>>(){}.getType();
                        SverResponse<List<Param>> result=JSONUtils.fromJson(response,type);
                        if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                        {
                            if(result.getData()==null)
                            {
                                return;
                            }
                            leftCategoryData.clear();
                            leftCategoryData.addAll(result.getData());
                            typeId=leftCategoryData.get(0).getId()+"";
                            leftCategoryData.get(0).setPressed(true);

                            findProductByParams(typeId,1,10,leftCategoryData.get(0).getName(),true);
                            categoryLeftAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


}
