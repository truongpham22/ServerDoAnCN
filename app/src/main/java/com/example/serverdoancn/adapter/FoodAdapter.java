package com.example.serverdoancn.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serverdoancn.R;
import com.example.serverdoancn.model.Category;
import com.example.serverdoancn.model.CurrentUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Category> categoryList;
    private Context context;

    public FoodAdapter(List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Category model = categoryList.get(position);
        holder.txtNfood.setText(model.getName());
        holder.txtPrice.setText(model.getPrice());
        Picasso.get().load(model.getImage()).into(holder.imgFood);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView txtNfood, txtPrice;
        public ImageView imgFood;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNfood = itemView.findViewById(R.id.txtNfood);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgFood = itemView.findViewById(R.id.img_Food);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("CHỌN CHỨC NĂNG");
            menu.add(0, 0, getAdapterPosition(), CurrentUser.UPDATE);
            menu.add(0, 1, getAdapterPosition(), CurrentUser.DELETE);
        }
    }
}
