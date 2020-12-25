package com.noplayer.rumahsakit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactUsActivity extends AppCompatActivity {

    ImageView ig, fb;
    TextView telpon, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ig = findViewById(R.id.instagram);
        fb = findViewById(R.id.fb);
        telpon = findViewById(R.id.tlp);
        email = findViewById(R.id.email);

        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlig = "https://instagram.com/rumahsakitadijaya";
                Intent instagram = new Intent(Intent.ACTION_VIEW, Uri.parse(urlig));
                startActivity(instagram);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlfb = "https://m.facebook.com";
                Intent facebook = new Intent(Intent.ACTION_VIEW, Uri.parse(urlfb));
                startActivity(facebook);

            }
        });

        telpon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tlpnih = new Intent(Intent.ACTION_DIAL, Uri.parse("0361223761"));
                startActivity(tlpnih);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

    }
    private void sendMail() {
        String kirimemail = email.getText().toString();
        Intent send= new Intent(Intent.ACTION_SEND);
        send.putExtra(send.EXTRA_EMAIL, kirimemail);

        startActivity(send);
    }
}