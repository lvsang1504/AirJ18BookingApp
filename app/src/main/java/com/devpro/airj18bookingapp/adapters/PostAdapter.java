package com.devpro.airj18bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.models.Notification;
import com.devpro.airj18bookingapp.utils.TimeAgo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Notification> mData;

    public PostAdapter(List<Notification> Data) {
        mData = Data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        /////*     initialize view   */////
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_notification_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // set values for each item

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = mData.get(position).timestamp;
//        try {
//            date = sdf.parse(strDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        viewHolder.fallText.setText(mData.get(position).content);
        viewHolder.text_date.setText(mData.get(position).timestamp);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fallText, text_date;

        public ViewHolder(View v) {
            super(v);
            fallText = v.findViewById(R.id.id_postText_TextView);
            text_date = v.findViewById(R.id.text_date);
        }

    }
}
