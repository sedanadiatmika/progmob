package com.noplayer.rumahsakit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

    }

    public void registmsk(View view) {
        Intent intent = new Intent(this, AdminRegisMasukActivity.class);
        startActivity(intent);
    }

    public void listuser(View view) {
        Intent intent = new Intent(this, AdminListUserActivity.class);
        startActivity(intent);
    }

    public void listadmin(View view) {
        Intent intent = new Intent(this, AdminListAdminActivity.class);
        startActivity(intent);
    }
}