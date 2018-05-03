package com.asus.futsalngalam_petugas.MenuProfil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.asus.futsalngalam_petugas.R;

public class UbahRekeningActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvNamaBank;
    private EditText etNamaRekening,
            etNomorRekening;
    private Button btnSimpan,
            btnHapus;
    private TextView tvNamaRek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_rekening);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Rekening");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvNamaBank = (TextView) findViewById(R.id.tvNamaBank);
        etNamaRekening = (EditText) findViewById(R.id.etNamaRekening);
        etNomorRekening = (EditText) findViewById(R.id.etNomorRekening);
//        btnSimpan = (Button) findViewById(R.id.simpan);
//        btnHapus = (Button) findViewById(R.id.hapus);

/**
 * Kita cek apakah ada Bundle atau tidak
 */
        if (getIntent().getExtras() != null) {
            /**
             * Jika Bundle ada, ambil data dari Bundle
             */
            Bundle bundle = getIntent().getExtras();
            tvNamaBank.setText(bundle.getString("namaBank"));
        } else {
            /**
             * Apabila Bundle tidak ada, ambil dari Intent
             */
            tvNamaBank.setText(getIntent().getStringExtra("namaBank"));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
