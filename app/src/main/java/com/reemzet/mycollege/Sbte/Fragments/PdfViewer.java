package com.reemzet.mycollege.Sbte.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.barteksc.pdfviewer.PDFView;
import com.reemzet.mycollege.R;

import java.io.File;


public class PdfViewer extends Fragment {


        PDFView pdfView;
        File folder;
    String pdffile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pdf_viewer, container, false);
         pdfView=view.findViewById(R.id.pdfView);
            pdffile=getArguments().getString("file");
        folder = getContext().getFilesDir();
        pdfView.fromFile(new File(folder.getAbsoluteFile() ,pdffile))
                .enableSwipe(true)
                .spacing(2)
                .load();











        return view;
    }
}