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
import android.widget.Toast;

import com.asus.futsalngalam_petugas.MenuProfil.List.FasilitasList;
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
    private ListView listFasilitas;
    private EditText etFasilitas;
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private String idPetugas;
    private DatabaseReference dbRef;
    private ProgressDialog mProgress;

    private List<Fasilitas> fasilitasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasilitas);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fasilitas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mProgress = new ProgressDialog(this);

        listFasilitas = (ListView) findViewById(R.id.fasilitas_list);
        etFasilitas = (EditText) findViewById(R.id.etFasilitas);
        btnTambah = (Button) findViewById(R.id.tambah);

        fasilitasList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        dbRef = FirebaseDatabase.getInstance().getReference();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahFasilitas();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbRef.child("fasilitas").child(idPetugas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fasilitasList.clear();
                for (DataSnapshot fasilitasSnapshot : dataSnapshot.getChildren()) {
                    Fasilitas fasilitas = fasilitasSnapshot.getValue(Fasilitas.class);

                    fasilitasList.add(fasilitas);
                }

                FasilitasList adapter = new FasilitasList(FasilitasActivity.this, fasilitasList);
                listFasilitas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void tambahFasilitas() {
        mProgress.setMessage("Menambahkan Data");
        String fasilitas = etFasilitas.getText().toString().trim();

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
