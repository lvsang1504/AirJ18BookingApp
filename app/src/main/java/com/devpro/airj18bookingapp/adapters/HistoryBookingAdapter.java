package com.devpro.airj18bookingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.listeners.HistoryBookingListener;
import com.devpro.airj18bookingapp.models.BookedRoom;
import com.devpro.airj18bookingapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class HistoryBookingAdapter extends RecyclerView.Adapter<HistoryBookingAdapter.ViewHolder> {
    ArrayList<BookedRoom> list;
    HistoryBookingListener listener;

    public HistoryBookingAdapter(ArrayList<BookedRoom> list, HistoryBookingListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_booking_item, parent, false);
        return new HistoryBookingAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(Constants.BASE_URL + list.get(position).roomThumbnail).placeholder(R.drawable.playholder).into(holder.image_booking);
        holder.txt_id.setText(list.get(position).bookingId + "");

        holder.txt_name.setText(list.get(position).roomName);
        holder.txt_checkin.setText(list.get(position).checkinDate);

        holder.cardViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onBookingDetailClicked(list.get(holder.getAdapterPosition()).roomId + "");
            }
        });

        holder.btn_get_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onBookingGetInvoiceClicked(list.get(holder.getAdapterPosition()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_booking;
        CardView cardViewHistory;
        TextView txt_id, txt_name, txt_checkin, btn_get_invoice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_booking = itemView.findViewById(R.id.image_booking);
            txt_id = itemView.findViewById(R.id.txt_id);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_checkin = itemView.findViewById(R.id.txt_checkin);
            btn_get_invoice = itemView.findViewById(R.id.btn_get_invoice);
            cardViewHistory = itemView.findViewById(R.id.cardViewHistory);

        }
    }
}