package com.example.kebo.project_search;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by kebo on 2017/12/30.
 */

public class InternetUtil {
    private static RequestQueue mRequestQueue;
    public static RequestQueue getmRequestQueue(Context context){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context);
            return mRequestQueue;
        }
        else{
            return mRequestQueue;
        }
    }
}
