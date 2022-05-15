package com.devpro.airj18bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.devpro.airj18bookingapp.BuildConfig;
import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.adapters.HistoryBookingAdapter;
import com.devpro.airj18bookingapp.listeners.BookingResponseDetailListener;
import com.devpro.airj18bookingapp.listeners.HistoryBookingListener;
import com.devpro.airj18bookingapp.models.BookedRoom;
import com.devpro.airj18bookingapp.models.BookingResponseDetail;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.utils.Constants;
import com.devpro.airj18bookingapp.utils.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class BookedActivity extends AppCompatActivity {
    RecyclerView rv_history_booking;
    RequestManager manager;

    private PreferenceManager preferenceManager;

    String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);
        preferenceManager = new PreferenceManager(this);
        cookie = preferenceManager.getString(Constants.KEY_COOKIE);
        manager = new RequestManager(this);

        manager.getBookingResponseDetail(bookingResponseDetailListener, cookie);

        // Inflate the layout for this fragment
        setViews();
        rv_history_booking.setHasFixedSize(true);
        rv_history_booking.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setViews() {
        rv_history_booking = findViewById(R.id.rv_history_booking);
    }

    private final BookingResponseDetailListener bookingResponseDetailListener = new BookingResponseDetailListener() {
        @Override
        public void didFetch(BookingResponseDetail response, String message) {
            HistoryBookingAdapter adapter = new HistoryBookingAdapter(response.data.bookedRooms, historyBookingListener);
            rv_history_booking.setAdapter(adapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(BookedActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    HistoryBookingListener historyBookingListener = new HistoryBookingListener() {
        @Override
        public void onBookingDetailClicked(String id) {
            startActivity(new Intent(getApplicationContext(), DetailsBookingActivity.class)
                    .putExtra("id", id));
        }

        @Override
        public void onBookingGetInvoiceClicked(BookedRoom bookedRoom) {
            try {
                createInvoice1(bookedRoom);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    private void createInvoice1(BookedRoom bookedRoom) throws IOException {
        int pageWidth = 1200;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Bitmap bmpScale = Bitmap.createScaledBitmap(bmp, 1200, 518, false);

        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage1.getCanvas();

        canvas.drawBitmap(bmpScale, 0, 0, paint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setColor(Color.BLUE);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(90);
        canvas.drawText("INVOICE BOOKING", pageWidth / 2, 620, titlePaint);

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        titlePaint.setTextSize(50);
        canvas.drawText("THÔNG TIN KHÁCH HÀNG", 50, 700, titlePaint);
        canvas.drawText("Tên: ", 50, 750, titlePaint);
        canvas.drawText(preferenceManager.getString(Constants.KEY_NAME), 200, 750, titlePaint);
        canvas.drawText("Email: ", 50, 800, titlePaint);
        canvas.drawText(preferenceManager.getString(Constants.KEY_EMAIL), 200, 800, titlePaint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setTextSize(35);

        canvas.drawLine(50, 900, pageWidth - 20, 900, paint);

        canvas.drawText("THÔNG TIN PHÒNG ĐÃ ĐẶT", 50, 1000, titlePaint);

        canvas.drawText("Tên phòng: ", 50, 1100, titlePaint);
        canvas.drawText(bookedRoom.roomName, 350, 1100, titlePaint);

        canvas.drawText("Ngày đặt: ", 50, 1200, titlePaint);
        canvas.drawText(bookedRoom.checkoutDate, 350, 1200, titlePaint);

        canvas.drawText("Ngày trả: ", 50, 1300, titlePaint);
        canvas.drawText(bookedRoom.checkoutDate, 350, 1300, titlePaint);

        canvas.drawText("Số ngày đặt: ", 50, 1400, titlePaint);
        canvas.drawText(bookedRoom.numberOfDays + "", 400, 1400, titlePaint);

        canvas.drawText("Giá 1 ngày: ", 50, 1500, titlePaint);
        canvas.drawText(bookedRoom.pricePerDay + "", 350, 1500, titlePaint);

        int offsetY = 1500;

        canvas.drawLine(680, offsetY + 80, pageWidth - 20, offsetY + 80, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Tổng tiền:", 500, offsetY + 150, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(bookedRoom.numberOfDays * bookedRoom.pricePerDay +"", pageWidth - 20, offsetY + 150, paint);

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
        Toast.makeText(getApplicationContext(), "File Saved on " + path, Toast.LENGTH_LONG).show();
        openPdfViewer(myFile);

    }


    private void openPdfViewer(File file) { //need to add provider in manifest and filepaths.xml
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(),
                BuildConfig.APPLICATION_ID + ".provider", file), "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 101);

    }
}