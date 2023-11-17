package com.example.serverdoancn.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.serverdoancn.R;
import com.example.serverdoancn.adapter.OrderDetailAdapter;
import com.example.serverdoancn.model.CurrentUser;
import com.example.serverdoancn.model.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderDetail extends AppCompatActivity {
    TextView txtId, txtPhone, txtAddress, txtTotalPrice;
    RecyclerView rcvOrderDetail;
    Request request;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference requests = database.getReference("Request");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        txtId = findViewById(R.id.txtKeyO);
        txtAddress = findViewById(R.id.txtAddressO);
        txtPhone = findViewById(R.id.txtPhoneO);
        txtTotalPrice = findViewById(R.id.txtTotal);

        rcvOrderDetail = findViewById(R.id.lst_food);
        rcvOrderDetail.setHasFixedSize(true);
        rcvOrderDetail.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            request = (Request) bundle.getSerializable("obj_request");
            // Hiển thị thông tin của đơn hàng
            if (request != null) {
                txtId.setText(request.getKey());
                txtTotalPrice.setText(request.getTotal());
                txtPhone.setText(request.getPhone());
                txtAddress.setText(request.getAddress());

                // Hiển thị danh sách sản phẩm trong đơn hàng
                OrderDetailAdapter adapter = new OrderDetailAdapter(request.getFoods());
                rcvOrderDetail.setAdapter(adapter);
            }
        }
    }
}