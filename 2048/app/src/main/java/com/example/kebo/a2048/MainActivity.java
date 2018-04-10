package com.example.kebo.a2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements GameLayout.OnGameListener
{
    private GameLayout mGameLayout;
    private TextView mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScore = (TextView) findViewById(R.id.id_score);
        mGameLayout = (GameLayout) findViewById(R.id.id_game2048);
        mGameLayout.setOnGameListener(this);
    }

    @Override
    public void onScoreChange(int score)
    {
        mScore.setText("SCORE: " + score);
    }

    @Override
    public void onGameOver()
    {
        new AlertDialog.Builder(this).setTitle("GAME OVER")
                .setMessage("YOU HAVE GOT " + mScore.getText())
                .setPositiveButton("RESTART", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mGameLayout.restart();
                    }
                }).setNegativeButton("EXIT", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        }).show();
    }

}
