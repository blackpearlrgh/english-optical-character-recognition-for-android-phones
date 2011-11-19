package com.Gray32Image.Image;

import jjil.core.Error;
import jjil.core.Gray32Image;
import jjil.core.Gray8Image;
import jjil.core.Image;
import jjil.core.PipelineStage;
import android.app.Activity;
import android.os.Bundle;

public class Gray32ImageActivity extends PipelineStage{
    
	public Gray32ImageActivity()
	{
	}
	/** Converts an 8-bit gray image into a 32-bit image by replicating
     * changing the data range of the bytes from -128->127 to 0->255.
     *
     * @param image the input image.
     * @throws IllegalArgumentException if the input is not a
     * Gray8Image
     */

	@Override
	public void push(Image image) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if (!(image instanceof Gray8Image)) {
            throw new IllegalArgumentException(image.toString() + "" +
                " should be a Gray8Image, but isn't");
        }
		Gray8Image gray=(Gray8Image) image; // get a pointer to the input parameter as a Gray8Image
		byte[] GrayData=gray.getData();// access the data (pixels) in the image
	
		Gray32Image gray32=new Gray32Image(image.getWidth(), image.getHeight());//for the o/p
		int[] gray32data=gray32.getData();//get a pointer to the output data
		
		for(int i=0;i<gray.getWidth()*gray.getHeight();i++)
		{
			/* Convert from signed byte value to unsigned byte for
             * storage in the 32-bit image.
             */
			int grayUnsigned = ((int)GrayData[i]) - Byte.MIN_VALUE;
            /* Assign 32-bit output */
            gray32data[i] = grayUnsigned;
		}
		 super.setOutput(gray32); // provide the output to the caller. This is done using PipelineStage’s protected setOutput method
	}		
}
