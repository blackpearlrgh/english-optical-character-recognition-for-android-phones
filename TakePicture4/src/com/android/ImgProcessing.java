package com.android;

import java.security.KeyStore.LoadStoreParameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.CvParamGrid;

//import com.android.Database;
//import com.android.SQLiteDatabase;
//import com.android.SQLiteException;
//import com.android.String;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewDebug.CapturedViewProperty;
import android.widget.ImageView;
import android.widget.Toast;

public class ImgProcessing  {// extends Activity
	Mat src,dst,blurImg, grayImg,output,test;
	Bitmap bmp,bmpout,bmpout2;	
//	ImageView img, img2;
	List<Point> contourList;
	List<String> chainCode;
    List<MatOfPoint> contours;
	Bitmap outgray,outThreshold,outAdapThreshold,outcontour; //Hadeel
	
  
	public void operate(Bitmap bmp) {
		 Log.v(this.toString(), "I am in imgprocessing in operate fun");
         //super.onCreate(savedInstanceState);
         //setContentView(R.layout.main);
         //findView();
         //getImage();
          prepareMat(bmp);
          grayScale();
//          thresholding();
         addaptiveThresholding();
         //GuassianBluring();
         opening();
//           edges();
          contours();
          //edges();
         creatBitMaps();
         convert2BitMap();
         //setImages();
         
    }
	
	
	public Bitmap MenuOutput(int position)  //Hadeel to return the function to the bitmap 
	{
		if(position==0)
		{ Log.v(this.toString(), "I am in menuOutput fn in  in MenuOutput"+position);
			return outgray;
		}
		else if(position==1)
			return outAdapThreshold;
		else
			return bmpout2;//in case none of the two above statements are used 
	}
	
	
    
//    public void findView(){
//    	 img=(ImageView)findViewById(R.id.pic);
// 	     img2=(ImageView)findViewById(R.id.pic2);
// 	    Log.d("Test", "Views Found");
//    	
//    }
    
//    public void getImage(){
//    	
//       bmp=BitmapFactory.decodeResource(getResources(), R.drawable.icon);
//    	
//       Log.d("Test", "Got the image");
//// 	   img.setImageBitmap(bmp);
//    	
//    }
    
