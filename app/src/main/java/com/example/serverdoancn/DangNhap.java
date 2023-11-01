package com.example.serverdoancn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.serverdoancn.model.CurrentUser;
import com.example.serverdoancn.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangNhap extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;
    FirebaseDatabase database;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnLogin);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String phone = edtPhone.getText().toString();
                        String password = edtPassword.getText().toString();
                        if (snapshot.child(phone).exists())
                        {
                            User user = snapshot.child(phone).getValue(User.class);
                            if (Boolean.parseBoolean(user.getRole())){
                                CurrentUser.currentUser = user;
                                if (user.getPassword().equals(password) && user.getPhone().equals(phone)){
                                    Toast.makeText(DangNhap.this, "Đăng nhập thành công!!",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DangNhap.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(DangNhap.this, "Đăng nhập thất bại!!",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                                Toast.makeText(DangNhap.this, "Hãy đăng nhập bằng tài khoản admin",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(DangNhap.this, "Tài khoản không tồn tại",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}