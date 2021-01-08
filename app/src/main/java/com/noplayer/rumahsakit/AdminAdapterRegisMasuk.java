package com.noplayer.rumahsakit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminAdapterRegisMasuk extends RecyclerView.Adapter<AdminAdapterRegisMasuk.ViewHolder> {

    ArrayList<Pendaftaran> pendaftaranList;

    Context context;

    public AdminAdapterRegisMasuk(ArrayList<Pendaftaran>pendaftaranList, Context context) {
        this.pendaftaranList = pendaftaranList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_admin_regismsk, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Pendaftaran pendaftaran = pendaftaranList.get(position);

        holder.textView.setText("No. Pendaftaran: "+(position+1));
        holder.textView1.setText("Nama Pasien: "+pendaftaran.getNamaPasien());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(context, AdminDetailRegisMasukActivity.class);
                intent.putExtra("position", position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return pendaftaranList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView, textView1;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.itemadminregis);
            textView = itemView.findViewById(R.id.nopdftr1);
            textView1 = itemView.findViewById(R.id.namapasien1);
            constraintLayout = itemView.findViewById(R.id.conslisrgsmsk);
        }
    }

}
