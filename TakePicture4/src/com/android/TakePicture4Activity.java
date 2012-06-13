package com.android;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TakePicture4Activity extends Activity {
    /** Called when the activity is first created. */
	  private static final int SELECT_PICTURE = 1;
	    private static final int OPEN_CAMERA = 2;
	    //from maha's project
	    private static final int GET_TEXT =3 ;   //Not in the other one
	    private static final int LAYOUT_CHANGE =4 ; //Not in the other one

	    private String selectedImagePath;
	    //ADDED
	    private String filemanagerstring;
	    Button b1,b2,Displaytext,Menubtn;
	    ImageView iv;
	    TextView tv ;   

	    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupui();
        addListners();

    }
    
    private void addListners() {
		b2.setOnClickListener(openCamera);
        b1.setOnClickListener(openGallaery);
        Displaytext.setOnClickListener(display_text);
        Menubtn.setOnClickListener(MenuList);
        Log.v(this.toString(), "Inside addListeners.");
      
	}
    

	private void setupui() {
		b1  =  (Button) findViewById(R.id.button1);
        b2  =  (Button) findViewById(R.id.button2);
        Displaytext  =  (Button) findViewById(R.id.DisplayTxt);
        Menubtn  =  (Button) findViewById(R.id.MenuBtn);
        iv  =  (ImageView) findViewById(R.id.imageView1);
        Log.v(this.toString(), "SEUTUPUI.");
    	
	}
	
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	if(requestCode == OPEN_CAMERA)
	        {
	            if(resultCode == RESULT_OK)
	            {
	                if(data.hasExtra("data"))
	                {
	                    /* if the data has an extra called "data" we assume the returned data 
	                     * is from the usual camera app*/
	                    //retrieve the bitmap from the intent
	                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
	                    //update the image view with the bitmap
	                    iv.setImageBitmap(thumbnail);
	                }
	                else if(data.getExtras()==null)
	                {
	                    /* if there are no extras we assume its the miui camera 
	                     * (which returns the path to the image in the returned data)*/
	                    Toast.makeText(getApplicationContext(), "No extras to retrieve!",Toast.LENGTH_SHORT).show();
	                    //retrieve the path from the intent using data.getData().getPath() and create a BitmapDrawable using this path
	                    BitmapDrawable thumbnail = new BitmapDrawable(getResources(), data.getData().getPath());
	                    //update the image view with the newly created drawable
	                    iv.setImageDrawable(thumbnail); 
	                }
	            }
	            else if (resultCode == RESULT_CANCELED)
	            {
	                Toast.makeText(getApplicationContext(), "Cancelled",Toast.LENGTH_SHORT).show();
	            }
	        }
	    	else if (requestCode == SELECT_PICTURE)
	    	{
	    	        if(resultCode == RESULT_OK)
	    	        {
	    	             if (data != null) {
	    	                  //our BitmapDrawable for the thumbnail
	    	                  BitmapDrawable bmpDrawable = null;
	    	                  //try to retrieve the image using the data from the intent
	    	                  Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
	    	                  if(cursor != null)
	    	                  {
	    	                      /*if the query worked the cursor will not be null, 
	    	                       * so we assume the normal gallery was used to choose the picture*/
	    	                      cursor.moveToFirst();  //if not doing this, 01-22 19:17:04.564: ERROR/AndroidRuntime(26264): Caused by: android.database.CursorIndexOutOfBoundsException: Index -1 requested, with a size of 1
	    	                      int idx = cursor.getColumnIndex(ImageColumns.DATA);
	    	                      String fileSrc = cursor.getString(idx);
	    	                      Bitmap bitmapPreview = BitmapFactory.decodeFile(fileSrc); //load preview image
	    	                      bmpDrawable = new BitmapDrawable(bitmapPreview);//set the BitmapDrawable to the loaded image
	    	                  }
	    	                  else
	    	                  {
	    	                      /*if the cursor is null after the query the data returned is different so we assume 
	    	                       * the miui gallery was used (so the data contains the path to the image)*/
	    	                      bmpDrawable = new BitmapDrawable(getResources(), data.getData().getPath());
	    	                  }
	    	                  iv.setImageDrawable(bmpDrawable);//update our imageview with the BitmapDrawable
	    	              }
	    	              else {
	    	                  Toast.makeText(getApplicationContext(), "Cancelled",Toast.LENGTH_SHORT).show();
	    	              }
	    	        }
	    	        else if (resultCode == RESULT_CANCELED)
	    	        {
	    	            Toast.makeText(getApplicationContext(), "Cancelled",Toast.LENGTH_SHORT).show();
	    	        }
	    	  }//end of else 
	    }//end of start activity
	  
	  /*** Using the Input Stream  */
	    private void displayImage() {
	    	FileInputStream in;
	        BufferedInputStream buf;
	            try 
	            {
	            	System.out.println(selectedImagePath);
	                in = new FileInputStream(selectedImagePath);
	                buf = new BufferedInputStream(in,4000);
	                System.out.println("1"+buf);
	                byte[] bMapArray= new byte[buf.available()];
	                buf.read(bMapArray);
	                Bitmap bMap = BitmapFactory.decodeFile(selectedImagePath);
	                iv.setImageBitmap(bMap);
	                if (in != null) 
	                {
	                    in.close();
	                }
	                if (buf != null) 
	                {
	                    buf.close();
	                }
	            } 
	            catch (Exception e) 
	            {
	                Log.e("Error reading file", e.toString());
	            }
		}

		/**
		 * Get Path in case of NULL Cursor 
		 * @param uri
		 * @return
		 */
	    public String getPath(Uri uri) {

	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = managedQuery(uri, projection, null, null, null);
	        if(cursor!=null)
	        {
	            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
	            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
	            int column_index = cursor
	            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	            cursor.moveToFirst();
	            return cursor.getString(column_index);
	        }
	        else return null;
	    }
	    
	    OnClickListener MenuList  = new OnClickListener() {
	    	//	@Override
	            public void onClick(View v) {
	    		/*	Intent intent2 = new Intent("com.android.MENU");
	    			startActivity(intent2); */
	            	
	    			try {
	    			Class myClass = Class.forName ("com.android.Menu");
	    				Intent menuIntent = new Intent (TakePicture4Activity.this , myClass);
	    				startActivity(menuIntent);
	    			} catch (ClassNotFoundException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			
	    			}	
	    		};		
	    			

	   
	  OnClickListener openCamera  = new OnClickListener() {
			//	@Override
				public void onClick(View v) {
					Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
				    startActivityForResult(intent2, OPEN_CAMERA);
					
				    }	
				};
				
							
						
		       OnClickListener openGallaery = new OnClickListener() {
		    	//	@Override
				public void onClick(View v) {
					 Intent intent = new Intent();
		             intent.setType("image/*");
		             intent.setAction(Intent.ACTION_GET_CONTENT);
		             startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
				}
			};


	OnClickListener display_text  = new OnClickListener() {
		//	@Override
			public void onClick(View v) {
				try {
					 Log.v(this.toString(), "Inside on click listener for screen 1.");
					Class myClass = Class.forName("com.android.ShowText");
					 Intent data = new Intent(TakePicture4Activity.this,myClass);			
					startActivity(data)	;
//					finish();
						
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

}