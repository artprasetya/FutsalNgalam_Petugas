package com.asus.futsalngalam_petugas.MenuProfil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asus.futsalngalam_petugas.Model.Lapangan;
import com.asus.futsalngalam_petugas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UbahLapanganActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView namaLapangan;
    private EditText ubahNamaLapangan;
    private EditText ubahHargaSewa;
    private Button btnSimpan;
    private FirebaseAuth auth;
    private String idPetugas;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_lapangan);

        setToolbar();

        namaLapangan = (TextView) findViewById(R.id.namaLapangan);
        ubahNamaLapangan = (EditText) findViewById(R.id.ubahNamaLapangan);
        ubahHargaSewa = (EditText) findViewById(R.id.ubahHargaSewa);
        btnSimpan = (Button) findViewById(R.id.simpanUbahan);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        getDataLapangan();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLapangan();
            }
        });
    }

    private void updateLapangan() {
        String idLapangan = getIntent().getStringExtra("idLapangan");
        String namaLapangan = ubahNamaLapangan.getText().toString().trim();
        double hargaSewa = Double.valueOf(ubahHargaSewa.getText().toString().trim());

        dbRef = FirebaseDatabase.getInstance().getReference();
        if (!TextUtils.isEmpty(namaLapangan) && (ubahHargaSewa.getText().toString() != null)) {
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("namaLapangan").setValue(namaLapangan);
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("hargaSewa").setValue(hargaSewa);
            Toast.makeText(this, "Data Berhasil Diperbarui.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UbahLapanganActivity.this, LapanganActivity.class));
        } else {
            Toast.makeText(this, "Lengkapi Data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataLapangan() {
        String idLapangan = getIntent().getStringExtra("idLapangan");
        dbRef = FirebaseDatabase.getInstance().getReference("lapangan");
        dbRef.child(idPetugas).child(idLapangan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Lapangan dataLapangan = dataSnapshot.getValue(Lapangan.class);
                if (dataLapangan != null) {
                    namaLapangan.setText(dataLapangan.getNamaLapangan());
                    ubahNamaLapangan.setText(dataLapangan.getNamaLapangan());
                    ubahHargaSewa.setText(String.valueOf(dataLapangan.getHargaSewa()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Lapangan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
