package com.noplayer.rumahsakit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PoliActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText tglperiksa;
    private Spinner spinnerPoli;
    private ArrayList<String> listPoli = new ArrayList<>();
    private ArrayAdapter<String> poliAdapter;
    private int count;
    RequestQueue requestQueue;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button bt_DatePicker, bt_poli, btn_daftar;

    SessionManager sessionManager;
    String getId, strIdPoli;
    private static String URL_READ_POLI = URLStorage.URL_READ_POLI;
    private static String URL_REGIST_CHECKUP = URLStorage.URL_REGIST_CHECKUP;

    private Menu action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poli);

        sessionManager = new SessionManager(PoliActivity.this);
        sessionManager.checkLogin();

        requestQueue = Volley.newRequestQueue(this);
        spinnerPoli = findViewById(R.id.spinner_poli);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                URL_READ_POLI, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("poli");
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String namaPoli = jsonObject.optString("nama_poli");
                        strIdPoli = jsonObject.optString("id");

                        listPoli.add(namaPoli);
                        poliAdapter = new ArrayAdapter<>(PoliActivity.this,
                                android.R.layout.simple_spinner_item, listPoli);
                        poliAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPoli.setAdapter(poliAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        spinnerPoli.setOnItemSelectedListener(this);

        btn_daftar = findViewById(R.id.btn_daftar);

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tanggal = tglperiksa.getText().toString().trim();

                if (!tanggal.isEmpty() ) {
                    Daftar();
                } else {
                    tglperiksa.setError("Masukkan tanggal");
                }

            }
        });

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        tglperiksa = (EditText) findViewById(R.id.tanggalperiksa);
        bt_DatePicker = (Button) findViewById(R.id.bt_pilihtanggal);
        bt_DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog ();
            }
        });

        tglperiksa = findViewById(R.id.tanggalperiksa);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

    };

    private void showDateDialog() {
        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                tglperiksa.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        count = position + 1 ;

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + strIdPoli, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showDialog(String danger){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("ERROR !");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage(danger)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("OKE",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void Daftar() {

        final Integer id_poli1 = this.spinnerPoli.getSelectedItemPosition() + 1;
//        final Integer id_poli1 = 1;
        final String id_poli = id_poli1.toString().trim();
//        final String poli = this.spinnerPoli.getSelectedItem().toString().trim();
        final String tanggal_periksa = this.tglperiksa.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST_CHECKUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("responnya",response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.optString("message");
                            String last_id = jsonObject.getString("last_id");


                            if (success.equals("1")) {
                                Toast.makeText(PoliActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();

//                                startActivity(new Intent(PoliActivity.this, BuktiDaftarActivity.class));

                                Intent intent = new Intent(PoliActivity.this, BuktiDaftarActivity.class);
                                intent.putExtra("ID_PENDAFTARAN", last_id);
                                intent.putExtra("ID_USER", getId);
                                startActivity(intent);

                            } else if (success.equals("2")) {
                                showDialog(message);
//                                loading.setVisibility(View.GONE);
//                                btn_regist.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PoliActivity.this, "Register Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("error",e.toString());
//                            loading.setVisibility(View.GONE);
//                            btn_regist.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PoliActivity.this, "Register Error! " + error.toString(), Toast.LENGTH_SHORT).show();
//                        loading.setVisibility(View.GONE);
                        error.printStackTrace();
                        Log.d("error",error.toString());
//                        btn_regist.setVisibility(View.VISIBLE);
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", getId);
                params.put("id_poli", id_poli);
                params.put("tanggal_periksa", tanggal_periksa);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

