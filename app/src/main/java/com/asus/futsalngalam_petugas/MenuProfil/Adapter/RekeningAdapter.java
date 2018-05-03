package com.asus.futsalngalam_petugas.MenuProfil.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asus.futsalngalam_petugas.MenuProfil.Model.Rekening;
import com.asus.futsalngalam_petugas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RekeningAdapter extends RecyclerView.Adapter<RekeningAdapter.ViewHolder> {

    Context context;
    List<Rekening> rekeningList;
    private FirebaseAuth auth;
    private String idPetugas;

    public RekeningAdapter(Context context, List<Rekening> rekeningList) {
        this.context = context;
        this.rekeningList = rekeningList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rekening_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Rekening rekening = rekeningList.get(position);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        holder.tvNamaRekeninig.setText(rekening.getNamaRekening());
        holder.tvNamaBank.setText(rekening.getNamaBank());

    }

    @Override
    public int getItemCount() {
        return rekeningList.size();
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
}
