package com.android;

import java.security.KeyStore.LoadStoreParameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.opencv.imgproc.Moments;
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
	Bitmap bmpoutOriginal;// Nashwa
	ImageView img, img2; //nashwa
	List<Point> contourList;
	List<String> chainCode;
    List<MatOfPoint> contours;
    List<Integer> Centroids_x;
	Bitmap outgray,outThreshold,outAdapThreshold,outcontour; //Hadeel
	private static final String zero="0"; 
	List <Integer> min_x = new ArrayList<Integer>();
	List <Integer> max_x = new ArrayList<Integer>();
	List <Integer> x_coordinates = new ArrayList<Integer>();
	
  
	public void operate(Bitmap bmp) {
		 Log.v(this.toString(), "I am in imgprocessing in operate fun");
         //super.onCreate(savedInstanceState);
         //setContentView(R.layout.main);
         //findView();
         //getImage();
          prepareMat(bmp);
          Log.v(this.toString(), "after prepare"); 
          grayScale();
          Log.v(this.toString(), "after grayscale"); 
//          thresholding();
         addaptiveThresholding();
         Log.v(this.toString(), "after adaptive");
         //GuassianBluring();
         opening();
         Log.v(this.toString(), "opening");
//           edges();
          contours();
          Log.v(this.toString(), "contours");
          //edges();
         creatBitMaps();
         Log.v(this.toString(), "create bitmaps");
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
	

    
    public void prepareMat(Bitmap bmp){
        //	src=Utils.bitmapToMat(bmp);
    	
   	  	//grayImg= new Mat(src.rows(), src.cols(), CvType.CV_8UC1);
    	   
     	src=new Mat();
    	Utils.bitmapToMat(bmp,src); 
   	  	
       //bmp.recycle();//Nashwa
    
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
   	 //	Log.d("src Depth"," "+ src.depth());
   	   // Log.d("src Channels1"," "+ src.channels());
   	    
   	    
   	  
   	    // grayImg info
   	 //	Log.d("Channels1"," "+ grayImg.channels());
   	 //	Log.d("Depth1"," "+ grayImg.depth());
   	 	//Log.d("type"," "+ grayImg.type());

   	  	
   	  //	Log.d("Test", "Prepared Mat");
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
    /*	Imgproc.morphologyEx(
    			grayImg, 
    			grayImg, 
    			Imgproc.MORPH_ERODE, 
    			Imgproc.getStructuringElement(Imgproc.MORPH_RECT,i) 
    			);*/
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
    	Moments m=new Moments();
    	int centroid_x=0, centroid_y=0, idx=0;
    	List<MatOfPoint> contoursList=new ArrayList<MatOfPoint>();
    	
    	
    	
    	 Centroids_x=new ArrayList<Integer>();
    	
    	contours=new ArrayList<MatOfPoint>(5);
    	output= new Mat(grayImg.rows(), grayImg.cols(),  CvType.CV_8UC3);
    	
    	Log.d("Test", "Entered Contours");
    	
  	 
    	Imgproc.findContours(grayImg, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
    	
    	chainCode=new ArrayList<String>(contours.size());

      
   
       //Random and Scalar only for generatin colorful contours 7aba dala3 y3ny :D
       Random r=new Random(); 
       Scalar color=new Scalar(r.nextInt(255),r.nextInt(255), r.nextInt(255));
    
   // Drawing all contours mara wa7da 
   //   Imgproc.drawContours(output, contours, -1, color, 0); // -1 means draw all the contours u have 
    
     
     /* ANOTHER WAY FOR  PRINTING CONTOURS  */
      
      /* DRAWING CONTOURS & GETTING GRADIENT */
      
       Log.d("Test", contours.size()+" coutners no.");
    
   
      for(int i=0;i<contours.size();i++){
    	
    	
    	   if(Imgproc.contourArea(contours.get(i))>300){  // 		   
    		 
    	//	  color=new Scalar(r.nextInt(255),r.nextInt(255), r.nextInt(255));
    		
    		  Imgproc.drawContours(output, contours, i, color, 0);
    		  
    		//  Log.d("list contours size", ""+ contours.get(i).size());
    		  Log.d("list contours area", ""+i+"="+ Imgproc.contourArea(contours.get(i)));
    		//  Log.d("list contours", ""+ contours.get(i));
    		  
    		  m= Imgproc.moments(contours.get(i), false);
    		 
    	     
    		  
    		  centroid_x=(int)(m.get_m10()/m.get_m00());
    		  centroid_y=(int)(m.get_m01()/m.get_m00());
    	  
    		  Centroids_x.add(idx, centroid_x);
         
    		  Log.d("centroid", ""+  centroid_x + ","+centroid_y);
    	 
    		  //ADDING ALL CONTOURS HERE 
    		
    		  contoursList.add(idx,contours.get(i));
    		  
    	
    	  
    		  
    		  idx++;
    	
    	   }
    	   
    
    	  
     } 
     
      Log.d("Test", ""+idx);
      idx=idx-1;
         
      List <Integer> oldCentroids_x_list=new ArrayList<Integer>();
    oldCentroids_x_list.addAll(Centroids_x);
      
 
    // SORTING CENTROIDS OF SELECTED CONTOURS 
    Collections.sort(Centroids_x);
   
    
    int oldIndex=0, words=0, prev_y=0,space=0, prev_space=0, last_word_size=0;
    List<Integer > numOfLetters_word= new ArrayList<Integer>();
   
  // testing
    for(int s=0; s<Centroids_x.size();s++){
    	
    	Log.d("After sorting",""+ Centroids_x.get(s));
    	
    }
    
  /* 
    
    Log.d("size",""+ Centroids_x.size());
    color=new Scalar(r.nextInt(255),r.nextInt(255), r.nextInt(255));
    oldIndex=oldCentroids_x_list.indexOf(Centroids_x.get(2));  
    Imgproc.drawContours(output, contoursList, oldIndex, color, 0);
   
    Log.d("oldIndex", ""+oldIndex);*/
    
       for(int z=0 ;z<Centroids_x.size();z++){
    	  
    	  oldIndex=oldCentroids_x_list.indexOf(Centroids_x.get(z));
    	  chainCode.add(z,  gradient(contoursList.get(oldIndex).toList(), z) ) ;
    	
    	  Log.d("CHain code", ""+ z+ "= " + chainCode.get(z));
    	  
    	/*  if(y+1 <Centroids_x.size()){
    	  oldIndex=oldCentroids_x_list.indexOf(Centroids_x.get(y+1));
    	  chainCode.add(y+1,  gradient(contoursList.get(oldIndex).toList(), y+1) ) ;
    	  }
    	  
    	  Log.d("CHain code", ""+ y+ "= " + chainCode.get(y+1));
    	  */

       }
       
       
       for(int y=0 ;y<Centroids_x.size();y++){
    	  
    	 
    	  
    	  if(y+1 <Centroids_x.size())
    	  {
    		//  Log.d("Space", "= "+( Centroids_x.get(y+1)-Centroids_x.get(y)) );
    		
    		//  if(y!=0){
    		 
    		//	  space=Centroids_x.get(y+1)-Centroids_x.get(y);
    			//  prev_space=Centroids_x.get(y)-Centroids_x.get(y-1);
    			
    			 space=min_x.get(y+1) - max_x.get(y);
    		
    			 Log.d("Space", "= "+space );
    			 
    			 // prev_space=min_x.get(y)-max_x.get(y-1);
    			 
    			 
    			  
    			  if(space >20){
    				  //this means a new word 
    				  words ++;
    				  if(y==1 || prev_y==0)
    					  numOfLetters_word.add(words-1,y-prev_y+1);
    				  else
    					  numOfLetters_word.add(words-1,y-prev_y);
    				
    				  Log.d("Number of words", "= "+ words );
    				  Log.d("y", "= "+ y);
    				  Log.d("y prev", "= "+ prev_y );
    				  Log.d("Number of letters in word no ", " "+ words +"="+ numOfLetters_word.get(words-1) );
    				 
    				 
    				  prev_y=y;
			  	
    		  }
    	//	  }
    		  
    	  } 
      }
       
       if(numOfLetters_word.size()!=0){
       
       last_word_size=chainCode.size()-numOfLetters_word.get(0);
       for(int l=1; l<numOfLetters_word.size(); l++){
    	   
    	   last_word_size=last_word_size-numOfLetters_word.get(l);
    	   
       }
     
       if(last_word_size !=0){
       numOfLetters_word.add(words,last_word_size);
       Log.d("last words letters ", "= "+ numOfLetters_word.get(words));
       words++;
       }
       }
    
    }
  

public String gradient(List<Point> list, int index){
	
	double currentx,nextx,currenty,nexty,dx,dy;
	int counter=0;

// 	List <Double> gradDirection=new ArrayList<Double>() ;
	String chainCode="";
	StringBuffer sb=new StringBuffer();
	double direction=0.0;
	int step=list.size()/50;
	int limit=(list.size()%50);// heba's idea
	
	

	/**************GETTING X-COORDINATES TO CALCULATE DISTANCE BETWEEN WORDS****************/
	
	//this part will be used in contours function
	for(int h=0;h<list.size();h++){
		
		x_coordinates.add(h,(int)list.get(h).x);
	
		
	}
	
//	max_x.add(index, Collections.max(x_coordinates));

	//min_x.add(index, Collections.min(x_coordinates));
	
	max_x.add(index, get_max(x_coordinates));
	
	min_x.add(index, get_min(x_coordinates));
	
	
	x_coordinates.clear();

	
	Log.d("Max", ""+index + "= " + max_x.get(index));
	Log.d("Min", ""+ index+ "= " + min_x.get(index));
	
	
	/******************CALCULATING CHAIN CODE**********************/	
	
	for(int i=0 ; i < list.size()-limit ;i=i+step){ //sampling: 20 points

		
		if( i+(int)list.size()/50 < list.size() ){
		
	
		
			currentx= list.get(i).x;
			currenty=list.get(i).y;
			
		//	Log.d("direction", ""+ i+ "= " + list.get(i));
			
			nextx= list.get(i+(int)list.size()/50).x;
			nexty=list.get(i+(int)list.size()/50).y;
		
			dx=nextx-currentx;
			dy=nexty-currenty;
			
		
			
			direction=Math.toDegrees(Math.atan2(dy,dx));
	
			//	Log.d("direction", ""+ i+ "= " + direction);
		
	if (direction < 0 || direction== -0.0)
		{
			direction=direction+360;
		}
	
//	Log.d("direction", ""+ i+ "= " + direction);
			
		if((direction >0 && direction <45) || direction == 45 ){
				
				chainCode=sb.append(0).toString();
				
				
			}
				
	
		
		else if((direction >22.5 && direction < 67.5) || (direction == 67.5) || (direction == 22.5)){
				chainCode=sb.append(1).toString();
			}
			
	
		else if((direction >67.5 && direction <112.5) || direction == 112.5){
				
				chainCode=sb.append(2).toString();
			}
	
		else if((direction >112.5 && direction <157.5) || direction == 157.5){	
				chainCode=sb.append(3).toString();
			}
			
			
			else if((direction >157.5 && direction <202.5) || direction == 202.5){
				chainCode=sb.append(4).toString();
			}
			
			else if((direction >202.5 && direction <247.5) || direction == 247.5){	
				chainCode=sb.append(5).toString();
			}
		
			else if((direction >247.5 && direction <292.5) || direction == 292.5){	
				chainCode=sb.append(6).toString();
			}
		
			else if((direction >292.5 && direction <337.5) || direction == 337.5){	
				chainCode=sb.append(7).toString();
			}
			else
				chainCode=sb.append(0).toString();
				
		
			
			counter ++; // increment counter (its max is 20 )
			
			
			
		}
		
		
	}
	Log.d("Separation", "-----------------------");
	
	
	
	
	
	return chainCode;
	
	
	
}

//public String Normalization(String mychainCode)
//{
//	int IntChain;
//	IntChain=Integer.parseInt(mychainCode);
//	
//	for(int i=0;i<mychainCode.length()-1;i++)
//	{
//		int num1=Integer.parseInt(mychainCode, (char)mychainCode.charAt(i));
//		int num2=Integer.parseInt(mychainCode, mychainCode.charAt(i+1));
//		
//		if(num1==num2)
//		{
//			
//		}
//	}
//	return mychainCode;
//}


public int get_max(List<Integer > x_coordinates){
	
	int maxValue = (Integer) x_coordinates.get(0);  
	  for(int i=1;i < x_coordinates.size();i++){  
	   
		  if(x_coordinates.get(i) > maxValue){  
	      
			  maxValue = x_coordinates.get(i);  
	    }  
	  }  
	
	
	return maxValue;
	
	
}

public int get_min(List<Integer > x_coordinates){
	
	int minValue = (Integer) x_coordinates.get(0);  
	  for(int i=1;i<x_coordinates.size();i++){  
	    if(x_coordinates.get(i) < minValue){  
	      minValue = x_coordinates.get(i);   
	    }  
	  }  
	
	return minValue;
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