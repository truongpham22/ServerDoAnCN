package com.example.serverdoancn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.serverdoancn.R;
import com.example.serverdoancn.model.Order;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>{
    List<Order> orderList;
    public OrderDetailAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }


    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderdetail,parent,false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txtNameP.setText(String.format("Tên: %s",order.getProductName()));
        holder.txtPriceP.setText(String.format("Giá: %s",order.getPrice()));
        holder.txtQuantity.setText(String.format("Số Lượng: %s",order.getQuantity()));
        holder.txtDiscountP.setText(String.format("Giảm giá: %s",order.getDiscount()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameP, txtPriceP, txtDiscountP, txtQuantity;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameP = itemView.findViewById(R.id.txtNameP);
            txtPriceP = itemView.findViewById(R.id.txtPriceP);
            txtQuantity = itemView.findViewById(R.id.txtQuantityP);
            txtDiscountP = itemView.findViewById(R.id.txtDiscountP);
        }
    }
}
