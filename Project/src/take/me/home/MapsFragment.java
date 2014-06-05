package take.me.home;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsFragment extends MapFragment {

	@SuppressWarnings("unused")
	private final static String TAG = "MAP";
	
	private final static float DEFAULT_ZOOM = 9;
	
	private GoogleMap mMap;
	private ViewGroup mView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = (ViewGroup)super.onCreateView(inflater, container, savedInstanceState);
		mView.setLongClickable(true);
		mView.setClickable(true);
		mMap = getMap();
		
		mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
			
			@Override
			public void onMyLocationChange(Location location) {
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
				mMap.setOnMyLocationChangeListener(null);
			}
		});

		mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng location) {
			}
		});

		mMap.setMyLocationEnabled(true);
		
		return mView;
	}
	
	public void saveLocation(LatLng location) {
		getActivity()
			.getPreferences(Context.MODE_PRIVATE)
			.edit()
			.putFloat("lat_dest", (float) location.latitude)
			.putFloat("lon_dest", (float) location.longitude)
			.commit();
	}

}