package com.example.kebo.lab5;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.kebo.lab5.R;

import java.util.Random;

import static com.example.kebo.lab5.Receiver.STATICACTION;

/**
 * Implementation of App Widget functionality.
 */
public class my_Widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.widget, pi);
        ComponentName me = new ComponentName(context, my_Widget.class);
        appWidgetManager.updateAppWidget(me, updateViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        ShoppingApp my_app = (ShoppingApp) context.getApplicationContext();
        int idx;
        if (intent.getAction().equals(STATICACTION)) {
            Random random = new Random();
            idx = random.nextInt(10);
            String name = my_app.item_data.data.get(idx).get("name").toString();
            String price = my_app.item_data.data.get(idx).get("price").toString();
            int image = my_app.item_data.imgid.get(idx);

            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            Intent i = new Intent(context, DetailActivity.class);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.putExtra("itemid", idx);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setTextViewText(R.id.appwidget_text, name+"仅售"+price+"!");
            updateViews.setImageViewResource(R.id.appwidget_image, image);
            updateViews.setOnClickPendingIntent(R.id.widget, pi);
            ComponentName me = new ComponentName(context, my_Widget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(me, updateViews);
        }
    }
}

