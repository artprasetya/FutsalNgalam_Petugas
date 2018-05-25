package com.asus.futsalngalam_petugas.MenuPesanan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asus.futsalngalam_petugas.Model.Pembayaran;
import com.asus.futsalngalam_petugas.Model.Pesanan;
import com.asus.futsalngalam_petugas.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailPesananActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView invoice;
    private TextView statusPesanan;
    private TextView namaPemesan;
    private TextView nomorTelepon;
    private TextView namaTempatFutsal;
    private TextView namaLapangan;
    private TextView tanggalPesan;
    private TextView durasiSewa;
    private TextView totalPembayaran;
    private TextView tvNamaBank;
    private TextView tvNomorRekening;
    private TextView tvNamaRekening;
    private Button btnLihatBukti;
    private Button btnKonfirmasi;
    private DatabaseReference dbRef;
    private TextView tvPembayaran;
    private TextView tvNominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        setToolbar();

        invoice = findViewById(R.id.tvInvoice);
        statusPesanan = (TextView) findViewById(R.id.tvStatus);
        namaPemesan = (TextView) findViewById(R.id.tvNamaPemesan);
        nomorTelepon = (TextView) findViewById(R.id.tvNomorPemesan);
        namaTempatFutsal = (TextView) findViewById(R.id.tvNamaTempatFutsal);
        namaLapangan = (TextView) findViewById(R.id.tvNamaLapangan);
        tanggalPesan = (TextView) findViewById(R.id.tvTanggalPesan);
        durasiSewa = (TextView) findViewById(R.id.tvDurasi);
        totalPembayaran = (TextView) findViewById(R.id.tvTotal);
        tvNamaBank = (TextView) findViewById(R.id.tvNamaBank);
        tvNomorRekening = (TextView) findViewById(R.id.tvNomorRekening);
        tvNamaRekening = (TextView) findViewById(R.id.tvNamaRekening);
        tvPembayaran = (TextView) findViewById(R.id.tvJenisPembayaran);
        tvNominal = (TextView) findViewById(R.id.tvNominal);
        btnLihatBukti = (Button) findViewById(R.id.btnLihatBukti);
        btnKonfirmasi = (Button) findViewById(R.id.btnKonfirmasi);

        getDataPesanan();
        getDataPembayaran();

        btnLihatBukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lihatBuktiPembayaran();
            }
        });

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfirmasiPesanan();
            }
        });

    }


    private void konfirmasiPesanan() {
        dbRef = FirebaseDatabase.getInstance().getReference();
        String idPesanan = getIntent().getStringExtra("idPesanan");
        dbRef.child("pesanan").child(idPesanan).child("statusPesanan").setValue("Sudah Dikonfirmasi");
        Toast.makeText(this, "Pesanan Berhasil Dikonfirmasi", Toast.LENGTH_LONG).show();
    }

    private void lihatBuktiPembayaran() {
        startActivity(new Intent(DetailPesananActivity.this, BuktiPembayaranActivity.class));
    }

    private void getDataPembayaran() {
        dbRef = FirebaseDatabase.getInstance().getReference();
        String idPesanan = getIntent().getStringExtra("idPesanan");

        dbRef.child("pesanan").child(idPesanan).child("pembayaran").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pembayaran dataPembayaran = dataSnapshot.getValue(Pembayaran.class);
                if (dataPembayaran != null) {
                    tvNamaBank.setText(dataPembayaran.getBankTempatFutsal());
                    tvNamaRekening.setText(dataPembayaran.getNamaRekPemesan());
                    tvNomorRekening.setText(dataPembayaran.getNomorRekPemesan());
                    tvPembayaran.setText(dataPembayaran.getJenisPembayaran());
                    tvNominal.setText(dataPembayaran.getNominalTransfer());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDataPesanan() {
        dbRef = FirebaseDatabase.getInstance().getReference();
        String idPesanan = getIntent().getStringExtra("idPesanan");

        dbRef.child("pesanan").child(idPesanan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pesanan dataPesanan = dataSnapshot.getValue(Pesanan.class);
                if (dataPesanan != null) {
                    invoice.setText(dataPesanan.getInvoice());
                    statusPesanan.setText(dataPesanan.getStatusPesanan());
                    namaPemesan.setText(dataPesanan.getNamaPemesan());
                    nomorTelepon.setText(dataPesanan.getNoTelepon());
                    namaTempatFutsal.setText(dataPesanan.getNamaTempatFutsal());
                    namaLapangan.setText(dataPesanan.getNamaLapangan());
                    tanggalPesan.setText(dataPesanan.getTanggalPesan());
                    durasiSewa.setText("Jam " + (dataPesanan.getJamMulai() + " - " + (dataPesanan.getJamSelesai() + " WIB")));
                    totalPembayaran.setText(String.valueOf(dataPesanan.getTotalPembayaran()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Pesanan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
