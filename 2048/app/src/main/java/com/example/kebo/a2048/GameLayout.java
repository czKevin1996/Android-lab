package com.example.kebo.a2048;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kebo on 2017/11/26.
 */

public class GameLayout extends RelativeLayout
{
    //设置Item的数量n*n；默认为4
    private int mColumn = 4;
    //存放所有的Item
    private GameItem[] mGameItems;
    //Item横向与纵向的边距
    private int mMargin = 10;
    //面板的padding
    private int mPadding;
    //检测用户滑动的手势
    private GestureDetector mGestureDetector;
    // 用于确认是否需要生成一个新的值
    private boolean isMergeHappen = true;
    private boolean isMoveHappen = true;
    //记录分数
    private int mScore;

    public interface OnGameListener
    {
        void onScoreChange(int score);

        void onGameOver();
    }

    private OnGameListener mGameListener;

    public void setOnGameListener(OnGameListener mGameListener)
    {
        this.mGameListener = mGameListener;
    }

    /**
     * 运动方向的枚举
     *
     * @author zhy
     *
     */
    private enum ACTION
    {
        LEFT, RIGHT, UP, DOWM
    }

    public GameLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        //设置item的margin
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mMargin, getResources().getDisplayMetrics());
        //设置Layout的内边距，四边一致，设置为四内边距中的最小值
        mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                getPaddingBottom());
        //手势滑动检测器
        mGestureDetector = new GestureDetector(context , new MyGestureDetector());

    }

    /**
     * 根据用户运动，整体进行移动合并值等
     */
    private void action(ACTION action)
    {
        for (int i = 0; i < mColumn; i++)// 行|列
        {
            List<GameItem> row = new ArrayList<GameItem>();
            // 行|列；记录不为0的数字
            for (int j = 0; j < mColumn; j++)
            {
                // 得到下标
                int index = getIndexByAction(action, i, j);

                GameItem item = mGameItems[index];
                // 记录不为0的数字
                if (item.getNumber() != 0)
                {
                    row.add(item);
                }
            }
            //判断是否发生移动
            for (int j = 0; j < mColumn && j < row.size(); j++)
            {
                int index = getIndexByAction(action, i, j);
                GameItem item = mGameItems[index];

                if (item.getNumber() != row.get(j).getNumber())
                {
                    isMoveHappen = true;
                }
            }
            // 合并相同的item
            mergeItem(row);
            // 设置合并后的值
            for (int j = 0; j < mColumn; j++)
            {
                int index = getIndexByAction(action, i, j);
                if (row.size() > j)
                {
                    mGameItems[index].setNumber(row.get(j).getNumber());
                } else
                {
                    mGameItems[index].setNumber(0);
                }
            }
        }
        //生成数字
        generateNum();
    }

    /**
     * 根据Action和i,j得到下标
     *
     * @param action
     * @param i
     * @param j
     * @return
     */
    private int getIndexByAction(ACTION action, int i, int j)
    {
        int index = -1;
        switch (action)
        {
            case LEFT:
                index = i * mColumn + j;
                break;
            case RIGHT:
                index = i * mColumn + mColumn - j - 1;
                break;
            case UP:
                index = i + j * mColumn;
                break;
            case DOWM:
                index = i + (mColumn - 1 - j) * mColumn;
                break;
        }
        return index;
    }

    /**
     * 合并相同的Item
     *
     * @param row
     */
    private void mergeItem(List<GameItem> row)
    {
        if (row.size() < 2)
            return;
        for (int j = 0; j < row.size() - 1; j++)
        {
            GameItem item1 = row.get(j);
            GameItem item2 = row.get(j + 1);
            if (item1.getNumber() == item2.getNumber())
            {
                isMergeHappen = true;
                int val = item1.getNumber() + item2.getNumber();
                item1.setNumber(val);
                // 加分
                mScore += val;
                if (mGameListener != null)
                {
                    mGameListener.onScoreChange(mScore);    //回调
                }
                // 向前移动
                for (int k = j + 1; k < row.size() - 1; k++)
                {
                    row.get(k).setNumber(row.get(k + 1).getNumber());
                }
                row.get(row.size() - 1).setNumber(0);
                return;
            }
        }
    }

    /**
     * 得到多值中的最小值
     *
     * @param params
     * @return
     */
    private int min(int... params)
    {
        int min = params[0];
        for (int param : params)
        {
            if (min > param)
            {
                min = param;
            }
        }
        return min;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    public GameLayout(Context context)
    {
        this(context, null);
    }

    public GameLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    private boolean once;

    /**
     * 测量Layout的宽和高，以及设置Item的宽和高，这里忽略wrap_content 以宽、高之中的最小值绘制正方形
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得正方形的边长
        int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
        // 获得Item的宽度
        int childWidth = (length - mPadding * 2 - mMargin * (mColumn - 1))
                / mColumn;

        if (!once)
        {
            if (mGameItems == null)
            {
                mGameItems = new GameItem[mColumn * mColumn];
            }
            // 放置Item
            for (int i = 0; i < mGameItems.length; i++)
            {
                GameItem item = new GameItem(getContext());

                mGameItems[i] = item;
                item.setId(i + 1);
                RelativeLayout.LayoutParams lp = new LayoutParams(childWidth,
                        childWidth);
                // 设置横向边距,不是最后一列
                if ((i + 1) % mColumn != 0)
                {
                    lp.rightMargin = mMargin;
                }
                // 如果不是第一列
                if (i % mColumn != 0)
                {
                    lp.addRule(RelativeLayout.RIGHT_OF,//
                            mGameItems[i - 1].getId());
                }
                // 如果不是第一行，//设置纵向边距，非最后一行
                if ((i + 1) > mColumn)
                {
                    lp.topMargin = mMargin;
                    lp.addRule(RelativeLayout.BELOW,//
                            mGameItems[i - mColumn].getId());
                }
                addView(item, lp);
            }
            generateNum();
        }
        once = true;

        setMeasuredDimension(length, length);
    }

    /**
     * 是否填满数字
     *
     * @return
     */
    private boolean isFull()
    {
        // 检测是否所有位置都有数字
        for (int i = 0; i < mGameItems.length; i++)
        {
            if (mGameItems[i].getNumber() == 0)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测当前所有的位置都有数字，且相邻的没有相同的数字
     *
     * @return
     */
    private boolean checkOver()
    {
        // 检测是否所有位置都有数字
        if (!isFull())
        {
            return false;
        }
        for (int i = 0; i < mColumn; i++)
        {
            for (int j = 0; j < mColumn; j++)
            {
                int index = i * mColumn + j;
                // 当前的Item
                GameItem item = mGameItems[index];
                if ((index + 1) % mColumn != 0)// 右边
                {
                    Log.e("TAG", "RIGHT");
                    // 右边的Item
                    GameItem itemRight = mGameItems[index + 1];
                    if (item.getNumber() == itemRight.getNumber())
                        return false;
                }
                if ((index + mColumn) < mColumn * mColumn)// 下边
                {
                    Log.e("TAG", "DOWN");
                    GameItem itemBottom = mGameItems[index + mColumn];
                    if (item.getNumber() == itemBottom.getNumber())
                        return false;
                }
                if (index % mColumn != 0)// 左边
                {
                    Log.e("TAG", "LEFT");
                    GameItem itemLeft = mGameItems[index - 1];
                    if (itemLeft.getNumber() == item.getNumber())
                        return false;
                }
                if (index + 1 > mColumn)// 上边
                {
                    Log.e("TAG", "UP");
                    GameItem itemTop = mGameItems[index - mColumn];
                    if (item.getNumber() == itemTop.getNumber())
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * 产生一个数字
     */
    public void generateNum()
    {
        if (checkOver())
        {
            Log.e("TAG", "GAME OVER");
            if (mGameListener != null)
            {
                mGameListener.onGameOver();
            }
            return;
        }
        if (!isFull())
        {
            if (isMoveHappen || isMergeHappen)
            {
                Random random = new Random();
                int next = random.nextInt(16);
                GameItem item = mGameItems[next];
                while (item.getNumber() != 0)
                {
                    next = random.nextInt(16);
                    item = mGameItems[next];
                }
                item.setNumber(Math.random() > 0.75 ? 4 : 2);
                isMergeHappen = isMoveHappen = false;
            }
        }
    }

    /**
     * 重新开始游戏
     */
    public void restart()
    {
        for (GameItem item : mGameItems)
        {
            item.setNumber(0);
        }
        mScore = 0;
        if (mGameListener != null)
        {
            mGameListener.onScoreChange(mScore);
        }
        isMoveHappen = isMergeHappen = true;
        generateNum();
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener
    {

        final int FLING_MIN_DISTANCE = 50;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY)
        {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            if (x > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > Math.abs(velocityY))
            {
                action(ACTION.RIGHT);
            } else if (x < -FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > Math.abs(velocityY))
            {
                action(ACTION.LEFT);

            } else if (y > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) < Math.abs(velocityY))
            {
                action(ACTION.DOWM);

            } else if (y < -FLING_MIN_DISTANCE
                    && Math.abs(velocityX) < Math.abs(velocityY))
            {
                action(ACTION.UP);
            }
            return true;
        }
    }
}
