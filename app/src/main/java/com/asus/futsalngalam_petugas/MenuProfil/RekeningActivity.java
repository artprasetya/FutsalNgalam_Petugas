package com.asus.futsalngalam_petugas.MenuProfil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.asus.futsalngalam_petugas.MenuProfil.Adapter.RekeningAdapter;
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
    private Spinner spinnerRekening;
    private EditText etNamaRekening;
    private EditText etNomorRekening;
    private Button btnTambah;
    private FirebaseAuth auth;
    private String idPetugas;
    private DatabaseReference dbRef;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;

    // Creating List of ImageUploadInfo class.
    List<Rekening> rekeningList = new ArrayList<>();

    public static final String NAMA_BANK = "namaBank";
    public static final String REKENING_ID = "idRekening";
    public static final String REKENING_NAMA = "namaRekening";
    public static final String REKENING_NO = "nomorRekening";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekening);

        setToolbar();

        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.rekening_list);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(RekeningActivity.this));

        mProgress = new ProgressDialog(this);

        spinnerRekening = (Spinner) findViewById(R.id.spinner);
        etNamaRekening = (EditText) findViewById(R.id.etNamaRekening);
        etNomorRekening = (EditText) findViewById(R.id.etNomoRekening);
        btnTambah = (Button) findViewById(R.id.tambah);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        getDataRekening();

        dbRef = FirebaseDatabase.getInstance().getReference();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahRekening();
            }
        });
    }

    private void getDataRekening() {
        dbRef = FirebaseDatabase.getInstance().getReference("rekening");
        // Adding Add Value Event Listener to databaseReference.
        dbRef.child(idPetugas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                rekeningList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Rekening rekening = postSnapshot.getValue(Rekening.class);

                    rekeningList.add(rekening);
                }

                adapter = new RekeningAdapter(getApplicationContext(), rekeningList);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rekening");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void tambahRekening() {
        mProgress.setMessage("Menambahkan Data");
        String namaBank = spinnerRekening.getSelectedItem().toString();
        String namaRekening = etNamaRekening.getText().toString().trim();
        String nomorRekening = etNomorRekening.getText().toString().trim();

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
            etNomorRekening.setText("");
            etNamaRekening.setText("");
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