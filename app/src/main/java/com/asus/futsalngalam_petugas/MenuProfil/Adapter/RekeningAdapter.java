package com.asus.futsalngalam_petugas.MenuProfil.Adapter;

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

import com.asus.futsalngalam_petugas.MenuProfil.Model.Rekening;
import com.asus.futsalngalam_petugas.MenuProfil.RekeningActivity;
import com.asus.futsalngalam_petugas.MenuProfil.UbahRekeningActivity;
import com.asus.futsalngalam_petugas.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RekeningAdapter extends RecyclerView.Adapter<RekeningAdapter.ViewHolder> implements View.OnClickListener {

    Context context;
    LayoutInflater mInflater;
    private List<Rekening> rekeningList;
    private DatabaseReference dbRef;

    public RekeningAdapter(Context context, List<Rekening> rekeningList) {
        this.context = context;
        this.rekeningList = rekeningList;

        mInflater = LayoutInflater.from(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNamaBank;
        public TextView tvNamaRekeninig;

        public ViewHolder(View itemView) {
            super(itemView);

            tvNamaBank = (TextView) itemView.findViewById(R.id.tvNamaBank);
            tvNamaRekeninig = (TextView) itemView.findViewById(R.id.tvNamaRekening);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rekening_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        dbRef = FirebaseDatabase.getInstance().getReference();

        final String idPetugas = rekeningList.get(position).getIdPetugas();
        final String idRekening = rekeningList.get(position).getIdRekening();
        final String namaRekening = rekeningList.get(position).getNamaRekening();
        final String namaBank = rekeningList.get(position).getNamaBank();

        holder.tvNamaRekeninig.setText(namaRekening);
        holder.tvNamaBank.setText(namaBank);

        holder.tvNamaRekeninig.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.opsi_dialog);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button editButton = (Button) dialog.findViewById(R.id.ubah);
                Button delButton = (Button) dialog.findViewById(R.id.hapus);

                //apabila tombol edit diklik
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, UbahRekeningActivity.class);
                        intent.putExtra("idRekening", rekeningList.get(position).getIdRekening());
                        context.startActivity(intent);
                    }
                });

                //apabila tombol delete diklik
                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        dbRef.child("rekening").child(idPetugas).child(idRekening).removeValue();
                        dbRef.child("tempatFutsal").child(idPetugas).child("rekening").child(idRekening).removeValue();
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
        return rekeningList.size();
    }

    @Override
    public void onClick(View v) {

    }
}
