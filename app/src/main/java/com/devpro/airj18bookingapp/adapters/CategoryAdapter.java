package com.devpro.airj18bookingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.listeners.CategoryClicksListener;
import com.devpro.airj18bookingapp.models.Category;
import com.devpro.airj18bookingapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;
    List<Category> list;
    CategoryClicksListener listener;

    public CategoryAdapter(Context context, List<Category> list, CategoryClicksListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.txt_title.setText(list.get(position).name);
        holder.txt_title.setSelected(true);
        holder.layout_category.setBackgroundResource(R.drawable.custom_border);
        Log.d("RRR", Constants.BASE_URL + list.get(position).iconPath);
        Picasso.get().load(Constants.BASE_URL + list.get(position).iconPath).into(holder.imageView);

        holder.layout_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.layout_category.setBackgroundResource(R.drawable.custom_border_selected);
                listener.onCategoryClicked(list.get(holder.getAdapterPosition()).id + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder {
    LinearLayout layout_category;
    ImageView imageView;
    TextView txt_title;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageViewCategory);
        txt_title = itemView.findViewById(R.id.txt_title_category);
        layout_category = itemView.findViewById(R.id.layout_category);

    }
}
