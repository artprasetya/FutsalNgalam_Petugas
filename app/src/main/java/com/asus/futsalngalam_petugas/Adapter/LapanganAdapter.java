package com.asus.futsalngalam_petugas.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asus.futsalngalam_petugas.Model.Lapangan;
import com.asus.futsalngalam_petugas.MenuProfil.RekeningActivity;
import com.asus.futsalngalam_petugas.MenuProfil.UbahLapanganActivity;
import com.asus.futsalngalam_petugas.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LapanganAdapter extends RecyclerView.Adapter<LapanganAdapter.ViewHolder> implements View.OnClickListener {

    Context context;
    LayoutInflater mInflater;
    private List<Lapangan> lapanganList;
    private DatabaseReference dbRef;

    public LapanganAdapter(Context context, List<Lapangan> lapanganList) {
        this.context = context;
        this.lapanganList = lapanganList;

        mInflater = LayoutInflater.from(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNamaLapangan;
        public TextView tvHarga;

        public ViewHolder(View itemView) {
            super(itemView);

            tvNamaLapangan = (TextView) itemView.findViewById(R.id.tvNamaLapangan);
            tvHarga = (TextView) itemView.findViewById(R.id.tvHarga);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lapangan_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        dbRef = FirebaseDatabase.getInstance().getReference();

        final String idPetugas = lapanganList.get(position).getIdPetugas();
        final String idLapangan = lapanganList.get(position).getIdLapangan();
        final String namaLapangan = lapanganList.get(position).getNamaLapangan();
        final double hargaSewa = lapanganList.get(position).getHargaSewa();

        holder.tvNamaLapangan.setText(namaLapangan);
        holder.tvHarga.setText("Rp. " + String.valueOf(hargaSewa));

        holder.tvNamaLapangan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.opsi_dialog);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button editButton = (Button) dialog.findViewById(R.id.ubah);
                Button delButton = (Button) dialog.findViewById(R.id.hapus);

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, UbahLapanganActivity.class);
                        intent.putExtra("idLapangan", lapanganList.get(position).getIdLapangan());
                        context.startActivity(intent);
                    }
                });

                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dbRef.child("lapangan").child(idPetugas).child(idLapangan).removeValue();
                        dbRef.child("tempatFutsal").child(idPetugas).child("lapangan").child(idLapangan).removeValue();
                        Toast.makeText(context, "Data Berhasil Dihapus.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, RekeningActivity.class);
                        context.startActivity(intent);
                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return lapanganList.size();
    }

    @Override
    public void onClick(View v) {

    }
}
