package com.example.kebo.lab4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static com.example.kebo.lab4.Receiver.STATICACTION;

//MainActivity.java包括商品列表和购物车的实现
public class MainActivity extends AppCompatActivity {
    //声明需要用到的控件
    protected RecyclerView rv_items;
    protected ListView lv_items;
    protected ShoppingApp my_app;
    protected FloatingActionButton fab_tochart;
    protected int mode; // mode=1:商品列表;  mode=2:购物车

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化activity，初始化各种变量
        my_app = (ShoppingApp) getApplication();
        rv_items = (RecyclerView) findViewById(R.id.itemlist);
        lv_items = (ListView) findViewById(R.id.chartlist);
        fab_tochart = (FloatingActionButton) findViewById(R.id.to_chart);
        mode = 1;
        lv_items.setVisibility(View.GONE);  //购物车不显示

        //静态广播
        Intent intentbroadcast =new Intent(STATICACTION);
        sendBroadcast(intentbroadcast);

        //设置FloatingActionButton的点击事件
        fab_tochart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == 1) mode = 2;
                else mode = 1;
                if(mode == 1){  //切换显示的页面
                    lv_items.setVisibility(View.GONE);
                    rv_items.setVisibility(View.VISIBLE);
                    fab_tochart.setImageResource(R.mipmap.shoplist);
                } else {
                    lv_items.setVisibility(View.VISIBLE);
                    rv_items.setVisibility(View.GONE);
                    fab_tochart.setImageResource(R.mipmap.mainpage);
                }
            }

        });

        // Item Part.
        MyRcAdpater rcad = new MyRcAdpater(MainActivity.this, R.layout.view_item,
                new ArrayList<>(my_app.item_data.data), new ArrayList<>(my_app.item_data.itemid)) {
            @Override   //用adapter填充listview
            public void convert(MyRcViewHolder vh, Map<String, Object> m) {
                TextView icon = vh.getView(R.id.item_icon);
                TextView name = vh.getView(R.id.item_name);
                TextView price = vh.getView(R.id.item_price);
                name.setText(m.get("name").toString());
                icon.setText(m.get("firstletter").toString());
                price.setText("");
            }
        };
        //设置商品列表Item的点击属性
        rcad.setOnItemClickListener(new MyOnItemClickListener() {
            @Override   //单击进入商品详情
            public void onClick(MyRcAdpater rcad, int position) {
                Intent it_item = new Intent();
                it_item.setClass(MainActivity.this, DetailActivity.class);
                it_item.putExtra("itemid", rcad.ad_dataid.get(position));
                startActivity(it_item);
            }

            @Override   //长按移除商品
            public void onLongClick(MyRcAdpater rcad, int position) {
                rcad.ad_dataid.remove(position);
                rcad.ad_data.remove(position);
                rcad.notifyItemRemoved(position);
                rcad.notifyItemRangeChanged(position, rcad.ad_data.size());
                Toast.makeText(rcad.ad_ctx,
                        "移除第 " + position + "个商品", Toast.LENGTH_SHORT).show();
            }
        });
        //设置RecyclerView
        LinearLayoutManager ly_man = new LinearLayoutManager(MainActivity.this);
        rv_items.setLayoutManager(ly_man);
        ly_man.setOrientation(OrientationHelper.VERTICAL);
        rv_items.setAdapter(rcad);
        rv_items.setItemAnimator(new DefaultItemAnimator());

        // Chart Part.
        final MyListViewAdapter lvad = new MyListViewAdapter(MainActivity.this,
                my_app.chart_data.data, my_app.chart_data.itemid);
        my_app.listview_adpater = lvad;
        lv_items.setAdapter(lvad);
        //设置购物车中item的单击属性
        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    Intent it_item = new Intent();
                    it_item.setClass(MainActivity.this, DetailActivity.class);
                    it_item.putExtra("itemid", my_app.chart_data.itemid.get(position));
                    startActivity(it_item);
                }
            }
        });
        //设置长按属性
        lv_items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                //弹出对话框
                if(position != 0) {
                    AlertDialog.Builder ad_builder;
                    ad_builder = new AlertDialog.Builder(MainActivity.this);
                    ad_builder
                            .setTitle("移除商品")
                            .setMessage("从购物车移除"
                                    + my_app.chart_data.data.get(position).get("name").toString()
                                    + "？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    my_app.chart_data.data.remove(position);
                                    my_app.chart_data.itemid.remove(position);
                                    my_app.chart_data.stared.remove(position);
                                    lvad.notifyDataSetChanged();
                                }
                            }).create().show();
                }
                return true;
            }
        });
        //注册EventBus
        EventBus.getDefault().register(this);
    }
    //当mainActivity启动之后收到intent的响应
    @Override
    protected void onNewIntent(Intent intent) {
        mode = 2;   //切换MainActivity显示的内容为购物车
        lv_items.setVisibility(View.VISIBLE);
        rv_items.setVisibility(View.GONE);
        fab_tochart.setImageResource(R.mipmap.mainpage);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //EventBus的订阅者方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Integer iid) {
        int idx = my_app.item_data.getIndex(iid);
        my_app.chart_data.add(  //将当前商品加入购物车的列表中
                my_app.item_data.data.get(idx),
                my_app.item_data.stared.get(idx),
                my_app.item_data.itemid.get(idx),
                my_app.item_data.imgid.get(idx)
        );
        ((MyListViewAdapter)lv_items.getAdapter()).notifyDataSetChanged();
    }

    public interface MyOnItemClickListener {
        //重载item点击事件方法
        void onClick(MyRcAdpater rcad, int position);
        void onLongClick(MyRcAdpater rcad, int position);
    }

    public class MyRcAdpater extends RecyclerView.Adapter<MyRcAdpater.MyRcViewHolder> {
    //自定义RecyclerView Adapter
        ArrayList<Map<String, Object>> ad_data;
        ArrayList<Integer> ad_dataid;
        Context ad_ctx;
        int ad_lid;
        MyOnItemClickListener ad_click;
        //重载构造函数
        public MyRcAdpater(Context ctx, int lay_id, ArrayList<Map<String, Object>> data,
                           ArrayList<Integer> data_id) {
            ad_ctx = ctx;
            ad_lid = lay_id;
            ad_data = data;
            ad_dataid = data_id;
        }
        //重载convert()方法
        public void convert(MyRcViewHolder vh, Map<String, Object> m) {
        }
        //重载点击事件
        public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
            ad_click = onItemClickListener;
        }
        //绑定数据到正确的item视图上
        @Override
        public void onBindViewHolder(final MyRcViewHolder vh, int pos) {
            convert(vh, ad_data.get(pos));
            if (ad_click != null) {
                vh.v_me.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad_click.onClick(MyRcAdpater.this, vh.getAdapterPosition());
                    }
                });
                vh.v_me.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ad_click.onLongClick(MyRcAdpater.this, vh.getAdapterPosition());
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return ad_data.size();
        }
        //自定义RecyclerView.ViewHolder
        @Override   //创建Item视图，返回相应的ViewHolder
        public MyRcViewHolder onCreateViewHolder(ViewGroup vg, int v_type) {
            return makeMyRcViewHolder(ad_ctx, vg, ad_lid);
        }

        public class MyRcViewHolder extends RecyclerView.ViewHolder {
            private SparseArray<View> v_sub;
            private View v_me;

            public MyRcViewHolder(View v_item) {
                super(v_item);
                v_me = v_item;
                v_sub = new SparseArray<>();
            }
            //获取ViewHolder实例
            public <T extends View> T getView(int v_id) {
                View v = v_sub.get(v_id);
                if (v == null) {
                    v = v_me.findViewById(v_id);
                    v_sub.put(v_id, v);
                }
                return (T) v;
            }
        }

        public MyRcViewHolder makeMyRcViewHolder(Context ctx, ViewGroup parent, int lay_id) {
            return new MyRcViewHolder(LayoutInflater.from(ctx).inflate(lay_id, parent, false));
        }
    }
    //自定义ListViewAdapter
    public class MyListViewAdapter extends BaseAdapter {
        Context lv_ctx;
        ArrayList<Map<String, Object>> lv_data;
        ArrayList<Integer> lv_dataid;

        public MyListViewAdapter(Context ctx, ArrayList<Map<String, Object>> data,
                                 ArrayList<Integer> data_id) {
            lv_ctx = ctx;
            lv_data = data;
            lv_dataid = data_id;
        }

        @Override
        public int getCount() {
            return lv_data.size();
        }

        @Override
        public Map<String, Object> getItem(int i) {
            return lv_data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        //getView()方法
        @Override
        public View getView(int i, View v, ViewGroup vg) {
            View ret;
            MyLvViewHolder lv_vh;
            if (v == null) {
                ret = LayoutInflater.from(lv_ctx).inflate(R.layout.view_item, null);
                lv_vh = new MyLvViewHolder();
                lv_vh.name = (TextView) ret.findViewById(R.id.item_name);
                lv_vh.first = (TextView) ret.findViewById(R.id.item_icon);
                lv_vh.price = (TextView) ret.findViewById(R.id.item_price);
                ret.setTag(lv_vh);
            } else {
                ret = v;
                lv_vh = (MyLvViewHolder) ret.getTag();
            }
            lv_vh.name.setText(lv_data.get(i).get("name").toString());
            lv_vh.first.setText(lv_data.get(i).get("firstletter").toString());
            lv_vh.price.setText(lv_data.get(i).get("price").toString());

            return ret;
        }

        public class MyLvViewHolder {
            public TextView first;
            public TextView name;
            public TextView price;
        }
    }
}
