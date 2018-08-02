package com.asus.futsalngalam_petugas.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asus.futsalngalam_petugas.MenuProfil.FasilitasActivity;
import com.asus.futsalngalam_petugas.MenuProfil.UbahFasilitasActivity;
import com.asus.futsalngalam_petugas.Model.Fasilitas;
import com.asus.futsalngalam_petugas.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FasilitasAdapter extends RecyclerView.Adapter<FasilitasAdapter.ViewHolder> implements View.OnClickListener {

    Context context;
    LayoutInflater mInflater;
    private List<Fasilitas> fasilitasList;
    private DatabaseReference dbRef;
    private EditText etFasilitas;

    public FasilitasAdapter(Context context, List<Fasilitas> fasilitasList) {
        this.context = context;
        this.fasilitasList = fasilitasList;

        mInflater = LayoutInflater.from(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFasilitas;

        public ViewHolder(View itemView) {
            super(itemView);

            tvFasilitas = (TextView) itemView.findViewById(R.id.tvFasilitas);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fasilitas_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        dbRef = FirebaseDatabase.getInstance().getReference();

        final String idPetugas = fasilitasList.get(position).getIdPetugas();
        final String idFasilitas = fasilitasList.get(position).getIdFasilitas();
        final String namaFasilitas = fasilitasList.get(position).getFasilitas();

        holder.tvFasilitas.setText(namaFasilitas);

        holder.tvFasilitas.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.opsi_dialog);
                dialog.setTitle("Pilih aksi");
                dialog.show();

                Button editButton = (Button) dialog.findViewById(R.id.ubah);
                Button delButton = (Button) dialog.findViewById(R.id.hapus);

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, UbahFasilitasActivity.class);
                        intent.putExtra("idFasilitas", fasilitasList.get(position).getIdFasilitas());
                        context.startActivity(intent);
                    }
                });

                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dbRef.child("fasilitas").child(idPetugas).child(idFasilitas).removeValue();
                        Toast.makeText(context, "Data Berhasil Dihapus.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, FasilitasActivity.class);
                        context.startActivity(intent);
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return fasilitasList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public interface FirebaseDataListener {
        void onDeleteData(Fasilitas fasilitas, int position);
    }
}
