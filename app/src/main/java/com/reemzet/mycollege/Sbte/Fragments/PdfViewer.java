package com.reemzet.mycollege.Sbte.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.reemzet.mycollege.R;

import java.io.File;


public class PdfViewer extends Fragment {


    PDFView pdfView;
    File folder;
    String pdffile;
    Toolbar toolbar;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pdf_viewer, container, false);
        pdfView = view.findViewById(R.id.pdfView);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        pdffile = getArguments().getString("file");
        folder = getContext().getFilesDir();
        pdfView.fromFile(new File(folder.getAbsoluteFile(), pdffile))
                .enableSwipe(true)
                .spacing(2)
                .load();

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Subject code-".concat(pdffile));
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