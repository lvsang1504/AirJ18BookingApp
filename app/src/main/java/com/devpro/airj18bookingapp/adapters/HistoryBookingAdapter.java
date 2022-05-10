package com.devpro.airj18bookingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.models.BookedRoom;

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
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_booking, parent, false);
        return new HistoryBookingAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvID.setText(list.get(position).bookingId+"");
//        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.get(position).getCreateAt());
//        holder.tv_date.setText(stringDate);
        holder.tvName.setText(list.get(position).bookingDate);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID,tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}