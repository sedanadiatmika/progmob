package com.noplayer.rumahsakit;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminDetailListUserActivity extends AppCompatActivity {

    TextView adminName, adminEmail, adminMobile, adminGender, adminAddress, adminBirthdate;
    int position;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_list_user);

        init();
        getIncomingExtra();
        getUser();
    }

    private void init(){
        circleImageView = (CircleImageView)findViewById(R.id.userPhoto);
        adminName = (TextView)findViewById(R.id.Nameadmin1);
        adminEmail = (TextView)findViewById(R.id.Emailadmin1);
        adminMobile = (TextView)findViewById(R.id.Phoneadmin1);
        adminGender = (TextView)findViewById(R.id.Genderadmin1);
        adminAddress = (TextView)findViewById(R.id.Addressadmin1);
        adminBirthdate = (TextView)findViewById(R.id.birthadmin1);
    }

    private void getIncomingExtra() {
        if(getIntent().hasExtra("position")){

            position = getIntent().getIntExtra("position", 0);

        }
    }

    private void getUser(){
        User user = AdminListUserActivity.userList.get(position);
        adminName.setText(user.getName());
        adminEmail.setText(user.getEmail());
        adminMobile.setText(user.getMobile());
        adminGender.setText(user.getGender());
        adminAddress.setText(user.getAddress());
        adminBirthdate.setText(user.getBirthdate());
    }
}
