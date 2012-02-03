package com.example;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView.Validator;

/*import org.opencv.android.Utils;
import com.googlecode.javacv.cpp.opencv_core;

import cls.org.opencv.core.Core;
import cls.org.opencv.core.Mat;
import cls.org.opencv.core.Point;
import cls.org.opencv.core.Scalar;
import cls.org.opencv.core.CvType;
import cls.org.opencv.imgproc.Imgproc;
import cls.org.opencv.objdetect.Objdetect;

import org.opencv.*;*/


import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_objdetect;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import com.googlecode.javacv.cpp.*;
import com.googlecode.javacpp.Loader;

import java.awt.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class OpencvtestActivity extends Activity {
    /** Called when the activity is first created. */
	//Mat img;
		IplImage dst;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // IplImage orgImg = (IplImage) cvLoad("colordetectimage.jpg");
        IplImage img =  cvLoadImage("IMAG0035.jpg");
        cvSmooth(img, dst, CV_GAUSSIAN,  11, 11, 0.2f, 0.1f);
        cvSaveImage("new.jpg", dst);
        
        
    }
}