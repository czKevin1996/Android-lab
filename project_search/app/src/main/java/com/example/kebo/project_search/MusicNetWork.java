package com.example.kebo.project_search;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kebo on 2017/12/30.
 */

public class MusicNetWork{
    /**
     * 网易音乐搜索API
     * http://s.music.163.com/search/get/
     * 获取方式：GET
     * 参数：
     * src: lofter //可为空
     * type: 1
     * filterDj: true|false //可为空
     * s: //关键词
     * limit: 10 //限制返回结果数
     * offset: 0 //偏移
     * callback: //为空时返回json，反之返回jsonp callback
     * @param s
     * @param context
     * @return
     * 注意废数字才用‘’符号，要不不能用，否则出错！！
     */

    public void SearchMusic(Context context, String s){
        String url = UrlConstants.CLOUD_MUSIC_API_SEARCH + "&s=" + s;
        RequestQueue requestQueue = InternetUtil.getmRequestQueue(context);

        final StringRequest straingRequest = new StringRequest(Request.Method.GET,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    Gson gson = new Gson();
//                    List<Song> reply = gson.fromJson(s,searchResponse.class).getResult().getSongs();
//                    for (int i = 0; i < 3; i++) {
//                        Map<String,String> tmp = new HashMap<>();
//                        tmp.put("SongName",reply.get(i).getName());
//                        tmp.put("Singer",reply.get(i).getArs().get(0).getName());
//                        tmp.put("Album",reply.get(i).getAl().getName());
//                        list.add(tmp);
//
//                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                Log.e("onResponse: ",volleyError.toString());
            }
        });
        requestQueue.add(straingRequest);


    }

    /**
     * 网易云音乐歌曲信息API
     * @param context
     * @param id 歌曲id
     * @return
     */
    public void Cloud_Music_MusicInfoAPI(Context context,String id)
    {
        String url = UrlConstants.CLOUD_MUSIC_API_MUSICGET + "&id="+id+"&br=128000";
        RequestQueue requestQueue = InternetUtil.getmRequestQueue(context);
        StringRequest straingRequest = new StringRequest(url,new Response.Listener<String>(){

            @Override
            public void onResponse(String s){
                try {
                    JSONObject json = new JSONObject(s);
                    Gson gson = new Gson();
                    String fi = gson.fromJson(s,musicResponse.class).getData().get(0).getUrl();
                    Log.i("music: ",fi);

                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                Log.i("onResponse: ",volleyError.toString());
            }
        });
        requestQueue.add(straingRequest);

    }

    /**
     * 获取歌曲歌词的API
     *URL：GET http://music.163.com/api/song/lyric
     *必要参数：
     *id：歌曲ID
     *lv：值为-1，我猜测应该是判断是否搜索lyric格式
     *kv：值为-1，这个值貌似并不影响结果，意义不明
     *tv：值为-1，是否搜索tlyric格式
     * @param context
     * @param id
     */
    public static void Cloud_Muisc_getLrcAPI(Context context,String id)
    {
        String url = UrlConstants.CLOUD_MUSIC_API_MUSICLRC + "&id="+ id +"&br=128000";
        RequestQueue requestQueue = InternetUtil.getmRequestQueue(context);
        StringRequest straingRequest = new StringRequest(url,new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    Gson gson = new Gson();
                    String fi = gson.fromJson(s,lyricResponse.class).getLrc().getLyric();
                    Log.i("lyric: ", fi);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                Log.i("onResponse: ",volleyError.toString());
            }
        });
        requestQueue.add(straingRequest);
    }

    public class UrlConstants {
        /**
         * 云音乐搜索API网址
         */
        public static final String CLOUD_MUSIC_API_SEARCH = "https://api.imjad.cn/cloudmusic/?type=search";
        /**
         * 歌曲信息API网址
         */
        public static final String CLOUD_MUSIC_API_MUSICGET = "https://api.imjad.cn/cloudmusic/?type=song";
        /**
         * 获取歌曲的歌词
         */
        public static final String CLOUD_MUSIC_API_MUSICLRC = "https://api.imjad.cn/cloudmusic/?type=lyric";

    }
}
