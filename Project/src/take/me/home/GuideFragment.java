package take.me.home;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GuideFragment  extends Fragment implements SensorEventListener {
	private ViewGroup mSearchView;
	private final static String LOG_TAG = "GUIDE FRAGMENT";

	//Sensor & SensorManager
	private Sensor accelerometer;
	private Sensor magnetometer;
	private SensorManager mSensorManager;

	// Storage for Sensor readings
	private float[] mGravity = null;

	private float[] mGeomagnetic = null;

	// Rotation around the Z axis
	private double mRotationInDegress;

	// View showing the compass arrow
	private CompassArrowView mCompassArrow;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity();
		// Get a reference to the SensorManager
		mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

		// Get a reference to the accelerometer
		accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// Get a reference to the magnetometer
		magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mSearchView = (ViewGroup)inflater.inflate(R.layout.fragment_guide, container, false);

		mCompassArrow = new CompassArrowView(getActivity().getApplicationContext());

		mSearchView.addView(mCompassArrow);

		return mSearchView;

	}

	@Override
	public void onResume() {
		super.onResume();


		// Register for sensor updates

		mSensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		
		mSensorManager.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_NORMAL);

	}
	@Override
	public void onPause() {
		super.onPause();

		// Unregister all sensors
		mSensorManager.unregisterListener(this);

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// Acquire accelerometer event data

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			mGravity = new float[3];
			System.arraycopy(event.values, 0, mGravity, 0, 3);

		} 

		// Acquire magnetometer event data

		else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

			mGeomagnetic = new float[3];
			System.arraycopy(event.values, 0, mGeomagnetic, 0, 3);

		}

		// If we have readings from both sensors then
		// use the readings to compute the device's orientation
		// and then update the display.

		if (mGravity != null && mGeomagnetic != null) {

			float rotationMatrix[] = new float[9];

			// Users the accelerometer and magnetometer readings
			// to compute the device's rotation with respect to
			// a real world coordinate system

			boolean success = SensorManager.getRotationMatrix(rotationMatrix,
					null, mGravity, mGeomagnetic);

			if (success) {

				float orientationMatrix[] = new float[3];

				// Returns the device's orientation given
				// the rotationMatrix

				SensorManager.getOrientation(rotationMatrix, orientationMatrix);

				// Get the rotation, measured in radians, around the Z-axis
				// Note: This assumes the device is held flat and parallel
				// to the ground

				float rotationInRadians = orientationMatrix[0];

				// Convert from radians to degrees
				mRotationInDegress = Math.toDegrees(rotationInRadians);

				// Request redraw
				mCompassArrow.invalidate();

				// Reset sensor event data arrays
				mGravity = mGeomagnetic = null;

			}
		}

	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public class CompassArrowView extends View {

		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.arrow);
		int mBitmapWidth = mBitmap.getWidth();

		// Height and Width of Main View
		int mParentWidth;
		int mParentHeight;

		// Center of Main View
		int mParentCenterX;
		int mParentCenterY;

		// Top left position of this View
		int mViewTopX;
		int mViewLeftY;

		public CompassArrowView(Context context) {
			super(context);
		};

		// Compute location of compass arrow
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			mParentWidth = mSearchView.getWidth();
			mParentHeight = mSearchView.getHeight();

			mParentCenterX = mParentWidth / 2;
			mParentCenterY = mParentHeight / 2;

			mViewLeftY = mParentCenterX - mBitmapWidth / 2;
			mViewTopX = mParentCenterY - mBitmapWidth / 2;
		}

		// Redraw the compass arrow
		@Override
		protected void onDraw(Canvas canvas) {

			// Save the canvas
			canvas.save();

			// Rotate this View
			canvas.rotate((float) -mRotationInDegress, mParentCenterX,
					mParentCenterY);

			// Redraw this View
			canvas.drawBitmap(mBitmap, mViewLeftY, mViewTopX, null);

			// Restore the canvas
			canvas.restore();

		}
	}
}
