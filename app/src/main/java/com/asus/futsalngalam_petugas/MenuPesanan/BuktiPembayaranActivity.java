package com.asus.futsalngalam_petugas.MenuPesanan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.asus.futsalngalam_petugas.Model.Pembayaran;
import com.asus.futsalngalam_petugas.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuktiPembayaranActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference dbRef;
    private ImageView gambarBukti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bukti_pembayaran);

        setToolbar();

        gambarBukti = findViewById(R.id.buktiPembayaran);

        getBuktiPembayaran();
    }

    private void getBuktiPembayaran() {
        dbRef = FirebaseDatabase.getInstance().getReference();
        String idPesanan = getIntent().getStringExtra("idPesanan");

        dbRef.child("pesanan").child(idPesanan).child("pembayaran").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pembayaran dataPembayaran = dataSnapshot.getValue(Pembayaran.class);
                if (dataPembayaran != null) {
                    Glide.with(getApplication()).load(dataPembayaran.getBuktiPembayaran()).into(gambarBukti);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bukti Pembayaran");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
