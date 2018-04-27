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

import com.asus.futsalngalam_petugas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FasilitasActivity extends AppCompatActivity {

    private Button btnTambah;
    private ListView listFasilitas;
    private EditText etFasilitas;
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private String idPetugas;
    private DatabaseReference dbRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasilitas);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mProgress = new ProgressDialog(this);

        listFasilitas = (ListView) findViewById(R.id.fasilitas_list);
        etFasilitas = (EditText) findViewById(R.id.etFasilitas);
        btnTambah = (Button) findViewById(R.id.tambah);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        dbRef = FirebaseDatabase.getInstance().getReference("fasilitas");

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahFasilitas();
            }
        });
    }

    private void tambahFasilitas() {
        mProgress.setMessage("Menambahkan Data");
        String fasilitas = etFasilitas.getText().toString().trim();

        if (!TextUtils.isEmpty(fasilitas)) {
            mProgress.show();
            String idFasilitas = dbRef.push().getKey();
            dbRef.child(idPetugas).child(idFasilitas).child("idFasilitas").setValue(idFasilitas);
            dbRef.child(idPetugas).child(idFasilitas).child("idPetugas").setValue(idPetugas);
            dbRef.child(idPetugas).child(idFasilitas).child("fasilitas").setValue(fasilitas);
            mProgress.dismiss();
            Toast.makeText(this, "Data berhasil ditambahkan.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Tidak boleh kosong.", Toast.LENGTH_LONG).show();
        }
    }
}
