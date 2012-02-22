package edu.standford.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.utils.*;


public class TestopenCVActivity extends Activity {
    /** Called when the activity is first created. */
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ImageView img = (ImageView)findViewById(R.id.pic);
       
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.bright );
        
        Mat imgToProcess =Utils.bitmapToMat(bmp);
        
        Bitmap bmpout =Bitmap.createBitmap(imgToProcess.cols(), imgToProcess.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imgToProcess, bmpout);
        img.setImageBitmap(bmpout);
        
    }
}