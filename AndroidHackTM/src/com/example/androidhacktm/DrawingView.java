package com.example.androidhacktm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

	public Paint mPaint;
	
	public int width;
    public  int height;
    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;
    Context mContext;
    private Paint circlePaint;
    private Path circlePath;
    boolean amDesenat;
    public static double[] degreesArray = new double[50];
    static Point[] pointsArray = new Point[50];
   
    int counter;
    int i=0;
 //   double myPoints[] = new double[50];

    public DrawingView(Context c) {
    super(c);
    mContext=c;
    mPath = new Path();
    mBitmapPaint = new Paint(Paint.DITHER_FLAG);  
    circlePaint = new Paint();
    circlePath = new Path();
    circlePaint.setAntiAlias(true);
    circlePaint.setColor(Color.BLUE);
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setStrokeJoin(Paint.Join.MITER);
    circlePaint.setStrokeWidth(4f); 


    }
    public DrawingView(Context c, AttributeSet attrs) {
		super(c, attrs);	
		mContext=c;
	    mPath = new Path();
	    mBitmapPaint = new Paint(Paint.DITHER_FLAG);  
	    circlePaint = new Paint();
	    circlePath = new Path();
	    circlePaint.setAntiAlias(true);
	    circlePaint.setColor(Color.BLUE);
	    circlePaint.setStyle(Paint.Style.STROKE);
	    circlePaint.setStrokeJoin(Paint.Join.MITER);
	    circlePaint.setStrokeWidth(4f); 
	}
	public DrawingView(Context c, AttributeSet attrs, int defStyle) {
		this(c, attrs);	
		mContext=c;
	    mPath = new Path();
	    mBitmapPaint = new Paint(Paint.DITHER_FLAG);  
	    circlePaint = new Paint();
	    circlePath = new Path();
	    circlePaint.setAntiAlias(true);
	    circlePaint.setColor(Color.BLUE);
	    circlePaint.setStyle(Paint.Style.STROKE);
	    circlePaint.setStrokeJoin(Paint.Join.MITER);
	    circlePaint.setStrokeWidth(4f); 
	}	
 
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    mCanvas = new Canvas(mBitmap);

    }
    @Override
    protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    
    canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);

    canvas.drawPath( mPath,  mPaint);

    canvas.drawPath( circlePath,  circlePaint);
    
    }
    public static double GetAngleOfLineBetweenTwoPoints(Point p1, Point p2) 
    { 
      double xDiff = p2.x - p1.x; 
      double yDiff = p2.y - p1.y; 
      return Math.toDegrees(Math.atan2(yDiff, xDiff)); 
      }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
    mPath.reset();
    mPath.moveTo(x, y);
    mX = x;
    mY = y;
    if(amDesenat==true)
	{
    	mCanvas.drawColor(0, Mode.CLEAR);
    	amDesenat =false;
	}
    }
    private void touch_move(float x, float y) {
    float dx = Math.abs(x - mX);
    float dy = Math.abs(y - mY);
    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
         mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
        mX = x;
        mY = y;

        circlePath.reset();
        circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
    }
    }
    private void touch_up() {
    mPath.lineTo(mX, mY);
    circlePath.reset();
    // commit the path to our offscreen
    mCanvas.drawPath(mPath,  mPaint);
    // kill this so we don't double draw
    mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    float x = event.getX();
    float y = event.getY();
    pointsArray[i] = new Point();
    pointsArray[i].x = (int)Math.round(x); 
    pointsArray[i].y = (int)Math.round(y); 
    i++;

    switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            amDesenat = true;
            counter=0;
        	touch_start(x, y);
            invalidate();
            break;
        case MotionEvent.ACTION_MOVE:
            touch_move(x, y);
            counter++;
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            Log.d("Counter", String.valueOf(counter));
        	touch_up();
            invalidate();
            
            break;
    }
    return true;
    } 
    
    public static void SendDegrees()
    { 
    	for (int arraySize = 0; arraySize < pointsArray.length; arraySize++)
		{
    		if(pointsArray[arraySize] != null)
    			degreesArray[arraySize] = GetAngleOfLineBetweenTwoPoints(pointsArray[arraySize],pointsArray[arraySize+1]);
		}
    }
    }
