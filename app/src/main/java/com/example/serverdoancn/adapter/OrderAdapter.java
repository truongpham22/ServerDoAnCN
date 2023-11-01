package com.example.serverdoancn.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.serverdoancn.R;
import com.example.serverdoancn.model.CurrentUser;
import com.example.serverdoancn.model.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    List<Request> requests;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Request");

    public OrderAdapter(List<Request> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_orders, parent, false);
        OrderViewHolder viewHolder = new OrderViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Request request = requests.get(position);
        String requestKey = request.getKey();
        holder.txtKey.setText(requestKey);
        holder.txtStatus.setText(convertStatus(request.getStatus()));
        holder.txtPhone.setText(request.getPhone());
        holder.txtAddress.setText(request.getAddress());

    }

    @Override
    public int getItemCount() {
        if (requests != null) {
            return requests.size();
        } else {
            return 0;
        }
    }
    private String convertStatus(String status){
        if (status.equals("0"))
            return "Đã nhận";
        else if (status.equals("1"))
            return "Đang giao";
        else
            return "Đã giao";

    }


    public static class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public TextView txtKey, txtAddress, txtStatus, txtPhone;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtKey = itemView.findViewById(R.id.txtKeyO);
            txtAddress = itemView.findViewById(R.id.txtAddressO);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtPhone = itemView.findViewById(R.id.txtPhoneO);
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
