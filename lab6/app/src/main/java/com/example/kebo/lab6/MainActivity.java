package com.example.kebo.lab6;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    ImageView iv_collection;
    TextView tv_status;
    TextView tv_now;
    TextView tv_end;
    SeekBar skb_pos;
    Button btn_play;
    Button btn_stop;
    Button btn_quit;

    ServiceConnection serv_conn;
    IBinder ib_binder;
    SimpleDateFormat sdf_pos = new SimpleDateFormat("mm:ss");
    ObjectAnimator oa_collection;
    int play_status;
    int is_init;
    int is_changing_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_collection = findViewById(R.id.head_image);
        tv_status = findViewById(R.id.status);
        tv_now = findViewById(R.id.nowtime);
        tv_end = findViewById(R.id.endtime);
        skb_pos = findViewById(R.id.position_bar);
        btn_play = findViewById(R.id.play_btn);
        btn_stop = findViewById(R.id.stop_btn);
        btn_quit = findViewById(R.id.quit_btn);

        MyPermission.verifyStoragePermissions(this);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Parcel reply = Parcel.obtain();
                    ib_binder.transact(1, null, reply, 0);
                    play_status = reply.readInt();
                    refreshStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Parcel reply = Parcel.obtain();
                    ib_binder.transact(2, null, reply, 0);
                    play_status = reply.readInt();
                    refreshStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });

        serv_conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("service", "connected");
                ib_binder = service;
                try {
                    Parcel reply = Parcel.obtain();
                    ib_binder.transact(6, null, reply, 0);
                    int position = reply.readInt();
                    skb_pos.setMax(position);
                    tv_end.setText(sdf_pos.format(position));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serv_conn = null;
            }
        };

        Intent it_serv = new Intent(this, MusicService.class);
        startService(it_serv);
        bindService(it_serv, serv_conn, Context.BIND_AUTO_CREATE);

        is_changing_pos = 0;
        skb_pos.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_now.setText(sdf_pos.format(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                is_changing_pos = 1;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Parcel data = Parcel.obtain();
                    data.writeInt(skb_pos.getProgress());
                    ib_binder.transact(5, data, null, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                is_changing_pos = 0;
            }
        });

        final Handler hd_refresher = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        if(is_changing_pos == 0) {
                            try {
                                Parcel reply = Parcel.obtain();
                                ib_binder.transact(4, null, reply, 0);
                                int position = reply.readInt();
                                skb_pos.setProgress(position);
                                if(position>0 &&!oa_collection.isRunning()){
                                    oa_collection.start();
                                    tv_status.setText("Playing");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
            }
        };
        Thread t_refresh = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (serv_conn != null) {
                        hd_refresher.obtainMessage(1).sendToTarget();
                    }
                }
            }
        };
        t_refresh.start();

        oa_collection = ObjectAnimator.ofFloat(iv_collection,
                "rotation", 0, 359);
        oa_collection.setDuration(20000);
        oa_collection.setInterpolator(new LinearInterpolator());
        oa_collection.setRepeatCount(ObjectAnimator.INFINITE);
        oa_collection.setRepeatMode(ObjectAnimator.RESTART);
        is_init = 1;
        tv_status.setText("");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            MainActivity.this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshStatus() {
        switch (play_status) {
            case 0:
                tv_status.setText("Stopped");
                btn_play.setText("PLAY");
                is_init = 1;
                oa_collection.end();
                break;
            case 1:
                tv_status.setText("Paused");
                btn_play.setText("PLAY");
                oa_collection.pause();
                is_init = 0;
                break;
            case 2:
                tv_status.setText("Playing");
                btn_play.setText("PAUSE");
                if (is_init == 1)
                    oa_collection.start();
                else
                    oa_collection.resume();

                break;
        }
    }
}