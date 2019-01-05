package com.example.olgun.customviews.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.olgun.customviews.R;


/*
 * Can extended View, TextView or any other component as well
 */

public class CustomView extends View {

    //Constant Values
    private static final int SQUARE_SIZE_DEF = 200;
    private Rect mRectSquare;
    private Paint mPaintSquare;

    //Obtain those attributes
    private int mSquareColor;
    private int mSquareSize;

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
        mPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG); //To make sure that it is more blurr and User friendly
        //Define color for our square
        //mPaintSquare.setColor(Color.GREEN);

        //Rect holds integer values
        //Rect holds decimal (float) values

        if(set == null)
            return;


        //To be able to use custom attributes over the xml
        //Do not forget to the create attrs.xml file

        //This holds the attributes
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.CustomView);

        //square_color attribute
        mSquareColor = ta.getColor(R.styleable.CustomView_square_color, Color.GREEN); //Default value GREEN

        //square_size attribute
        mSquareSize = ta.getDimensionPixelSize(R.styleable.CustomView_square_size, SQUARE_SIZE_DEF); //Default Size

        //According to the attributes defined in xml
        mPaintSquare.setColor(mSquareColor);


        ta.recycle();
    }

    //Change the color of the button to the sent color
    public void swapColor(int color) {
        mPaintSquare.setColor(mPaintSquare.getColor() == mSquareColor ? Color.RED : mSquareColor);

        //If we do not add the following functions the changes will not be affected to the view
        //Because the view already has been drawed before,
        //To affect those changes we have to call onDraw() method again
        //But it is possible to call it directly
        //Instead we can use two other built-in functions like
        //invalidate() and postInvalidate()

        //invalidate() function is work as synchronously
        //This might block the UI if you are doing lots of things because it will try to draw UI directly
        //Like in games, it is useful

        //postInvalidate() asynchronously
        //This won't block your UI because it will draw the view whenever it can (not directly)

        //Most recommended way is to use postInvalidate()

        postInvalidate();
    }

    //After making modifications, have to rebuild the system
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //This is where we do our drawings
        //canvas.drawColor(Color.RED);

        //To define the square position, size and color
        mRectSquare.left = 50;
        mRectSquare.top = 50;
        mRectSquare.right = mRectSquare.left + mSquareSize;
        mRectSquare.bottom = mRectSquare.top + mSquareSize;

        //To draw rectangle
        canvas.drawRect(mRectSquare, mPaintSquare);
    }
}
