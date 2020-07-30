package com.example.firenotes.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firenotes.MainActivity;
import com.example.firenotes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class register extends AppCompatActivity {

    EditText rUsername,remail,ruserpass,ruserconpass;
    Button syncaccount;
    TextView loginact;
    FirebaseAuth fAuth;
    ProgressBar progressBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("creat new account");
        rUsername= findViewById(R.id.userName);
        remail= findViewById(R.id.userEmail);
        ruserpass= findViewById(R.id.password);
        ruserconpass= findViewById(R.id.passwordConfirm);
        syncaccount= findViewById(R.id.createAccount);
       loginact = findViewById(R.id.login) ;
       fAuth = FirebaseAuth.getInstance();
       progressBar = findViewById(R.id.progressBar4);


       loginact.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),login.class));
           }
       });


       syncaccount.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String uUserName = rUsername.getText().toString();
               String uUseremail = remail.getText().toString();
               String userpass = ruserpass.getText().toString();
               String uconpass = ruserconpass.getText().toString();

               if (uUserName.isEmpty()||uUseremail.isEmpty()||userpass.isEmpty()||uconpass.isEmpty()){
                   Toast.makeText(register.this, "all fields are required", Toast.LENGTH_SHORT).show();
                   return;
               }
               if (!userpass.equals(uconpass)){
                   ruserconpass.setError("password dont match");
                   return;
               }
               progressBar.setVisibility(View.VISIBLE);
               AuthCredential crediential = EmailAuthProvider.getCredential(uUseremail,userpass);
               fAuth.getCurrentUser().linkWithCredential(crediential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                   @Override
                   public void onSuccess(AuthResult authResult) {
                       Toast.makeText(register.this, "Notes are synced", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(getApplicationContext(),MainActivity.class));
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(register.this, "failed "+e.getMessage(), Toast.LENGTH_LONG).show();
                      progressBar.setVisibility(View.GONE);
                    }
               });
               FirebaseUser usr = fAuth.getCurrentUser();
               UserProfileChangeRequest request= new UserProfileChangeRequest.Builder()
                       .setDisplayName(uUserName)
                       .build();
               usr.updateProfile(request);

               startActivity(new Intent(getApplicationContext(),MainActivity.class));



           }
       });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        startActivity(new Intent(this, MainActivity.class));
        finish();

        return super.onOptionsItemSelected(item);
    }
}
