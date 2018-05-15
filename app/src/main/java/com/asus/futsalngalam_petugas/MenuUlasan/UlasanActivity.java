package com.asus.futsalngalam_petugas.MenuUlasan;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.asus.futsalngalam_petugas.Adapter.UlasanAdapter;
import com.asus.futsalngalam_petugas.Model.Ulasan;
import com.asus.futsalngalam_petugas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UlasanActivity extends AppCompatActivity {

    // Creating DatabaseReference.
    DatabaseReference dbRef;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<Ulasan> ulasanList = new ArrayList<>();

    private FirebaseAuth auth;
    private String idPetugas;
    private Toolbar toolbar;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ulasan);

        context = this;

        setToolbar();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(UlasanActivity.this));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(UlasanActivity.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Memuat...");

        // Showing progress dialog.
        progressDialog.show();

        getDataUlasan();
    }

    private void getDataUlasan() {
        //database path
        dbRef = FirebaseDatabase.getInstance().getReference("ulasan");

        dbRef.child(idPetugas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ulasanList.clear();
                for (DataSnapshot ulasanSnapshot : dataSnapshot.getChildren()) {
                    Ulasan dataUlasan = ulasanSnapshot.getValue(Ulasan.class);
                    ulasanList.add(dataUlasan);
                }
                adapter = new UlasanAdapter(context, ulasanList);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ulasan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
