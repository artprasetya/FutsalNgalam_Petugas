package com.asus.futsalngalam_petugas.MenuProfil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.asus.futsalngalam_petugas.MenuProfil.List.LapanganList;
import com.asus.futsalngalam_petugas.MenuProfil.Model.Lapangan;
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

public class LapanganActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listLapangan;
    private EditText etLapangan,
            etHarga;
    private Button btnTambah;
    private Spinner spinnerKategori;
    private List<Lapangan> lapanganList;
    private FirebaseAuth auth;
    private String idPetugas;
    private DatabaseReference dbRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapangan);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lapangan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mProgress = new ProgressDialog(this);

        listLapangan = (ListView) findViewById(R.id.lapangan_list);
        etLapangan = (EditText) findViewById(R.id.etLapangan);
        etHarga = (EditText) findViewById(R.id.etHarga);
        spinnerKategori = (Spinner) findViewById(R.id.spinner);
        btnTambah = (Button) findViewById(R.id.tambah);

        lapanganList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        dbRef = FirebaseDatabase.getInstance().getReference();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahLapangan();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbRef.child("lapangan").child(idPetugas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lapanganList.clear();
                for (DataSnapshot lapanganSnapshot : dataSnapshot.getChildren()) {
                    Lapangan lapangan = lapanganSnapshot.getValue(Lapangan.class);

                    lapanganList.add(lapangan);
                }

                LapanganList adapter = new LapanganList(LapanganActivity.this, lapanganList);
                listLapangan.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void tambahLapangan() {
        mProgress.setMessage("Menambahkan Data");
        String namaLapangan = etLapangan.getText().toString().trim();
        double hargaSewa = Double.valueOf(etHarga.getText().toString().trim());
        String kategori = spinnerKategori.getSelectedItem().toString();

        if (!TextUtils.isEmpty(namaLapangan) && (etHarga.getText().toString() != null)) {
            mProgress.show();
            String idLapangan = dbRef.push().getKey();
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("idLapangan").setValue(idLapangan);
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("idPetugas").setValue(idPetugas);
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("namaLapangan").setValue(namaLapangan);
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("hargaSewa").setValue(hargaSewa + ".00");
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("kategori").setValue(kategori);
            dbRef.child("tempatFutsal").child(idPetugas).child("lapangan").child(idLapangan).child("idLapangan").setValue(idLapangan);
            dbRef.child("tempatFutsal").child(idPetugas).child("lapangan").child(idLapangan).child("namaLapangan").setValue(namaLapangan);
            mProgress.dismiss();
            Toast.makeText(this, "Lapangan berhasil ditambahkan.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Lengkapi data lapangan.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
