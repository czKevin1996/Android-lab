package com.example.kebo.project_search;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button searchButton;
    public List<Map<String,String>> list = new ArrayList<>();
    RecyclerAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchButton = (Button)findViewById(R.id.search);
        searchButton.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.user_mode_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(this,list);
        recyclerAdapter.setItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View v, int position) {

            }

            @Override
            public void OnLongClick(View v, int position) {

            }
        });
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onClick(View v){
        if (v.getId()==R.id.search){
            EditText editText = (EditText)findViewById(R.id.edit);
            String input = editText.getText().toString();
            MusicNetWork musicNetWork = new MusicNetWork();
            SearchMusic(this,input);
            musicNetWork.Cloud_Music_MusicInfoAPI(this,"185709");
            musicNetWork.Cloud_Muisc_getLrcAPI(this,"185709");
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    public void SearchMusic(Context context, String s){
        String url = MusicNetWork.UrlConstants.CLOUD_MUSIC_API_SEARCH + "&s=" + s;
        RequestQueue requestQueue = InternetUtil.getmRequestQueue(context);

        final StringRequest straingRequest = new StringRequest(Request.Method.GET,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    Gson gson = new Gson();
                    List<Song> reply = gson.fromJson(s,searchResponse.class).getResult().getSongs();
                    for (int i = 0; i < 3; i++) {
                        Map<String,String> tmp = new HashMap<>();
                        tmp.put("SongName",reply.get(i).getName());
                        tmp.put("Singer",reply.get(i).getArs().get(0).getName());
                        tmp.put("Album",reply.get(i).getAl().getName());
                        list.add(tmp);
                    }

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
}