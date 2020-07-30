package com.example.firenotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class editNote extends AppCompatActivity {
    FirebaseFirestore fstore;
    Intent data;
    TextView editnotecontent,editnotetitle;
    ProgressBar progressBar2;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar= findViewById(R.id.toolbaren);
        setSupportActionBar(toolbar);
        progressBar2 = findViewById(R.id.progressBar2);
        fstore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        editnotecontent= findViewById(R.id.editnotecontent);
        editnotetitle = findViewById(R.id.editnoteTitle);


        data= getIntent();
        String noteTitle= data.getStringExtra("title");
        String noteContent= data.getStringExtra("content");


        editnotetitle.setText(noteTitle);
        editnotecontent.setText(noteContent);

        FloatingActionButton fbut= findViewById(R.id.saveeditednote);
        fbut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                String ntitle= editnotetitle.getText().toString();
                String ncontent = editnotecontent.getText().toString();
                if (ntitle.isEmpty()||ncontent.isEmpty())
                {
                    Toast.makeText(editNote.this, "field is empty notes cant save", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar2.setVisibility(View.VISIBLE);

                DocumentReference docref= fstore.collection("notes").document(user.getUid()).collection("myNotes").document(Objects.requireNonNull(data.getStringExtra("docid")));
                Map<String,Object> note = new HashMap<>();
                note.put("title",ntitle);
                note.put("content",ncontent);

                docref.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editNote.this, "note is updated", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(editNote.this,MainActivity.class);
                        startActivity(in);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editNote.this, "Error,Try Again", Toast.LENGTH_SHORT).show();
                        progressBar2.setVisibility(View.VISIBLE);
                    }
                });
            }
        });



    }
}
