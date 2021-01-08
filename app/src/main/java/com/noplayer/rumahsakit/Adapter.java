package com.noplayer.rumahsakit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<Pendaftaran> pendaftarans;
    Context context;

    public Adapter(Context ctx, List<Pendaftaran> pendaftarans){
        this.inflater = LayoutInflater.from(ctx);
        this.pendaftarans = pendaftarans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_daftar_layout,parent, false);
//        ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind data
        holder.noAntre.setText(pendaftarans.get(position).getNoAntre());
        holder.namaPasien.setText(pendaftarans.get(position).getNamaPasien());
        holder.poliTujuan.setText(pendaftarans.get(position).getPoliTujuan());
        holder.tanggalPeriksa.setText(pendaftarans.get(position).getTanggalPeriksa());
        holder.idPendaftaran.setText(pendaftarans.get(position).getIdPendaftaran());
        holder.statusPendaftaran.setText(pendaftarans.get(position).getStatusPendaftaran());
    }

    @Override
    public int getItemCount() {
        return pendaftarans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView noAntre, namaPasien, poliTujuan, tanggalPeriksa, idPendaftaran, statusPendaftaran;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noAntre = itemView.findViewById(R.id.noAntrian);
            namaPasien = itemView.findViewById(R.id.nama_pasien);
            poliTujuan = itemView.findViewById(R.id.poli_tujuan);
            tanggalPeriksa = itemView.findViewById(R.id.tanggal_periksa);
            idPendaftaran = itemView.findViewById(R.id.id_pendaftaran);
            statusPendaftaran = itemView.findViewById(R.id.status_pendaftaran);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Toast.makeText(v.getContext(), "Item Position", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
