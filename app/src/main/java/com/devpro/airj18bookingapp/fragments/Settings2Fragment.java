package com.devpro.airj18bookingapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.TextView;
import android.widget.Toast;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.activities.BookedActivity;
import com.devpro.airj18bookingapp.activities.ChartActivity;
import com.devpro.airj18bookingapp.activities.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;

public class Settings2Fragment extends Fragment {
    LinearLayout lvl_chart,lvl_language,lvl_booked;
    String LOCALE_VIETNAM = "vi";
    String LOCALE_ENGLISH = "en";
    Locale mLocale;
    TextView tvLanguage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_settings2, container, false);

        getViews(view);

        mLocale=getResources().getConfiguration().locale;
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        this.getResources().updateConfiguration(config,
                this.getResources().getDisplayMetrics());

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

        lvl_language.setOnClickListener(view -> {
            if(LOCALE_VIETNAM.equals(mLocale.toString()))
            {
                mLocale = new Locale(LOCALE_ENGLISH);
                Locale.setDefault(mLocale);
                Configuration config1 = new Configuration();
                config1.locale = mLocale;
                getContext().getResources().updateConfiguration(config1,
                        getContext().getResources().getDisplayMetrics());
                tvLanguage.setText(mLocale.toString());
            }//if
            else if (LOCALE_ENGLISH.equals(mLocale.toString()))
            {
                mLocale = new Locale(LOCALE_VIETNAM);
                Locale.setDefault(mLocale);
                Configuration config1 = new Configuration();
                config1.locale = mLocale;
                getContext().getResources().updateConfiguration(config1,
                        getContext().getResources().getDisplayMetrics());
                tvLanguage.setText(mLocale.toString());
            }//else if

        });
        lvl_booked.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), BookedActivity.class));
        });
    }
    private void dialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage(R.string.changLanguage);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        getActivity().finish();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                });

        builder1.setNegativeButton(
                R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void getViews(View view) {
        lvl_language=view.findViewById(R.id.lvl_language);
        lvl_chart=view.findViewById(R.id.lvl_chart);
        lvl_booked=view.findViewById(R.id.lvl_booked);
        tvLanguage=view.findViewById(R.id.tvLanguage);
    }

}