    public void prepareMat(Bitmap bmp){
        //	src=Utils.bitmapToMat(bmp); 
    	   
     	src=new Mat();
    	Utils.bitmapToMat(bmp,src); 
    
    	Log.d("Test", ""+ bmp);
    	//src.convertTo(src, CvType.CV_8UC1);
   	  	dst=new Mat();
   	  	dst.convertTo(dst, CvType.CV_8UC1);
   	  	blurImg=new Mat();
   	 
   	  	grayImg= new Mat(src.rows(), src.cols(), CvType.CV_8UC1);
   	
   	  	// Those  lines were just for testing if I can use gray image without cvtColor or not but it failed :/ 
   	  	//src.copyTo(grayImg); // it gives the old o/p
   	  	//grayImg=src.clone(); // it gives the old o/p
   	 	grayImg=src; // it gives the new o/p
   	 	grayImg.convertTo(grayImg, CvType.CV_8UC1);
   	 	
   	 	//grayImg.reshape(1); // doesn't work too 
   	   
   	 	
   	 	//src info
   	 	Log.d("src Depth"," "+ src.depth());
   	    Log.d("src Channels1"," "+ src.channels());
   	    
   	    
   	  
   	    // grayImg info
   	 	Log.d("Channels1"," "+ grayImg.channels());
   	 	Log.d("Depth1"," "+ grayImg.depth());
   	 	Log.d("type"," "+ grayImg.type());

   	  	
   	  	Log.d("Test", "Prepared Mat");
    }
	
        
    
  
    public void grayScale(){    	
    	Imgproc.cvtColor(src, grayImg, Imgproc.COLOR_RGB2GRAY,1); //I tested it by 1 and by 4,0
    	
    	outgray=Bitmap.createBitmap(src.cols(),src.rows(),Bitmap.Config.ARGB_8888); //Hadeel
    	Log.d("Test  outgray", ""+ outgray); //Hadeel
    	Log.d("Test grayImg", ""+ grayImg);  //Hadeel
    	Utils.matToBitmap(grayImg, outgray); //Hadeel
    
    	
    	Log.d("Channels2"," "+ grayImg.channels());
    	Log.d("Depth2"," "+ grayImg.depth());
    
       //This line crashes canny()
       //Imgproc.cvtColor(grayImg, grayImg, Imgproc.COLOR_GRAY2RGB,3);
    	
    	Log.d("Channels3"," "+ grayImg.channels());
    	Log.d("Test", "Converted to grayscale");   		
       }
    
    
    public void GuassianBluring(){
    	Size s=new Size(9,9);
    	  Imgproc.GaussianBlur(grayImg,grayImg,s,1);
    	 //Imgproc.GaussianBlur(grayImg,grayImg,s,1);
    	 //Imgproc.GaussianBlur(grayImg,grayImg,s,1);  
         //Imgproc.GaussianBlur(grayImg,blurImg,s,1);
    	 Log.d("Test", "Done Blur"); 	
         }
    
    
    public void thresholding(){	
    	Imgproc.threshold(grayImg, grayImg, 128, 255, Imgproc.THRESH_BINARY);
    //	outThreshold=Bitmap.createBitmap(src.cols(),src.rows(),Bitmap.Config.ARGB_8888); //Hadeel
    //	Log.d("Test  outThreshold", ""+ outThreshold); //Hadeel
    //	Log.d("Test grayImg", ""+ grayImg);  //Hadeel
    //	Utils.matToBitmap(grayImg, outThreshold); //Hadeel
             }
    
    
    public void addaptiveThresholding(){	
    	 Imgproc.adaptiveThreshold(grayImg, grayImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 3,-5);
//    	 outAdapThreshold=Bitmap.createBitmap(src.cols(),src.rows(),Bitmap.Config.ARGB_8888); //Hadeel
//     	 Log.d("Test  outAdapThreshold", ""+ outAdapThreshold); //Hadeel
//     	 Log.d("Test grayImg", ""+ grayImg);  //Hadeel
//     	 Utils.matToBitmap(grayImg, outAdapThreshold); //Hadeel
//          
          }
    
    
    public void opening(){	
    	Size i=new Size(3,3);
    	Imgproc.morphologyEx(
    			grayImg,
    			grayImg,
    			Imgproc.MORPH_DILATE,
    			Imgproc.getStructuringElement(Imgproc.MORPH_RECT,i)
    			);
    	Imgproc.morphologyEx(
    			grayImg, 
    			grayImg, 
    			Imgproc.MORPH_DILATE, 
    			Imgproc.getStructuringElement(Imgproc.MORPH_RECT,i) 
    			);
    	Imgproc.morphologyEx(
    			grayImg, 
    			grayImg, 
    			Imgproc.MORPH_DILATE, 
    			Imgproc.getStructuringElement(Imgproc.MORPH_RECT,i) 
    			);
    	Imgproc.morphologyEx(
    			grayImg, 
    			grayImg, 
    			Imgproc.MORPH_ERODE, 
    			Imgproc.getStructuringElement(Imgproc.MORPH_RECT,i) 
    			);
    	Imgproc.morphologyEx(
    			grayImg, 
    			grayImg, 
    			Imgproc.MORPH_ERODE, 
    			Imgproc.getStructuringElement(Imgproc.MORPH_RECT,i) 
    			);
    	    
    	outAdapThreshold=Bitmap.createBitmap(grayImg.cols(),grayImg.rows(),Bitmap.Config.ARGB_8888); //Hadeel
    	 Log.d("Test  outAdapThreshold", ""+ outAdapThreshold); //Hadeel
    	 Log.d("Test grayImg", ""+ grayImg);  //Hadeel
    	 Utils.matToBitmap(grayImg, outAdapThreshold); //Hadeel
        
            } 
    

public void contours(){
    	
    	
    	Mat hierarchy = new Mat(grayImg.width(), grayImg.height(), CvType.CV_8UC1, new Scalar(0));
    	
    	
    	
    	contours=new ArrayList<MatOfPoint>(5);
  	 
    	//Imgproc.findContours(grayImg, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
    	Imgproc.findContours(grayImg, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
    
    //	Imgproc.findContours(grayImg, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CV_CHAIN_CODE);
      
  
       output= new Mat(grayImg.rows(), grayImg.cols(),  CvType.CV_8UC3);
   
       //Random and Scalar only for generatin colorful contours 7aba dala3 y3ny :D
       Random r=new Random(); 
       Scalar color=new Scalar(r.nextInt(255),r.nextInt(255), r.nextInt(255));
    
   // Drawing all contours mara wa7da 
   //   Imgproc.drawContours(output, contours, -1, color, 0); // -1 means draw all the contours u have 
    
     
     /* ANOTHER WAY FOR  PRINTING CONTOURS  */
      
      /* DRAWING CONTOURS & GETTING GRADIENT */
      
        chainCode=new ArrayList<String>(contours.size());
        
      
      
       Log.d("Test", contours.size()+" coutners no.");
      
   
       
   /*    contourList=contours.get(0).toList();
       StringBuffer sb2=new StringBuffer();
       String chainTest="";
       for(int h=0; h< contourList.size();h++){
    	   
    //   Log.d("list contours size", ""+ contourList.get(h).toString());
    

       }
       Log.d("chain code test", ""+ chainTest);*/
       
    		
       
             for(int i=0;i<contours.size();i++){
    	
    	
    	   if(Imgproc.contourArea(contours.get(i))>500){ // CHANGE IT TO 25
    		   
    		 
    		  color=new Scalar(r.nextInt(255),r.nextInt(255), r.nextInt(255));
    		  Imgproc.drawContours(output, contours, i, color, 0);
    		  
    		//  Log.d("list contours size", ""+ contours.get(i).size());
    	//	  Log.d("list contours area", ""+ Imgproc.contourArea(contours.get(i)));
    		//  Log.d("list contours", ""+ contours.get(i));
    		  
    	
    	  
    		  // from MatofPoints to List<point> 
    		 
    		  contourList=contours.get(i).toList();
    		  Log.d("list size", i+"="+ contourList.size());
    		
    		// ADDING CHAIN CODE OF EACH CONTOUR
    		
    		chainCode.add(i, gradient(contourList))  ;
    		 Log.d("CHain code", ""+ i+ "= " + chainCode.get(i));
    	
    	   }
    	   
    	   else {
    		  
    		   String value="0";
    		  
    		  chainCode.add(i, value);
    		   
    		   
    	   }
    	  
     } 
      

          
      /* Drawing rectangles */
      /*for(int z=0;z<contours.size();z++){ 
    	  color=new Scalar(r.nextInt(255),r.nextInt(255), r.nextInt(255));
    	  Core.rectangle(output, 
	   Imgproc.boundingRect(contours.get(z)).br(), 
	   Imgproc.boundingRect( contours.get(z)).tl(),color);
	    }*/
    
    }
    
  
public String gradient(List<Point> list){
	
	double currentx,nextx,currenty,nexty,dx,dy;
	int counter=0;

// 	List <Double> gradDirection=new ArrayList<Double>() ;
	String chainCode="";
	StringBuffer sb=new StringBuffer();
	double direction=0.0;
	
	
	for(int i=0 ; i < list.size() ;i=i+(int)(list.size()/20)){ //sampling: 20 points
		
		if(i!=0 && i+(int)list.size()/20 < list.size() ){
			
			currentx= list.get(i).x;
			currenty=list.get(i).y;
			
			nextx= list.get(i+(int)list.size()/20).x;
			nexty=list.get(i+(int)list.size()/20).y;
			
			dx=nextx-currentx;
			dy=nexty-currenty;
			
			//gradDirection.add(counter, Math.tan(Math.toDegrees(dy/dx))) ;
			
		direction=Math.toDegrees(Math.atan(Math.toDegrees(dy/dx)));
	
		//Log.d("direction", ""+ i+ "= " + direction);
		
	if (direction < 0)
		{
			direction=direction+360;
		}
	//	Log.d("direction", ""+ i+ "= " + direction);
			
		if((direction >0 && direction <45) || direction == 45 ){
				
				chainCode=sb.append(0).toString();
				
				
			}
				
			else if((direction >45 && direction < 90) || direction == 90){
				
				chainCode=sb.append(1).toString();
			}
			
			else if((direction >90 && direction <135) || direction == 135){
				
				chainCode=sb.append(2).toString();
			}
			else if((direction >135 && direction <180) || direction == 180){
				
				chainCode=sb.append(3).toString();
			}
			
			else if((direction >180 && direction <225) || direction == 225){
				
				chainCode=sb.append(4).toString();
			}
			else if((direction >225 && direction <270) || direction == 270){
				
				chainCode=sb.append(5).toString();
			}
			else if((direction >270 && direction <315) || direction == 315){
				
				chainCode=sb.append(6).toString();
			}
			else if((direction >315 && direction <360) || direction == 360){
				
				chainCode=sb.append(7).toString();
			}
			else
				chainCode=sb.append(0).toString();
				
			
		
		//	Log.d("Gradient", "Grad "+ counter+"="+ gradDirection.get(counter));
			
			counter ++; // increment counter (its max is 20 )
			
			
			
		}
		
		
	}
	Log.d("Separation", "-----------------------");
	
	return chainCode;
	
	
	
}

  
    public void edges(){
    	Imgproc.Canny(grayImg, grayImg, 10, 100,3,true);
    }
   
   
    
    public void creatBitMaps(){
    	 bmpout =Bitmap.createBitmap(src.rows(), src.cols(),Bitmap.Config.ARGB_8888);  //returning the image back to bitmap format bec the emulator only deals with this format
         bmpout2 =Bitmap.createBitmap(src.cols(),src.rows(),Bitmap.Config.ARGB_8888);  //returning the image back to bitmap format bec the emulator only deals with this format
         Log.d("Test", "Created Bitmap");
    }
   
   
    public void convert2BitMap(){
    	       Utils.matToBitmap(output, bmpout2); 
    	       
                  }
    

}
