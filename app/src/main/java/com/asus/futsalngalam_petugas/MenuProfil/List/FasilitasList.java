package com.asus.futsalngalam_petugas.MenuProfil.List;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.asus.futsalngalam_petugas.MenuProfil.Model.Fasilitas;
import com.asus.futsalngalam_petugas.R;

import java.util.List;

public class FasilitasList extends ArrayAdapter<Fasilitas> {
    private Activity context;
    private List<Fasilitas> fasilitasList;

    public FasilitasList(Activity context, List<Fasilitas> fasilitasList){
        super(context, R.layout.list_fasilitas, fasilitasList);
        this.context = context;
        this.fasilitasList = fasilitasList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_fasilitas, null, true);

        TextView tvFasilitas = (TextView) listViewItem.findViewById(R.id.tvFasilitas);

        Fasilitas fasilitas = fasilitasList.get(position);

        tvFasilitas.setText(fasilitas.getFasilitas());

        return listViewItem;
    }
}
