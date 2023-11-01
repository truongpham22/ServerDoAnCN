package com.example.serverdoancn.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serverdoancn.R;
import com.example.serverdoancn.adapter.FoodAdapter;
import com.example.serverdoancn.adapter.OrderAdapter;
import com.example.serverdoancn.model.Category;
import com.example.serverdoancn.model.CurrentUser;
import com.example.serverdoancn.model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    RecyclerView rcvOrder;
    OrderAdapter orderAdapter;
    List<Request> requestList;
    Request request;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference requests = database.getReference("Request");
    FirebaseRecyclerAdapter<Request, OrderAdapter.OrderViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_food);

        rcvOrder= findViewById(R.id.rcvOrder);
        rcvOrder.setHasFixedSize(true);
        rcvOrder.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        requestList = new ArrayList<>();
        orderAdapter = new OrderAdapter(requestList);
        rcvOrder.setAdapter(orderAdapter);
        loadOrder(CurrentUser.currentUser.getPhone());
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    private void loadOrder(String phone) {
        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Request request = dataSnapshot.getValue(Request.class);
                    requestList.add(request);
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(requests, Request.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderAdapter.OrderViewHolder>(options){

            @NonNull
            @Override
            public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders,parent,false);
                return new OrderAdapter.OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position, @NonNull Request model) {
                String requestKey = request.getKey();
                holder.txtKey.setText(requestKey);
                holder.txtStatus.setText(CurrentUser.convertStatus(request.getStatus()));
                holder.txtPhone.setText(request.getPhone());
                holder.txtAddress.setText(request.getAddress());
            }
        };
        adapter.startListening();
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(CurrentUser.UPDATE)) {
            DialogUpdate(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals(CurrentUser.DELETE)) {
            DialogDelete(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void DialogDelete(String key) {
        requests.child(key).removeValue();
    }

    private void DialogUpdate(String key, Request item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderActivity.this);
        alertDialog.setTitle("Cập nhật trạng thái");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.order_status,null);
        Spinner spinner = view.findViewById(R.id.spinner);
        String[] sl = new String[]{"Đã nhận", "Đang giao", "Đã giao"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, sl);
        spinner.setAdapter(spinnerAdapter);
        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedItemPosition()));
                requests.child(localKey).setValue(item);
                orderAdapter.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
