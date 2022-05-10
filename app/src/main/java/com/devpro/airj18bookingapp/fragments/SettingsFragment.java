package com.devpro.airj18bookingapp.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.activities.ChartActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class SettingsFragment extends Fragment {

    LinearLayout lvl_chart,lvl_pdf;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_settings, container, false);

        getViews(view);
        setEvent();

        return view;
    }

    private void setEvent() {
        lvl_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChartActivity.class));
            }
        });
        lvl_pdf.setOnClickListener(view -> {

        });
    }

    private void getViews(View view) {
        lvl_pdf=view.findViewById(R.id.lvl_pdf);
        lvl_chart=view.findViewById(R.id.lvl_chart);
    }

}