package com.devpro.airj18bookingapp.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devpro.airj18bookingapp.BuildConfig;
import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.activities.AboutActivity;
import com.devpro.airj18bookingapp.activities.BookedActivity;
import com.devpro.airj18bookingapp.activities.ChartActivity;
import com.devpro.airj18bookingapp.activities.LoginActivity;
import com.devpro.airj18bookingapp.activities.MainActivity;
import com.devpro.airj18bookingapp.adapters.HistoryBookingAdapter;
import com.devpro.airj18bookingapp.listeners.BookingResponseDetailListener;
import com.devpro.airj18bookingapp.models.BookingResponseDetail;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.utils.Constants;
import com.devpro.airj18bookingapp.utils.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;

public class Settings2Fragment extends Fragment {
    LinearLayout lvl_chart,lvl_language,lvl_booked,lvl_pdf,lvl_contact,lvl_about;
    String LOCALE_VIETNAM = "vi";
    String LOCALE_ENGLISH = "en";
    Locale mLocale;
    TextView tvLanguage;
    TextView settings,bookingHistory,about,contact,language,chart,pdf,logout;

    RequestManager manager;
    private PreferenceManager preferenceManager;

    String cookie;
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
        manager = new RequestManager(getContext());
        preferenceManager = new PreferenceManager(getContext());
        cookie = preferenceManager.getString(Constants.KEY_COOKIE);
        manager.getBookingResponseDetail(bookingResponseDetailListener, cookie);

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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new Settings2Fragment()).commit();


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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new Settings2Fragment()).commit();
            }//else if

        });
        lvl_booked.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), BookedActivity.class));
        });
        lvl_pdf.setOnClickListener(view -> {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PackageManager.PERMISSION_GRANTED);
                try {
                    createInvoice1();
                    Toast.makeText(getContext(), "duoc", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    Toast.makeText(getContext(), "khong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

        });
        lvl_contact.setOnClickListener(view -> {
            Intent open = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sang.luong.1504/"));
            startActivity(open);
        });
        lvl_about.setOnClickListener(view -> {
            Intent open = new Intent(getActivity(), AboutActivity.class);
            startActivity(open);
        });
        logout.setOnClickListener(view -> signOut());
    }
    private void signOut() {
        preferenceManager.clear();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
    int i=1;
    private void setText(Locale locale) {

        if(i%1!=0){//settings,bookingHistory,about,contact,language,chart,pdf,logout
            tvLanguage.setText(locale.toString());
            settings.setText("Cài đặt");
            bookingHistory.setText("Lịch sử đặt phòng");
            about.setText("Về chúng tôi");
            contact.setText("Liên hệ");
            language.setText("Ngôn ngữ");
            chart.setText("Biểu đồ");
            pdf.setText("In PDF");
            logout.setText("Đăng xuất");
        }else {
            tvLanguage.setText(locale.toString());
            settings.setText("Settings");
            bookingHistory.setText("Booking history");
            about.setText("About");
            contact.setText("Contact");
            language.setText("Language");
            chart.setText("Chart");
            pdf.setText("Print PDF");
            logout.setText("Logout");
        }
        i++;
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
        lvl_pdf=view.findViewById(R.id.lvl_pdf);
        lvl_contact=view.findViewById(R.id.lvl_contact);
        lvl_about=view.findViewById(R.id.lvl_about);


        settings=view.findViewById(R.id.settings);
        bookingHistory=view.findViewById(R.id.bookingHistory);
        about=view.findViewById(R.id.about);
        contact=view.findViewById(R.id.contact);
        language=view.findViewById(R.id.language);
        chart=view.findViewById(R.id.chart);
        pdf=view.findViewById(R.id.pdf);
        logout=view.findViewById(R.id.logout);

    }
    private final BookingResponseDetailListener bookingResponseDetailListener = new BookingResponseDetailListener() {
        @Override
        public void didFetch(BookingResponseDetail response, String message) {

        }

        @Override
        public void didError(String message) {
            //Toast.makeText(BookedActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };
    //private void createInvoice1(List<TableData> list, PhieuNhap phieuNhap, Kho kho) throws IOException {
    private void createInvoice1() throws IOException {
        int pageWidth = 1200;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Bitmap bmpScale = Bitmap.createScaledBitmap(bmp, 1200, 518, false);

        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage1.getCanvas();

        canvas.drawBitmap(bmpScale,0,0,paint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(90);
        canvas.drawText("Invoice booking", pageWidth / 2, 600, titlePaint);

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setColor(Color.GRAY);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        titlePaint.setTextSize(35);
        canvas.drawText("Mã kho: " , 50, 350, titlePaint);
        canvas.drawText("Tên kho: ", 700, 350, titlePaint);
        canvas.drawText("Số phiếu nhập: " , 50, 420, titlePaint);
        canvas.drawText("Ngày lập phiếu: " , 700, 420, titlePaint);

//

        //main
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setTextSize(35);
        canvas.drawRect(20, 760, pageWidth - 20, 860, paint);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("STT", 40, 830, paint);
        canvas.drawText("Mã VT", 130, 830, paint);
        canvas.drawText("Tên vật tư", 290, 830, paint);
        canvas.drawText("Xuất xứ", 610, 830, paint);
        canvas.drawText("ĐVT", 880, 830, paint);
        canvas.drawText("Số lượng", 1030, 830, paint);

        canvas.drawLine(110, 790, 110, 840, paint);
        canvas.drawLine(260, 790, 260, 840, paint);
        canvas.drawLine(580, 790, 580, 840, paint);
        canvas.drawLine(840, 790, 840, 840, paint);
        canvas.drawLine(1020, 790, 1020, 840, paint);

        int offsetY = 950;
        int total = 0;


//        for (int i = 0; i < list.size(); i++) {
//            TableData t = list.get(i);
//            int tt = i + 1;
//            canvas.drawText(tt + "", 40, offsetY, paint);
//            canvas.drawText("", 130, offsetY, paint);//t.maVT
//            canvas.drawText("", 290, offsetY, paint);//t.tenVT
//            canvas.drawText("g", 610, offsetY, paint);//t.xuatXu
//            canvas.drawText("", 880, offsetY, paint);//t.DVT
//            canvas.drawText(  "fgd", 1030, offsetY, paint);//t.soLuong
//            offsetY = offsetY + 60;
//            total += 1;//t.soLuong
//        }

        canvas.drawLine(680, offsetY + 80, pageWidth - 20, offsetY + 80, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Tổng số lượng:", 700, offsetY + 150, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(total + "", pageWidth - 20, offsetY + 150, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Số loại vật tư:", 700, offsetY + 220, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(10 + "", pageWidth - 20, offsetY + 220, paint);

        myPdfDocument.finishPage(myPage1);

        String folder = Environment.getExternalStorageDirectory().getPath() + "/documents";
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        String path = folder + "/Calcuradora_" + System.currentTimeMillis() + ".pdf";
        File myFile = new File(path);
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myPdfDocument.writeTo(fOut);
        myPdfDocument.close();
        myOutWriter.close();
        fOut.close();
        Toast.makeText(getContext(), "File Saved on " + path, Toast.LENGTH_LONG).show();
        openPdfViewer(myFile);

    }




    private void openPdfViewer(File file) { //need to add provider in manifest and filepaths.xml
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider.getUriForFile(getContext(),
                BuildConfig.APPLICATION_ID + ".provider", file), "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 101);

    }

}