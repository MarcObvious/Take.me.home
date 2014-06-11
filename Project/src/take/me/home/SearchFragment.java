package take.me.home;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SearchFragment extends Fragment implements OnClickListener {

	private ViewGroup mSearchView;
	private final static String LOG_TAG = "SEARCH FRAGMENT";
	private AutoCompleteTextView actv;
	private Location dest_loc;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mSearchView = (ViewGroup)inflater.inflate(R.layout.fragment_search, container, false);
		Button b_go_home = (Button) mSearchView.findViewById(R.id.Button_home);
		b_go_home .setOnClickListener(this); 

		Button b_search = (Button) mSearchView.findViewById(R.id.Button_search);
		b_search.setOnClickListener(this); 

		String[] countries = getResources().
				getStringArray(R.array.list_of_countries);
		ArrayAdapter adapter = new ArrayAdapter
				(getActivity(),android.R.layout.simple_list_item_1,countries);

		actv = (AutoCompleteTextView) mSearchView.findViewById(R.id.autocompletetext);

		actv.setAdapter(adapter);

		return mSearchView;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.Button_home:
			saveLocation();
			drawLocation(dest_loc);
			Toast.makeText(getActivity().getApplicationContext(), "GO HOME",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.Button_search:
			saveLocation();
			drawLocation(dest_loc);
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
			
			Toast.makeText(getActivity().getApplicationContext(), "SEARCH",
					Toast.LENGTH_LONG).show();
			break;
		default:
			Log.i(LOG_TAG, "Unknown: " + view.getId());
			break;
		}
	}

	public void saveLocation() {

		dest_loc = new Location("");
		dest_loc.setLatitude(51.3855);
		dest_loc.setLongitude(10.1786);

		getActivity().getPreferences(Context.MODE_PRIVATE)
		.edit()
		.putFloat("lat_home", (float) dest_loc.getLatitude())
		.putFloat("lon_home", (float) dest_loc.getLongitude())
		.commit();
	}

	private void drawLocation(Location loc) {

		if (loc == null) return;

		TextView lat = (TextView) mSearchView.findViewById(R.id.dest_latitude);

		lat.setText( Double.toString(loc.getLatitude()) );	

		TextView lon = (TextView) mSearchView.findViewById(R.id.dest_longitude);

		lon.setText( Double.toString(loc.getLongitude()) );



	}
}