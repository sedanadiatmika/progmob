package com.noplayer.rumahsakit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {

    TextView alamatku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        alamatku = (TextView) findViewById(R.id.alamat);

        alamatku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri urialamat=Uri.parse("geo:0,0?q="+alamatku.getText().toString());
                Intent jalan=new Intent(Intent.ACTION_VIEW,urialamat);
                jalan.setPackage("com.google.android.apps.maps");
                if(jalan.resolveActivity(getPackageManager())!=null)
                {
                    startActivity(jalan);
                }
            }
        });
    }
}
