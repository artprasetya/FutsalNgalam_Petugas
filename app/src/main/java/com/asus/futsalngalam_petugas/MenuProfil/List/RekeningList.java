package com.asus.futsalngalam_petugas.MenuProfil.List;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.asus.futsalngalam_petugas.MenuProfil.Model.Rekening;
import com.asus.futsalngalam_petugas.R;

import java.util.List;

public class RekeningList extends ArrayAdapter<Rekening> {
    private Activity context;
    private List<Rekening> rekeningList;

    public RekeningList(Activity context, List<Rekening> rekeningList) {
        super(context, R.layout.list_rekening, rekeningList);
        this.context = context;
        this.rekeningList = rekeningList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_rekening, null, true);

        TextView tvRekening = (TextView) listViewItem.findViewById(R.id.tvRekening);

        Rekening rekening = rekeningList.get(position);

        tvRekening.setText(rekening.getNamaBank());

        return listViewItem;
    }
}