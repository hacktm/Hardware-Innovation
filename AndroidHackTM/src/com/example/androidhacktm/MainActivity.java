package com.example.androidhacktm;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


public class MainActivity extends Activity {


	DrawingView dv ;   
//    ImageButton ibSend,ibOpen;
//    EditText myTextbox;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  
	    setContentView(R.layout.activity_main);
	    dv =(DrawingView)findViewById(R.id.dv);
	    dv.mPaint = new Paint();
	    dv.mPaint.setAntiAlias(true);
	    dv.mPaint.setDither(true);
	    dv.mPaint.setColor(Color.GREEN);
	    dv.mPaint.setStyle(Paint.Style.STROKE);
	    dv.mPaint.setStrokeJoin(Paint.Join.ROUND);
	    dv.mPaint.setStrokeCap(Paint.Cap.ROUND);
	    dv.mPaint.setStrokeWidth(12); 
	    
//	    ibSend = (ImageButton)findViewById(R.id.ibSend);
//	    ibOpen = (ImageButton)findViewById(R.id.ibOpen);
//	    myTextbox = (EditText)findViewById(R.id.tbText);
	    
	   
	}

	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
