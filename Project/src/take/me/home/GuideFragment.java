package take.me.home;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class GuideFragment  extends Fragment implements SensorEventListener, LocationListener {
	private ViewGroup mSearchView;
	private final static String LOG_TAG = "GUIDE FRAGMENT";
	private float default_value = 0;


	private Location loc_dest, currentLocation;

	//Sensor & SensorManager
	private Sensor accelerometer;
	private Sensor magnetometer;
	private SensorManager mSensorManager;

	private LocationManager mLocationManager;

	// Storage for Sensor readings
	private float[] mGravity = null;

	private float[] mGeomagnetic = null;

	// Rotation around the Z axis
	private double mRotationInDegress;
	private double mAngle;

	// View showing the compass arrow
	private CompassArrowView mCompassArrow;

	//We do it here, Because it's better not allocate objects on the draw methods
	private Paint p = new Paint();
	private static ColorFilter red ;
	private static ColorFilter blue;
	private static ColorFilter green;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get a reference to the SensorManager
		mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		// Get a reference to the accelerometer
		accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// Get a reference to the magnetometer
		magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		startLocationUpdates();

		p = new Paint();

		red = new LightingColorFilter(Color.RED, 1);

		blue = new LightingColorFilter(Color.BLUE, 1);

		green = new LightingColorFilter(Color.GREEN, 1);


	}

	private double getRotationDegrees() {
		return mRotationInDegress + mAngle;
	}

	private void startLocationUpdates() {
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
		if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
	}

	private void stopLocationUpdates() {
		mLocationManager.removeUpdates(this);
	}

	private void locationUpdated(Location location) {
		currentLocation = location;

		TextView text = (TextView) mSearchView.findViewById(R.id.actlatitude);
		text.setText( currentLocation == null ? "" : Double.toString(currentLocation.getLatitude()) );
		text = (TextView) mSearchView.findViewById(R.id.actlongitude);
		text.setText( currentLocation == null ? "" : Double.toString(currentLocation.getLongitude()) );

		if (loc_dest != null && currentLocation != null) {
			mAngle = Math.toDegrees(loc_dest.getLatitude() == currentLocation.getLatitude() ? 0 : Math.atan((loc_dest.getLongitude() - currentLocation.getLongitude()) / (loc_dest.getLatitude() - currentLocation.getLatitude())));
			if (loc_dest.getLatitude() < currentLocation.getLatitude()) mAngle += Math.toDegrees(Math.PI);

			mCompassArrow.invalidate();

			text = (TextView) mSearchView.findViewById(R.id.x);
			text.setText( String.valueOf(mAngle) );
			text = (TextView) mSearchView.findViewById(R.id.z);
			text.setText( String.valueOf(currentLocation.bearingTo(loc_dest)) );
		}
	}

	private boolean getDestination () {
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

		Float lat = sharedPref.getFloat("lat_home", default_value);
		Float lon =  sharedPref.getFloat("lon_home", default_value);

		if (lat == default_value && lon == default_value) {
			lat = sharedPref.getFloat("lat_dest", default_value);
			lon = sharedPref.getFloat("lon_dest", default_value);
		}

		if (lat == default_value && lon == default_value) return false;

		loc_dest = new Location("");

		loc_dest.setLatitude((double) lat);
		loc_dest.setLongitude((double) lon);

		return true;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mSearchView = (ViewGroup)inflater.inflate(R.layout.fragment_guide, container, false);

		mCompassArrow = new CompassArrowView(getActivity().getApplicationContext());

		mSearchView.addView(mCompassArrow);

		if ( !getDestination() ) {
			Log.e(LOG_TAG, "NO HOME LOCATION & NO DESTINATION LOCATION : ");
		}
		else {
			Log.i(LOG_TAG, "LONGITUDE" + Double.toString(loc_dest.getLongitude()));
			Log.i(LOG_TAG, "LATITUDE" + Double.toString(loc_dest.getLatitude()));

			TextView text = (TextView) mSearchView.findViewById(R.id.destlatitude);
			text.setText( Double.toString(loc_dest.getLatitude()) );

			text = (TextView) mSearchView.findViewById(R.id.destlongitude);
			text.setText( Double.toString(loc_dest.getLongitude()) );
		}

		Location tempLocation = new Location("");
		tempLocation.setLatitude(41.3872545);
		tempLocation.setLongitude(2.1728199);
		locationUpdated(tempLocation);

		return mSearchView;
	}

	@Override
	public void onResume() {
		super.onResume();

		// Register for sensor updates
		mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

		startLocationUpdates();
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregister all sensors
		mSensorManager.unregisterListener(this);

		stopLocationUpdates();
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

			/*
			TextView text = (TextView) mSearchView.findViewById(R.id.x);
			text.setText( Float.toString(mGeomagnetic[0]) );
			text = (TextView) mSearchView.findViewById(R.id.y);
			text.setText( Float.toString(mGeomagnetic[1]) );
			text = (TextView) mSearchView.findViewById(R.id.z);
			text.setText( Float.toString(mGeomagnetic[2]) );
			 */
		}

		// If we have readings from both sensors then
		// use the readings to compute the device's orientation
		// and then update the display.

		if (mGravity != null && mGeomagnetic != null) {

			float rotationMatrix[] = new float[9];

			// Users the accelerometer and magnetometer readings
			// to compute the device's rotation with respect to
			// a real world coordinate system

			if (SensorManager.getRotationMatrix(rotationMatrix, null, mGravity, mGeomagnetic)) {

				float orientationMatrix[] = new float[3];

				// Returns the device's orientation given
				// the rotationMatrix

				SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, rotationMatrix);
				SensorManager.getOrientation(rotationMatrix, orientationMatrix);

				// Get the rotation, measured in radians, around the Z-axis
				// Note: This assumes the device is held flat and parallel
				// to the ground

				float rotationInRadians = orientationMatrix[0];

				// Convert from radians to degrees
				mRotationInDegress = mRotationInDegress * 0.8 + Math.toDegrees(rotationInRadians) * 0.2;

				// Request redraw
				mCompassArrow.invalidate();

				// Reset sensor event data arrays
				mGravity = mGeomagnetic = null;

			}
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onLocationChanged(Location location) {
		locationUpdated(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
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

			//setAlpha(0.3f);
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

		protected void checkDistance() {
			Double lat_cu = currentLocation.getLatitude();
			Double lon_cu = currentLocation.getLongitude();
			Double lat_dest = loc_dest.getLatitude();
			Double lon_dest = loc_dest.getLongitude();

			if (lat_dest - lat_cu  < 10 && lon_dest - lon_cu  < 10) 
				p.setColorFilter(red);
			else if (lat_dest - lat_cu  < 50 && lon_dest - lon_cu  < 50) 
				p.setColorFilter(blue);
			else 
				p.setColorFilter(green);
		}



		// Redraw the compass arrow
		@Override
		protected void onDraw(Canvas canvas) {

			// Save the canvas
			canvas.save();

			// Rotate this View
			canvas.rotate((float) -getRotationDegrees(), mParentCenterX,
					mParentCenterY);


			checkDistance();

			// Redraw this View
			canvas.drawBitmap(mBitmap, mViewLeftY, mViewTopX, p);

			// Restore the canvas
			canvas.restore();

		}
	}
}
