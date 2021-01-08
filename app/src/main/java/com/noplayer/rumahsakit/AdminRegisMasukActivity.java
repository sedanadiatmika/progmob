package com.noplayer.rumahsakit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminRegisMasukActivity extends AppCompatActivity {

    public  static RecyclerView recyclerView;
    AdminAdapterRegisMasuk adminAdapterRegismsk;
    public static ArrayList<Pendaftaran> pendaftaranList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_regis_masuk);
        recyclerView = findViewById(R.id.recycler3);

        getPendaftaran();
    }

    private void getPendaftaran(){
        pendaftaranList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.GET_REGIS_MASUK, response -> {
            try {
                JSONObject object1 = new JSONObject(response);
                if (object1.getBoolean("success")) {
                    JSONArray user = new JSONArray(object1.getString("pendaftaran"));
                    for (int i=0; i<user.length(); i++){
                        JSONObject daftar = user.getJSONObject(i);

                        Pendaftaran pendaftaran = new Pendaftaran();
                        pendaftaran.setNoAntre(daftar.getString("no_antre"));
                        pendaftaran.setNamaPasien(daftar.getString("nama_pasien"));
                        pendaftaran.setTanggalPeriksa(daftar.getString("tanggal_periksa"));
                        pendaftaran.setPoliTujuan(daftar.getString("poli_tujuan"));
                        pendaftaran.setIdPendaftaran(daftar.getString("id_pendaftaran"));
                        pendaftaranList.add(pendaftaran);
                    }
                    adminAdapterRegismsk = new AdminAdapterRegisMasuk(pendaftaranList, getApplicationContext());
                    recyclerView.setAdapter(adminAdapterRegismsk);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Get Failed Failed", Toast.LENGTH_SHORT).show();
            }
        },error -> {
            error.printStackTrace();
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
