package com.kyat.DJbooth;

import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.Toast;

public class PhotoFunctions {

	public static double getTextScale(String photoFilePath){
    	double temp;
    	BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
    	scaleOptions.inJustDecodeBounds = true;
    	BitmapFactory.decodeFile(photoFilePath,scaleOptions);
    	
    	temp = ((double)(scaleOptions.outHeight * scaleOptions.outWidth)) / (double)(800*600);
    	
    	if(temp < 1.0){
    		temp = 1.0;
    	}
    	
    	return temp;
    }
	public static String getDate(){
		  Date currTime = new Date(System.currentTimeMillis());
		  return Integer.toString(currTime.getDate()) + Integer.toString(currTime.getMonth()) + Integer.toString(currTime.getYear()+1900) + Integer.toString(currTime.getHours()) + Integer.toString(currTime.getMinutes()) + Integer.toString(currTime.getSeconds());
		 }
	
	public static Bitmap getPhotoBitmap(String photoFilePath){
	  	  BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
	  	  
	  	  bmpOptions.inMutable = true;
	  	  bmpOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
	  	  bmpOptions.inSampleSize = 1;//scaleRatio(photoFilePath);
	  	  
	  	  return BitmapFactory.decodeFile(photoFilePath,bmpOptions);
	    }
	
	public static Bitmap rotateBitmap(Bitmap inBMP, int rotation){
        Matrix  rotMat =  new Matrix();
  
        rotMat.postRotate(rotation);
        return Bitmap.createBitmap(inBMP, 0, 0, inBMP.getWidth(), inBMP.getHeight(), rotMat, true);
       }
	
public static Bitmap addTextToBitmap(Bitmap inBMP, String userName, String modelNo, Double textScale){
    	
		double scale = Math.sqrt(textScale);
		int fontSize =  (int)(16.0 * scale);
		int blackOutSize = (int)(2.0 * scale);
		int xpos = (int)(30.0 * scale);
		int ypos = (int)(40.0 * scale);
		int xposBox = (int)((double)xpos - 10.0*scale);
		int yposBox = ypos - fontSize - (2*blackOutSize); //(int)((double)(ypos - fontSize) - (10.0*scale));
		int yposBox2 = (int)((double)(ypos)+10.0*scale);
		int xposBox2;
		String userString, modelString = "";
		boolean modelFlag = false;
		//float[] boxPoints = new float[6];
		
		Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		
	  strokePaint.setARGB(150, 0,  0, 0);
	  strokePaint.setTextSize(fontSize);
	  strokePaint.setStyle(Paint.Style.STROKE);
	  strokePaint.setStrokeWidth(blackOutSize); 	
    	
	  Canvas editBMP = new Canvas(inBMP);
	  Paint textOptions = new Paint(Paint.ANTI_ALIAS_FLAG);
	  Paint boxOptions = new Paint();
	  Paint lineOptions = new Paint();
	  
  	  textOptions.setColor(Color.YELLOW); //text colour
  	  //textOptions.setStrokeWidth(1);
  	  textOptions.setTextSize(fontSize); //set text size
  	  //textOptions.setFakeBoldText(true); //DUN MAKE DIS BOLD
  	  
  	  //set box transparency
  	  boxOptions.setColor(Color.WHITE);
  	  boxOptions.setAlpha(164);
  	  lineOptions.setColor(Color.BLACK);
  	  lineOptions.setStrokeWidth((float)(1.0*scale));
  	  
  	  editBMP.drawBitmap(inBMP, 0, 0,textOptions);
  	  userString = "Photo taken by: " + userName;
  	  
  	  //Check if the model number is specified
  	  if(!(modelNo.equals(""))) {
  		  modelString = "Model number: " + modelNo;
  		  yposBox2 = (int)((double)yposBox2 + (double)fontSize + (double)blackOutSize);
  		  modelFlag = true;
  	  }
  	  
  	  //Stupid complicated length calculations
  	  if (userString.length() > modelString.length()){
  		  xposBox2 = (int)(((double)userString.length())*10.0*scale + 10.0*scale);
  	  }
  	  else{
  		xposBox2 = (int)(((double)modelString.length())*10.0*scale + 10.0*scale);
  	  }
  	  
  	  //Draw the box
  	  editBMP.drawRect(xposBox, yposBox, xposBox2, yposBox2, boxOptions);
  	  
  	  //Draw the line: top,bottom,left,right
  	  float[] boxPoints = {xposBox,yposBox,xposBox2,yposBox,xposBox2,yposBox,xposBox2,yposBox2,xposBox2,yposBox2,xposBox,yposBox2,xposBox,yposBox2,xposBox,yposBox};
  	  editBMP.drawLines(boxPoints,lineOptions);
  	  //editBMP.drawLine(xposBox,yposBox,xposBox2,yposBox, strokePaint);
  	  
  	  //Draw the text
  	  editBMP.drawText(userString, xpos, ypos, strokePaint);
	  editBMP.drawText(userString, xpos, ypos, textOptions);
	  
	  if(modelFlag){
		  editBMP.drawText(modelString, xpos, ypos + fontSize + blackOutSize, strokePaint);
		  editBMP.drawText(modelString, xpos, ypos + fontSize + blackOutSize, textOptions);
	  }
	  
  	  return inBMP;
    }
	
}
