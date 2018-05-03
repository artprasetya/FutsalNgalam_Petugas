package com.asus.futsalngalam_petugas.MenuProfil.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asus.futsalngalam_petugas.MenuProfil.Model.Fasilitas;
import com.asus.futsalngalam_petugas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class FasilitasAdapter extends RecyclerView.Adapter<FasilitasAdapter.ViewHolder> {

    Context context;
    List<Fasilitas> fasilitasList;

    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private String idPetugas;

    public FasilitasAdapter(Context context, List<Fasilitas> fasilitasList) {
        this.context = context;
        this.fasilitasList = fasilitasList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fasilitas_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Fasilitas fasilitas = fasilitasList.get(position);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPetugas = user.getUid();

        holder.tvFasilitas.setText(fasilitas.getFasilitas());

        //coba hapus
//        holder.hapus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbRef.child("fasilitas").child(idPetugas).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Fasilitas fasilitasTemp = snapshot.getValue(Fasilitas.class);
//                            if (fasilitas.getIdFasilitas().equals(fasilitasTemp.getIdFasilitas())) {
//                                dbRef.child("fasilitas").child((fasilitas.getIdPetugas())).child(snapshot.getKey().toString()).removeValue();
//                                fasilitasList.remove(position);
//                                notifyDataSetChanged();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return fasilitasList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFasilitas;
        public TextView hapus;

        public ViewHolder(View itemView) {
            super(itemView);

            tvFasilitas = (TextView) itemView.findViewById(R.id.tvFasilitas);
            hapus = (TextView) itemView.findViewById(R.id.hapus);
        }
    }
}
