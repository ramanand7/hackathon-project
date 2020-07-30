package com.example.firenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class shareapp extends AppCompatActivity {

    Button share;
    TextView quot;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareapp);
        share= findViewById(R.id.sharebutton);
        quot= findViewById(R.id.sharetextt);
        getSupportActionBar().setTitle("Welcome User");

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tex= quot.getText().toString();
                Intent objectintent= new Intent();
                objectintent.setAction(Intent.ACTION_SEND);
                objectintent.setType("text/plain");
                objectintent.putExtra(Intent.EXTRA_TEXT,tex);
                startActivity(objectintent);
                finish();

            }
        });

    }



}
