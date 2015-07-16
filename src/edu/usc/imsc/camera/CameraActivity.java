package edu.usc.imsc.camera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.usc.imsc.activities.LayersSelectionActivity;
import edu.usc.imsc.util.Tools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class CameraActivity extends Activity
{
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int UPLOAD_IMAGE_ACTIVITY_REQUEST_CODE = 200;
	private static final int MEDIA_TYPE_IMAGE = 0;
	private static final int MEDIA_TYPE_VIDEO = 1;
	private Uri fileUri;

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type)
	{
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FoodSeeker");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists())
		{
			if (!mediaStorageDir.mkdirs())
			{
				Log.d("FoodSeeker", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".JPG");
		} else if (type == MEDIA_TYPE_VIDEO)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".MP4");
		} else
		{
			return null;
		}

		return mediaFile;
	}

	private static Uri getOutputMediaFileUri(int type)
	{
		return Uri.fromFile(getOutputMediaFile(type));
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// create Intent to take a picture and return control to the calling
		// application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to
															// save the image
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
															// name

		// start the image capture Intent
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		
		//	start upload image
		Intent i = new Intent().setClass(this, ImageUploadActivity.class);
		i.putExtra("imgUri", fileUri.toString());
		Log.d("imgUri", fileUri.toString());
		startActivityForResult(i, UPLOAD_IMAGE_ACTIVITY_REQUEST_CODE);
	}
}