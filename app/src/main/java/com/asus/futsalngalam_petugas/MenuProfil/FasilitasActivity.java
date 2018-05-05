package com.asus.futsalngalam_petugas.MenuProfil;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asus.futsalngalam_petugas.MenuProfil.Adapter.FasilitasAdapter;
import com.asus.futsalngalam_petugas.MenuProfil.Model.Fasilitas;
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

        etFasilitas = (EditText) findViewById(R.id.etFasilitas);
        btnTambah = (Button) findViewById(R.id.tambah);

        mProgress = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        getDataFasilitas();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahFasilitas();
            }
        });
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
            dbRef.child("tempatFutsal").child(idPetugas).child("fasilitas").child(idFasilitas).child("idFasilitas").setValue(idFasilitas);
            dbRef.child("tempatFutsal").child(idPetugas).child("fasilitas").child(idFasilitas).child("fasilitas").setValue(fasilitas);
            mProgress.dismiss();
            Toast.makeText(this, "Data berhasil ditambahkan.", Toast.LENGTH_LONG).show();
            etFasilitas.setText("");
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
