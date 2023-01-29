package com.reemzet.adminedushorts.UserFargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.reemzet.adminedushorts.R;



public class MenuFrag extends Fragment {

    CardView cardaku, cardsbte, cardbseb,cardcbse;
    NavController navController;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        cardaku = view.findViewById(R.id.cardaku);
        cardsbte = view.findViewById(R.id.cardsbte);
        cardbseb = view.findViewById(R.id.cardbseb);
        cardcbse=view.findViewById(R.id.cardcbse);
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");

        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;

        navController = navHostFragment.getNavController();


        cardsbte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_menuFrag_to_home3);

            }
        });
        cardaku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cardbseb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cardcbse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

}