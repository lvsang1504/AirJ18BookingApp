package com.devpro.airj18bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.listeners.BookingClicksListener;
import com.devpro.airj18bookingapp.models.Room;
import com.devpro.airj18bookingapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingViewHolder> {

    Context context;
    List<Room> list;
    BookingClicksListener listener;

    public BookingAdapter(Context context, List<Room> list, BookingClicksListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingViewHolder(LayoutInflater.from(context).inflate(R.layout.list_booking_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        holder.txt_title.setText(list.get(position).name);
        Picasso.get().load(Constants.BASE_URL + list.get(position).thumbnail).placeholder(R.drawable.playholder).into(holder.imageView);
//        Animation anim_from_button = AnimationUtils.loadAnimation(context, R.anim.anim_from_bottom);
//        holder.cardView.setAnimation(anim_from_button);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBookingClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class BookingViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    ImageView imageView;
    TextView txt_title, txt_rate_num, txt_num_vote;


    public BookingViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.cardView);
        imageView = itemView.findViewById(R.id.image_booking);
        txt_title = itemView.findViewById(R.id.txt_title);
        txt_rate_num = itemView.findViewById(R.id.txt_rate_num);
        txt_num_vote = itemView.findViewById(R.id.txt_num_vote);
    }
}
