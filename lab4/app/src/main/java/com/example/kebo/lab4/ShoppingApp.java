package com.example.kebo.lab4;

import android.app.Application;
//重载android自带的Application类为ShoppingApp，用于全局变量的访问
public class ShoppingApp extends Application {
    public ItemData item_data;
    public ItemData chart_data;
    public MainActivity.MyListViewAdapter listview_adpater;

    @Override
    public void onCreate() {
        super.onCreate();
        item_data = new ItemData(true);//商品列表中的商品
        chart_data = new ItemData(false);//购物车中的商品
    }
}
