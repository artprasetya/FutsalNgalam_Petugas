package com.asus.futsalngalam_petugas.MenuProfil;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.asus.futsalngalam_petugas.MenuProfil.Model.TempatFutsal;
import com.asus.futsalngalam_petugas.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UbahProfilActivity extends AppCompatActivity implements View.OnClickListener {

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //object
    private EditText etKontak,
            etDeskripsi;

    private Button btn_simpan,
            btn_simpan_foto,
            btn_ubah_foto,
            gotoFasilitas,
            gotoLapangan,
            gotoRekening;

    private ImageView imageView;

    //uri store file
    private Uri filePath;

    //firebase object
    private DatabaseReference dbRef;
    private StorageReference storageRef;

    private ProgressDialog mProgress;

    private String idPetugas, namaTempatFutsal, alamat;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);

        dbRef = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();

        mProgress = new ProgressDialog(this);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        idPetugas = user.getUid();

        imageView = (ImageView) findViewById(R.id.imageView);
        etKontak = (EditText) findViewById(R.id.etKontak);
        etDeskripsi = (EditText) findViewById(R.id.etDeskripsi);
        btn_ubah_foto = (Button) findViewById(R.id.ubah_foto);
        btn_simpan_foto = (Button) findViewById(R.id.simpan_foto);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        gotoFasilitas = (Button) findViewById(R.id.fasilitas);
        gotoLapangan = (Button) findViewById(R.id.lapangan);
        gotoRekening = (Button) findViewById(R.id.rekening);

        setToolbar();
        getDataFutsal();

        btn_ubah_foto.setOnClickListener(this);
        btn_simpan_foto.setOnClickListener(this);
        btn_simpan.setOnClickListener(this);
        gotoFasilitas.setOnClickListener(this);
        gotoLapangan.setOnClickListener(this);
        gotoRekening.setOnClickListener(this);
}

    @Override
    public void onClick(View view) {
        if (view == btn_ubah_foto) {
            showFileChooser();
        } else if (view == btn_simpan_foto) {
            simpanFoto();
        } else if (view == btn_simpan) {
            simpanData();
        } else if (view == gotoFasilitas) {
            startActivity(new Intent(UbahProfilActivity.this, FasilitasActivity.class));
        } else if (view == gotoLapangan) {
            startActivity(new Intent(UbahProfilActivity.this, LapanganActivity.class));
        } else if (view == gotoRekening) {
            startActivity(new Intent(UbahProfilActivity.this, RekeningActivity.class));
        }
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbRef.child("tempatFutsal").child(idPetugas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                namaTempatFutsal = (String) dataSnapshot.child("namaTempatFutsal").getValue();
                alamat = (String) dataSnapshot.child("alamat").getValue();
                toolbar.setTitle(namaTempatFutsal);
                toolbar.setSubtitle(alamat);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void simpanFoto() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageRef.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            //simpan ke database
                            dbRef.child("tempatFutsal").child(idPetugas).child("fotoProfil").setValue(taskSnapshot.getDownloadUrl().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

    private void simpanData() {
        mProgress.setMessage("Menyimpan Data");

        final String kontak = etKontak.getText().toString().trim();
        final String deskripsi = etDeskripsi.getText().toString().trim();
        if (!TextUtils.isEmpty(kontak) && !TextUtils.isEmpty(deskripsi) && !TextUtils.isEmpty(alamat)) {
            mProgress.show();
            dbRef.child("tempatFutsal").child(idPetugas).child("deskripsi").setValue(deskripsi);
            dbRef.child("tempatFutsal").child(idPetugas).child("noTelepon").setValue(kontak);
            mProgress.dismiss();
            startActivity(new Intent(UbahProfilActivity.this, ProfilActivity.class));
        }
    }

    public void getDataFutsal() {
        dbRef.child("tempatFutsal").child(idPetugas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TempatFutsal tempatFutsal = dataSnapshot.getValue(TempatFutsal.class);
                if (tempatFutsal != null) {
                    Glide.with(getApplication()).load(tempatFutsal.getFotoProfil()).into(imageView);
                    etKontak.setText(tempatFutsal.getNoTelepon());
                    etDeskripsi.setText(tempatFutsal.getDeskripsi());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
