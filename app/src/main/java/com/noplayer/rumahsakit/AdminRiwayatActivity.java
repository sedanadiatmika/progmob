package com.noplayer.rumahsakit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminRiwayatActivity extends AppCompatActivity {

    public  static RecyclerView recyclerView;
    AdminAdapterRiwayat adminAdapterRiwayat;
    public static ArrayList<Pendaftaran> pendaftaranList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_riwayat);

        recyclerView = findViewById(R.id.recycler2);

        getPendaftaran();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_recent);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),HomeAdminActivity.class));
                        overridePendingTransition(0,0);
                        break;


                    case R.id.nav_recent:
                        overridePendingTransition(0,0);

                        break;


                    case R.id.nav_user:
                        startActivity(new Intent(getApplicationContext(), AdminProfileActivity.class));
                        overridePendingTransition(0,0);

                        break;

                }

                return false;
            }
        });
    }

    private void getPendaftaran(){
        pendaftaranList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.GET_REGIS_DIRESPON, response -> {
            try {
                JSONObject object1 = new JSONObject(response);
                if (object1.getBoolean("success")) {
                    JSONArray user = new JSONArray(object1.getString("pendaftaran"));
                    for (int i=0; i<user.length(); i++){
                        JSONObject daftar = user.getJSONObject(i);

                        Pendaftaran pendaftaran = new Pendaftaran();
                        pendaftaran.setIdPendaftaran(daftar.getString("id"));
                        pendaftaran.setPoliTujuan(daftar.getString("poli"));
                        pendaftaran.setStatusPendaftaran(daftar.getString("status"));
                        pendaftaran.setTanggalPeriksa(daftar.getString("tgl_regis"));
                        pendaftaranList.add(pendaftaran);
                    }
                    adminAdapterRiwayat = new AdminAdapterRiwayat(pendaftaranList, getApplicationContext());
                    recyclerView.setAdapter(adminAdapterRiwayat);
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
