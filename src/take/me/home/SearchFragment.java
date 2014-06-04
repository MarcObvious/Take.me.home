package take.me.home;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SearchFragment extends Fragment implements OnClickListener {

	private ViewGroup mSearchView;
	private final static String LOG_TAG = "SEARCH FRAGMENT";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mSearchView = (ViewGroup)inflater.inflate(R.layout.fragment_search, container, false);
		Button b_go_home = (Button) mSearchView.findViewById(R.id.Button_set_home);
		b_go_home .setOnClickListener(this); 
		Button b_search = (Button) mSearchView.findViewById(R.id.Button_search);
		b_search.setOnClickListener(this); 

		return mSearchView;

	}


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.Button_home:
			Toast.makeText(getActivity().getApplicationContext(), "GO HOME",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.Button_search:
			Toast.makeText(getActivity().getApplicationContext(), "SEARCH",
					Toast.LENGTH_LONG).show();
			break;
		default:
			Log.i(LOG_TAG, "Unknown: " + view.getId());
			break;
		}
	}



	//	public void getLocationClicked(View aView) {
	//		// Acquire a reference to the system Location Manager
	//		final LocationManager locationManager = (LocationManager) this
	//				.getSystemService(Context.LOCATION_SERVICE);
	//
	//		// Define a listener that responds to location updates
	//		LocationListener locationListener = new LocationListener() {
	//			public void onLocationChanged(Location location) {
	//				// Called when a new location is found by the network location
	//				// provider.
	//
	//				Toast.makeText(getApplicationContext(),
	//						"New Location obtained.", Toast.LENGTH_LONG).show();
	//				makeUseOfNewLocation(location);
	//				locationManager.removeUpdates(this);
	//
	//			}
	//
	//			public void onStatusChanged(String provider, int status,
	//					Bundle extras) {
	//			}
	//
	//			public void onProviderEnabled(String provider) {
	//			}
	//
	//			public void onProviderDisabled(String provider) {
	//			}
	//		};
	//
	//		// Register the listener with the Location Manager to receive location
	//		// updates
	//		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	//			Log.d(LOG_TAG, "locationManager.isProviderEnabled = true/gps");
	//			locationManager.requestLocationUpdates(
	//					LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	//			Location location = locationManager
	//					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	//			if (location != null) {
	//				makeUseOfNewLocation(location);
	//			} else {
	//				Toast.makeText(getApplicationContext(),
	//						"GPS has yet to calculate location.", Toast.LENGTH_LONG)
	//						.show();
	//			}
	//
	//		} else {
	//			Toast.makeText(getApplicationContext(), "GPS is not enabled.",
	//					Toast.LENGTH_LONG).show();
	//		}
	//	}
}