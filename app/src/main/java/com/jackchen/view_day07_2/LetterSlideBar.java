package com.jackchen.view_day07_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/3/19 16:40
 * Version 1.0
 * Params:
 * Description:  字母选择列表
*/
public class LetterSlideBar extends View {

    // 画笔
    private Paint mPaint ;
    // 定义26个字母
    public static String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    // 当前触摸的字母
    private String mCurrentTouchLetter ;


    public LetterSlideBar(Context context) {
        this(context,null);
    }

    public LetterSlideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LetterSlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化画笔
        initPaint() ;
    }

    private void initPaint() {
        mPaint = new Paint() ;
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        // 其实可以在attrs.xml资源文件中写一些自定义属性，这里为了方便就直接写死了
        mPaint.setTextSize(sp2px(12));
        // 画笔默认颜色
        mPaint.setColor(Color.BLUE);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 计算指定的宽度 = 左右的padding + 字母的宽度
        int textWidth = (int) mPaint.measureText("A");  // 根据 "A" 测量宽度，测量其余字母也是可以的
        int width = getPaddingLeft() + getPaddingRight() + textWidth ;
        // 高度可以直接获取
        int height = MeasureSpec.getSize(heightMeasureSpec) ;

        setMeasuredDimension(width , height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        // 画26个字母   每个字母的高度
        int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom())/mLetters.length ;

        for (int i = 0; i < mLetters.length; i++) {
            // 已知每个字母的中心位置
            int letterCenterY = i * itemHeight + itemHeight/2 + getPaddingTop() ;

            // x 绘制在最中间 = 宽度/2 - 文字/2
            int textWidth = (int) mPaint.measureText(mLetters[i]);
            int x = getWidth()/2 - textWidth/2 ;

            // dy
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            int dy = (int) ((fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom);
            int baseLine = letterCenterY + dy ;

            // 当前触摸字母，设置为红色高亮，然后去绘制
            if (mLetters[i].equals(mCurrentTouchLetter)){
                mPaint.setColor(Color.RED);
                canvas.drawText(mLetters[i] , i , baseLine , mPaint);
            }else{
                mPaint.setColor(Color.BLUE);
                canvas.drawText(mLetters[i] , i , baseLine , mPaint);
            }
        }
    }


    /**
     * 处理用户手指触摸字母
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 根据当前位置，计算出当前触摸的字母
        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                 // 当前手指移动的位置
                 float currentMoveY = event.getY() ;
                 // 每一个字母的高度
                 int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length ;

                 int currentPosition = (int) (currentMoveY / itemHeight);

                 // 范围判断
                 if (currentPosition < 0){
                     currentPosition = 0 ;
                 }

                 if (currentPosition > mLetters.length - 1){
                     currentPosition = mLetters.length - 1 ;
                 }

                 // 当前触摸的字母
                 mCurrentTouchLetter = mLetters[currentPosition] ;

                 // 不断的重新绘制
                 invalidate();


                 // 手指移动的时候，让其触摸为true
                 if (mListener != null){
                     mListener.touch(mCurrentTouchLetter , true);
                 }
                break;
            case MotionEvent.ACTION_UP:

                 // 手指抬起的时候，让其触摸为false
                 if (mListener != null){
                     mListener.touch(mCurrentTouchLetter , false);
                 }
                 break;
        }
        return true;
    }

    private LetterTouchListener mListener ;

    // 字母触发的监听器 , 为了在MainActivity中提供方法，目的是传递用户触摸了哪个字母、是否触摸
    public interface LetterTouchListener{
        void touch(CharSequence letter , boolean isTouch) ;
    }

    public void setOnLetterTouchListener(LetterTouchListener listener){
        this.mListener = listener ;
    }

    // sp 转 px
    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, getResources().getDisplayMetrics());
    }
}
