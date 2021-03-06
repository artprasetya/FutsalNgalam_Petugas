package com.asus.futsalngalam_petugas.MenuProfil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.asus.futsalngalam_petugas.Adapter.RekeningAdapter;
import com.asus.futsalngalam_petugas.Model.Rekening;
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
    private FloatingActionButton fab;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;

    // Creating List of ImageUploadInfo class.
    List<Rekening> rekeningList = new ArrayList<>();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekening);

        context = this;

        setToolbar();

        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.rekening_list);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(RekeningActivity.this));

        fab = findViewById(R.id.fab);

        mProgress = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        getDataRekening();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(RekeningActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.tambah_rekening_dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Tambah Rekening");

        spinnerRekening = (Spinner) dialogView.findViewById(R.id.spinner);
        etNamaRekening = (EditText) dialogView.findViewById(R.id.etNamaRekening);
        etNomorRekening = (EditText) dialogView.findViewById(R.id.etNomoRekening);

        dialog.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tambahRekening();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
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

                adapter = new RekeningAdapter(context, rekeningList);

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

        dbRef = FirebaseDatabase.getInstance().getReference();
        if (!TextUtils.isEmpty(namaRekening) && (!TextUtils.isEmpty(nomorRekening))) {
            mProgress.show();
            String idRekening = dbRef.push().getKey();
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("idRekening").setValue(idRekening);
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("idPetugas").setValue(idPetugas);
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("namaBank").setValue(namaBank);
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("namaRekening").setValue(namaRekening);
            dbRef.child("rekening").child(idPetugas).child(idRekening).child("nomorRekening").setValue(nomorRekening);
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