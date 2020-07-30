package com.example.firenotes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addnotes extends AppCompatActivity {

    FirebaseFirestore fstore;
    TextView notetitle,notecontent;
    ProgressBar progressbarsave;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnotes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notecontent= findViewById(R.id.addnotecontent);
        notetitle = findViewById(R.id.addnoteTitle);
        progressbarsave = findViewById(R.id.progressBar);
        user = FirebaseAuth.getInstance().getCurrentUser();



        fstore = FirebaseFirestore.getInstance();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ntitle= notetitle.getText().toString();
                String ncontent = notecontent.getText().toString();
                if (ntitle.isEmpty()||ncontent.isEmpty())
                {
                    Toast.makeText(addnotes.this, "field is empty notes cant save", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressbarsave.setVisibility(View.VISIBLE);

                DocumentReference docref= fstore.collection("notes").document(user.getUid()).collection("myNotes").document();
                Map<String,Object> note = new HashMap<>();
                note.put("title",ntitle);
                note.put("content",ncontent);

                docref.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(addnotes.this, "Note Added", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addnotes.this, "Error,Try Again", Toast.LENGTH_SHORT).show();
                        progressbarsave.setVisibility(View.VISIBLE);
                    }
                });



            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.close_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.close)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
