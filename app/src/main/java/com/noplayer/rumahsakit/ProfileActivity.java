package com.noplayer.rumahsakit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName(); //getting info
    private TextView nama, username, no_telp, alamat;
    private EditText tgl_lahir;
    private AutoCompleteTextView jenis_kelamin;

    private Button bt_jenis_kelamin;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button btDatePicker;

    private Button btn_logout;
    SessionManager sessionManager;
    String getId;
    String getToken;
    private static String URL_READ = URLStorage.URL_READ;
    private static String URL_EDIT = URLStorage.URL_EDIT;
    private Menu action;

    private static final String[] jk = new String[] {"Laki-laki", "Perempuan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

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

        nama = findViewById(R.id.nama);
        username = findViewById(R.id.username);
        no_telp = findViewById(R.id.no_telp);
        alamat = findViewById(R.id.alamat);
        btn_logout = findViewById(R.id.btn_logout);

        nama.setFocusableInTouchMode(false);
        username.setFocusableInTouchMode(false);
        jenis_kelamin.setFocusableInTouchMode(false);
        tgl_lahir.setFocusableInTouchMode(false);
        no_telp.setFocusableInTouchMode(false);
        alamat.setFocusableInTouchMode(false);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        getToken = user.get(sessionManager.TOKEN);




        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
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

    //getUserDetail
    private void getUserDetail() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");


                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {


                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strName = object.getString("name").trim();
                                    String strUsername = object.getString("username").trim();
                                    String strJenisKelamin = object.getString("jenis_kelamin").trim();
                                    String strTanggalLahir = object.getString("tanggal_lahir").trim();
                                    String strNomorTelp = object.getString("nomor_telp").trim();
                                    String strAlamat = object.getString("alamat").trim();

                                    nama.setText(strName);
                                    username.setText(strUsername);
                                    jenis_kelamin.setText(strJenisKelamin);
                                    tgl_lahir.setText(strTanggalLahir);
                                    no_telp.setText(strNomorTelp);
                                    alamat.setText(strAlamat);

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Error Reading Detail " +e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Error Reading Detail " +error.toString(), Toast.LENGTH_SHORT).show();

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
                params.put("id", getId);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action, menu);

        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit:

                bt_jenis_kelamin.setVisibility(View.VISIBLE);
                btDatePicker.setVisibility(View.VISIBLE);
                btn_logout.setVisibility(View.GONE);

                nama.setFocusableInTouchMode(true);
                username.setFocusableInTouchMode(true);
                jenis_kelamin.setFocusableInTouchMode(true);
                no_telp.setFocusableInTouchMode(true);
                alamat.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(nama, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return true;

            case R.id.menu_save:

                SaveEditDetail();

                bt_jenis_kelamin.setVisibility(View.GONE);
                btDatePicker.setVisibility(View.GONE);
                btn_logout.setVisibility(View.VISIBLE);

                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);

                nama.setFocusableInTouchMode(false);
                username.setFocusableInTouchMode(false);
                jenis_kelamin.setFocusableInTouchMode(false);
                tgl_lahir.setFocusableInTouchMode(false);
                no_telp.setFocusableInTouchMode(false);
                alamat.setFocusableInTouchMode(false);

                nama.setFocusable(false);
                username.setFocusable(false);
                jenis_kelamin.setFocusable(false);
                tgl_lahir.setFocusable(false);
                no_telp.setFocusable(false);
                alamat.setFocusable(false);

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }

    //saveEditDetail
    private void SaveEditDetail() {

        final String nama = this.nama.getText().toString().trim();
        final String username = this.username.getText().toString().trim();
        final String jenis_kelamin = this.jenis_kelamin.getText().toString().trim();
        final String tgl_lahir = this.tgl_lahir.getText().toString().trim();
        final String no_telp = this.no_telp.getText().toString().trim();
        final String alamat = this.alamat.getText().toString().trim();
        final String id = getId;
        final String token = getToken;

        if (nama.length() < 5 ) {
            Toast.makeText(ProfileActivity.this, "Nama minimal 5 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (no_telp.length() < 10 ) {
            Toast.makeText(ProfileActivity.this, "Nomor telepon minimal 10 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (alamat.length() < 5 ) {
            Toast.makeText(ProfileActivity.this, "Alamat minimal 5 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.length() < 5 ) {
            Toast.makeText(ProfileActivity.this, "Username minimal 5 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("username", username);
                params.put("jenis_kelamin", jenis_kelamin);
                params.put("tgl_lahir", tgl_lahir);
                params.put("no_telp", no_telp);
                params.put("alamat", alamat);
                params.put("id", id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
