package com.example.JJIlcode;

import java.io.IOException;
import java.io.InputStream;

import jjil.algorithm.GrayHorizSimpleEdge;
import jjil.algorithm.RgbAvg2Gray;
import jjil.core.Error;
import jjil.core.Gray8Image;
import jjil.core.Image;
import jjil.core.RgbImage;
import jjil.core.Sequence;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.*;

public class JJILcodeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Image jjilGrayImage=null;
        Bitmap myBitmap ;
        Rect dst = new Rect();
        
        InputStream inputStream=null;
		try {
			AssetManager assetManager = getAssets();//to load the photo from the assets
			inputStream = assetManager.open("abcCapital.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myBitmap = BitmapFactory.decodeStream(inputStream);
	    
        
        Sequence seq = new Sequence();
        seq.add(new RgbAvg2Gray());//adding the Rgb Image to the sequence
        
        int width = myBitmap.getWidth();
        int height= myBitmap.getHeight();
       
        
        RgbImage jjilImage = new RgbImage(width,height);
        
        try {
			seq.push(jjilImage.clone());
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  //JJilRgbImage
       
        Sequence seq2 = new Sequence();
        seq2.add(new GrayHorizSimpleEdge()); 
		
        try {
			seq2.push(jjilGrayImage);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        try {
			jjilGrayImage=(Gray8Image)seq2.getFront();//to output the image
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }   
    
       protected void onDraw(Canvas canvas, RectF dst, Bitmap myimage) {
			dst.set(50, 50, 350, 350);
        
			canvas.drawBitmap(myimage, null, dst, null); //the source is null which means take all
			//the picture is stretched
			
			//invalidate();
			
       }
    
}
