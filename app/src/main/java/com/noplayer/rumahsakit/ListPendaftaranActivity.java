package com.noplayer.rumahsakit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListPendaftaranActivity extends AppCompatActivity {
    String TAG = "ListPendaftaran";

    private TextView no_antre, namapasien, tgl_lahir, poli_tujuan, tanggalperiksa, qrcode, statuspendaftaran;
    RecyclerView recyclerView;
    List<Pendaftaran> pendaftarans;
    Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    SessionManager sessionManager;
    String getId, getToken, id_pendaftaran, id_user;
    private static String URL_LOAD_CHECKUP_ALL = URLStorage.URL_LOAD_CHECKUP_ALL;
    private static String URL_LOAD_CHECKUP_P = URLStorage.URL_LOAD_CHECKUP_P;

    private Button btn_delete;
    private Context context = this;

    DatabaseHelper SQLite = new DatabaseHelper(this);
    public static final String TAG_ID = "id_pendaftaran";
    public static final String TAG_NAME = "nama_user";
    public static final String TAG_DATE = "tanggal_periksa";
    public static final String TAG_POLI = "poli_tujuan";
    public static final String TAG_ANTR = "no_antrian";
    public static final String TAG_STTS = "status_pendaftaran";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pendaftaran);

        sessionManager = new SessionManager(ListPendaftaranActivity.this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        getToken = user.get(sessionManager.TOKEN);

        recyclerView = findViewById(R.id.pendaftaranList);
        pendaftarans = new ArrayList<>();

        if (onCheckConnection()) {
            Log.e("CONNECTION", "ONLINE");
            getListPendaftaran();
//            getAllData(getId);

        } else {
            Log.e("CONNECTION", "OFFLINE");
            getAllData(getId);
//            getListPendaftaran();

        }

        Map<String, String> params = new HashMap<>();
        params.put("id_user", getId);

        /////
        SQLite = new DatabaseHelper(getApplicationContext());
        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite.delete(getId);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new Adapter(getApplicationContext(), pendaftarans);
        recyclerView.setAdapter(adapter);

        SQLite.getAllData(getId);

//        isNetworkConnected();

    }

    private void getAllData(String id_user) {
        ArrayList<HashMap<String, String>> row = SQLite.getAllData(id_user);

        for (int i = 0; i < row.size(); i++) {
            String id = row.get(i).get(TAG_ID);
            String nama = row.get(i).get(TAG_NAME);
            String tanggal = row.get(i).get(TAG_DATE);
            String poli = row.get(i).get(TAG_POLI);
            String antre = row.get(i).get(TAG_ANTR);
            String status = row.get(i).get(TAG_STTS);

            Pendaftaran pendaftaran = new Pendaftaran();
            pendaftaran.setIdPendaftaran(id);
            pendaftaran.setNamaPasien(nama);
            pendaftaran.setPoliTujuan(poli);
            pendaftaran.setTanggalPeriksa(tanggal);
            pendaftaran.setNoAntre(antre);
            pendaftaran.setStatusPendaftaran(status);

            pendaftarans.add(pendaftaran);

        }

//        adapter.notifyDataSetChanged();
    }

//    public boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
//
//        Boolean status = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
//        Log.e("insert sqlite ", "" + status);
//
//        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
//    }

//    public boolean isNetworkAvailable(final Context context) {
//        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
//        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
//    }

//    private boolean isNetworkConnected() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        Boolean result =  cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
//                Log.e("insert sqlite ", "" + result);
//
//        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
//    }

    public boolean onCheckConnection() {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);

                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                }
            }

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null) {
                return networkInfo.isConnected();
            }
        }

        return false;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }

    private void getListPendaftaran() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOAD_CHECKUP_ALL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responnya",response);
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            final int numberOfItemsInResp = jsonArr.length();
                            SQLite.delete(getId);

                            for (int i = 0; i < numberOfItemsInResp; i++) {
                                try {
                                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                                    Pendaftaran pendaftaran = new Pendaftaran();
                                    pendaftaran.setNamaPasien(jsonObj.getString("nama_user"));
                                    pendaftaran.setPoliTujuan(jsonObj.getString("poli_tujuan"));
                                    pendaftaran.setNoAntre(jsonObj.getString("no_antrian"));
                                    pendaftaran.setTanggalPeriksa(jsonObj.getString("tanggal_periksa"));
                                    pendaftaran.setIdPendaftaran(jsonObj.getString("id"));
                                    pendaftaran.setStatusPendaftaran(jsonObj.getString("status_pendaftaran"));
                                    pendaftarans.add(pendaftaran);

                                    String id_pendaftaran = jsonObj.getString("id");
                                    String nama = jsonObj.getString("nama_user");
                                    String poli = jsonObj.getString("poli_tujuan");
                                    String antri = jsonObj.getString("no_antrian");
                                    String tanggal = jsonObj.getString("tanggal_periksa");
                                    String status = jsonObj.getString("status_pendaftaran");

                                    SQLite.insert(nama, tanggal, poli, antri, getId, status);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(ListPendaftaranActivity.this, "Register Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("error", e.toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        getAllData(getId);

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter = new Adapter(getApplicationContext(), pendaftarans);
                        recyclerView.setAdapter(adapter);
                        Toast.makeText(ListPendaftaranActivity.this, "Id User: " + getId, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListPendaftaranActivity.this, "Register Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        Log.d("error",error.toString());
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}



