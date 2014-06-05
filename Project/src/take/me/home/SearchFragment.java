package take.me.home;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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
		Button b_go_home = (Button) mSearchView.findViewById(R.id.Button_home);
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
}