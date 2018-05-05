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

import com.asus.futsalngalam_petugas.MenuProfil.Model.Rekening;
import com.asus.futsalngalam_petugas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UbahRekeningActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView namaBank;
    private EditText ubahNamaRekening;
    private EditText ubahNomorRekening;
    private Button btnSimpan;
    private FirebaseAuth auth;
    private String idPetugas;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_rekening);

        setToolbar();

        namaBank = (TextView) findViewById(R.id.namaBank);
        ubahNamaRekening = (EditText) findViewById(R.id.ubahNamaRekening);
        ubahNomorRekening = (EditText) findViewById(R.id.ubahNomorRekening);
        btnSimpan = (Button) findViewById(R.id.simpanUbahan);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        dbRef = FirebaseDatabase.getInstance().getReference();

        getDataRekening();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRekening();
            }
        });
    }

    private void updateRekening() {
        String idRekening = getIntent().getStringExtra("data");
        String namaRekening = ubahNamaRekening.getText().toString().trim();
        String nomorRekening = ubahNomorRekening.getText().toString().trim();

        if (!TextUtils.isEmpty(namaRekening) && (!TextUtils.isEmpty(nomorRekening))) {
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("namaRekening").setValue(namaRekening);
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("nomorRekening").setValue(nomorRekening);
            Toast.makeText(this, "Data berhasil diperbarui.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(UbahRekeningActivity.this, RekeningActivity.class));
        } else {
            Toast.makeText(this, "Lengkapi data.", Toast.LENGTH_LONG).show();
        }

    }

    private void getDataRekening() {
        String idRekening = getIntent().getStringExtra("data");

        dbRef.child("rekening").child(idPetugas).child(idRekening).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Rekening rekening = dataSnapshot.getValue(Rekening.class);
                if (rekening != null) {
                    namaBank.setText(rekening.getNamaBank());
                    ubahNamaRekening.setText(rekening.getNamaRekening());
                    ubahNomorRekening.setText(rekening.getNomorRekening());
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
        getSupportActionBar().setTitle("Ubah Rekening");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
