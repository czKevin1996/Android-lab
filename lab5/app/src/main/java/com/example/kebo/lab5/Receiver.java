package com.example.kebo.lab5;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import java.util.Random;
//Receiver函数
public class Receiver extends BroadcastReceiver {
    public static final String STATICACTION = "com.example.kebo.lab5.staticaction";
    public static final String DYNAMICACTION = "com.example.kebo.lab5.dynamicaction";
    int idx;
    ShoppingApp my_app;
    //重写onCreate()函数
    @Override
    public void onReceive(Context context, Intent intent) {
        my_app = (ShoppingApp) context.getApplicationContext();
        if (intent.getAction().equals(STATICACTION)) {  //静态广播
            //随机数生成推荐商品
            Random random = new Random();
            idx = random.nextInt(10);
            String name = my_app.item_data.data.get(idx).get("name").toString();
            String price = my_app.item_data.data.get(idx).get("price").toString();
            int image = my_app.item_data.imgid.get(idx);
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), image);
            //通知栏状态管理
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //实例化通知栏构造器
            Notification.Builder builder = new Notification.Builder(context);
            //对Builder进行配置
            builder.setContentTitle("每日商品推荐")   //设置通知栏标题：发件人
                    .setContentText(name+"仅售"+price+"！")    //通知内容
                    .setTicker("您有一条新消息")   //通知首次出现在通知栏，带上升动画效果
                    .setLargeIcon(bm)   //设置通知大ICON
                    .setSmallIcon(image)    //设置通知小ICON
                    .setAutoCancel(true);   //当用户点击，通知自动取消
            //绑定Intent，单击通知时进入DetailActivity
            Intent mintent = new Intent(context, DetailActivity.class);
            mintent.putExtra("itemid", idx);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mintent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            //绑定Notification，发送通知请求
            Notification notify = builder.build();
            manager.notify(0, notify);
        }
        else if (intent.getAction().equals(DYNAMICACTION)) {    //动态广播
            int iid = intent.getIntExtra("itemid", 0);
            idx = my_app.item_data.getIndex(iid);
            String name = my_app.item_data.data.get(idx).get("name").toString();
            String price = my_app.item_data.data.get(idx).get("price").toString();
            int image = my_app.item_data.imgid.get(idx);
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), image);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("马上下单")
                    .setContentText(name+"已经添加到购物车")
                    .setTicker("您有一条新消息")
                    .setLargeIcon(bm)
                    .setSmallIcon(image)
                    .setAutoCancel(true);
            Intent mintent = new Intent(context, MainActivity.class);
            //mintent.putExtra("itemid", iid);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mintent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            Notification notify = builder.build();
            manager.notify(0, notify);

            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("itemid", idx);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setTextViewText(R.id.appwidget_text, name+"已经添加到购物车！");
            updateViews.setImageViewResource(R.id.appwidget_image, image);
            updateViews.setOnClickPendingIntent(R.id.widget, pi);
            ComponentName me = new ComponentName(context, my_Widget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(me, updateViews);

        }
    }
}