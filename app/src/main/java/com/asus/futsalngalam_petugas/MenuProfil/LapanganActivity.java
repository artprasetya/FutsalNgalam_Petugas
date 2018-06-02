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

import com.asus.futsalngalam_petugas.Adapter.LapanganAdapter;
import com.asus.futsalngalam_petugas.Model.Lapangan;
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
    private EditText etLapangan,
            etHarga;
    private Button btnTambah;
    private Spinner spinnerKategori;
    private FirebaseAuth auth;
    private String idPetugas;
    private DatabaseReference dbRef;
    private ProgressDialog mProgress;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;

    // Creating List of ImageUploadInfo class.
    List<Lapangan> lapanganList = new ArrayList<>();

    Context context;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapangan);

        context = this;

        setToolbar();

        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.lapangan_list);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(LapanganActivity.this));

        fab = findViewById(R.id.fab);

        mProgress = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        getDataLapangan();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(LapanganActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.tambah_lapangan_dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Tambah Lapangan");

        etLapangan = (EditText) dialogView.findViewById(R.id.etLapangan);
        etHarga = (EditText) dialogView.findViewById(R.id.etHarga);
        spinnerKategori = (Spinner) dialogView.findViewById(R.id.spinner);

        dialog.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tambahLapangan();
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

    private void getDataLapangan() {
        dbRef = FirebaseDatabase.getInstance().getReference("lapangan");
        // Adding Add Value Event Listener to databaseReference.
        dbRef.child(idPetugas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                lapanganList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Lapangan lapangan = postSnapshot.getValue(Lapangan.class);

                    lapanganList.add(lapangan);
                }

                adapter = new LapanganAdapter(context, lapanganList);

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
        getSupportActionBar().setTitle("Lapangan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void tambahLapangan() {
        mProgress.setMessage("Menambahkan Data");
        String namaLapangan = etLapangan.getText().toString().trim();
        double hargaSewa = Double.valueOf(etHarga.getText().toString().trim());
        String kategori = spinnerKategori.getSelectedItem().toString();

        dbRef = FirebaseDatabase.getInstance().getReference();
        if (!TextUtils.isEmpty(namaLapangan) && (etHarga.getText().toString() != null)) {
            mProgress.show();
            String idLapangan = dbRef.push().getKey();
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("idLapangan").setValue(idLapangan);
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("idPetugas").setValue(idPetugas);
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("namaLapangan").setValue(namaLapangan);
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("hargaSewa").setValue(hargaSewa);
            dbRef.child("lapangan").child(idPetugas).child(idLapangan).child("kategori").setValue(kategori);
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
