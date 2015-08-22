package com.jianyue.main.controller;

import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback
{

	private Context mContext;

	private Camera mCamera;

	private SurfaceHolder mHolder;

	private List<Size> mSupportedPreviewSizes;

	private List<String> focusModes;
	private int numberOfCameras;

	public static int defaultCameraId;

	Size mPreviewSize;

	private FLASH_MODE flash_mode;

	public CameraPreview(Context context, FLASH_MODE flash_mode)
	{
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		this.flash_mode = flash_mode;
		init();
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void init()
	{
		// TODO Auto-generated method stub

		Log.e("log_tag", "init");

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		if (android.os.Build.VERSION.SDK_INT >= 9)
		{
			try
			{
				numberOfCameras = Camera.getNumberOfCameras();
				// Find the ID of the default camera
				CameraInfo cameraInfo = new CameraInfo();
				for (int i = 0; i < numberOfCameras; i++)
				{
					Camera.getCameraInfo(i, cameraInfo);
					if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT)
					{
						defaultCameraId = i;
					}
				}
			} catch (NoSuchMethodError e)
			{
				e.printStackTrace();
				numberOfCameras = 1;
				defaultCameraId = 0;
			}
		}
		Log.d("Tag","numberOfCameras: " + numberOfCameras);
		Log.d("Tag","defaultCameraId: " + defaultCameraId);
		setCamera();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// We purposely disregard child measurements because act as a
		// wrapper to a SurfaceView that centers the camera preview instead
		// of stretching it.
		Log.e("log_tag", "onMeasure");
		final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		setMeasuredDimension(width, height);

		Log.e("log_tag", "width: " + width);
		Log.e("log_tag", "height: " + height);

		if (mSupportedPreviewSizes != null)
		{
			mPreviewSize = getBestPreviewSize(width, height, mSupportedPreviewSizes);
			try
			{
				Log.d("log_tag", "Actual Preview " + mPreviewSize.height + mPreviewSize.width);
			}
			catch(Exception e)
			{
				Log.d("log_tag", "Actual Preview error in catch " + e.getMessage() );
			}
		}
	}

	private Camera.Size getBestPreviewSize(int width, int height, List<Size> sizes)
	{
		Camera.Size result = null;

		for (Camera.Size size : sizes)
		{
			if (size.width <= width && size.height <= height)
			{
				if (result == null)
				{
					result = size;
				}
				else
				{
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea)
					{
						result = size;
					}
				}
			}
		}

		return (result);
	}

	/*@SuppressWarnings("unused")
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h)
	{
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes)
		{
			Log.e("log_tag", "supported size: " + size.width + " : " + size.height);
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff)
			{
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null)
		{
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes)
			{
				if (Math.abs(size.height - targetHeight) < minDiff)
				{
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}*/

	public void setCamera()
	{
		// setHolder();
		Log.e("log_tag", "setCamera");
		mCamera = CameraPreview.getCameraInstance();
		if (mCamera != null)
		{
			Log.e("log_tag", "Camera not null");
			mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
			Log.e("log_tag", "Supporeted size count " + mSupportedPreviewSizes.size() + "");
			for(int i = 0 ; i < mSupportedPreviewSizes.size() ; i++)
			{
				Log.e("log_tag", "Supporeted  Height : " + mSupportedPreviewSizes.get(i).height + "  Width : " + mSupportedPreviewSizes.get(i).width);
			}
			focusModes = mCamera.getParameters().getSupportedFocusModes();
			requestLayout();
		}

	}

//	private String getFlashMode(FLASH_MODE mode)
//	{
//		switch (mode)
//		{
//		case ON:
//			return Camera.Parameters.FLASH_MODE_ON;
//
//		case AUTO:
//			return Camera.Parameters.FLASH_MODE_AUTO;
//
//		case OFF:
//			return Camera.Parameters.FLASH_MODE_OFF;
//		default:
//			break;
//		}
//		return Camera.Parameters.FLASH_MODE_OFF;
//	}

