package com.example.se_time.mall.config;

public class Constant {

    public static class API{
        //测试地址
        //public static final String BASE_URL="http://39.97.173.123:8080//actionmall/";
       // public static final String BASE_URL2="http://118.190.37.204:8080/mall";
        public static final String BASE_URL="http://192.168.43.72:8888/mall/";
//        public static final String BASE_URL2="http://10.0.2.2:8080/mall";
        //产品类型参数
        public static final String CATEGOTYRY_PARAM_URL=BASE_URL+"param/findallparams.do";
        //热销商品
        public static final String HOT_PRODUCT_URL=BASE_URL+"product/findhotproducts.do";
        //商品分页列表
        public static final String CATEGORY_PRODUCT_URL=BASE_URL+"product/findproducts.do";
        //获取商品详情
        public static final String PRODUCT_DETAIL_URL=BASE_URL+"product/getdetail.do";


        //购物车列表
        public static final String CART_LIST_URL=BASE_URL+"cart/findallcarts.do";
        //删除购物车中商品
        public static final String CART_DELETE_URL=BASE_URL+"cart/delcarts.do";
        //更新购物中商品的数量
        public static final String CART_UPDATE_URL=BASE_URL+"cart/updatecarts.do";
        //加入购物车
        public static final String CART_ADD_URL=BASE_URL+"cart/savecart.do";

        //创建订单
        public static final String ORDER_CREATE_URL=BASE_URL+"order/createorder.do";
        //获取订单列表
        public static final String ORDER_LIST_URL=BASE_URL+"order/getlist.do";
        //获取订单详情
        public static final String ORDER_DETAIL_URL=BASE_URL+"order/getdetail.do";
        //取消订单
        public static final String ORDER_CANCEL_URL=BASE_URL+"order/cancelorder.do";

        //获取用户信息
        public static final String USER_INFO_URL=BASE_URL+"user/getuserinfo.do";
        //登陆接口
        public static final String USER_LOGIN_URL=BASE_URL+"user/do_login.do";
        //注销
        public static final String USER_LOGOUT_URL=BASE_URL+"user/do_logout.do";
        //注册1
        public static final String USER_REGISTER_URL=BASE_URL+"user/do_register.do";

        //地址列表
        public static final String USER_ADDR_LIST_URL=BASE_URL+"addr/findaddrs.do";
        //删除地址
        public static final String USER_ADDR_DELETE_URL=BASE_URL+"addr/deladdr.do";
        //添加新地址
        public static final String USER_ADDR_ADD_URL=BASE_URL+"addr/saveaddr.do";
        //设置默认地址
        public static final String USER_ADDR_DEFAULT_URL=BASE_URL+"addr/setdefault.do";


    }

    public static class ACTION{

        public static final String LOAD_CART_ACTION="com.example.se_time.mall.LOAD_CART_ACTION";
    }
}
