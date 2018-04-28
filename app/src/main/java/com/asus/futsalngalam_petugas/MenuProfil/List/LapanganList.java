package com.asus.futsalngalam_petugas.MenuProfil.List;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.asus.futsalngalam_petugas.MenuProfil.Model.Lapangan;
import com.asus.futsalngalam_petugas.R;

import java.util.List;

public class LapanganList extends ArrayAdapter<Lapangan> {
    private Activity context;
    private List<Lapangan> lapanganList;

    public LapanganList(Activity context, List<Lapangan> lapanganList){
        super(context, R.layout.list_lapangan, lapanganList);
        this.context = context;
        this.lapanganList = lapanganList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_lapangan, null, true);

        TextView tvLapangan = (TextView) listViewItem.findViewById(R.id.tvNamaLapangan);
        TextView tvKategori = (TextView) listViewItem.findViewById(R.id.tvKategori);

        Lapangan lapangan = lapanganList.get(position);

        tvLapangan.setText(lapangan.getNamaLapangan());
        tvKategori.setText(lapangan.getKategori());

        return listViewItem;
    }
}