//	public void setFlashModeProperty(FLASH_MODE mode)
//	{
//		flash_mode = mode;
//		Camera.Parameters parameters = mCamera.getParameters();
//		parameters.setFlashMode(getFlashMode(flash_mode));
//
//		mCamera.setParameters(parameters);
//	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void setCameraParameters()
	{
		Log.e("log_tag", "setCameraParameters");
		Camera.Parameters parameters = mCamera.getParameters();
//		parameters.set("orientation", "portrait");
		Log.d("log_tag", "Width : " + mPreviewSize.width + "   Height : " + mPreviewSize.height);
//		if(mPreviewSize.width > mPreviewSize.height)
//		{
//			parameters.setPreviewSize(mPreviewSize.height, mPreviewSize.width);
//		}
//		else
//		{
//			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//		}
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//		parameters.setPreviewSize(480,480);
		if (android.os.Build.VERSION.SDK_INT >= 14)
		{
			if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
			{
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			}
		}

		// if (parameters.getSceneMode() == null) {
		// isCounterForRotate = false;
		// Log.i("camera paramerter are to be null", "true");
		// mCamera.setDisplayOrientation(270);
		// } else {
		// Log.i("camera parameters are to be auto", "true");
		// mCamera.setDisplayOrientation(90);
		// }

		// SharedPreferences sps = context.getSharedPreferences("flashlight",
		// Context.MODE_WORLD_WRITEABLE);
		// Log.i("flash is ", "" + flash);
		// if (flash == 0)
		// {
		// LogM.i( "flash mode is auto");
		// parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
		// }
		// else

		//parameters.setFlashMode(getFlashMode(flash_mode));

		mCamera.setParameters(parameters);
		requestLayout();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub
		Log.e("log_tag", "surfaceCreated");

		// The Surface has been created, now tell the camera where to draw the
		// preview.
		// try {
		// setCameraParameters();
		// mCamera.setDisplayOrientation(90);
		// mCamera.setPreviewDisplay(mHolder);
		//
		// } catch (IOException e) {
		// Log.d("log_tag", "Error setting camera preview: " + e.getMessage());
		// }
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		// TODO Auto-generated method stub

		Log.e("log_tag", "surfaceChnaged");

		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (mHolder.getSurface() == null)
		{
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try
		{
			mCamera.stopPreview();
		} catch (Exception e)
		{
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try
		{
			setCameraParameters();
			mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e)
		{
			Log.d("log_tag", "Error starting camera preview: " + e.getMessage());
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Log.e("log_tag", "surfaceDestroyed");
		if (mCamera != null)
		{
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	/** Check if this device has a camera */
	@SuppressWarnings("unused")
	private boolean checkCameraHardware(Context context)
	{
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
		{
			// this device has a camera
			return true;
		}
		else
		{
			// no camera on this device
			return false;
		}
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance()
	{
		Camera c = null;
		try
		{
			if (android.os.Build.VERSION.SDK_INT >= 9)
			{
				c = Camera.open(defaultCameraId); // attempt to get a Camera instance
			}
			else
			{
				c = Camera.open();
			}
		} catch (Exception e)
		{
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	@SuppressLint("NewApi")
	public void switchCamera()
	{
		Log.e("log_tag", "Switch Camera");

		if (android.os.Build.VERSION.SDK_INT >= 9)
		{
			try
			{

				if (mCamera != null)
				{
					mCamera.stopPreview();
					// mPreview.setCamera(null);
					mCamera.release();
					mCamera = null;
				}

				// Acquire the next camera and request Preview to reconfigure
				// parameters.
				mCamera = Camera.open((defaultCameraId + 1) % numberOfCameras);
				defaultCameraId = (defaultCameraId + 1) % numberOfCameras;

				// Start the preview

				mCamera.setPreviewDisplay(mHolder);
				if (mCamera != null)
				{
					mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
					focusModes = mCamera.getParameters().getSupportedFocusModes();
					requestLayout();
				}

				setCameraParameters();

				mCamera.setDisplayOrientation(90);

				mCamera.startPreview();

				Log.e("log_tag", "Switch Camera Orgnl");
			} catch (IOException exception)
			{
				Log.e("log_tag", "IOException caused by setPreviewDisplay()", exception);
			} catch (Exception e)
			{
				// TODO: handle exception
				Log.i("log_tag", "Exception", e);
			}
		}
		else
		{
			Toast.makeText(mContext, "Not Supported!", Toast.LENGTH_SHORT).show();
		}

	}

	public void onPause()
	{
		// TODO Auto-generated method stub
		Log.e("log_tag", "onPause");
		if (mCamera != null)
		{
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	public void takePicture(ShutterCallback shutter, PictureCallback raw, PictureCallback jpeg)
	{
		mCamera.takePicture(shutter, raw, jpeg);
	}

	public Bitmap getBitmapFromBuffer(byte[] data, int width, int height)
	{

		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, bmpFactoryOptions);

		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

		if (heightRatio > 1 || widthRatio > 1)
		{
			if (heightRatio > widthRatio)
			{
				bmpFactoryOptions.inSampleSize = heightRatio;
			}
			else
			{
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}
		bmpFactoryOptions.inJustDecodeBounds = false;

		Bitmap source = BitmapFactory.decodeByteArray(data, 0, data.length, bmpFactoryOptions);

		Matrix matrix = null;
		if (defaultCameraId == 0)
		{
			matrix = new Matrix();
			matrix.setRotate(90, source.getWidth() / 2, source.getHeight() / 2);
		}
		else if (defaultCameraId == 1)
		{
			matrix = new Matrix();
			matrix.setRotate(-90, source.getWidth() / 2, source.getHeight() / 2);

		}

		if (matrix != null)
		{

			Bitmap targetBitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

			source.recycle();
			Log.i("source bitmap width", "" + targetBitmap.getWidth());

			Log.i("source bitmap height", "" + targetBitmap.getHeight());
			return targetBitmap;
		}
		else
		{
			return source;
		}
	}

	public boolean isCameraLoad()
	{
		// TODO Auto-generated method stub
		return mCamera == null ? false : true;
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event)
	// {
	// if(event.getAction() == MotionEvent.ACTION_DOWN){
	// float x = event.getX();
	// float y = event.getY();
	// float touchMajor = event.getTouchMajor();
	// float touchMinor = event.getTouchMinor();
	//
	// Rect touchRect = new Rect(
	// (int)(x - touchMajor/2),
	// (int)(y - touchMinor/2),
	// (int)(x + touchMajor/2),
	// (int)(y + touchMinor/2));
	//
	// ((AndroidCamera)getContext()).touchFocus(touchRect);
	// }
	//
	//
	// return true;
	// }
}