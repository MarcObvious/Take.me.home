package take.me.home;

import java.util.Random;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements OnClickListener, LocationUpdatesListener {

	private ViewGroup mSettingsView;
	private final static String LOG_TAG = "SEARCH FRAGMENT";
	private float default_value = 0;
	private Location homeLocation, currentLocation;

	private boolean saveOnNextUpdate = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mSettingsView = (ViewGroup)inflater.inflate(R.layout.fragment_settings, container, false);

		Button b_home = (Button) mSettingsView.findViewById(R.id.Button_set_home);
		b_home.setOnClickListener(this);

		Button b_fake = (Button) mSettingsView.findViewById(R.id.Button_fake_home);
		b_fake.setOnClickListener(this);

		if (getLocation())
			drawLocation(homeLocation);

		return mSettingsView;
	}

	private boolean getLocation () {
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

		Float lat = sharedPref.getFloat("lat_home", default_value);
		Float lon =  sharedPref.getFloat("lon_home", default_value);

		if (lat == default_value && lon == default_value) return false;

		homeLocation = new Location("");

		homeLocation.setLatitude((double) lat);
		homeLocation.setLongitude((double) lon);

		return true;

	}

	public void saveLocation() {
		if (currentLocation == null) {
			saveOnNextUpdate = true;
			return;
		}

		getActivity().getPreferences(Context.MODE_PRIVATE)
		.edit()
		.putFloat("lat_home", (float) currentLocation.getLatitude())
		.putFloat("lon_home", (float) currentLocation.getLongitude())
		.commit();
		homeLocation = currentLocation;
		saveOnNextUpdate = false;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.Button_set_home:
			saveLocation();
			Toast.makeText(getActivity().getApplicationContext(), "THIS IS HOME",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.Button_fake_home:
			getFakeLocation();
			Toast.makeText(getActivity().getApplicationContext(), "THIS IS FAKE HOME",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			Log.i(LOG_TAG, "Unknown: " + view.getId());
			break;
		}
		drawLocation(homeLocation);	
	}

<<<<<<< HEAD
	public void getFakeLocation() {

		Random r = new Random();

		Double _lat = r.nextDouble() *100;
		Double _lon = r.nextDouble() *100;


		loc = new Location("");
		loc.setLatitude(/*41.3869691*/_lat);
		loc.setLongitude(/*2.1706107*/_lon);

		drawLocation(loc);	
	}

	public void getLocationClicked() {
		// Acquire a reference to the system Location Manager
				
		final LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.

				Toast.makeText(getActivity().getApplicationContext(),
						"New Location obtained.", Toast.LENGTH_LONG).show();

				makeUseOfNewLocation(location);
				locationManager.removeUpdates(this);

			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Log.d(LOG_TAG, "locationManager.isProviderEnabled = true/gps");
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				makeUseOfNewLocation(location);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"GPS has yet to calculate location.", Toast.LENGTH_LONG)
						.show();
			}

		} else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Log.d(LOG_TAG, "locationManager.isProviderEnabled = true/network");
			locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
			//wait(100);
			//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				makeUseOfNewLocation(location);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"Network has yet to calculate location.", Toast.LENGTH_LONG)
						.show();
			}
		}
		else {
			Toast.makeText(getActivity().getApplicationContext(), "GPS is not enabled, and Network Position disponible",
					Toast.LENGTH_LONG).show();
		}
	}
=======
>>>>>>> googlelocation

	private void drawLocation(Location loc) {

		if (loc == null) return;

		TextView lat = (TextView) mSettingsView.findViewById(R.id.latitude);

		lat.setText( Double.toString(loc.getLatitude()) );	

		TextView lon = (TextView) mSettingsView.findViewById(R.id.longitude);

		lon.setText( Double.toString(loc.getLongitude()) );



	}

	public void getFakeLocation() {

		Random r = new Random();

		Double _lat = r.nextDouble() *100;
		Double _lon = r.nextDouble() *100;

		homeLocation = new Location("");
		homeLocation.setLatitude(_lat);
		homeLocation.setLongitude(_lon);

		getActivity().getPreferences(Context.MODE_PRIVATE)
		.edit()
		.putFloat("lat_home", (float) homeLocation.getLatitude())
		.putFloat("lon_home", (float) homeLocation.getLongitude())
		.commit();
	}

	@Override
	public void onLocationChanged(Location location) {
		currentLocation = location;
		if (saveOnNextUpdate) saveLocation();
	}
}