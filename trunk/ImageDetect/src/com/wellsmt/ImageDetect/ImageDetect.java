package com.wellsmt.ImageDetect;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


public class ImageDetect extends Activity implements SurfaceHolder.Callback,
View.OnClickListener{
final int RESTART_PREVIEW = 1;
final int PROGRESS = 2;
final int RESTART_PREVIEW2 = 3;
private boolean boolCaptureOnFocus = false;
private boolean boolFocusButtonPressed = false;
private boolean boolFocused = false;
private boolean boolFocusing = false;
private boolean boolPreviewing = false;
private Camera camera = null;
private int nPreviewWidth, nPreviewHeight;
private SurfaceView preview = null;
private SurfaceHolder surfaceHolder = null;
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
Toast.makeText(this, "Welcome to ImageProx", Toast.LENGTH_LONG).show();
setContentView(R.layout.capture);
preview = (SurfaceView) findViewById(R.id.Preview);
SurfaceHolder s = preview.getHolder();
s.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
s.addCallback(this);
}
private final class AutoFocusCallback implements Camera.AutoFocusCallback {
public void onAutoFocus(boolean focused, Camera camera) {
boolFocusing = false;
boolFocused = focused;
if (focused) {
if (boolCaptureOnFocus) {
Camera.Parameters parameters = camera.getParameters();
parameters.set("jpeg-quality", 75);
parameters.setPictureSize(320, 240);
camera.setParameters(parameters);
camera.takePicture(null, null, new JpegPictureCallback());
clearFocus();
}
boolCaptureOnFocus = false;
}
}
};
private final class JpegPictureCallback implements PictureCallback {
	public void onPictureTaken(byte [] jpegData, android.hardware.Camera camera) {
		ModImage.setJpegData(jpegData);
		startActivity(new Intent("com.wellsmt.ImageDetect.ModImage"));
		stopPreview();
		}
		};
		private void autoFocus() {
		if (!this.boolFocusing) {
		if (this.camera != null) {
		this.boolFocusing = true;
		this.boolFocused = false;
		this.camera.autoFocus(new AutoFocusCallback());
		}
		}
		}
		private void clearFocus() {
		this.boolFocusButtonPressed = false;
		this.boolFocused = false;
		this.boolFocusing = false;
		}
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		switch (keyCode) {
		case KeyEvent.KEYCODE_CAMERA:
		case KeyEvent.KEYCODE_DPAD_CENTER:
		if (event.getRepeatCount() == 0) {
		if (this.boolFocused || !this.boolPreviewing) {
		clearFocus();
		} else {
		this.boolCaptureOnFocus = true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && !this.boolFocusButtonPressed)
		{
		autoFocus();
		}
		}
		return true;
		case KeyEvent.KEYCODE_FOCUS:
		this.boolFocusButtonPressed = true;
		if (event.getRepeatCount() == 0) {
		if (this.boolPreviewing) {
		autoFocus();
		}
		}
		return true;
		}
		return super.onKeyDown(keyCode, event);
		}
		private void startPreview(int nWidth, int nHeight) {
		this.nPreviewWidth = nWidth;
		this.nPreviewHeight = nHeight;
		if (this.boolPreviewing) {
		return;
		}
		if (this.camera == null) {
		this.camera = android.hardware.Camera.open();
		}
		if (this.camera != null && this.surfaceHolder != null) {
		Camera.Parameters parm = this.camera.getParameters();
		parm.setPreviewSize(nWidth, nHeight);
		this.camera.setParameters(parm);
		try {
		this.camera.setPreviewDisplay(this.surfaceHolder);
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.camera.startPreview();
		this.boolPreviewing = true;
		}
		}
		private void stopPreview() {
		if (this.camera != null) {
		this.camera.stopPreview();
		this.camera.release();
		this.camera = null;
		this.boolPreviewing = false;
		}
		}
		public void onClick(View v) {
		// TODO Auto-generated method stub
		}
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
		int height) {
		if (holder.isCreating()) {
		startPreview(width, height);
		}
		}
		public void surfaceCreated(SurfaceHolder holder) {
		this.surfaceHolder = holder;
		}
		public void surfaceDestroyed(SurfaceHolder holder) {
		stopPreview();
		this.surfaceHolder = null;
		}
		}
}