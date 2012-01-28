package com.thenewboston.android.fatma;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Adapter;
public class window extends Activity {
    /** Called when the activity is first created. */
	TextView textTargetUri;
	ImageView targetImage;
	Button b1,b2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Gallery gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));

        gallery.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(HelloGallery.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        b1=(Button) findViewById(R.id.loadimage);
        b2=(Button) findViewById(R.id.binarization);
        textTargetUri = (TextView)findViewById(R.id.targeturi);
        targetImage=(ImageView) findViewById(R.id.targetimage);
        b1.setOnClickListener(new Button.OnClickListener(){

     @Override
     public void onClick(View arg0) {
        	   // TODO Auto-generated method stub
        	   Intent intent = new Intent(Intent.ACTION_PICK,
        	     android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        	   startActivityForResult(intent, 0);
        	  }});
    }
        
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 // TODO Auto-generated method stub
 super.onActivityResult(requestCode, resultCode, data);
 if (resultCode == RESULT_OK){
	 Uri targetUri = data.getData();
	 textTargetUri.setText(targetUri.toString());
	 Bitmap bitmap;
	 try {
	  bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
	  targetImage.setImageBitmap(bitmap);
	 } catch (FileNotFoundException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	 }
	}
	}
}
public class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;

    private Integer[] mImageIds = {
            R.drawable.sample_1,
            R.drawable.sample_2,
            R.drawable.sample_3,
            R.drawable.sample_4,
            R.drawable.sample_5,
            R.drawable.sample_6,
            R.drawable.sample_7
    };

    public ImageAdapter(Context c) {
        mContext = c;
        TypedArray attr = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
        mGalleryItemBackground = attr.getResourceId(
                R.styleable.HelloGallery_android_galleryItemBackground, 0);
        attr.recycle();
    }
    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);

        imageView.setImageResource(mImageIds[position]);
        imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(mGalleryItemBackground);

        return imageView;
    }
}
	 
       