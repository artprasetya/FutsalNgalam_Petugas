package com.asus.futsalngalam_petugas.MenuProfil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asus.futsalngalam_petugas.MenuProfil.Model.Fasilitas;
import com.asus.futsalngalam_petugas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UbahFasilitasActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText ubahNamaFasilitas;
    private Button btnSimpan;
    private FirebaseAuth auth;
    private String idPetugas;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_fasilitas);

        setToolbar();

        ubahNamaFasilitas = (EditText) findViewById(R.id.ubahNamaFasilitas);
        btnSimpan = (Button) findViewById(R.id.simpanUbahan);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        getDataFasilitas();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFasilitas();
            }
        });
    }

    private void updateFasilitas() {
        String idFasilitas = getIntent().getStringExtra("idFasilitas");
        String namaFasilitas = ubahNamaFasilitas.getText().toString().trim();

        dbRef = FirebaseDatabase.getInstance().getReference();
        if (!TextUtils.isEmpty(namaFasilitas)) {
            dbRef.child("fasilitas").child(idPetugas).child(idFasilitas).child("fasilitas").setValue(namaFasilitas);
            dbRef.child("tempatFutsal").child(idPetugas).child("fasilitas").child(idFasilitas).child("fasilitas").setValue(namaFasilitas);
            Toast.makeText(this, "Data Berhasil Diperbarui.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UbahFasilitasActivity.this, FasilitasActivity.class));
        } else {
            Toast.makeText(this, "Lengkapi Data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataFasilitas() {
        String idFasilitas = getIntent().getStringExtra("idFasilitas");
        dbRef = FirebaseDatabase.getInstance().getReference("fasilitas");
        dbRef.child(idPetugas).child(idFasilitas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Fasilitas dataFasilitas = dataSnapshot.getValue(Fasilitas.class);
                if (dataFasilitas != null) {
                    ubahNamaFasilitas.setText(dataFasilitas.getFasilitas());
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
        getSupportActionBar().setTitle("Ubah Fasilitas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
