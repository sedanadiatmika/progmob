package com.noplayer.rumahsakit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.WriterException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class BuktiDaftarActivity extends AppCompatActivity {

    private static final String TAG = BuktiDaftarActivity.class.getSimpleName();
    private TextView no_antre, namapasien, tgl_lahir, poli_tujuan, tanggalperiksa, qrcode;
    ImageView qrImage;
    SessionManager sessionManager;
    String getId, getToken, id_pendaftaran, id_user;
    private static String URL_READ = URLStorage.URL_READ;
    private static String URL_LOAD_CHECKUP = URLStorage.URL_LOAD_CHECKUP;
    private static String URL_REGIST_CHECKUP = URLStorage.URL_REGIST_CHECKUP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bukti_daftar);

        sessionManager = new SessionManager(BuktiDaftarActivity.this);
        sessionManager.checkLogin();

        no_antre = findViewById(R.id.noantre);
        namapasien = findViewById(R.id.namapasien);
        tgl_lahir = findViewById(R.id.tgllahir);
        poli_tujuan = findViewById(R.id.politujuan);
        tanggalperiksa = findViewById(R.id.tglperiksa);

        qrImage = findViewById(R.id.qrcode);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        getToken = user.get(sessionManager.TOKEN);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("ID_USER");
        id_pendaftaran = intent.getStringExtra("ID_PENDAFTARAN");

    }

    private void getBuktiDaftar() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOAD_CHECKUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {


                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strAntrian = object.getString("no_antrian").trim();
                                    String strNamaPasien = object.getString("nama_user").trim();
                                    String strDOBPasien = object.getString("tanggal_lahir").trim();
                                    String strTanggalperiksa= object.getString("tanggal_periksa").trim();
                                    String strPolitujuan = object.getString("poli_tujuan").trim();
                                    String strQRCode = object.getString("qrcode").trim();


                                    namapasien.setText(strNamaPasien);
                                    tgl_lahir.setText(strDOBPasien);
                                    no_antre.setText(strAntrian);
                                    tanggalperiksa.setText(strTanggalperiksa);
                                    poli_tujuan.setText(strPolitujuan);

                                    QRGEncoder qrgEncoder = new QRGEncoder(strQRCode, null, QRGContents.Type.TEXT, 500);
                                    qrgEncoder.setColorBlack(Color.BLACK);
                                    qrgEncoder.setColorWhite(Color.WHITE);
                                    // Getting QR-Code as Bitmap
                                    Bitmap bitmap = qrgEncoder.getBitmap();
                                    // Setting Bitmap to ImageView
                                    qrImage.setImageBitmap(bitmap);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(BuktiDaftarActivity.this, "Error Reading Detail " +e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(BuktiDaftarActivity.this, "Error Reading Detail " +error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + getToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id_pendaftaran);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getBuktiDaftar();

        String value = "naga";

//        QRGEncoder qrgEncoder = new QRGEncoder(strQR, null, QRGContents.Type.TEXT, 500);
//        qrgEncoder.setColorBlack(Color.BLACK);
//        qrgEncoder.setColorWhite(Color.WHITE);
//        // Getting QR-Code as Bitmap
//        Bitmap bitmap = qrgEncoder.getBitmap();
//        // Setting Bitmap to ImageView
//        qrImage.setImageBitmap(bitmap);
    }
}
