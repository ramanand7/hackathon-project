package com.example.firenotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class shownotedetail extends AppCompatActivity {
  Intent deta;
   ImageView fab1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shownotedetail);
        Toolbar toolbar= findViewById(R.id.toolbar2);
        fab1=findViewById(R.id.fabimag);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deta = getIntent();
        TextView content = findViewById(R.id.notedetailcontent);
        content.setMovementMethod(new ScrollingMovementMethod());
        TextView title = findViewById(R.id.notedetailtitle);

        content.setText(deta.getStringExtra("contennt"));
        title.setText(deta.getStringExtra("title"));

        content.setBackgroundColor(getResources().getColor(deta.getIntExtra("codecolr",0)));
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent i = new Intent(view.getContext(),editNote.class);

             i.putExtra("title",deta.getStringExtra("title"));
             i.putExtra("content",deta.getStringExtra("contennt"));
             i.putExtra("docid",deta.getStringExtra("noteid"));
             startActivity(i);

            }
        });







    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
