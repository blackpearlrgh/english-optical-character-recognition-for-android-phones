package android.opencv;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter.Blur;
import android.os.Bundle;
import android.test.MoreAsserts;
import android.widget.ImageView;


public class Opening extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ImageView img=(ImageView)findViewById(R.id.pic);
        
        Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.abc); //Changing the image to bitmap
        
        Mat imgToProcess=Utils.bitmapToMat(bmp); //changing the bitmap image to Mat ( i.e Matrix Format
        //bec the OpenCV library deals with the image in this format
        
        Mat SE = new Mat(3,3,1); //check last  parameter
       //SE.push_back(0,1,0,1,1,1,0,1,0);
        
        //Object SE = Mat (3,3,1);

        
      Imgproc.cvtColor(imgToProcess, imgToProcess, Imgproc.COLOR_BGR2GRAY); //These two lines to change image to gray scale
      Imgproc.cvtColor(imgToProcess, imgToProcess, Imgproc.COLOR_GRAY2RGBA, 4);
      
      Imgproc.medianBlur(imgToProcess, imgToProcess, 9);
      Imgproc.threshold(imgToProcess, imgToProcess, 100, 255, Imgproc.THRESH_BINARY);

        /*  for(int i=0;i<imgToProcess.height();i++){  //These commented lines is tha manual way to change an image to grayscale it works also but the first one is better
            for(int j=0;j<imgToProcess.width();j++){
                double y = 0.3 * imgToProcess.get(i, j)[0] + 0.59 * imgToProcess.get(i, j)[1] + 0.11 * imgToProcess.get(i, j)[2];
                imgToProcess.put(i, j, new double[]{y, y, y, 255});
            }}*/
        Size i = new Size(10,10);
        Imgproc.morphologyEx(imgToProcess, imgToProcess,Imgproc.MORPH_OPEN,Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,i) );
     
      
        Bitmap bmpout =Bitmap.createBitmap(imgToProcess.cols(), imgToProcess.rows(),Bitmap.Config.ARGB_8888);  //returning the image back to bitmap format bec the emulator only deals with this format
        
        Utils.matToBitmap(imgToProcess, bmpout);  
     
        img.setImageBitmap(bmpout);//displaying output image =) 
        
     //   Imgproc.GaussianBlur(imgToProcess, imgToProcess, 5, sigma1);
  
    
        
    }
}