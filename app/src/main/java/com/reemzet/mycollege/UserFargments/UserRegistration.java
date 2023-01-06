package com.reemzet.mycollege.UserFargments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.reemzet.mycollege.R;


public class UserRegistration extends Fragment {
AutoCompleteTextView autocity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_user_registration, container, false);
      String []city=getResources().getStringArray(R.array.array_bihar_districts);
        autocity=view.findViewById(R.id.autocity);
ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,city);
       autocity.setThreshold(3);
        autocity.setAdapter(adapter);




    return  view;
    }
}