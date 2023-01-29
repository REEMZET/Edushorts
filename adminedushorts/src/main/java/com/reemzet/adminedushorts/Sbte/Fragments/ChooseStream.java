package com.reemzet.adminedushorts.Sbte.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.reemzet.adminedushorts.R;


public class ChooseStream extends Fragment {


    String semster,menu;
    TextView tvcivil, tvelectrical, tvcomputer, tvmechnical, tvelectronics;
    NavController navController;
    Bundle bundle;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_stream, container, false);
        Spinner sem1 = view.findViewById(R.id.spinner);
        tvcivil = view.findViewById(R.id.tvcivil);
        tvcomputer = view.findViewById(R.id.tvcomputer);
        tvelectrical = view.findViewById(R.id.tvelectrical);
        tvelectronics = view.findViewById(R.id.tvelectronics);
        tvmechnical = view.findViewById(R.id.tvmechnical);
        menu=getArguments().getString("menu");

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



        bundle = new Bundle();
        tvcivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("streamkey", semster + tvcivil.getText());
                switch (menu){
                    case "syllabus": navController.navigate(R.id.action_chooseStream_to_subjectList, bundle);
                    break;
                    case "learning": navController.navigate(R.id.action_chooseStream_to_learning22, bundle);
                    break;
                    case "quesbank": navController.navigate(R.id.action_chooseStream_to_quesbankyear,bundle);
                    break;
                    case "library": navController.navigate(R.id.action_chooseStream_to_libraryList,bundle);
                    break;
                     case "lab": navController.navigate(R.id.action_chooseStream_to_labPracticalList,bundle);
                    break;

                }

            }
        });
        tvcomputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               bundle.putString("streamkey", semster + tvcomputer.getText());
                switch (menu){
                    case "syllabus": navController.navigate(R.id.action_chooseStream_to_subjectList, bundle);
                    break;
                    case "learning": navController.navigate(R.id.action_chooseStream_to_learning22, bundle);
                    break;
                    case "quesbank": navController.navigate(R.id.action_chooseStream_to_quesbankyear,bundle);
                    break;
                    case "library": navController.navigate(R.id.action_chooseStream_to_libraryList,bundle);
                    break;
                    case "lab": navController.navigate(R.id.action_chooseStream_to_labPracticalList,bundle);
                    break;
                }
            }
        });
        tvmechnical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               bundle.putString("streamkey", semster + tvmechnical.getText());
                switch (menu){
                    case "syllabus": navController.navigate(R.id.action_chooseStream_to_subjectList, bundle);
                    break;
                    case "learning": navController.navigate(R.id.action_chooseStream_to_learning22, bundle);
                     break;
                    case "quesbank": navController.navigate(R.id.action_chooseStream_to_quesbankyear,bundle);
                    break;
                    case "library": navController.navigate(R.id.action_chooseStream_to_libraryList,bundle);
                    break;
                    case "lab": navController.navigate(R.id.action_chooseStream_to_labPracticalList,bundle);
                    break;
                }
            }
        });

        tvelectronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("streamkey", semster + tvelectronics.getText());
                switch (menu){
                    case "syllabus": navController.navigate(R.id.action_chooseStream_to_subjectList, bundle);
                    break;
                    case "learning": navController.navigate(R.id.action_chooseStream_to_learning22, bundle);
                         break;
                    case "quesbank": navController.navigate(R.id.action_chooseStream_to_quesbankyear,bundle);
                    break;
                    case "library": navController.navigate(R.id.action_chooseStream_to_libraryList,bundle);
                    break;
                    case "lab": navController.navigate(R.id.action_chooseStream_to_labPracticalList,bundle);
                    break;
                }
            }
        });
        tvelectrical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               bundle.putString("streamkey", semster + tvelectrical.getText());
                switch (menu){
                    case "syllabus": navController.navigate(R.id.action_chooseStream_to_subjectList, bundle);
                    break;
                    case "learning": navController.navigate(R.id.action_chooseStream_to_learning22, bundle);
                     break;
                    case "quesbank": navController.navigate(R.id.action_chooseStream_to_quesbankyear,bundle);
                    break;
                    case "library": navController.navigate(R.id.action_chooseStream_to_libraryList,bundle);
                    break;
                    case "lab": navController.navigate(R.id.action_chooseStream_to_labPracticalList,bundle);
                    break;
                }
            }
        });
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Choose Stream");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });

        return view;
    }
}