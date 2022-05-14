package com.devpro.airj18bookingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.models.BookedRoom;
import com.devpro.airj18bookingapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class HistoryBookingAdapter extends RecyclerView.Adapter<HistoryBookingAdapter.ViewHolder>{
    ArrayList<BookedRoom> list;

    public HistoryBookingAdapter(ArrayList<BookedRoom> list) {
        this.list = list;
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
        holder.txt_id.setText(list.get(position).bookingId+"");
//        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.get(position).getCreateAt());
//        holder.tv_date.setText(stringDate);
        holder.txt_name.setText(list.get(position).roomName);
        holder.txt_checkin.setText(list.get(position).checkinDate);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_booking;
        TextView txt_id,txt_name,txt_checkin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_booking = itemView.findViewById(R.id.image_booking);
            txt_id = itemView.findViewById(R.id.txt_id);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_checkin = itemView.findViewById(R.id.txt_checkin);

        }
    }
}