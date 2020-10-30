package com.noplayer.rumahsakit;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText nama, tgl_lahir, no_telp, alamat, username, password, c_password;
    private AutoCompleteTextView jenis_kelamin;
    private Button btn_regist;
    private ProgressBar loading;
    private static String URL_REGIST = URLStorage.URL_REGIST;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button btDatePicker, bt_jenis_kelamin;

    private static final String[] jk = new String[] {"Laki-laki", "Perempuan"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        jenis_kelamin = findViewById(R.id.jenis_kelamin);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, jk);
        jenis_kelamin.setAdapter(adapter);

        bt_jenis_kelamin = findViewById(R.id.bt_jenis_kelamin);

        bt_jenis_kelamin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenis_kelamin.showDropDown();
            }
        });

        dateFormatter = new SimpleDateFormat("YYYY-MM-dd", Locale.US);

        tgl_lahir = (EditText) findViewById(R.id.tgl_lahir);
        btDatePicker = (Button) findViewById(R.id.bt_datepicker);
        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog ();
            }
        });

        loading = findViewById(R.id.loading);
        nama = findViewById(R.id.nama);

        tgl_lahir = findViewById(R.id.tgl_lahir);
        no_telp = findViewById(R.id.no_telp);
        alamat = findViewById(R.id.alamat);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        btn_regist = findViewById(R.id.btn_regist);

        jenis_kelamin.setFocusableInTouchMode(false);
        tgl_lahir.setFocusableInTouchMode(false);


        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Regist();
            }
        });

    }

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
                tgl_lahir.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }


    private void Regist() {

        final String nama  = this.nama.getText().toString().trim();
        final String jenis_kelamin  = this.jenis_kelamin.getText().toString().trim();
        final String tgl_lahir = this.tgl_lahir.getText().toString().trim();
        final String no_telp = this.no_telp.getText().toString().trim();
        final String alamat = this.alamat.getText().toString().trim();
        final String username = this.username.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
        final String c_password = this.c_password.getText().toString().trim();


        //validation

        if (nama.length() < 5 ) {
            Toast.makeText(RegisterActivity.this, "Nama minimal 5 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (no_telp.length() < 10 ) {
            Toast.makeText(RegisterActivity.this, "Nomor telepon minimal 10 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (alamat.length() < 5 ) {
            Toast.makeText(RegisterActivity.this, "Alamat minimal 5 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.length() < 5 ) {
            Toast.makeText(RegisterActivity.this, "Username minimal 5 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8 ) {
            Toast.makeText(RegisterActivity.this, "Password minimal 8 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(c_password)) {
            Toast.makeText(RegisterActivity.this, "Password berbeda", Toast.LENGTH_SHORT).show();
            return;
        }
        //end of validation

        loading.setVisibility(View.VISIBLE);
        btn_regist.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("responnya",response);
                        String success = jsonObject.getString("success");
                        String message = jsonObject.optString("message");

                        if (success.equals("1")) {
                            Toast.makeText(RegisterActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btn_regist.setVisibility(View.VISIBLE);
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else if (success.equals("2")) {
                            showDialog(message);
                            loading.setVisibility(View.GONE);
                            btn_regist.setVisibility(View.VISIBLE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Register Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("error",e.toString());
                        loading.setVisibility(View.GONE);
                        btn_regist.setVisibility(View.VISIBLE);
                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Register Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        Log.d("error",error.toString());
                        btn_regist.setVisibility(View.VISIBLE);
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("jenis_kelamin", jenis_kelamin);
                params.put("tgl_lahir", tgl_lahir);
                params.put("no_telp", no_telp);
                params.put("alamat", alamat);
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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


}
