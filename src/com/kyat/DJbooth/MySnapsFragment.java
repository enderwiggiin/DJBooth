package com.kyat.DJbooth;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MySnapsFragment extends Fragment {
	
	public MySnapsFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_my_snaps, container, false);
        Button aButton;
        aButton = (Button)findViewById(R.id.aButton);
        //int ass = PhotoFunctions.test(2, 3);
        //commenting out your gay toast faget AHEAHEHAHEAHEAHHEAHA							#swag
        //Toast.makeText(getActivity(),Integer.toString(ass), Toast.LENGTH_LONG).show();	
         
        return rootView;
    }

	private Button findViewById(int abutton) {
		// TODO Auto-generated method stub
		return null;
	}
}