package com.example.se_time.mall.fragment;


import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.se_time.mall.R;
import com.example.se_time.mall.adapter.HomeActAdapter;
import com.example.se_time.mall.adapter.HomeHotProductAdapter;
import com.example.se_time.mall.adapter.HomeTopBannerAndParamAdapter;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.listener.OnItemClickListener;
import com.example.se_time.mall.pojo.PageBean;
import com.example.se_time.mall.pojo.Param;
import com.example.se_time.mall.pojo.Product;
import com.example.se_time.mall.pojo.ResponseCode;
import com.example.se_time.mall.pojo.SverResponse;
import com.example.se_time.mall.ui.DetailActivity;
import com.example.se_time.mall.ui.OrderListActivity;
import com.example.se_time.mall.ui.SearchActivity;
import com.example.se_time.mall.utils.Utils;
import com.example.se_time.mall.utils.JSONUtils;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;

import static android.support.v4.content.ContextCompat.getSystemService;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private MaterialRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private List<Integer> images;
    private List<Param> mCategoryData;
    private List<Product> mProductData;
    private final int PARAM_ROW_COL=3;
    private EditText search_edit;

    private HomeTopBannerAndParamAdapter homeTopBannerAndParamAdapter;
    private HomeHotProductAdapter homeHotProductAdapter;
    private DelegateAdapter delegateAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        loadParams();
        loadHotProducts();
        bindRefreshListener();
        return view;
    }

    private void initView(View view) {
        refreshLayout=(MaterialRefreshLayout)view.findViewById(R.id.refresh_layout);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv);

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
                        Toast.makeText(getActivity(),"搜索不能为空",Toast.LENGTH_SHORT).show();
                    }
                    //hide input

                }
                return false;
            }
        });

        images=new ArrayList<>();
        images.add(R.drawable.lunbo1);
        images.add(R.drawable.lunbo2);
        images.add(R.drawable.lunbo3);

        mCategoryData=new ArrayList<>();
        mProductData=new ArrayList<>();

        VirtualLayoutManager virtualLayoutManager=new VirtualLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(virtualLayoutManager);

        List<DelegateAdapter.Adapter> adapters=new LinkedList<>();

        /*
        set banner and params
         */

        View headView=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_banner,null,false);
        Banner banner=(Banner)headView.findViewById(R.id.banner);
        //set banner's width and height
       banner.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Utils.getScreenHeight(getContext()) /4));
        //param layout settings
        GridLayoutHelper gridLayoutHelper=new GridLayoutHelper(PARAM_ROW_COL);
        gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position==0)
                    return PARAM_ROW_COL;
                else return 1;
            }
        });

        homeTopBannerAndParamAdapter=new HomeTopBannerAndParamAdapter(mCategoryData,getActivity(),gridLayoutHelper);
        homeTopBannerAndParamAdapter.setmHeadView(banner);
        adapters.add(homeTopBannerAndParamAdapter);     //加入处理banner与param的适配器，第一部分完成

        homeTopBannerAndParamAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                String id=mCategoryData.get(pos).getId()+"";
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                intent.putExtra("productTypeId",id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int pos) {

            }
        });


        /*
            activity
         */

        LinearLayoutHelper linearLayoutHelper=new LinearLayoutHelper();
        linearLayoutHelper.setMarginBottom(Utils.dp2px(getActivity(),20));
        adapters.add(new HomeActAdapter(getActivity(),linearLayoutHelper));


         /*
        hot products list
     */
        LinearLayoutHelper hotLayoutHelper=new LinearLayoutHelper();
        homeHotProductAdapter=new HomeHotProductAdapter(mProductData,getActivity(),hotLayoutHelper);
        adapters.add(homeHotProductAdapter);

        homeHotProductAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                String id=mProductData.get(pos).getId()+"";
                Intent intent=new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int pos) {

            }
        });

        //



        delegateAdapter=new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.addAdapters(adapters);          //将所有适配器集成
        mRecyclerView.setAdapter(delegateAdapter);


        //start banner
        banner.setImages(images);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(getActivity()).load(path).into(imageView);

            }
        });
        banner.start();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
        {
           // loadParams();
            loadHotProducts();
        }

    }

    /*
        加载分类数据
         */
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
                            if(result.getData()==null) {
                                return;
                            }
                            mCategoryData.clear();
                            if(result.getData().size()%PARAM_ROW_COL==0)
                            {
                                mCategoryData.addAll(result.getData());
                            }
                            else{
//                                System.out.println("debug:"+result.getData().size());
                                int count=result.getData().size()/PARAM_ROW_COL;
                                mCategoryData.addAll(result.getData().subList(0,count*PARAM_ROW_COL));
                            }
                            homeTopBannerAndParamAdapter.notifyDataSetChanged();

                        }

                    }
                });
    }

    private void  loadHotProducts()
    {
        OkHttpUtils.get()
                .url(Constant.API.HOT_PRODUCT_URL)
                .addParams("num","10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(),"网络故障，请稍后重试",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println("homeHot:"+response);
                        final Type type= new TypeToken<SverResponse<List<Product>>>(){}.getType();
                        SverResponse<List<Product>> result=JSONUtils.fromJson(response,type);
                        if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                        {
                            if(result.getData()!=null)
                            {
                                mProductData.clear();
                                mProductData.addAll(result.getData());
                                homeHotProductAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
        refreshLayout.finishRefresh();
    }

    private void bindRefreshListener()
    {
        refreshLayout.setLoadMore(true);

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新
               // loadParams();
                loadHotProducts();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //super.onRefreshLoadMore(materialRefreshLayout);
                //上拉加载更多
                refreshLayout.finishRefreshLoadMore();

            }
        });
    }
}
