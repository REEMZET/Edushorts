package com.reemzet.mycollege.Sbte.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.reemzet.mycollege.R;


public class ChooseStream extends Fragment {


String semster;
TextView tvcivil,tvelectrical,tvcomputer,tvmechnical,tvelectronics;
NavController navController;
Bundle bundle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_choose_stream, container, false);
       Spinner sem1 = view.findViewById(R.id.spinner);
       tvcivil=view.findViewById(R.id.tvcivil);
       tvcomputer=view.findViewById(R.id.tvcomputer);
       tvelectrical=view.findViewById(R.id.tvelectrical);
       tvelectronics=view.findViewById(R.id.tvelectronics);
       tvmechnical=view.findViewById(R.id.tvmechnical);

          NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;

        navController = navHostFragment.getNavController();

         ArrayAdapter<CharSequence> adaptersem = ArrayAdapter.createFromResource(getContext(), R.array.semester, R.layout.custom_spinner);
        adaptersem.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        sem1.setAdapter(adaptersem);
        sem1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semster = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bundle=new Bundle();


        tvcivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("streamkey",semster+tvcivil.getText());
            navController.navigate(R.id.action_chooseStream_to_subjectList,bundle);
            }
        });
  tvcomputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
  tvmechnical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

  tvelectronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    tvelectrical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




       return view;
    }
}