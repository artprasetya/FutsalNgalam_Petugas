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

import com.asus.futsalngalam_petugas.MenuProfil.List.RekeningList;
import com.asus.futsalngalam_petugas.MenuProfil.Model.Rekening;
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

public class RekeningActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressDialog mProgress;
    private ListView listRekening;
    private Spinner spinnerRekening;
    private EditText etNamaRekening;
    private EditText etNoRekening;
    private Button btnTambah;
    private List<Rekening> rekeningList;
    private FirebaseAuth auth;
    private String idPetugas;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekening);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rekening");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mProgress = new ProgressDialog(this);

        listRekening = (ListView) findViewById(R.id.rekening_list);
        spinnerRekening = (Spinner) findViewById(R.id.spinner);
        etNamaRekening = (EditText) findViewById(R.id.etNamaRekening);
        etNoRekening = (EditText) findViewById(R.id.etNoRekening);
        btnTambah = (Button) findViewById(R.id.tambah);

        rekeningList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        dbRef = FirebaseDatabase.getInstance().getReference();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahRekening();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbRef.child("rekening").child(idPetugas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rekeningList.clear();
                for (DataSnapshot rekeningSnapshot : dataSnapshot.getChildren()) {
                    Rekening rekening = rekeningSnapshot.getValue(Rekening.class);

                    rekeningList.add(rekening);
                }

                RekeningList adapter = new RekeningList(RekeningActivity.this, rekeningList);
                listRekening.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void tambahRekening() {
        mProgress.setMessage("Menambahkan Data");
        String namaRekening = etNamaRekening.getText().toString().trim();
        String nomorRekening = etNoRekening.getText().toString().trim();
        String namaBank = spinnerRekening.getSelectedItem().toString();

        if (!TextUtils.isEmpty(namaRekening) && (!TextUtils.isEmpty(nomorRekening))) {
            mProgress.show();
            String idRekening = dbRef.push().getKey();
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("idRekening").setValue(idRekening);
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("idPetugas").setValue(idPetugas);
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("namaBank").setValue(namaBank);
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("namaRekening").setValue(namaRekening);
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("nomorRekening").setValue(nomorRekening);
            dbRef.child("tempatFutsal").child(idPetugas).child("rekening").child(idRekening).child("idRekening").setValue(idRekening);
            dbRef.child("tempatFutsal").child(idPetugas).child("rekening").child(idRekening).child("namaBank").setValue(namaBank);
            mProgress.dismiss();
            Toast.makeText(this, "Data berhasil ditambahkan.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Lengkapi data.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
