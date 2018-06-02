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
import android.widget.Toast;

import com.asus.futsalngalam_petugas.Adapter.FasilitasAdapter;
import com.asus.futsalngalam_petugas.Model.Fasilitas;
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

public class FasilitasActivity extends AppCompatActivity {

    private Button btnTambah;
    private EditText etFasilitas;
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private String idPetugas;
    private DatabaseReference dbRef;
    private ProgressDialog mProgress;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;

    // Creating List of ImageUploadInfo class.
    List<Fasilitas> fasilitasList = new ArrayList<>();

    Context context;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasilitas);

        context = this;

        setToolbar();

        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.fasilitas_list);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(FasilitasActivity.this));

        fab = findViewById(R.id.fab);

        mProgress = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        getDataFasilitas();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(FasilitasActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.tambah_fasilitas_dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Tambah Fasilitas");

        etFasilitas = (EditText) dialogView.findViewById(R.id.etNamaFasilitas);

        dialog.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tambahFasilitas();
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


    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fasilitas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //berhasil
    private void getDataFasilitas() {
        dbRef = FirebaseDatabase.getInstance().getReference("fasilitas");
        // Adding Add Value Event Listener to databaseReference.
        dbRef.child(idPetugas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                fasilitasList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Fasilitas fasilitas = postSnapshot.getValue(Fasilitas.class);

                    fasilitasList.add(fasilitas);
                }

                adapter = new FasilitasAdapter(context, fasilitasList);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void tambahFasilitas() {
        mProgress.setMessage("Menambahkan Data");
        String fasilitas = etFasilitas.getText().toString().trim();
        dbRef = FirebaseDatabase.getInstance().getReference();

        if (!TextUtils.isEmpty(fasilitas)) {
            mProgress.show();
            String idFasilitas = dbRef.push().getKey();
            dbRef.child("fasilitas").child(idPetugas).child(idFasilitas).child("idFasilitas").setValue(idFasilitas);
            dbRef.child("fasilitas").child(idPetugas).child(idFasilitas).child("idPetugas").setValue(idPetugas);
            dbRef.child("fasilitas").child(idPetugas).child(idFasilitas).child("fasilitas").setValue(fasilitas);
            mProgress.dismiss();
            Toast.makeText(this, "Data berhasil ditambahkan.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Tidak boleh kosong.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
