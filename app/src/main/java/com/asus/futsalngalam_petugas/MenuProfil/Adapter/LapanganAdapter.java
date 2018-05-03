package com.asus.futsalngalam_petugas.MenuProfil.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asus.futsalngalam_petugas.MenuProfil.Model.Lapangan;
import com.asus.futsalngalam_petugas.R;

import java.util.List;

public class LapanganAdapter extends RecyclerView.Adapter<LapanganAdapter.ViewHolder> {

    Context context;
    List<Lapangan> lapanganList;

    public LapanganAdapter(Context context, List<Lapangan> lapanganList) {
        this.context = context;
        this.lapanganList = lapanganList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lapangan_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Lapangan lapangan = lapanganList.get(position);

        holder.tvNamaLapangan.setText(lapangan.getNamaLapangan());
        holder.tvHarga.setText("Rp. " + String.valueOf(lapangan.getHargaSewa()));
        //holder.tvHarga.setText("Rp."+ BaseActivity.rupiah().format(lapangan.getHargaSewa()));

    }

    @Override
    public int getItemCount() {
        return lapanganList.size();
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
}
