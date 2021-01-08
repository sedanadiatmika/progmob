package com.noplayer.rumahsakit;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDetailRwtActivity extends AppCompatActivity {

    TextView nama, poli, status, tanggal, tanggalLayout;
    int position;
    String statusPasien, idRegis, tokenLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_rwt);
        init();
    }

    private void init(){
        nama = (TextView) findViewById(R.id.txtNama);
        poli = (TextView)findViewById(R.id.txtPoli);
        status = (TextView)findViewById(R.id.txtStatus);
        tanggal = (TextView)findViewById(R.id.txtTanggal);
        tanggalLayout = (TextView)findViewById(R.id.txtTanggalDetail);
        getIncomingExtra();
        setDetail();

    }

    private void getIncomingExtra() {

        if(getIntent().hasExtra("position")){
            position = getIntent().getIntExtra("position", 0);
        }
    }

    private void setDetail(){
        Pendaftaran pendaftaran = AdminRiwayatActivity.pendaftaranList.get(position);
        nama.setText(pendaftaran.getNamaPasien());
        poli.setText(pendaftaran.getPoliTujuan());
        statusPasien = pendaftaran.getStatusPendaftaran();
        idRegis = pendaftaran.getIdPendaftaran();

        if(statusPasien.equals("pending")){
            status.setText("Pending");
            tanggalLayout.setVisibility(View.GONE);
            tanggal.setVisibility(View.GONE);
        }else if(statusPasien.equals("rejected")){
            status.setText("Rejected");
            tanggalLayout.setVisibility(View.GONE);
            tanggal.setVisibility(View.GONE);
        }else if(statusPasien.equals("accepted")){
            status.setText("Accepted");
            tanggal.setText(pendaftaran.getTanggalPeriksa());
        }
    }
}
