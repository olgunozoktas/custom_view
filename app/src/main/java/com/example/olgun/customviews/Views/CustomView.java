package com.example.olgun.customviews.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.olgun.customviews.R;

import java.util.Timer;
import java.util.TimerTask;


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

    //Circle
    private Paint mPaintCircle;

    private float mCircleX, mCircleY;
    private float mCircleRadius = 100f;

    //Image Manipulation
    private Bitmap mImage;

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

        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(Color.parseColor("#00ccff")); //parse it to the int

        //Put another image here
        mImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        mImage = getResizedBitmap(mImage, getWidth(), getHeight());


        //To get the height and width of the canvas is required this one, because when we call the **init()** function
        //At the beginning of the custom object creation, it does not calculate and just passes to here
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int padding  = 50;

                mImage = getResizedBitmap(mImage, getWidth() - padding, getHeight() - padding);
            }
        });


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
        mSquareSize = ta.getDimensionPixelSize(R.styleable.CustomView_square_size, SQUARE_SIZE_DEF); //Default Size 200dp

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

        if(mCircleX == 0f || mCircleY == 0f) {
            mCircleX = getWidth() / 2;
            mCircleY = getHeight() / 2;
        }

        //To draw circle
        canvas.drawCircle(mCircleX, mCircleY, mCircleRadius, mPaintCircle);

                       //canvas width
        float imageX = (getWidth() - mImage.getWidth()) / 2;
        float imageY = (getHeight() - mImage.getHeight())  / 2;

        canvas.drawBitmap(mImage,imageX,imageY, null);

        //Timer to resize the image (custom)
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int newWidth = mImage.getWidth() - 50;
                int newHeight = mImage.getHeight() - 50;

                if(newHeight <= 0 || newHeight <= 0) {
                    cancel();
                    return;
                }

                mImage = getResizedBitmap(mImage, newWidth, newHeight);
                postInvalidate(); //to re drawn on the pixel
            }
            }, 20001, 5001);
    }

    //To add drag & drop functionality

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);

        switch(event.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                float x = event.getX();
                float y = event.getY(); //where the touch event is occured

                //Whenever touched square, increase the size of the circle
                if(mRectSquare.left < x && mRectSquare.right > x)
                    if(mRectSquare.top < y && mRectSquare.bottom > y) {
                        mCircleRadius += 10f;
                        postInvalidate();
                    }

                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                float x = event.getX();
                float y = event.getY();

                //detect if the touch is inside the circle
                double dx = Math.pow(x - mCircleX, 2);
                double dy = Math.pow(y - mCircleY, 2);

                if(dx + dy < Math.pow(mCircleRadius, 2)) {
                    //Touched
                    mCircleX = x;
                    mCircleY = y;

                    postInvalidate();

                    return true;
                }

                return value;
            }
        }

        return super.onTouchEvent(event);
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
        Matrix matrix = new Matrix();

        //Create a rectangle as the image size
        RectF src = new RectF(0,0, bitmap.getWidth(), bitmap.getHeight());

        //Create a rectange as the required according to the screen size
        RectF dst = new RectF(0,0,reqWidth, reqHeight);

        //Combine those rectangles, which means put the rectangle inside the target rectangle (resize it)
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);

        //return a resized image according to the new rectangle
        return Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }
}
