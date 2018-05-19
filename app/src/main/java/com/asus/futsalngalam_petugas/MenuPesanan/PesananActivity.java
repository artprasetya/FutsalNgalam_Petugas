package com.asus.futsalngalam_petugas.MenuPesanan;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.asus.futsalngalam_petugas.Adapter.PesananAdapter;
import com.asus.futsalngalam_petugas.Model.Pesanan;
import com.asus.futsalngalam_petugas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PesananActivity extends AppCompatActivity {
    // Creating DatabaseReference.
    DatabaseReference dbRef;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<Pesanan> listPesanan = new ArrayList<>();

    private FirebaseAuth auth;
    private String idPetugas;
    private Toolbar toolbar;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(PesananActivity.this));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(PesananActivity.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Memuat...");

        // Showing progress dialog.
        progressDialog.show();

        getDataPesanan();
    }

    private void getDataPesanan() {
        //database path
        dbRef = FirebaseDatabase.getInstance().getReference("pesanan");

        Query query = dbRef.orderByChild("idPetugas").equalTo(idPetugas);

        // Adding Add Value Event Listener to databaseReference.
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listPesanan.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Pesanan dataPesanan = postSnapshot.getValue(Pesanan.class);
                    listPesanan.add(dataPesanan);
                }

                adapter = new PesananAdapter(context, listPesanan);

                recyclerView.setAdapter(adapter);

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pesanan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
