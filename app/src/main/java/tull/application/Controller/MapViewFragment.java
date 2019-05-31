package tull.application.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import tull.application.R;


public class MapViewFragment extends Fragment {


    public MapViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

        final RelativeLayout mainheader = (RelativeLayout) view.findViewById(R.id.mainheader);
        mainheader.setVisibility(View.VISIBLE);

        Button button_cross = (Button) view.findViewById(R.id.button_cross);


        return view;
    }

}
