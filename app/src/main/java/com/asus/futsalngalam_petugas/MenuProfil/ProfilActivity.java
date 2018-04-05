package com.asus.futsalngalam_petugas.MenuProfil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asus.futsalngalam_petugas.MenuProfil.Model.TempatFutsal;
import com.asus.futsalngalam_petugas.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilActivity extends AppCompatActivity {

    private Button btnUbah, btn_foto;

    private Toolbar toolbar;

    private DatabaseReference dbRef;

    private ImageView imageView;
    private TextView tvKontak, tvDeskripsi, tvHarga;
    private FirebaseAuth auth;

    private String idPetugas, namaTempatFutsal, alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        dbRef = FirebaseDatabase.getInstance().getReference();

        imageView = (ImageView) findViewById(R.id.imageView);
        tvHarga = (TextView) findViewById(R.id.tvHarga);
        tvKontak = (TextView) findViewById(R.id.tvKontak);
        tvDeskripsi = (TextView) findViewById(R.id.tvDeskripsi);
        btn_foto = (Button) findViewById(R.id.btn_foto);
        btnUbah = (Button) findViewById(R.id.btn_ubah);

        getDataFutsal();

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilActivity.this, UbahProfilActivity.class));
            }
        });

        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilActivity.this, AlbumFotoActivity.class));
            }
        });

        dbRef.child("tempatFutsal").child(idPetugas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                namaTempatFutsal = (String) dataSnapshot.child("namaTempatFutsal").getValue();
                alamat = (String) dataSnapshot.child("alamat").getValue();
                toolbar.setTitle(namaTempatFutsal);
                toolbar.setSubtitle(alamat);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getDataFutsal() {
        dbRef.child("tempatFutsal").child(idPetugas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TempatFutsal tempatFutsal = dataSnapshot.getValue(TempatFutsal.class);
                if (tempatFutsal != null) {
                    Glide.with(getApplication()).load(tempatFutsal.getFotoProfil()).into(imageView);
                    tvKontak.setText(tempatFutsal.getNoTelepon());
                    tvDeskripsi.setText(tempatFutsal.getDeskripsi());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
