package com.asus.futsalngalam_petugas.MenuPesanan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

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
    private String emailPemesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        setToolbar();

        invoice = findViewById(R.id.tvInvoice);
        statusPesanan = findViewById(R.id.tvStatus);
        namaPemesan = findViewById(R.id.tvNamaPemesan);
        nomorTelepon = findViewById(R.id.tvNomorPemesan);
        namaTempatFutsal = findViewById(R.id.tvNamaTempatFutsal);
        namaLapangan = findViewById(R.id.tvNamaLapangan);
        tanggalPesan = findViewById(R.id.tvTanggalPesan);
        durasiSewa = findViewById(R.id.tvDurasi);
        totalPembayaran = findViewById(R.id.tvTotal);
        tvNamaBank = findViewById(R.id.tvNamaBank);
        tvNomorRekening = findViewById(R.id.tvNomorRekening);
        tvNamaRekening = findViewById(R.id.tvNamaRekening);
        tvPembayaran = findViewById(R.id.tvJenisPembayaran);
        tvNominal = findViewById(R.id.tvNominal);
        btnLihatBukti = findViewById(R.id.btnLihatBukti);
        btnKonfirmasi = findViewById(R.id.btnKonfirmasi);

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
                showDialog();
            }
        });

    }

    private void showDialog() {
        String status = statusPesanan.getText().toString();
        if (status.equals("Menunggu Konfirmasi")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Konfirmasi Pesanan")
                    .setCancelable(true)
                    .setMessage("Silahkan pilih 'YA' untuk mengkonfirmasi pesanan.")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            konfirmasiPesanan();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            alert.show();
        } else if (status.equals("Belum Bayar")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Pemberitahuan")
                    .setCancelable(true)
                    .setMessage("Pemesan Belum Melakukan Pembayaran, anda tidak bisa melakukan konfirmasi pesanan.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            alert.show();
        } else if (status.equals("Sudah dikonfirmasi")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Pemberitahuan")
                    .setCancelable(true)
                    .setMessage("Pesanan sudah di Konfirmasi")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            buatPemberitahuan();
                            dialogInterface.dismiss();
                        }
                    });
            alert.show();
        }
    }

    private String getEmail() {
        dbRef = FirebaseDatabase.getInstance().getReference();
        String idPesanan = getIntent().getStringExtra("idPesanan");
        dbRef.child("pesanan").child(idPesanan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pesanan dataEmail = dataSnapshot.getValue(Pesanan.class);
                String email = dataEmail.getEmailPemesan();
                emailPemesan = email;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return emailPemesan;
    }

    private void buatPemberitahuan() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    getEmail();

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic ZTM1ZDViNjMtMzAxMS00YTZiLWJmYjUtNjQ0NjZmN2IyYzUz");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"65dbf75c-f04c-4559-875d-d6a0da59ea30\","
                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + emailPemesan + "\"}],"
                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"Pesanan Anda Sudah Dikonfirmasi\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
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
        String idPesanan = getIntent().getStringExtra("idPesanan");
        Intent intent = new Intent(this, BuktiPembayaranActivity.class);
        intent.putExtra("idPesanan", idPesanan);
        startActivity(intent);
    }

    private void getDataPembayaran() {
        dbRef = FirebaseDatabase.getInstance().getReference();
        String idPesanan = getIntent().getStringExtra("idPesanan");

        dbRef.child("pesanan").child(idPesanan).child("pembayaran").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pembayaran dataPembayaran = dataSnapshot.getValue(Pembayaran.class);
                if (dataPembayaran != null) {
                    tvNamaBank.setText(dataPembayaran.getNamaBank());
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
                    totalPembayaran.setText("Rp." + String.valueOf(dataPesanan.getTotalPembayaran()));
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

    //    private void buatPemberitahuan() {
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        dbRef = FirebaseDatabase.getInstance().getReference();
//        String idPesanan = getIntent().getStringExtra("idPesanan");
//        String emailPemesan = dbRef.child("pesanan").child(idPesanan).child("emailPemesan").toString().trim();
//
//        try {
//            String jsonResponse;
//
//            URL url = new URL("https://onesignal.com/api/v1/notifications");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setUseCaches(false);
//            con.setDoOutput(true);
//            con.setDoInput(true);
//
//            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            con.setRequestProperty("Authorization", "Basic ZTM1ZDViNjMtMzAxMS00YTZiLWJmYjUtNjQ0NjZmN2IyYzUz");
//            con.setRequestMethod("POST");
//
//            String strJsonBody = "{"
//                    + "\"app_id\": \"65dbf75c-f04c-4559-875d-d6a0da59ea30\","
//                    + "\"included_segments\": [\"All\"],"
//                    + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + emailPemesan + "\"}],"
//                    //+   "\"data\": {\"foo\": \"bar\"},"
//                    + "\"contents\": {\"en\": \"Pesanan Anda Telah Dikonfirmasi\"}"
//                    + "}";
//
//
//            System.out.println("strJsonBody:\n" + strJsonBody);
//
//            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
//            con.setFixedLengthStreamingMode(sendBytes.length);
//
//            OutputStream outputStream = con.getOutputStream();
//            outputStream.write(sendBytes);
//
//            int httpResponse = con.getResponseCode();
//            System.out.println("httpResponse: " + httpResponse);
//
//            if (httpResponse >= HttpURLConnection.HTTP_OK
//                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
//                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
//                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
//                scanner.close();
//            } else {
//                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
//                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
//                scanner.close();
//            }
//            System.out.println("jsonResponse:\n" + jsonResponse);
//
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
}
