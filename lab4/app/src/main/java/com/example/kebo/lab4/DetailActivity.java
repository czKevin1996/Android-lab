package com.example.kebo.lab4;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import static com.example.kebo.lab4.Receiver.DYNAMICACTION;

//商品详情页面
public class DetailActivity extends AppCompatActivity {
    ShoppingApp my_app;
    TextView t_price;
    TextView t_name;
    TextView t_type;
    TextView t_info;
    Button b_addchart;
    Button b_back;
    Button b_star;
    ImageView i_img;
    ListView lv_more;
    ListView lv_op;
    int idx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //初始化商品详情这个activity
        setContentView(R.layout.activity_detail);   //从xml文件加载布局
        //注册动态广播
        registerReceiver();
        my_app = (ShoppingApp) getApplication();    //读取全局变量
        final int iid = getIntent().getIntExtra("itemid", 0);
        //Intent传递的内容
        idx = my_app.item_data.getIndex(iid);
        //findViewById()方法获取xml布局中的元素
        t_price = (TextView) findViewById(R.id.price);
        t_name = (TextView) findViewById(R.id.name);
        t_type = (TextView) findViewById(R.id.type);
        t_info = (TextView) findViewById(R.id.ifo);
        b_addchart = (Button) findViewById(R.id.addchart);
        b_back = (Button) findViewById(R.id.back);
        b_star = (Button) findViewById(R.id.star);
        i_img = (ImageView) findViewById(R.id.itemimg);
        lv_more = (ListView) findViewById(R.id.more);
        lv_op = (ListView) findViewById(R.id.operation);
        //获取ItemData商品详情
        t_price.setText(my_app.item_data.data.get(idx).get("price").toString());
        t_name.setText(my_app.item_data.data.get(idx).get("name").toString());
        t_type.setText(my_app.item_data.data.get(idx).get("infotype").toString());
        t_info.setText(my_app.item_data.data.get(idx).get("infovalue").toString());
        i_img.setImageResource(my_app.item_data.imgid.get(idx));

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //用finish()方法设置返回按钮
                finish();
            }
        });

        update_star();  //更新收藏星星状态
        b_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //设置点击收藏按钮事件
                int val = my_app.item_data.stared.get(idx);
                if (val == 1) val = 0;
                else val = 1;
                my_app.item_data.stared.set(idx, val);
                update_star();
            }
        });
        b_addchart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //设置添加购物车按钮事件
//                my_app.chart_data.add(  //将当前商品加入购物车的列表中
//                        my_app.item_data.data.get(idx),
//                        my_app.item_data.stared.get(idx),
//                        my_app.item_data.itemid.get(idx),
//                        my_app.item_data.imgid.get(idx)
//                );
                EventBus.getDefault().post(iid);    //用EventBus发送itemid
                Intent intentbroadcast = new Intent(DYNAMICACTION);
                intentbroadcast.putExtra("itemid", iid);
                sendBroadcast(intentbroadcast); //发送动态广播
                my_app.listview_adpater.notifyDataSetChanged(); //刷新列表
                Toast.makeText(DetailActivity.this,
                        "商品已经添加到购物车", Toast.LENGTH_SHORT).show();
            }

        });
        final String[] s_more = {"更多产品信息"}; //商品详情页的操作按钮，以Array形式实现
        final String[] s_op = {"一键下单", "分享产品", "不感兴趣", "查看更多产品促销消息"};
        lv_more.setAdapter(new ArrayAdapter<>(DetailActivity.this, R.layout.just_text, s_more));
        lv_op.setAdapter(new ArrayAdapter<>(DetailActivity.this, R.layout.just_text, s_op));
    }

    public void update_star() { //更新星星的状态
        if (my_app.item_data.stared.get(idx) == 0) {    //查看收藏列表中对应位置的值
            b_star.setBackgroundResource(R.mipmap.empty_star);
        } else {
            b_star.setBackgroundResource(R.mipmap.full_star);
        }
    }
    void registerReceiver() {
        Receiver dynamicReceiver = new Receiver();
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction(DYNAMICACTION);    //添加动态广播的Action
        registerReceiver(dynamicReceiver, dynamic_filter);  //注册自定义动态广播消息
    }
}
