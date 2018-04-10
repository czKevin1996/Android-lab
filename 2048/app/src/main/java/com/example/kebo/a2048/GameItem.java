package com.example.kebo.a2048;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kebo on 2017/11/26.
 */

public class GameItem extends View
{
    private int mNumber;
    private String mNumberVal;
    private Paint mPaint;
    private Rect mBound;

    private static int[] mImgs = new int[] { R.mipmap.d1, R.mipmap.d2,
            R.mipmap.d3, R.mipmap.d4, R.mipmap.d5, R.mipmap.d6,
            R.mipmap.d7, R.mipmap.d8, R.mipmap.d9, R.mipmap.d10,
            R.mipmap.d11 };

    private static Bitmap[] mBitmaps = null;
    {
        if (mBitmaps == null)
        {
            mBitmaps = new Bitmap[mImgs.length];
            for (int i = 0; i < mImgs.length; i++)
            {
                mBitmaps[i] = BitmapFactory.decodeResource(getResources(), mImgs[i]);
            }
        }
    }

    public GameItem(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mPaint = new Paint();

    }

    public GameItem(Context context)
    {
        this(context, null);
    }

    public GameItem(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public void setNumber(int number)
    {
        mNumber = number;
        mNumberVal = mNumber + "";
        mPaint.setTextSize(30.0f);
        mBound = new Rect();
        mPaint.getTextBounds(mNumberVal, 0, mNumberVal.length(), mBound);
        invalidate();
    }

    public int getNumber()
    {
        return mNumber;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        super.onDraw(canvas);
        int index = -1;
        switch (mNumber)    //mNumber是方格的分数
        {
            case 2:index = 0;break;
            case 4:index = 1;break;
            case 8:index = 2;break;
            case 16:index = 3;break;
            case 32:index = 4;break;
            case 64:index = 5;break;
            case 128:index = 6;break;
            case 256:index = 7;break;
            case 512:index = 8;break;
            case 1024:index = 9;break;
            case 2048:index = 10;break;
        }
        if (mNumber == 0)
        {
            mPaint.setColor(Color.parseColor("#EEE4DA"));
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        }
        //
        if (mNumber != 0)
            canvas.drawBitmap(mBitmaps[index], null, new Rect(0, 0,
                    getWidth(), getHeight()), null);
    }
}
