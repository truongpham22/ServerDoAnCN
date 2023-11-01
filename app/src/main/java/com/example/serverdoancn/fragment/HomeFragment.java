package com.example.serverdoancn.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.serverdoancn.R;
import com.example.serverdoancn.adapter.FoodAdapter;
import com.example.serverdoancn.model.Category;
import com.example.serverdoancn.model.CurrentUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    RecyclerView rcvFood;
    View mView;
    List<Category> categoryList;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FloatingActionButton fBut;
    CardView cardView;
    EditText edtNameF, edtPriceF, edtDescriptionF, edtDiscountF;
    Button btnImage, btnSave;
    Category category;
    FoodAdapter fadapter;
    Uri saveUri;
    FirebaseRecyclerAdapter<Category, FoodAdapter.FoodViewHolder> adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Category");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        rcvFood = mView.findViewById(R.id.rcv_Store);
        fBut = mView.findViewById(R.id.fBut);
        fBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        rcvFood.setHasFixedSize(true);
        rcvFood.setLayoutManager(gridLayoutManager);
        categoryList = new ArrayList<>();
        fadapter = new FoodAdapter(categoryList);
        rcvFood.setAdapter(fadapter);
        loadMenu();
        return mView;
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


    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Thêm món mới");
        alertDialog.setMessage("Điền đầy đủ thông tin");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_food_activity,null);
        cardView = view.findViewById(R.id.cardView);
        edtNameF = view.findViewById(R.id.edtName);
        edtPriceF = view.findViewById(R.id.edtPrice);
        edtDescriptionF = view.findViewById(R.id.edtDiscription);
        edtDiscountF = view.findViewById(R.id.edtDiscount);
        btnImage = view.findViewById(R.id.btnImage);
        btnSave = view.findViewById(R.id.btnUpload);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImg();
            }
        });
        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.icon_add);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (category != null){
                    reference.push().setValue(category);
                    Toast.makeText(getActivity(),"Đã thêm " + category.getName(),Toast.LENGTH_SHORT).show();
                }
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

    private void UploadImg() {
        if (saveUri != null){
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imgFolder = storageReference.child("images/"+imageName);
            imgFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Uploaded !!!", Toast.LENGTH_SHORT).show();
                            imgFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    category = new Category();
                                    category.setName(edtNameF.getText().toString());
                                    category.setPrice(edtPriceF.getText().toString());
                                    category.setDescription(edtDescriptionF.getText().toString());
                                    category.setDiscount(edtDiscountF.getText().toString());
                                    category.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+progress+"%");
                        }
                    });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CurrentUser.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                saveUri = data.getData();
                btnImage.setText("Đã Chọn");
                Log.d("Debug", "Image selected: " + saveUri);
            }
        }
    }

    private void setImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "CHỌN ẢNH"), CurrentUser.PICK_IMAGE_REQUEST);
    }

    private void loadMenu() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Category item = postSnapshot.getValue(Category.class);
                    categoryList.add(item);
                }
                fadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi khi tải dữ liệu
            }
        });
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(reference, Category.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Category, FoodAdapter.FoodViewHolder>(options){

            @NonNull
            @Override
            public FoodAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent,false);
                return new FoodAdapter.FoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position, @NonNull Category model) {
                holder.txtNfood.setText(model.getName());
                holder.txtPrice.setText(model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imgFood);
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
        reference.child(key).removeValue();
    }

    private void DialogUpdate(String key, Category item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Chỉnh sửa món");
        alertDialog.setMessage("Điền đầy đủ thông tin");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_food_activity,null);

        cardView = view.findViewById(R.id.cardView);
        edtNameF = view.findViewById(R.id.edtName);
        edtPriceF = view.findViewById(R.id.edtPrice);
        edtDescriptionF = view.findViewById(R.id.edtDiscription);
        edtDiscountF = view.findViewById(R.id.edtDiscount);
        btnImage = view.findViewById(R.id.btnImage);
        btnSave = view.findViewById(R.id.btnUpload);

        edtNameF.setText(item.getName());
        edtDiscountF.setText(item.getDiscount());
        edtDescriptionF.setText(item.getDescription());
        edtPriceF.setText(item.getPrice());

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeImg(item);
            }
        });
        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.icon_add);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setName(edtNameF.getText().toString());
                item.setPrice(edtPriceF.getText().toString());
                item.setDiscount(edtDiscountF.getText().toString());
                item.setDescription(edtDescriptionF.getText().toString());
                reference.child(key).setValue(item);
                Toast.makeText(getActivity(),"Đã chỉnh sửa " + item.getName(),Toast.LENGTH_SHORT).show();
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


//    private void showDialogUpdate(Category items) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle("Thêm món mới");
//        alertDialog.setMessage("Điền đầy đủ thông tin");
//
//        LayoutInflater inflater = this.getLayoutInflater();
//        View view = inflater.inflate(R.layout.add_food_activity,null);
//        cardView = view.findViewById(R.id.cardView);
//        edtNameF = view.findViewById(R.id.edtName);
//        edtPriceF = view.findViewById(R.id.edtPrice);
//        edtDescriptionF = view.findViewById(R.id.edtDiscription);
//        edtDiscountF = view.findViewById(R.id.edtDiscount);
//        btnImage = view.findViewById(R.id.btnImage);
//        btnSave = view.findViewById(R.id.btnUpload);
//
//        edtNameF.setText(items.getName());
//        edtDiscountF.setText(items.getDiscount());
//        edtDescriptionF.setText(items.getDescription());
//        edtPriceF.setText(items.getPrice());
//
//        btnImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setImage();
//            }
//        });
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChangeImg(items);
//            }
//        });
//        alertDialog.setView(view);
//        alertDialog.setIcon(R.drawable.icon_add);
//
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                if (items != null){
//                    categoryList.clear();
//                    items.setName(edtNameF.getText().toString());
//                    items.setPrice(edtPriceF.getText().toString());
//                    items.setDiscount(edtDiscountF.getText().toString());
//                    items.setDescription(edtDescriptionF.getText().toString());
//                    reference.child(reference.getKey()).setValue(items);
//                    Toast.makeText(getActivity(),"Đã chỉnh sửa " + items.getName(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        alertDialog.show();
//    }

    private void ChangeImg(Category items) {
        if (saveUri != null){
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imgFolder = storageReference.child("images/"+imageName);
            imgFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Uploaded !!!", Toast.LENGTH_SHORT).show();
                            imgFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    items.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+progress+"%");
                        }
                    });

        }
    }

}