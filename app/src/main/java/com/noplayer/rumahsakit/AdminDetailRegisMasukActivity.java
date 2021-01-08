package com.noplayer.rumahsakit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdminDetailRegisMasukActivity extends AppCompatActivity {

    TextView nama, poli, tanggal, tanggalLayout,status;
    Button btnAcc, btnRef, btnAccept1, btnTanggal;
    int position;
    ProgressDialog dialog1;
    String statusPasien, idRegis, tokenLogin;
    EditText tanggalpemeriksaan;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_regis_masuk);

        getIncomingExtra();
        init();

    }

    private void init(){
        dateFormatter = new SimpleDateFormat("YYYY-MM-dd", Locale.US);
        nama = (TextView)findViewById(R.id.txtNama);
        poli = (TextView)findViewById(R.id.txtPoli);
        status = (TextView)findViewById(R.id.txtStatus);
        tanggal = (TextView)findViewById(R.id.txtTanggal);
        tanggalLayout = (TextView)findViewById(R.id.txtTanggalDetail);
        tanggalpemeriksaan = (EditText)findViewById(R.id.tanggal_accept);

        btnAcc = (Button)findViewById(R.id.accregis);
        btnRef = (Button)findViewById(R.id.tolakregis);

        dialog1 = new ProgressDialog(AdminDetailRegisMasukActivity.this);
        dialog1.setCancelable(false);

        getIncomingExtra();
        setDetail();

        btnRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminDetailRegisMasukActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Reject Registrasi?");
                builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog1.setMessage("Rejecting Data");
                        dialog1.show();
                        StringRequest request = new StringRequest(Request.Method.POST, Constant.REJECT_REGIS_MASUK, response -> {
                            try {
                                JSONObject object1 = new JSONObject(response);
                                if (object1.getInt("success")==1){
                                    AdminRegisMasukActivity.recyclerView.getAdapter().notifyItemRemoved(position);
                                    AdminRegisMasukActivity.recyclerView.getAdapter().notifyDataSetChanged();

                                    Intent intent = new Intent(AdminDetailRegisMasukActivity.this, AdminRegisMasukActivity.class);
                                    startActivity(intent);
                                    finish();
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Reject Registrasi Success", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Reject Registrasi Failed", Toast.LENGTH_SHORT).show();
                            }
                            dialog1.dismiss();
                        },error -> {
                            error.printStackTrace();
                            dialog1.dismiss();
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<>();
                                // Basic Authentication
                                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                                headers.put("Authorization", "Bearer " + tokenLogin);
                                return headers;
                            }

                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> map = new HashMap<>();
                                map.put("id",idRegis);
                                return map;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        queue.add(request);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        btnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AdminDetailRegisMasukActivity.this);
                dialog.setContentView(R.layout.dialog_accept_regis);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width,height);
                dialog.show();
                tanggalpemeriksaan = dialog.findViewById(R.id.tanggal_accept);
                btnAccept1 = dialog.findViewById(R.id.btnAccepted);
                btnTanggal = dialog.findViewById(R.id.btnTanggal);

                btnTanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDateDialog();
                    }
                });

                btnAccept1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest request = new StringRequest(Request.Method.POST, Constant.ACCEPT_REGIS_MASUK, response -> {
                            try {
                                JSONObject object1 = new JSONObject(response);
                                if (object1.getInt("success")==1){
                                    AdminRegisMasukActivity.recyclerView.getAdapter().notifyItemRemoved(position);
                                    AdminRegisMasukActivity.recyclerView.getAdapter().notifyDataSetChanged();

                                    Intent intent = new Intent(AdminDetailRegisMasukActivity.this, AdminRegisMasukActivity.class);
                                    startActivity(intent);
                                    finish();
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Accept Registrasi Success", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Accept Registrasi Failed", Toast.LENGTH_SHORT).show();
                            }
                            dialog1.dismiss();
                        },error -> {
                            error.printStackTrace();
                            dialog1.dismiss();
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<>();
                                // Basic Authentication
                                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                                headers.put("Authorization", "Bearer " + tokenLogin);
                                return headers;
                            }

                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> map = new HashMap<>();
                                map.put("id",idRegis);
                                map.put("tanggal", tanggalpemeriksaan.getText().toString());
                                return map;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        queue.add(request);
                    }
                });

            }
        });

    }

    private void getIncomingExtra() {

        if(getIntent().hasExtra("position")){
            position = getIntent().getIntExtra("position", 0);
        }
    }

    private void setDetail(){
        Pendaftaran pendaftaran = AdminRegisMasukActivity.pendaftaranList.get(position);
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
            btnAcc.setVisibility(View.GONE);
            btnRef.setVisibility(View.GONE);
        }else if(statusPasien.equals("accepted")){
            status.setText("Accepted");
            tanggal.setText(pendaftaran.getTanggalPeriksa());
            btnAcc.setVisibility(View.GONE);
            btnRef.setVisibility(View.GONE);
        }

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

                tanggalpemeriksaan.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
}
