package take.me.home;

import java.util.Random;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements OnClickListener {

	private ViewGroup mSettingsView;
	private final static String LOG_TAG = "SEARCH FRAGMENT";
	private Location loc;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mSettingsView = (ViewGroup)inflater.inflate(R.layout.fragment_settings, container, false);
		
		Button b_home = (Button) mSettingsView.findViewById(R.id.Button_set_home);
		b_home.setOnClickListener(this);
		
		Button b_fake = (Button) mSettingsView.findViewById(R.id.Button_fake_home);
		b_fake.setOnClickListener(this);
		
		drawLocation(loc);

		return mSettingsView;

	}
	
	public void saveLocation() {
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = sharedPref.edit();
		
		editor.putFloat("lat_home", (float) loc.getLatitude());
		
		editor.putFloat("lon_home", (float) loc.getLongitude());
		
		editor.commit();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.Button_set_home:
			getLocationClicked();
			Toast.makeText(getActivity().getApplicationContext(), "THIS IS HOME",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.Button_fake_home:
			getFakeLocation();
			drawLocation(loc);
			Toast.makeText(getActivity().getApplicationContext(), "THIS IS HOME",
					Toast.LENGTH_LONG).show();
			break;
		default:
			Log.i(LOG_TAG, "Unknown: " + view.getId());
			break;
		}
	}
	public void getFakeLocation() {
		Random r = new Random();
		Double _lat = r.nextDouble() *100;
		
		Double _lon = r.nextDouble() *100;
		
		  Location loc = new Location("");
		  loc.setLatitude(_lat);
		  loc.setLongitude(_lon);
		  
		  drawLocation(loc);
		
		}

	public void getLocationClicked() {
		// Acquire a reference to the system Location Manager
		final LocationManager locationManager = (LocationManager) this.getActivity()
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

		} else {
			Toast.makeText(getActivity().getApplicationContext(), "GPS is not enabled.",
					Toast.LENGTH_LONG).show();
		}
	}
	
	private void drawLocation(Location loc) {
		
		TextView lat = (TextView) mSettingsView.findViewById(R.id.latitude);
	
		lat.setText( Double.toString(loc.getLatitude()) );	

		TextView lon = (TextView) mSettingsView.findViewById(R.id.longitude);
	
		lon.setText( Double.toString(loc.getLongitude()) );
		

		
	}

	private void makeUseOfNewLocation(Location location) {
		loc = location;
		
		drawLocation(loc);
		
	}
}