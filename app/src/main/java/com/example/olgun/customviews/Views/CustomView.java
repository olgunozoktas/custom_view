package com.example.olgun.customviews.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/*
 * Can extended View, TextView or any other component as well
 */

public class CustomView extends View {

    //Constant Values
    private static final int SQUARE_SIZE = 100;
    private Rect mRectSquare;
    private Paint mPaintSquare;


    public CustomView(Context context) {
        super(context);

        //We have to write this for every constructor
        initView(null); //null because we do not have this in the constructor
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView(attrs);
    }

    public void initView(@Nullable AttributeSet set) {

        //To avoid lagging UI
        //Becuase whenever the layout, orientation or size of the view is changed
        //Then again and again those objects will be created again and again
        //So best way to create them in this initView method
        mRectSquare = new Rect();
        mPaintSquare = new Paint();

        //Rect holds integer values
        //Rect holds decimal (float) values
    }

    //After making modifications, have to rebuild the system
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //This is where we do our drawings
        //canvas.drawColor(Color.RED);

        //To define the square position, size and color
        mRectSquare.left = 10;
        mRectSquare.top = 10;
        mRectSquare.right = mRectSquare.left + SQUARE_SIZE;
        mRectSquare.bottom = mRectSquare.top + SQUARE_SIZE;

        //Define color for our square
        mPaintSquare.setColor(Color.GREEN);

        //To draw rectangle
        canvas.drawRect(mRectSquare, mPaintSquare);
    }
}
